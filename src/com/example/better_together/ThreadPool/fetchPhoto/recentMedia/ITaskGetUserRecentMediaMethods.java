package com.example.better_together.ThreadPool.fetchPhoto.recentMedia;

import android.graphics.Bitmap;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by ssdd on 10/29/14.
 */
public interface ITaskGetUserRecentMediaMethods {
    void setGetUserRecentMediaThread(Thread thread);
    URL getUserRecentMediaURL();
    void setGetUserRecentMediaResponse(Bitmap[] photos, String[] captions, String[] creationDates);
}
