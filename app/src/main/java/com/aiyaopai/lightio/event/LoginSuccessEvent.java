package com.aiyaopai.lightio.event;

public class LoginSuccessEvent {
    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public LoginSuccessEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isLogin;


}
