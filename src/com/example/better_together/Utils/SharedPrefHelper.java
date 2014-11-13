package com.example.better_together.Utils;

import android.app.Activity;
import android.content.Context;
import com.example.better_together.storage.SharedPrefStorage;

/**
 * Created by ssdd on 11/9/14.
 */
public class SharedPrefHelper {

    private static SharedPrefStorage sInstance;
    private static volatile boolean sIsInitiated = false;
    private static Activity sContext;

    public static void setActivityContext(Activity activityContext){
        sContext = activityContext;
    }

    public static SharedPrefStorage getInstance(){
        if(!sIsInitiated){
            sInstance = new SharedPrefStorage(sContext,Context.MODE_PRIVATE);
        }
        return sInstance;
    }
}
