package com.example.better_together.ThreadPool.fetchPhoto.recentMedia;

import android.graphics.Bitmap;
import org.json.JSONObject;

import java.net.URL;
import java.util.Date;

/**
 * Created by ssdd on 10/29/14.
 */
public interface ITaskGetUserRecentMediaMethods {
    void setGetUserRecentMediaThread(Thread thread);
    URL getUserRecentMediaURL();
    int getIndex();
    void setGetUserRecentMediaResponse(URL photo, String caption, Date creationDate);
}
