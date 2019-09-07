package com.viktor.e_commerce;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;


public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        SDKInitializer.initialize(this);
    }

    public static Context getContext(){
        return context;
    }
}
