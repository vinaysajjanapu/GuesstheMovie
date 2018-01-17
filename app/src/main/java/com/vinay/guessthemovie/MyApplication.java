package com.vinay.guessthemovie;

import android.app.Application;
import android.content.Context;

/**
 * Created by salimatte on 13-01-2018.
 */

public class MyApplication extends Application {
    public static Context context;

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

}
