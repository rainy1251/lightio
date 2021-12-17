package com.aiyaopai.lightio.components.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.util.PermissionUtils;

public class USBActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb_permission);

        Button openUsb = findViewById(R.id.open_usb);
        Button checkAccess = findViewById(R.id.check_Access);
        Button openCamear = findViewById(R.id.open_Camera);

        requestAllPower();

        openUsb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(USBActivity.this, OpenUsbActivity.class);
                startActivity(intent);
            }
        });
        openCamear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(USBActivity.this, LiveActivity.class));
            }
        });
        checkAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAllPower();
            }
        });
    }


    public void requestAllPower() {
        if (PermissionUtils.isGrantExternalRW(this, 1)) {
            Toast.makeText(USBActivity.this, "申请成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(USBActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //检验是否获取权限，如果获取权限，外部存储会处于开放状态，会弹出一个toast提示获得授权
                    String sdCard = Environment.getExternalStorageState();
                    if (sdCard.equals(Environment.MEDIA_MOUNTED)) {
                        Toast.makeText(this, "获得授权", Toast.LENGTH_LONG).show();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(USBActivity.this, "buxing", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
