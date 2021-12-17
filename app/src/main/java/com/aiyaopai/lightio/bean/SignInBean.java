package com.aiyaopai.lightio.bean;

public class SignInBean {

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getChangePasswordRequired() {
        return ChangePasswordRequired;
    }

    public void setChangePasswordRequired(String changePasswordRequired) {
        ChangePasswordRequired = changePasswordRequired;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getActivitiesCount() {
        return ActivitiesCount;
    }

    public void setActivitiesCount(String activitiesCount) {
        ActivitiesCount = activitiesCount;
    }

    public int getOriginalPicturesCount() {
        return OriginalPicturesCount;
    }

    public void setOriginalPicturesCount(int originalPicturesCount) {
        OriginalPicturesCount = originalPicturesCount;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public String Id; //用户 Id
    public String Role; //用户类型
    public String ChangePasswordRequired; //是否需要修改密码
    public String Token; //用户令牌
    public boolean Success; //用户令牌
    public String Avatar; //头像
    public String Nickname; //昵称
    public String ActivitiesCount; //活动场次
    public int OriginalPicturesCount; //原始照片数量
    public String Message; //xinxi
    public String Title; //活动名称
    public int Total; //总数

}
