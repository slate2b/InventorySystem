package com.cs360.inventorysystem;

public class Preferences {

    private String mUsername;
    private boolean mSmsActive;
    private boolean mInAppActive;

    private static Preferences mPreferences;

    public static Preferences getInstance(String usrnm, boolean sms, boolean inApp) {
        if (mPreferences == null) {
            mPreferences = new Preferences(usrnm, sms, inApp);
        }
        return mPreferences;
    }

    private Preferences(String usrnm, boolean sms, boolean inApp) {
        mUsername = usrnm;
        mSmsActive = sms;
        mInAppActive = inApp;
    }

    public String getUserName() {
        return mUsername;
    }

    public void setUserName(String usrnm) {
        mUsername = usrnm;
    }

    public boolean getSmsActive() {
        return mSmsActive;
    }

    public void setSmsActive(boolean sms) {
        mSmsActive = sms;
    }

    public boolean getInAppActive() {
        return mInAppActive;
    }

    public void setInAppActive(boolean inApp) {
        mInAppActive = inApp;
    }
}
