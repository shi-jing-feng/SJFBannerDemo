package com.shijingfeng.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentProvider;
import android.content.Context;
import android.os.Build;

/**
 * Function: Application
 * Author: ShiJingFeng
 * Date: 2020/1/5 10:32
 * Description:
 */
public class AppApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

    public static Context getContext() {
        if (sContext == null) {
            throw new IllegalArgumentException("App还没初始化完毕");
        }
        return sContext;
    }

}
