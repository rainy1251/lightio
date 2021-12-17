package com.aiyaopai.lightio.net.qiniu;

import org.json.JSONObject;

public class QiNiuParams {
    private int type;//1代表成功，0代表上传中
    private int progress;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path;



    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }


    public JSONObject getRes() {
        return res;
    }

    public void setRes(JSONObject res) {
        this.res = res;
    }

    public QiNiuParams(int type, int progress,String path, JSONObject res) {
        this.type = type;
        this.progress = progress;
        this.res = res;
        this.path = path;
    }

    private JSONObject res;
}
