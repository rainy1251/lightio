package com.aiyaopai.lightio.components.activity;

import android.app.PendingIntent;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aiyaopai.lightio.R;

public class OpenUsbActivity extends AppCompatActivity {
    private static final String ACTION_USB_PERMISSION = "com.demo.otgusb.USB_PERMISSION";
    private UsbManager mUsbManager;
    private PendingIntent mPermissionIntent;
    private static String TAG = "rain";
   // private FilesAdapter adapter;
    private TextView tvUpload;
  //  private FileSystem currentFs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_usb);

      //  init();
        initView();
        initListener();
    }

    private void initListener() {
//        tvUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<UsbFile> upData = adapter.getData();
//                copyFile(upData.get(0));
//                for (UsbFile file : upData) {
//                    Log.i(TAG, file.getName());
//                }
//            }
//        });
    }

    private void initView() {
        RecyclerView rvFiles = findViewById(R.id.rv_files);
        tvUpload = findViewById(R.id.tv_upload);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvFiles.setLayoutManager(layoutManager);
//        adapter = new FilesAdapter(this);
//        rvFiles.setAdapter(adapter);
    }

//    private void init() {
//        //USB管理器
//        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
//        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
//
//        //注册广播,监听USB插入和拔出
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
//        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
//        intentFilter.addAction(ACTION_USB_PERMISSION);
//        registerReceiver(mUsbReceiver, intentFilter);
//
//        //读写权限
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
//        }
//    }
//    public void readUFile() {
//        try {
//            UsbMassStorageDevice[] devices = UsbMassStorageDevice.getMassStorageDevices(this);
//            UsbFile newFile = null;
//            for (UsbMassStorageDevice device : devices) {
//                if (!mUsbManager.hasPermission(device.getUsbDevice())) {
//                    mUsbManager.requestPermission(device.getUsbDevice(), mPermissionIntent);
//                    break;
//                }
//                // before interacting with a device you need to call init()!
//                device.init();
//
//                // Only uses the first partition on the device
//                currentFs = device.getPartitions().get(0).getFileSystem();
//                Log.d(TAG, "Volume Label: " + currentFs.getVolumeLabel());
//                Log.d(TAG, "Capacity: " + currentFs.getCapacity());
//                Log.d(TAG, "Occupied Space: " + currentFs.getOccupiedSpace());
//                Log.d(TAG, "Free Space: " + currentFs.getFreeSpace());
//                Log.d(TAG, "Chunk size: " + currentFs.getChunkSize());
//                UsbFile root = currentFs.getRootDirectory();
//                UsbFile[] files = root.listFiles();
//                List<UsbFile> usbFilesList =new ArrayList<>();
//                for (UsbFile file : files){
//                    if (file.isDirectory()) {
//                        usbFilesList.add(file);
//                    }
//                }
//                adapter.setData(usbFilesList);
//                adapter.notifyDataSetChanged();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d(TAG,  e.toString());
//        }
//    }
//
//    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            Log.d(TAG, "onReceive: " + intent);
//            String action = intent.getAction();
//            if (action == null)
//                return;
//            switch (action) {
//                case ACTION_USB_PERMISSION://用户授权广播
//                    synchronized (this) {
//                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) { //允许权限申请
//                            readUFile();
//                        } else {
//                            Log.d(TAG,"用户未授权，访问USB设备失败");
//                        }
//                    }
//                    break;
//                case UsbManager.ACTION_USB_DEVICE_ATTACHED://USB设备插入广播
//                    Log.d(TAG,"USB设备插入");
//                    readUFile();
//                    break;
//                case UsbManager.ACTION_USB_DEVICE_DETACHED://USB设备拔出广播
//                    Log.d(TAG,"USB设备拔出");
//                    break;
//            }
//        }
//    };
//
//    public void copyFile(UsbFile usbFile) {
//        InputStream is = UsbFileStreamFactory.createBufferedInputStream(usbFile, currentFs);
//        byte[] buffer = new byte[currentFs.getChunkSize()];
//        int len;
//        File sdFile = new File("/sdcard/111");
//        sdFile.mkdirs();
//        FileOutputStream sdOut = null;
//        try {
//            sdOut = new FileOutputStream(sdFile.getAbsolutePath() + "/" + usbFile.getName());
//            while ((len = is.read(buffer)) != -1) {
//                sdOut.write(buffer, 0, len);
//            }
//            is.close();
//            sdOut.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.d(TAG,"读文件: " + usbFile.getName() + " ->复制到/sdcard/111/");
//    }

}
