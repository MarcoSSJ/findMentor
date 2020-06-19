package com.example.findmentorapp;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static MyApplication mInstance;
    private static Context mContext;
    private static String sessionID;
    private static String id;

    @Override
    public void onCreate() {
        super.onCreate();
        //setSessionID("");
        mInstance = this;
        mContext = getApplicationContext();
    }
    public static MyApplication getInstance(){
        if(mInstance == null){
            mInstance = new MyApplication();
        }
        return mInstance;
    }
    public static Context getContext(){
        return mContext;
    }
    public void setSessionID(String sessionID) {
        MyApplication.sessionID = sessionID;
    }
    public void setID(String id) {
        MyApplication.id = id;
    }

    public String getSessionID() {
        if (sessionID == null)
            return "";
        return sessionID;
    }
    public String getID() {
        if (id == null)
            return "";
        return id;
    }
}
