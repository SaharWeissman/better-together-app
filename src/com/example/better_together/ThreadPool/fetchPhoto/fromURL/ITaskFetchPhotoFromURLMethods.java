package com.example.better_together.ThreadPool.fetchPhoto.fromURL;

import android.graphics.Bitmap;

import java.net.URL;
import java.util.HashMap;

/**
 * Created by ssdd on 10/23/14.
 */
public interface ITaskFetchPhotoFromURLMethods {
    void setFetchPhotoFromURLThread(Thread thread);
    void setFetchPhotoFromURLResult(Bitmap result);
    URL getFetchPhotoURL();
    Bitmap getFetchPhotoResponse();
}
