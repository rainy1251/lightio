package com.aiyaopai.lightio.components.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.bean.BaseBean;
import com.aiyaopai.lightio.bean.UploadTokenBean;
import com.aiyaopai.lightio.components.activity.SettingActivity;
import com.aiyaopai.lightio.adapter.PicAdapter;
import com.aiyaopai.lightio.base.BaseMvpFragment;
import com.aiyaopai.lightio.bean.PicBean;
import com.aiyaopai.lightio.databinding.FragmentLiveBinding;
import com.aiyaopai.lightio.mvp.contract.LiveContract;
import com.aiyaopai.lightio.mvp.presenter.LivePresenter;
import com.aiyaopai.lightio.ptp.Camera;
import com.aiyaopai.lightio.ptp.PtpConstants;
import com.aiyaopai.lightio.ptp.model.LiveViewData;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.MyToast;
import com.aiyaopai.lightio.util.SPUtils;
import com.aiyaopai.lightio.util.TrafficInfo;
import com.aiyaopai.lightio.view.AppDB;
import com.aiyaopai.lightio.view.CommonDialog;
import com.aiyaopai.lightio.view.LargePicDialog;
import com.aiyaopai.lightio.view.QrCodeDialog;
import com.aiyaopai.lightio.view.ScanningDialog;
import com.aiyaopai.lightio.view.SessionActivity;
import com.aiyaopai.lightio.view.SessionView;
import com.king.zxing.util.CodeUtils;
import com.qiniu.android.utils.AsyncRun;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LiveFragment extends BaseMvpFragment<LivePresenter, FragmentLiveBinding>
        implements Camera.StorageInfoListener, LiveContract.View, SessionView, View.OnClickListener
        , CommonDialog.OnConfirmListener {

    private int storageId;
    private List<PicBean> dataList;
    private final List<Integer> picIds = new ArrayList<>();
    private PicAdapter mAdapter;
    private boolean isCreate = true;
    private LivePresenter presenter;
    private ProgressBar pbBar;
    private TextView tvProgress;
    private ScanningDialog dialog;
    private String qiNiuToken;
    private int total;
    private String albumId;
    private int localNum;
    private boolean autoScan;
    private TrafficInfo speed;

    @Override
    protected void initView() {
        ((SessionActivity) Objects.requireNonNull(getActivity())).setSessionView(this);
        presenter = new LivePresenter(this);
        presenter.initToolbar(viewBinding);
        presenter.initRecycleView(getActivity(), viewBinding);
        initRefreshLayout(viewBinding.srlView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        autoScan = SPUtils.getBooleanDefTrue(Contents.AUTO);
        Intent intent = (Objects.requireNonNull(getActivity())).getIntent();

        String title = intent.getStringExtra(Contents.Title);
        albumId = intent.getStringExtra(Contents.AlbumId);
        qiNiuToken = intent.getStringExtra(Contents.QiNiuToken);
        total = intent.getIntExtra(Contents.Total, 0);

        viewBinding.includeLive.tvToolbarTitle.setText(title);
        viewBinding.tvId.setText("相册码：" + albumId);
        viewBinding.tvSuccess.setText(String.valueOf(total));
        testNetSpeed();

        presenter.queryUploadedPics();
        initListener();
    }

    private void initListener() {
        viewBinding.includeLive.ivBack.setOnClickListener(this);
        viewBinding.includeLive.ivSet.setOnClickListener(this);
        viewBinding.llTotal.setOnClickListener(this);
        viewBinding.includeLive.tvRightRight.setOnClickListener(this);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            String mode = SPUtils.getModeString(Contents.UPLOAD_MODE);
            if (dataList.get(position).getStatus() == 0 && mode.equals(Contents.HAND_UPLOAD)) {
                presenter.handUploadPic(dataList.get(position), qiNiuToken);
            } else {
                LargePicDialog dialog = new LargePicDialog(getActivity(),dataList.get(position).getPicPath());
                dialog.show();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live;
    }

    protected Camera camera() {
        if (getActivity() == null) {
            return null;
        }
        return ((SessionActivity) getActivity()).getCamera();
    }

    @Override
    public void onStorageFound(int handle, String label) {
        storageId = handle;
    }

    @Override
    public void onAllStoragesFound() {

        camera().retrieveImageHandles(this, storageId, PtpConstants.ObjectFormat.EXIF_JPEG);
    }

    //获取照片总数
    @Override
    public void onImageHandlesRetrieved(int[] handles) {
        AsyncRun.runInMain(() -> {

            presenter.setModePx(viewBinding, mAdapter);

            localNum = handles.length;
            viewBinding.tvTotal.setText(String.valueOf(localNum));
            if (!isCreate) {
                return;
            }
            picIds.clear();
            if (autoScan) {
                presenter.getScanPicIds(handles);
            }
            isCreate = false;
        });
    }

    @Override
    public void scanIdComplete(List<Integer> ids) {
        if (ids.size() > 0) {
            picIds.addAll(ids);
            showDialog(ids);
        } else {
            MyToast.show("扫描完成，没有找到新照片");

        }
    }

    /**
     * 手动上传进度更新
     */
    @Override
    public void getUploadHandNext(PicBean bean) {
        if (bean.getStatus() == 1) {
            AppDB.getInstance().picDao().update(bean);
            total++;
            viewBinding.tvSuccess.setText(String.valueOf(total));
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 手动模式下，按快门增加照片
     */
    @Override
    public void getUploadHandAdd(PicBean bean) {
        if (bean != null) {
            dataList.add(0, bean);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void showDialog(List<Integer> ids) {
        String title = "扫描有新照片";
        String content = "共扫描" + ids.size() + "张，是否上传";
        CommonDialog dialog = new CommonDialog(getActivity(), title, content, 1);
        dialog.setOnConfirmListener(this);
        dialog.show();
    }

    @Override
    public void enableUi(boolean enabled) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void cameraStarted(Camera camera) {
        if (camera != null) {
            isCreate = true;
            String cameraName = camera.getDeviceName();
            viewBinding.tvConnect.setText("已连接");
            viewBinding.tvConnect.setTextColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.color_success));
            viewBinding.tvCamara.setText("相机型号：" + cameraName);
            camera.retrieveStorages(this);
        }

    }

    @Override
    public void cameraStopped(Camera camera) {
        if (camera == null) {
            viewBinding.tvCamara.setText("相机未连接");
            viewBinding.tvConnect.setText("未连接");
            isCreate = false;
            viewBinding.tvConnect.setTextColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.color_error));
        }

    }

    @Override
    public void cameraNoFound() {

        presenter.setModePx(viewBinding, mAdapter);
    }

    @Override
    public void propertyChanged(int property, int value) {

    }

    @Override
    public void propertyDescChanged(int property, int[] values) {

    }


    @Override
    public void setCaptureBtnText(String text) {

    }

    @Override
    public void focusStarted() {

    }

    @Override
    public void focusEnded(boolean hasFocused) {

    }

    @Override
    public void liveViewStarted() {

    }

    @Override
    public void liveViewStopped() {

    }

    @Override
    public void liveViewData(LiveViewData data) {

    }

    @Override
    public void capturedPictureReceived(int objectHandle, String filename, Bitmap thumbnail, Bitmap bitmap) {
        Log.i("rain", filename + "===filename");

    }

    //增加照片
    @Override
    public void objectAdded(int handle, int format) {
        if (camera() == null) {
            return;
        }
        localNum++;
        String uploadMode = SPUtils.getModeString(Contents.UPLOAD_MODE);
        if (uploadMode.equals(Contents.HAND_UPLOAD)) {
            presenter.addHandBean(handle, camera(), albumId, qiNiuToken, uploadMode);
        } else {
            presenter.upLoadSingle(handle, camera(), albumId, qiNiuToken);
        }
    }

    @Override
    public void rateAdded(int handle) {
        if (camera() == null) {
            return;
        }
        localNum++;
        PicBean byId = AppDB.getInstance().picDao().findById(String.valueOf(handle));

        if (byId == null) {

            presenter.upLoadSingle(handle, camera(), albumId, qiNiuToken);
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                (Objects.requireNonNull(getActivity())).finish();
                break;
            case R.id.iv_set:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);

                break;
            case R.id.tv_right_right:
                createQrCode(albumId);
                break;
        }
    }

    @Override
    public void getTokenSuccess(UploadTokenBean bean) {

    }

    @Override
    public void getRecycleViewData(List<PicBean> dataList, PicAdapter adapter) {
        this.dataList = dataList;
        this.mAdapter = adapter;
    }

    @Override
    public void confirm(int type) {

        dialog = new ScanningDialog(getActivity());
        pbBar = dialog.findViewById(R.id.pb_bar);
        tvProgress = dialog.findViewById(R.id.tv_progress);
        pbBar.setMax(picIds.size());
        dialog.show();
        presenter.scanningPic(picIds, camera(), albumId);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getScanProgress(int progress) {
        if (pbBar == null) {
            return;
        }
        pbBar.setProgress(progress);
        tvProgress.setText(progress + "/" + picIds.size());
    }

    /**
     * 相机扫描完成
     */
    @Override
    public void getScanComplete() {
        dialog.dismiss();
        dataList.clear();
        presenter.queryUploadedPics();
    }


    @Override
    public void getUploadNext(PicBean bean) {

        if (bean.getStatus() == 1) {
            AppDB.getInstance().picDao().update(bean);
            total++;
            viewBinding.tvSuccess.setText(String.valueOf(total));
        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void getUploadSingleNext(PicBean bean) {
        if (bean.getStatus() == 1) {
            AppDB.getInstance().picDao().update(bean);
            total++;
            viewBinding.tvSuccess.setText(String.valueOf(total));
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 快门照片添加到列表
     */
    @Override
    public void getUploadSingleAdd(PicBean bean) {
        AsyncRun.runInMain(() -> {
            if (bean != null) {

                dataList.add(0, bean);
                mAdapter.notifyItemRangeChanged(0, 1);
            }
        });
    }

    @Override
    public void getAllPics(Map<String, List<PicBean>> map) {
        if (map.containsKey("net")) {
            List<PicBean> pics1 = map.get("net");
            if (pics1 != null && pics1.size() > 0) {
                dataList.addAll(pics1);
                mAdapter.notifyDataSetChanged(); //获取同步后的照片
            }
        } else if (map.containsKey("local")) {
            List<PicBean> pics2 = map.get("local");
            if (pics2 != null && pics2.size() > 0) {
                dataList.addAll(0, pics2);
                mAdapter.notifyDataSetChanged();
                presenter.upLoadPic(pics2, albumId);//上传全部未上传的照片
            }
        }

    }

    /**
     * 开启网速监测
     */
    private void testNetSpeed() {
        try {
            Handler mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 1) {
                        if (viewBinding != null) {

                            viewBinding.tvSpeed.setText(String.format("网速：%s", (msg.obj).toString()));
                        }
                    }
                    super.handleMessage(msg);
                }

            };
            speed = new TrafficInfo(getActivity(), mHandler, TrafficInfo.getUid());
            speed.startCalculateNetSpeed();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void createQrCode(String id) {
        Drawable drawable = Objects.requireNonNull(getActivity()).getResources().getDrawable(R.mipmap.icon);
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bm = bd.getBitmap();
        Bitmap qrCode = CodeUtils.createQRCode(Contents.ACTIVITY_URL + id, 600, bm);
        QrCodeDialog dialog = new QrCodeDialog(getActivity(), qrCode);
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (speed != null) {
            speed.stopCalculateNetSpeed();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isCreate = true;
        presenter.deleteDB();

    }
}
