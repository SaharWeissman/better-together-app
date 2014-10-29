package com.example.better_together.ThreadPool.recentMedia;

import org.json.JSONObject;

import java.net.URL;

/**
 * Created by ssdd on 10/29/14.
 */
public interface ITaskGetUserRecentMediaMethods {
    void setGetUserRecentMediaThread(Thread thread);
    URL getUserRecentMediaURL();
    public void setUserRecentMediaResponse(JSONObject responseAsJSON);
}
