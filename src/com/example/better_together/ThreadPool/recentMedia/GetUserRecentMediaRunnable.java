package com.example.better_together.ThreadPool.recentMedia;

import android.os.*;
import android.os.Process;
import android.util.Log;
import com.example.better_together.network.HttpRequestHelper;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by ssdd on 10/29/14.
 */
public class GetUserRecentMediaRunnable implements Runnable {

    private static final String TAG = GetUserRecentMediaRunnable.class.getName();
    private final ITaskGetUserRecentMediaMethods mUserRecentMediaTask;
    private final HttpRequestHelper mHttpRequestHelper;

    public GetUserRecentMediaRunnable(ITaskGetUserRecentMediaMethods userRecentMediaTask){
        this.mUserRecentMediaTask = userRecentMediaTask;
        this.mHttpRequestHelper = new HttpRequestHelper();
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        mUserRecentMediaTask.setGetUserRecentMediaThread(Thread.currentThread());

        URL userRecentMediaURL = mUserRecentMediaTask.getUserRecentMediaURL();
        if(userRecentMediaURL == null){
            throw new IllegalArgumentException("url cannot be null");
        }
        JSONObject responseAsJson = mHttpRequestHelper.makeGetRequest(userRecentMediaURL);
        if(responseAsJson == null){
            Log.d(TAG, "got null response");
        }else{
            Log.d(TAG,String.format("got response: %s",responseAsJson.toString()));
            mUserRecentMediaTask.setUserRecentMediaResponse(responseAsJson);
        }
    }
}
