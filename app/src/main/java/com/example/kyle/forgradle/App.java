package com.example.kyle.forgradle;

import android.app.Application;

import com.example.kyle.forgradle.util.CrashHandler;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //CrashHandler.getInstance().init(this);
    }
}
