package com.aiyaopai.lightio.net.qiniu;

/**
 * ClassName: FPUploadException
 * Description:
 * Author: levi
 * CreateDate: 2020/8/14 12:13
 */
public class QiNiuUploadException extends Throwable {
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    private int code;

    public QiNiuUploadException(int code, String error) {
        this.code = code;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    private String error;
}
