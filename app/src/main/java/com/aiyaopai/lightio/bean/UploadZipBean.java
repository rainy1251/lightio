package com.aiyaopai.lightio.bean;

public class UploadZipBean {
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String token;
    private String path;

    public UploadZipBean(String token, String path) {
        this.token = token;
        this.path = path;
    }
}
