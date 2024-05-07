package com.example.aspect;

import android.app.Application;

import androidx.multidex.MultiDex;

/**
 * @Description
 * @Author wangwenbo
 * @CreateTime 2024年05月07日 16:14:16
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

    }

    public static boolean isLogin(){
        return false;
    }
}
