package com.aiyaopai.lightio.mvp.presenter;

import androidx.exifinterface.media.ExifInterface;

import com.aiyaopai.lightio.base.BasePresenter;
import com.aiyaopai.lightio.base.CommonObserver;
import com.aiyaopai.lightio.base.CustomObserver;
import com.aiyaopai.lightio.bean.ActivityListBean;
import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.PicBean;
import com.aiyaopai.lightio.mvp.contract.NoticeContract;
import com.aiyaopai.lightio.mvp.model.NoticeModel;
import com.aiyaopai.lightio.net.RxScheduler;
import com.aiyaopai.lightio.util.AppConfig;
import com.aiyaopai.lightio.util.ModifyExif;
import com.aiyaopai.lightio.util.MyLog;
import com.aiyaopai.lightio.view.AppDB;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NoticePresenter extends BasePresenter<NoticeContract.View> implements NoticeContract.Presenter {

    private final NoticeModel model;
    private int syncNum = 0;

    public NoticePresenter() {
        model = new NoticeModel();
    }

    @Override
    public void getUpLoadToken(String activityId) {
        if (!isViewAttached()) {
            return;
        }
        model.getUpLoadToken(activityId)
                .compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new CustomObserver<BaseBean>(mView) {
                    @Override
                    public void onNext(@NotNull BaseBean bean) {
                        mView.getTokenSuccess(bean);
                    }
                });
    }

    @Override
    public void getOriginalPic(String activityId, String photographerId, int pageIndex) {
        if (mView == null) {
            return;
        }
        model.getOriginalPic(activityId, photographerId, pageIndex)
                .compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())//解决内存泄漏
                .subscribe(new CommonObserver<ActivityListBean>() {
                    @Override
                    public void onNext(@NotNull ActivityListBean bean) {
                        mView.getOriginalPic(bean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        mView.getError();
                    }
                });
    }

    @Override
    public void syncData(ArrayList<ActivityListBean.ResultBean> result) {
        Flowable.fromIterable(result).parallel(4)
                .runOn(Schedulers.io())
                .doOnCancel(new Action() {
                    @Override
                    public void run() throws Throwable {
                    }
                })
                .map(new Function<ActivityListBean.ResultBean, Integer>() {
                    @Override
                    public Integer apply(@NonNull ActivityListBean.ResultBean bean) throws Throwable {

                        if (bean.LocalKey != null) {
                            if (bean.LocalKey.contains(":")) {
                                String[] split = bean.LocalKey.split(":");
                                PicBean picBean;
                                if (split.length > 1) {
                                    picBean = new PicBean(split[1], bean.getSize(), bean.getUrl(), split[0], 100, 1);
                                } else {
                                    picBean = new PicBean("YAOPAI", bean.getSize(), bean.getUrl(), "", 100, 1);
                                }
                                AppDB.getInstance().picDao().insert(picBean);
                                syncNum++;
                            }
                        }
                        return syncNum;
                    }

                })
                .sequential()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Throwable {
                        mView.syncComplete();
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        mView.syncNum(integer);
                    }

                });
    }

    @Override
    public void syncSD(String activityId) {

        File fileAll = new File(AppConfig.BASE_PATH, activityId);
        if (!fileAll.exists()) {
            mView.syncSDComplete();
            return;
        }
        File[] files = fileAll.listFiles();
        if (files == null) {
            mView.syncSDComplete();
            return;
        }

        if (files.length == 0) {
            mView.syncSDComplete();
            return;
        }
        for (File file : files) {
            String id = ModifyExif.getExif(file.getAbsolutePath()).getAttribute(ExifInterface.TAG_COPYRIGHT);
            //String data = ModifyExif.getExif(file.getAbsolutePath()).getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL);
            PicBean picBean = new PicBean(file.getName(), (int) file.length()
                    , file.getAbsolutePath(), id, 0, 0);
         //  ModifyExif.setExif(file.getAbsolutePath(), id,data);
            AppDB.getInstance().picDao().insert(picBean);

        }
        mView.syncSDComplete();
    }
}
