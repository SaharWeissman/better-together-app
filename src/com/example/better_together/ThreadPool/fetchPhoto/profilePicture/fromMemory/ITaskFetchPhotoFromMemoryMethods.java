package com.example.better_together.ThreadPool.fetchPhoto.profilePicture.fromMemory;

import android.graphics.Bitmap;

/**
 * Created by ssdd on 10/29/14.
 */
public interface ITaskFetchPhotoFromMemoryMethods {
    void setFetchPhotoFromMemoryThread(Thread thread);
    void setFetchPhotoFromMemoryResult(Bitmap result);
    String getFetchPhotoPath();
    Bitmap getFetchPhotoResponse();
}
