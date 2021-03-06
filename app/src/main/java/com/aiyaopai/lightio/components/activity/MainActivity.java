package com.aiyaopai.lightio.components.activity;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.aiyaopai.lightio.R;
import com.aiyaopai.lightio.base.BaseActivity;
import com.aiyaopai.lightio.components.fragment.AlbumFragment;
import com.aiyaopai.lightio.components.fragment.MineFragment;
import com.aiyaopai.lightio.databinding.ActivityMainBinding;
import com.aiyaopai.lightio.event.LoginAgainEvent;
import com.aiyaopai.lightio.event.LoginExitEvent;
import com.aiyaopai.lightio.event.LoginSuccessEvent;
import com.aiyaopai.lightio.util.Contents;
import com.aiyaopai.lightio.util.MyLog;
import com.aiyaopai.lightio.util.MyToast;
import com.aiyaopai.lightio.util.PermissionUtils;
import com.aiyaopai.lightio.util.SPUtils;
import com.aiyaopai.lightio.view.AppDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements BottomNavigationView.OnNavigationItemSelectedListener {

    private List<Fragment> fragments;
    private boolean isExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

//        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
//        View view = binding.root;
//        setContentView(view);

        EventBus.getDefault().register(this);
        viewBinding.bottomNavBar.setItemIconTintList(null);
        viewBinding.bottomNavBar.setOnNavigationItemSelectedListener(this);
        requestExternalRW();
    }

    @Override
    protected void initData() {
        fragments = new ArrayList<>();
        // fragments.add(new HomeFragment());
        fragments.add(new AlbumFragment());
        fragments.add(new MineFragment());
        setFragmentPosition(0);
        AppDB.getInstance().picDao().delete();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.home:
//                setFragmentPosition(0);
//                return true;
            case R.id.live:
                setFragmentPosition(0);
                return true;
            case R.id.mine:
                String access_token = SPUtils.getString(Contents.access_token);
                if (TextUtils.isEmpty(access_token)) {
                    LoginActivity.start(this);
                } else {
                    setFragmentPosition(1);
                }
                return true;
        }
        return true;
    }

    private int lastIndex = 0;

    private void setFragmentPosition(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = fragments.get(position);
        Fragment lastFragment = fragments.get(lastIndex);
        lastIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.container, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }

    public void requestExternalRW() {
        if (!PermissionUtils.isGrantExternalRW(this, 1)) {
            MyToast.show("????????????????????????");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //???????????????????????????????????????????????????????????????????????????????????????????????????toast??????????????????
                String sdCard = Environment.getExternalStorageState();
                if (sdCard.equals(Environment.MEDIA_MOUNTED)) {
                    MyToast.show("????????????");
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onBackPressed() {
        exitBy2Click();
    }

    private void exitBy2Click() {
        Timer tExit;
        if (!isExit) {
            isExit = true;
            MyToast.show("????????????????????????");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);

        } else {
            finish();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginAgainEvent event) {
        LoginActivity.start(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginSuccessEvent(LoginSuccessEvent event) {
        viewBinding.bottomNavBar.setSelectedItemId(R.id.live);
        setFragmentPosition(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginExitEvent(LoginExitEvent event) {
        viewBinding.bottomNavBar.setSelectedItemId(R.id.live);
        setFragmentPosition(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(true)) {
            EventBus.getDefault().unregister(this);
        }
    }
}