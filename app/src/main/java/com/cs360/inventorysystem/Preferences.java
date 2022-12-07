package com.cs360.inventorysystem;

/**
 * The user preferences for the app
 */
public class Preferences {

    private long mPreferencesId;
    private boolean mSmsActive;
    private boolean mInAppActive;
    private String mUsername;

    public long getPreferencesId() {
        return mPreferencesId;
    }

    public void setPreferencesId(long id) {
        mPreferencesId = id;
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

    public String getUserName() {
        return mUsername;
    }

    public void setUserName(String usrnm) {
        mUsername = usrnm;
    }
}
