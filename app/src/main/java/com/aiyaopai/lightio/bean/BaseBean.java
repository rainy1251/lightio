package com.aiyaopai.lightio.bean;

import java.util.List;

public class BaseBean {

    /**
     * Code : 301103
     * Message : 用户名或密码错误
     * Extras : []
     */

    private int Code;
    private String message;
    private boolean Success;
    private String Id;
    private String Token;//上传图片令牌
    private List<?> Extras;

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public List<?> getExtras() {
        return Extras;
    }

    public void setExtras(List<?> Extras) {
        this.Extras = Extras;
    }
}
