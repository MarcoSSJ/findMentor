package com.example.findmentorapp;

import android.app.Application;

public class MyApplication extends Application {
    private String sessionID;
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
    public String getSessionID() {
        return this.sessionID;
    }
}
