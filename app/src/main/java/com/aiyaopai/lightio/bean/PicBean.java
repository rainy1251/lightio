package com.aiyaopai.lightio.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PicBean {
    @PrimaryKey(autoGenerate = true)
    public int pid;
   // status =-1 fail ;=0 wait; =1 success;
    public PicBean(String picName, int picSize, String picPath, String picId, int progress, int status) {
        this.picName = picName;
        this.picSize = picSize;
        this.picPath = picPath;
        this.picId = picId;
        this.progress = progress;
        this.status = status;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    @ColumnInfo(name = "pic_name")
    public String picName;

    public int getPicSize() {
        return picSize;
    }

    public void setPicSize(int picSize) {
        this.picSize = picSize;
    }

    public int picSize;

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String picPath;

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public String picId;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int progress;
    public int status;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String token;

}
