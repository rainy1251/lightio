package com.aiyaopai.lightio.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 应用的流量信息
 *
 * @author yuyuhang
 */
public class TrafficInfo {

    private static final int UNSUPPORTED = -1;
    private static final String LOG_TAG = "test";

    private static TrafficInfo instance;

    static int uid;
    private long preRxBytes = 0;
    private Timer mTimer = null;
    private Context mContext;
    private Handler mHandler;

    /**
     * 更新频率（每几秒更新一次,至少1秒）
     */
    private final int UPDATE_FREQUENCY = 1;
    private int times = 1;

    public TrafficInfo(Context mContext, Handler mHandler, int uid) {
        this.mContext = mContext;
        this.mHandler = mHandler;
        this.uid = uid;
    }

    public TrafficInfo(Context mContext, Handler mHandler) {
        this.mContext = mContext;
        this.mHandler = mHandler;
    }

    public static TrafficInfo getInstant(Context mContext, Handler mHandler) {
        if (instance == null) {
            instance = new TrafficInfo(mContext, mHandler);
        }
        return instance;
    }

    /**
     * 获取总流量
     */
    public long getTrafficInfo() {
        long rcvTraffic = UNSUPPORTED; // 下载流量
        long sndTraffic = UNSUPPORTED; // 上传流量
        rcvTraffic = getRcvTraffic();
        sndTraffic = getSndTraffic();
        if (rcvTraffic == UNSUPPORTED || sndTraffic == UNSUPPORTED)
            return UNSUPPORTED;
        else
            return rcvTraffic + sndTraffic;
    }

    /**
     * 获取下载流量 某个应用的网络流量数据保存在系统的/proc/uid_stat/$UID/tcp_rcv | tcp_snd文件中
     */
    public long getRcvTraffic() {
        long rcvTraffic = UNSUPPORTED; // 下载流量
        rcvTraffic = TrafficStats.getUidRxBytes(uid);
        if (rcvTraffic == UNSUPPORTED) { // 不支持的查询
            return UNSUPPORTED;
        }
        Log.i("test", rcvTraffic + "--1");
        RandomAccessFile rafRcv = null, rafSnd = null; // 用于访问数据记录文件
        String rcvPath = "/proc/uid_stat/" + uid + "/tcp_rcv";
        try {
            rafRcv = new RandomAccessFile(rcvPath, "r");
            rcvTraffic = Long.parseLong(rafRcv.readLine()); // 读取流量统计
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "FileNotFoundException: " + e.getMessage());
            rcvTraffic = UNSUPPORTED;
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rafRcv != null)
                    rafRcv.close();
                if (rafSnd != null)
                    rafSnd.close();
            } catch (IOException e) {
                Log.w(LOG_TAG, "Close RandomAccessFile exception: " + e.getMessage());
            }
        }
        Log.i("test", rcvTraffic + "--2");
        return rcvTraffic;
    }

    /**
     * 获取上传流量
     */
    public long getSndTraffic() {
        long sndTraffic = UNSUPPORTED; // 上传流量
        sndTraffic = TrafficStats.getUidTxBytes(uid);
        if (sndTraffic == UNSUPPORTED) { // 不支持的查询
            return UNSUPPORTED;
        }
        RandomAccessFile rafRcv = null, rafSnd = null; // 用于访问数据记录文件
        String sndPath = "/proc/uid_stat/" + uid + "/tcp_snd";
        try {
            rafSnd = new RandomAccessFile(sndPath, "r");
            sndTraffic = Long.parseLong(rafSnd.readLine());
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "FileNotFoundException: " + e.getMessage());
            sndTraffic = UNSUPPORTED;
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rafRcv != null)
                    rafRcv.close();
                if (rafSnd != null)
                    rafSnd.close();
            } catch (IOException e) {
                Log.w(LOG_TAG, "Close RandomAccessFile exception: " + e.getMessage());
            }
        }
        return sndTraffic;
    }

    /**
     * 获取当前下载流量总和
     */
    public static long getNetworkRxBytes() {
        return TrafficStats.getTotalRxBytes();
    }

    /**
     * 获取当前上传流量总和
     */
    public static long getNetworkTxBytes() {
        return TrafficStats.getTotalTxBytes();
    }

    /**
     * 获取当前网速，小数点保留一位
     */
    public String getNetSpeed() {
        String speed;
        long curRxBytes = getNetworkRxBytes();
        if (preRxBytes == 0)
            preRxBytes = curRxBytes;
        long bytes = curRxBytes - preRxBytes;
        preRxBytes = curRxBytes;
        //int kb = (int) Math.floor(bytes / 1024 + 0.5);
        double kb = (double) bytes / (double) 1024;
        BigDecimal bd;
        if (kb > 1024) {
            double mb = (double) kb / (double) 1024;
            bd = new BigDecimal(mb);
            speed = "mb/s";
        } else {
            bd = new BigDecimal(kb);
            speed = "kb/s";
        }
        double v = bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return v+speed;
    }

    /**
     * 开启流量监控
     */
    public void startCalculateNetSpeed() {
        preRxBytes = getNetworkRxBytes();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (times == UPDATE_FREQUENCY) {
                        Message msg = new Message();
                        msg.what = 1;
                        //msg.arg1 = getNetSpeed();
                        msg.obj = getNetSpeed();
                        mHandler.sendMessage(msg);
                        times = 1;
                    } else {
                        times++;
                    }
                }
            }, 1000, 1000); // 每秒更新一次
        }
    }

    public void stopCalculateNetSpeed() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    /**
     * 获取当前应用uid
     */
    public static int getUid() {
        try {
            PackageManager pm = UiUtils.getContext().getPackageManager();
            @SuppressLint("WrongConstant") ApplicationInfo ai = pm.getApplicationInfo(UiUtils.getContext().getPackageName(), PackageManager.GET_ACTIVITIES);
            return ai.uid;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }
}