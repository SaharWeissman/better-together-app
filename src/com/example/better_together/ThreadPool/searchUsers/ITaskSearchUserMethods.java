package com.example.better_together.ThreadPool.searchUsers;

import org.json.JSONObject;

import java.net.URL;

/**
 * Created by ssdd on 10/22/14.
 */
public interface ITaskSearchUserMethods {
    public void setUserSearchResponse(JSONObject responseAsJSON);
    public void setSearchUserThread(Thread thread);
    public URL getSearchUserURL();
}
