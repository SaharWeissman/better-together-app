package com.example.better_together.ThreadPool.searchUsers;

import android.util.Log;
import com.example.better_together.network.HttpRequestHelper;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by ssdd on 10/22/14.
 */
public class SearchUserRunnable implements Runnable {

    private static final String TAG = SearchUserRunnable.class.getName();
    private final ITaskSearchUserMethods mUserTask;
    private final HttpRequestHelper mHttpRequestHelper;

    public SearchUserRunnable(ITaskSearchUserMethods userTask){
        this.mUserTask = userTask;
        this.mHttpRequestHelper = new HttpRequestHelper();
    }

    @Override
    public void run() {
        // run in bkgd. + set current running thread (so we can interrupt if necessary)
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mUserTask.setSearchUserThread(Thread.currentThread());

        URL searchUserURL = mUserTask.getSearchUserURL();
        if(searchUserURL == null){
            throw new IllegalArgumentException("search user url cannot be null");
        }
        JSONObject responseAsJson = mHttpRequestHelper.makeGetRequest(searchUserURL);
        if(responseAsJson == null){
            Log.d(TAG,"got null response");
        }else{
            Log.d(TAG,String.format("got response: %s",responseAsJson.toString()));
            mUserTask.setUserSearchResponse(responseAsJson);
        }
    }
}
