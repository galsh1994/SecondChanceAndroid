package com.example.secondchance;

import android.app.Application;
import android.content.Context;

public class MyApplicaion extends Application {
    public static Context context;
    @Override
    public void onCreate() {

        super.onCreate();
        context=getApplicationContext();
    }
}
