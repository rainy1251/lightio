package com.aiyaopai.lightio.components.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.components.fragment.LiveFragment;
import com.aiyaopai.lightio.ptp.Camera;
import com.aiyaopai.lightio.ptp.PtpService;
import com.aiyaopai.lightio.ptp.model.LiveViewData;
import com.aiyaopai.lightio.util.AppConfig;
import com.aiyaopai.lightio.util.AppSettings;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.FilesUtil;
import com.aiyaopai.lightio.util.SPUtils;
import com.aiyaopai.lightio.view.CommonDialog;
import com.aiyaopai.lightio.view.SessionActivity;
import com.aiyaopai.lightio.view.SessionView;
import com.gyf.immersionbar.ImmersionBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/***
 * 集成指南
 * 1. copy two *.jar to libs folder
 * 2.  虚拟硬件功能 <uses-feature android:name="android.hardware.usb.host"/>
 * 3.  WLAN 通信因此需要这个权限 <uses-permission android:name="android.permission.INTERNET"/>
 * 4.  xml/device_filter.xml
 * *****/
public class LiveActivity extends SessionActivity implements Camera.CameraListener
        , CommonDialog.OnConfirmListener,CommonDialog.OnCancelListener {

    //日志
    private final String TAG = LiveActivity.class.getSimpleName();

    private static final int DIALOG_PROGRESS = 1;
    private static final int DIALOG_NO_CAMERA = 2;

    private final Handler handler = new Handler();

    private PtpService ptp;
    private Camera camera;

    private boolean isInStart;
    private boolean isInResume;
    private SessionView sessionFrag;
    private AppSettings settings;
    private List<Fragment> fragments;
    private boolean mReceiverTag;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        if (AppConfig.LOG) {
            Log.i(TAG, "onCreate");
        }
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        settings = new AppSettings(this);

        ptp = PtpService.Singleton.getInstance(this);

        initData();
    }

    private void initData() {

        String albumId = getIntent().getStringExtra(Contents.AlbumId);
        SPUtils.save(Contents.AlbumId, albumId);

        createDir(albumId);

        fragments = new ArrayList<>();
        fragments.add(new LiveFragment());
        setFragmentPosition();

        registerReceiver();
        settings = new AppSettings(this);
        ptp = PtpService.Singleton.getInstance(this);
    }

    private int lastIndex = 0;

    private void setFragmentPosition() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = fragments.get(0);
        Fragment lastFragment = fragments.get(lastIndex);
        lastIndex = 0;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.container, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();


    }

    /**
     * 创建文件夹
     */
    private void createDir(String albumId) {
        File activityDir = new File(AppConfig.BASE_PATH);
        if (!activityDir.exists()) {
            activityDir.mkdir();
        }
        File appDir = new File(FilesUtil.getDirPath(albumId));
        if (!appDir.exists()) {
            appDir.mkdir();
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (AppConfig.LOG) {
            Log.i(TAG, "onNewIntent " + intent.getAction());
        }
        this.setIntent(intent);
        if (isInStart) {
            ptp.initialize(this, intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (AppConfig.LOG) {
            Log.i(TAG, "onStart");
        }
        isInStart = true;
        ptp.setCameraListener(this);
        ptp.initialize(this, getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInResume = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isInResume = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (AppConfig.LOG) {
            Log.i(TAG, "onStop");
        }
        isInStart = false;
        ptp.setCameraListener(null);
        if (isFinishing()) {
            ptp.shutdown();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (AppConfig.LOG) {
            Log.i(TAG, "onDestroy");
        }
        if (isFinishing()) {
            ptp.shutdown();
        }
        unregisterReceiver();
    }


    private void sendDeviceInformation() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                File dir = getExternalCacheDir();
                final File out = dir != null ? new File(dir, "deviceinfo.txt") : null;

                if (camera != null) {
                    camera.writeDebugInfo(out);
                }

                final String shortDeviceInfo = out == null && camera != null ? camera.getDeviceInfo() : "unknown";

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        LiveActivity.this.dismissDialog(DIALOG_PROGRESS);
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "RYC USB Feedback");
                        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"PUT_EMAIL_HERE"});
                        if (out != null && camera != null) {
                            sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + out.toString()));
                            sendIntent.putExtra(Intent.EXTRA_TEXT, "Any problems or feature whishes? Let us know: ");
                        } else {
                            sendIntent.putExtra(Intent.EXTRA_TEXT,
                                    "Any problems or feature whishes? Let us know: \n\n\n" + shortDeviceInfo);
                        }
                        startActivity(Intent.createChooser(sendIntent, "Email:"));
                    }
                });
            }
        });
        th.start();
    }

    /****
     * 重写接口方法
     * **/
    @Override
    public void onCameraStarted(Camera camera) {
        this.camera = camera;
        if (AppConfig.LOG) {
            Log.i(TAG, "camera started");
        }
        try {
            dismissDialog(DIALOG_NO_CAMERA);
        } catch (IllegalArgumentException e) {
        }

        camera.setCapturedPictureSampleSize(settings.getCapturedPictureSampleSize());
        if (sessionFrag != null) {

            sessionFrag.cameraStarted(camera);
        }
    }

    @Override
    public void onCameraStopped(Camera camera) {
        if (AppConfig.LOG) {
            Log.i(TAG, "camera stopped");
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.camera = null;
        sessionFrag.cameraStopped(camera);
    }

    @Override
    public void onNoCameraFound() {
        sessionFrag.cameraNoFound();
        Toast.makeText(LiveActivity.this, "没检测到相机", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String message) {
        sessionFrag.enableUi(false);
        sessionFrag.cameraStopped(null);
//        Toast.makeText(LiveActivity.this, message, Toast.LENGTH_SHORT).show();
        startVibrator();
        showVibratorDialog();
    }

    @Override
    public void onPropertyChanged(int property, int value) {
        sessionFrag.propertyChanged(property, value);
    }

    @Override
    public void onPropertyStateChanged(int property, boolean enabled) {

    }

    @Override
    public void onPropertyDescChanged(int property, int[] values) {
        sessionFrag.propertyDescChanged(property, values);
    }

    @Override
    public void onLiveViewStarted() {
        sessionFrag.liveViewStarted();
    }

    @Override
    public void onLiveViewData(LiveViewData data) {
        if (!isInResume) {
            return;
        }
        sessionFrag.liveViewData(data);
    }

    @Override
    public void onLiveViewStopped() {
        sessionFrag.liveViewStopped();
    }

    @Override
    public void onCapturedPictureReceived(int objectHandle, String filename, Bitmap thumbnail, Bitmap bitmap) {
        if (thumbnail != null) {
            sessionFrag.capturedPictureReceived(objectHandle, filename, thumbnail, bitmap);
        } else {
            Toast.makeText(this, "No thumbnail available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBulbStarted() {
        sessionFrag.setCaptureBtnText("0");
    }

    @Override
    public void onBulbExposureTime(int seconds) {
        sessionFrag.setCaptureBtnText("" + seconds);
    }

    @Override
    public void onBulbStopped() {
        sessionFrag.setCaptureBtnText("Fire");
    }

    @Override
    public void onFocusStarted() {
        sessionFrag.focusStarted();
    }

    @Override
    public void onFocusEnded(boolean hasFocused) {
        sessionFrag.focusEnded(hasFocused);
    }

    @Override
    public void onFocusPointsChanged() {

    }

    @Override
    public void onObjectAdded(int handle, int format) {
        sessionFrag.objectAdded(handle, format);
    }

    @Override
    public void onRateAdded(int handle) {
        sessionFrag.rateAdded(handle);
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

    @Override
    public void setSessionView(SessionView view) {
        sessionFrag = view;
    }

    @Override
    public AppSettings getSettings() {
        return settings;
    }

    /**
     * 代码中动态注册广播
     */

    private void registerReceiver() {
        if (!mReceiverTag) {     //避免重复多次注册广播
            IntentFilter filter = new IntentFilter();
            filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
            registerReceiver(mUsbReceiver, filter);
            mReceiverTag = true;
        }
    }

    /**
     * 注销广播
     */

    private void unregisterReceiver() {
        if (mReceiverTag) {
            mReceiverTag = false;
            unregisterReceiver(mUsbReceiver);
        }

    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
//                ptp = PtpService.Singleton.getInstance(MainActivity.this);
                //   ptp.setCameraListener(MainActivity.this);
                ptp.initialize(LiveActivity.this, getIntent());
            }
        }
    };

    private void showVibratorDialog() {
        String title = getResources().getString(R.string.tips);
        String content = getResources().getString(R.string.tips_content);
        CommonDialog dialog = new CommonDialog(this,title ,content, 0);
        dialog.setOnConfirmListener(this);
        dialog.setOnCancelListener(this);
        dialog.show();
    }

    /**
     * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
     */
    private void startVibrator() {
        boolean isVibrate = SPUtils.getBooleanDefTrue(Contents.VIBRATE);
        if (isVibrate) {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {100, 400, 100, 400,100, 400, 100, 400,100, 400, 100, 400}; // 停止 开启 停止 开启
            vibrator.vibrate(pattern, -1); //重复两次上面的pattern 如果只想震动一次，index设为-1
        }
    }

    @Override
    public void confirm(int type) {
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    @Override
    public void cancel(int type) {
        if (vibrator != null) {
            vibrator.cancel();
        }
    }
}
