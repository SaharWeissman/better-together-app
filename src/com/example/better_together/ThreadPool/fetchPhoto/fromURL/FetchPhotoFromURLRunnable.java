package com.example.better_together.ThreadPool.fetchPhoto.fromURL;

import android.graphics.Bitmap;
import android.os.Process;
import android.util.Log;
import com.example.better_together.network.HttpRequestHelper;

import java.net.URL;

/**
 * Created by ssdd on 10/23/14.
 */
public class FetchPhotoFromURLRunnable implements Runnable {

    private static final String TAG = FetchPhotoFromURLRunnable.class.getName();

    private final ITaskFetchPhotoFromURLMethods mFetchPhotoTask;
    private final HttpRequestHelper mHttpRequestHelper;

    public FetchPhotoFromURLRunnable(ITaskFetchPhotoFromURLMethods fetchPhotoTask){
        this.mFetchPhotoTask = fetchPhotoTask;
        this.mHttpRequestHelper = new HttpRequestHelper();
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        mFetchPhotoTask.setFetchPhotoFromURLThread(Thread.currentThread());

        URL fetchPhotoURL = mFetchPhotoTask.getFetchPhotoURL();
        if(fetchPhotoURL == null){
            throw new IllegalArgumentException("fetch photo url cannot be null");
        }
        Bitmap fetchPhotoBitmap = mHttpRequestHelper.fetchPhotoFromURL(fetchPhotoURL);
        if(fetchPhotoBitmap == null){
            Log.d(TAG,"got null bitmap");
        }else{
            Log.d(TAG,"successfully fetched image from URL");
            mFetchPhotoTask.setFetchPhotoFromURLResult(fetchPhotoBitmap);
        }
    }
}
