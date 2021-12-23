package com.aiyaopai.lightio.mvp.presenter;

import android.text.TextUtils;

import androidx.exifinterface.media.ExifInterface;

import com.aiyaopai.lightio.base.BasePresenter;
import com.aiyaopai.lightio.base.CommonObserver;
import com.aiyaopai.lightio.bean.OriginalPicBean;
import com.aiyaopai.lightio.bean.PicBean;
import com.aiyaopai.lightio.mvp.contract.NoticeContract;
import com.aiyaopai.lightio.mvp.model.NoticeModel;
import com.aiyaopai.lightio.net.RxScheduler;
import com.aiyaopai.lightio.util.AppConfig;
import com.aiyaopai.lightio.util.ModifyExif;
import com.aiyaopai.lightio.view.AppDB;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

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

    public NoticePresenter(NoticeContract.View view) {
        super(view);
        model = new NoticeModel();
    }

    @Override
    public void getOriginalPic(int pageIndex, String albumId, String photographerId) {

        model.getOriginalPic(pageIndex, albumId, photographerId)
                .compose(RxScheduler.Obs_io_main())
                .to(getView().bindAutoDispose())//解决内存泄漏
                .subscribe(new CommonObserver<OriginalPicBean>() {
                    @Override
                    public void onNext(@NotNull OriginalPicBean bean) {
                        getView().getOriginalPic(bean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        getView().getError();
                    }
                });
    }

    @Override
    public void syncData(ArrayList<OriginalPicBean.ResultBean> result) {
        Flowable.fromIterable(result).parallel(4)
                .runOn(Schedulers.io())
                .doOnCancel(new Action() {
                    @Override
                    public void run() throws Throwable {
                    }
                })
                .map(new Function<OriginalPicBean.ResultBean, Integer>() {
                    @Override
                    public Integer apply(@NonNull OriginalPicBean.ResultBean bean) throws Throwable {
//TODO
                        if (!TextUtils.isEmpty(bean.getName())) {
                            PicBean picBean;
                            if (bean.getName().contains("_")) {
                                String[] split = bean.getName().split("_");

                                if (split.length > 1) {
                                    picBean = new PicBean(split[0], bean.getSize(), bean.getUrl(), split[0], 100, 1);
                                } else {
                                    picBean = new PicBean("YAOPAI", bean.getSize(), bean.getUrl(), "", 100, 1);
                                }

                            } else {
                                picBean = new PicBean(bean.getName(), bean.getSize(), bean.getUrl(),"133", 100, 1);
                            }
                            AppDB.getInstance().picDao().insert(picBean);
                            syncNum++;
                        }
                        return syncNum;
                    }

                })
                .sequential()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Throwable {
                        getView().syncComplete();
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        getView().syncNum(integer);
                    }

                });
    }

    @Override
    public void syncSD(String albumId) {

        File fileAll = new File(AppConfig.BASE_PATH, albumId);
        if (!fileAll.exists()) {
            getView().syncSDComplete();
            return;
        }
        File[] files = fileAll.listFiles();
        if (files == null) {
            getView().syncSDComplete();
            return;
        }

        if (files.length == 0) {
            getView().syncSDComplete();
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
        getView().syncSDComplete();
    }
}
