package com.example.better_together.ThreadPool.fetchPhoto.profilePicture.fromURL;

import android.graphics.Bitmap;
import com.example.better_together.Views.models.User;
import com.example.better_together.storage.SharedPrefStorage;

import java.net.URL;

/**
 * Created by ssdd on 10/23/14.
 */
public interface ITaskFetchPhotoFromURLMethods {
    void setFetchPhotoFromURLThread(Thread thread);
    void setFetchPhotoFromURLResult(Bitmap result);
    URL getFetchPhotoURL();
    Bitmap getFetchPhotoResponse();
    FetchPhotoFromURLTask.PhotoType getPhotoType();
    User getUser();
}
