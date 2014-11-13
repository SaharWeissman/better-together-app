package com.example.better_together;

import android.app.Application;
import android.util.Log;

/**
 * Created by ssdd on 11/13/14.
 */
public class App extends Application {
    private static final String TAG = App.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"application created");
    }
}
