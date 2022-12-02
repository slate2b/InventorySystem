package com.cs360.inventorysystem;

public class LoginInfo {

    private String mUsername;
    private String mPassword;

    private static LoginInfo mLoginInfo;

    public static LoginInfo getInstance(String usrnm, String pw) {
        if (mLoginInfo == null) {
            mLoginInfo = new LoginInfo(usrnm, pw);
        }
        return mLoginInfo;
    }

    private LoginInfo(String usrnm, String pw) {
        mUsername = usrnm;
        mPassword = pw;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String usrnm) {
        mUsername = usrnm;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String pw) {
        mPassword = pw;
    }
}
