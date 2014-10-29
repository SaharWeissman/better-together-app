package com.example.better_together.ThreadPool.recentMedia;

import com.example.better_together.ThreadPool.ThreadPoolManager;
import com.example.better_together.Views.models.User;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by ssdd on 10/29/14.
 */
public class GetUserRecentMediaTask implements ITaskGetUserRecentMediaMethods {

    private final GetUserRecentMediaRunnable mGetUserRecentMediaRunnable;
    private final ThreadPoolManager mUserManager;
    private URL mGetUserRecentMediaURL;
    private Thread mCurrentThread;
    private JSONObject mUserRecentMediaResponse;
    private User mUser;

    public GetUserRecentMediaTask(){
        this.mGetUserRecentMediaRunnable = new GetUserRecentMediaRunnable(this);
        this.mUserManager = ThreadPoolManager.getInstance();
    }

    public void initGetUserRecentMediaTask(URL getUserRecentMediaURL,User user){
        this.mGetUserRecentMediaURL = getUserRecentMediaURL;
        this.mUser = user;
    }

    @Override
    public void setGetUserRecentMediaThread(Thread thread) {
        this.mCurrentThread = thread;
    }

    @Override
    public URL getUserRecentMediaURL() {
        return mGetUserRecentMediaURL;
    }

    @Override
    public void setUserRecentMediaResponse(JSONObject responseAsJSON) {
        this.mUserRecentMediaResponse = responseAsJSON;
        mUserManager.handleGetUserRecentMediaTaskResponse(this);
    }

    public JSONObject getUserRecentMediaResponse(){
        return mUserRecentMediaResponse;
    }

    public User getUser(){return mUser;}
}
