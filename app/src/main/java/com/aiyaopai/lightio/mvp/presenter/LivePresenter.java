package com.aiyaopai.lightio.mvp.presenter;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.adapter.PicAdapter;
import com.aiyaopai.lightio.base.BasePresenter;
import com.aiyaopai.lightio.base.CommonObserver;
import com.aiyaopai.lightio.base.CustomObserver;
import com.aiyaopai.lightio.bean.PicBean;
import com.aiyaopai.lightio.bean.UploadFileBean;
import com.aiyaopai.lightio.bean.UploadTokenBean;
import com.aiyaopai.lightio.databinding.FragmentLiveBinding;
import com.aiyaopai.lightio.mvp.contract.LiveContract;
import com.aiyaopai.lightio.mvp.model.LiveModel;
import com.aiyaopai.lightio.net.RetrofitClient;
import com.aiyaopai.lightio.net.RxScheduler;
import com.aiyaopai.lightio.net.qiniu.QiNiuImageSubscribe;
import com.aiyaopai.lightio.ptp.Camera;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.FilesUtil;
import com.aiyaopai.lightio.util.MyLog;
import com.aiyaopai.lightio.util.SPUtils;
import com.aiyaopai.lightio.view.AppDB;
import com.aiyaopai.lightio.view.SpaceItemDecoration;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class LivePresenter extends BasePresenter<LiveContract.View> implements LiveContract.Presenter {

    private final LiveModel model;
    private int progress = 0;
    private List<Integer> picIds;

    public LivePresenter(LiveContract.View view) {
        super(view);
        model = new LiveModel();
    }

    @Override
    public void initToolbar(FragmentLiveBinding binding) {
        binding.includeLive.ivIcon.setVisibility(View.GONE);
        binding.includeLive.ivBack.setVisibility(View.VISIBLE);
        binding.includeLive.tvToolbarTitle.setVisibility(View.VISIBLE);
        binding.includeLive.ivSet.setVisibility(View.VISIBLE);
        binding.includeLive.tvRightRight.setVisibility(View.VISIBLE);
        binding.includeLive.tvRightRight.setText("分享");
    }

    @Override
    public void initRecycleView(Context context, FragmentLiveBinding binding) {
        List<PicBean> dataList = new ArrayList<>();
        PicAdapter mAdapter = new PicAdapter(context, R.layout.item_pic, dataList);
        GridLayoutManager manager = new GridLayoutManager(context, 3);
        binding.rvView.setLayoutManager(manager);
        binding.rvView.setAdapter(mAdapter);
        binding.rvView.addItemDecoration(new SpaceItemDecoration(10, 20));
        mAdapter.setEmptyView(R.layout.empty_layout);
        getView().getRecycleViewData(dataList, mAdapter);
    }

    @Override
    public void setModePx(FragmentLiveBinding viewBinding, PicAdapter adapter) {
        String px = SPUtils.getPxString(Contents.PHOTO_PX);
        String mode = SPUtils.getModeString(Contents.UPLOAD_MODE);
        if (px.equals(Contents.Standard)) {
            viewBinding.tvPx.setText("分辨率：标清");
        } else if (px.equals(Contents.Original)) {
            viewBinding.tvPx.setText("分辨率：原图");
        }

        switch (mode) {
            case Contents.DIRECT_UPLOAD:
                adapter.setHandType(false);
                viewBinding.tvMode.setText("上传方式：边拍边传");
                break;
            case Contents.RATING_UPLOAD:
                adapter.setHandType(false);
                viewBinding.tvMode.setText("上传方式：标记上传");
                break;
            case Contents.HAND_UPLOAD:
                viewBinding.tvMode.setText("上传方式：点选上传");
                adapter.setHandType(true);
                break;
        }

    }

    /**
     * 查询扫描照片的ID
     */

    @Override
    public void getScanPicIds(int[] ids) {
        picIds = new ArrayList<>();
        List<Integer> strings = new ArrayList<>();
        for (int id : ids) {
            strings.add(id);
        }
        Flowable.fromIterable(strings).parallel(2)
                .runOn(Schedulers.io())
                .doOnCancel(new Action() {
                    @Override
                    public void run() throws Throwable {
                    }
                })
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Throwable {
                        PicBean byPid = AppDB.getInstance().picDao().findById(String.valueOf(integer));
                        if (byPid == null) {
                            picIds.add(integer);
                        }
                        return "1";
                    }
                })
                .sequential()
                .observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
            @Override
            public void run() throws Throwable {
                getView().scanIdComplete(picIds);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Throwable {

            }
        });


    }

    /**
     * 扫描照片
     */
    @Override
    public void scanningPic(List<Integer> picIds, Camera camera, String albumId) {

        String uploadMode = SPUtils.getModeString(Contents.UPLOAD_MODE);
        String photoPx = SPUtils.getPxString(Contents.PHOTO_PX);
        Observable.fromIterable(picIds)
                .concatMap((Function<Integer, ObservableSource<String>>) id -> Observable.create((ObservableOnSubscribe<String>) emitter ->
                        camera.retrieveImage((objectHandle, byteBuffer, length, info) -> {
                            String filePath = FilesUtil.getFileFromBytes(albumId, uploadMode,
                                    byteBuffer.array(), length, info.filename, objectHandle, info.captureDate, photoPx);
                            emitter.onNext(filePath);
                            emitter.onComplete();
                        }, id)))
                .compose(RxScheduler.Obs_io_main())
                .to(getView().bindAutoDispose())
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        progress++;
                        getView().getScanProgress(progress);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();

                        getView().getScanComplete();
                    }
                });
    }

    @Override
    public void getUpLoadToken(String albumId) {

        model.getUpLoadToken(albumId)
                .compose(RxScheduler.Obs_io_main())
                .to(getView().bindAutoDispose())//解决内存泄漏
                .subscribe(new CustomObserver<UploadTokenBean>(getView()) {
                    @Override
                    public void onNext(@NotNull UploadTokenBean bean) {
                        getView().getTokenSuccess(bean);
                    }
                });
    }

    /**
     * 批量上传
     */

    PicBean mPicBean ;
    @Override
    public void upLoadPic(List<PicBean> pathList, String albumId) {

        String mode = SPUtils.getModeString(Contents.UPLOAD_MODE);
        if (mode.equals(Contents.HAND_UPLOAD)) {
            return;
        }
        MyLog.show("hhaaa");
        Observable.fromIterable(pathList)
                .concatMap(new Function<PicBean, ObservableSource<UploadTokenBean>>() {
                    @Override
                    public ObservableSource<UploadTokenBean> apply(PicBean picBean) throws Throwable {
                        mPicBean = picBean;
                        TimeZone tz = TimeZone.getDefault();
                        int timezoneOffset = (tz.getRawOffset()) / (3600 * 1000);
                        Map<String, Object> map = new HashMap<>();
                        map.put("albumId", albumId);
                        map.put("timezoneOffset", timezoneOffset);
                        Gson gson = new Gson();
                        String strEntity = gson.toJson(map);
                        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
                        return RetrofitClient.getServer().getQiNiuToken(body);
                    }
                })
                .flatMap(new Function<UploadTokenBean, ObservableSource<Response<UploadFileBean>>>() {
                    @Override
                    public ObservableSource<Response<UploadFileBean>> apply(UploadTokenBean uploadTokenBean) throws Throwable {
                        MyLog.show(uploadTokenBean.getResult().getToken());
                        MyLog.show(mPicBean.getPicPath());

                        File file = new File(mPicBean.getPicPath());

                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part body = MultipartBody.Part.createFormData("attach_file",
                                file.getName(), requestFile);

                        return RetrofitClient.getServer().getUpLoad(uploadTokenBean.getResult().getToken(),body);
                    }
                })
                .compose(RxScheduler.Obs_io_main())
                .to(getView().bindAutoDispose())
                .subscribe(new CommonObserver<Response<UploadFileBean>>() {
                    @Override
                    public void onNext(@NonNull Response<UploadFileBean> picBean) {
                      //  getView().getUploadNext(picBean);
                        MyLog.show(picBean.body().getResult().getSize());
                    }
                });


    }

    /**
     * 边拍边传
     */
    @Override
    public void upLoadSingle(int picId, Camera camera, String albumId, String qiNiuToken) {
        String uploadMode = SPUtils.getModeString(Contents.UPLOAD_MODE);
        String photoPx = SPUtils.getPxString(Contents.PHOTO_PX);
        Observable.create(new ObservableOnSubscribe<PicBean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<PicBean> emitter) throws Throwable {
                camera.retrieveImage((objectHandle, byteBuffer, length, info) -> {
                    FilesUtil.getFileFromBytes(albumId, uploadMode,
                            byteBuffer.array(), length, info.filename, objectHandle, info.captureDate, photoPx);
                    PicBean byId = AppDB.getInstance().picDao().findById(String.valueOf(picId));
                    getView().getUploadSingleAdd(byId);
                    emitter.onNext(byId);
                    emitter.onComplete();
                }, picId);
            }
        }).flatMap(new Function<PicBean, ObservableSource<PicBean>>() {
            @Override
            public ObservableSource<PicBean> apply(PicBean bean) throws Throwable {
                return Observable.create(new QiNiuImageSubscribe(bean, qiNiuToken));

            }
        }).compose(RxScheduler.Obs_io_main())
                .to(getView().bindAutoDispose())
                .subscribe(new CommonObserver<PicBean>() {
                    @Override
                    public void onNext(PicBean bean) {
                        getView().getUploadSingleNext(bean);
                    }
                })
        ;

    }

    /**
     * 手动上传时，动态获取照片
     */
    @Override
    public void addHandBean(int picId, Camera camera, String albumId, String qiNiuToken, String mode) {
        String photoPx = SPUtils.getPxString(Contents.PHOTO_PX);
        Observable.create(new ObservableOnSubscribe<PicBean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<PicBean> emitter) throws Throwable {
                camera.retrieveImage((objectHandle, byteBuffer, length, info) -> {
                    FilesUtil.getFileFromBytes(albumId, mode,
                            byteBuffer.array(), length, info.filename, objectHandle, info.captureDate, photoPx);
                    PicBean byId = AppDB.getInstance().picDao().findById(String.valueOf(picId));
                    emitter.onNext(byId);
                    emitter.onComplete();
                }, picId);
            }
        }).compose(RxScheduler.Obs_io_main())
                .to(getView().bindAutoDispose())
                .subscribe(new CommonObserver<PicBean>() {
                    @Override
                    public void onNext(PicBean bean) {
                        getView().getUploadHandAdd(bean);
                    }
                })
        ;

    }

    @Override
    public void deleteDB() {
        Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Boolean> emitter) throws Throwable {
                AppDB.getInstance().picDao().delete();
                emitter.onNext(true);
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    /**
     * 查询已上传的照片
     */
    @Override
    public void queryUploadedPics() {

        Flowable<Map<String, List<PicBean>>> f1 = Flowable.create(new FlowableOnSubscribe<Map<String, List<PicBean>>>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Map<String, List<PicBean>>> emitter) throws Throwable {
                List<PicBean> all = AppDB.getInstance().picDao().getStatus(1);
                Map<String, List<PicBean>> map = new HashMap<>();
                map.put("net", all);
                emitter.onNext(map);
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER);

        Flowable<Map<String, List<PicBean>>> f2 = Flowable.create(new FlowableOnSubscribe<Map<String, List<PicBean>>>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Map<String, List<PicBean>>> emitter) throws Throwable {
                List<PicBean> all2 = AppDB.getInstance().picDao().getStatus(0);
                Map<String, List<PicBean>> map2 = new HashMap<>();
                map2.put("local", all2);
                emitter.onNext(map2);
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER);


        Flowable.concat(f1, f2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Map<String, List<PicBean>>>() {
                    @Override
                    public void accept(Map<String, List<PicBean>> map) throws Throwable {
                        getView().getAllPics(map);
                    }
                });
    }

    /**
     * 手动上传
     */
    @Override
    public void handUploadPic(PicBean bean, String qiNiuToken) {

        Observable.create(new ObservableOnSubscribe<PicBean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<PicBean> emitter) throws Throwable {
                emitter.onNext(bean);
                emitter.onComplete();
            }
        }).flatMap(new Function<PicBean, ObservableSource<PicBean>>() {
            @Override
            public ObservableSource<PicBean> apply(PicBean bean) throws Throwable {
                return Observable.create(new QiNiuImageSubscribe(bean, qiNiuToken));

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<PicBean>() {
                    @Override
                    public void onNext(PicBean bean) {
                        getView().getUploadHandNext(bean);
                    }
                });
    }


}
