package com.wjl.blog.entity;

/**
 * 用户信息
 */
public class UserInfo {

    private String userID;// 用户id

    private String userName;// 用户名称

    private String userPassword; //用户密码

    private String userNum; // 用户账号

    private String userEndTime; // 用户最后登录时间

    private String userStart; //用户状态

    private String userDetail; // 用户备注

    private String userStartTime; // 用户注册时间

    private String iphone;// 用户手机号

    public String getIphone() {
        return iphone;
    }

    public void setIphone(String iphone) {
        this.iphone = iphone;
    }
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public String getUserEndTime() {
        return userEndTime;
    }

    public void setUserEndTime(String userEndTime) {
        this.userEndTime = userEndTime;
    }

    public String getUserStart() {
        return userStart;
    }

    public void setUserStart(String userStart) {
        this.userStart = userStart;
    }

    public String getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(String userDetail) {
        this.userDetail = userDetail;
    }

    public String getUserStartTime() {
        return userStartTime;
    }

    public void setUserStartTime(String userStartTime) {
        this.userStartTime = userStartTime;
    }
}
