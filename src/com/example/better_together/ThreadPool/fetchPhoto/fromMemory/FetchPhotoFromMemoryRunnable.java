package com.example.better_together.ThreadPool.fetchPhoto.fromMemory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.*;

/**
 * Created by ssdd on 10/29/14.
 */
public class FetchPhotoFromMemoryRunnable implements Runnable{
    private final ITaskFetchPhotoFromMemoryMethods mFetchPhotoTask;

    public FetchPhotoFromMemoryRunnable(ITaskFetchPhotoFromMemoryMethods fetchPhotoFromMemoryTask) {
        mFetchPhotoTask = fetchPhotoFromMemoryTask;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mFetchPhotoTask.setFetchPhotoFromMemoryThread(Thread.currentThread());

        String path = mFetchPhotoTask.getFetchPhotoPath();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        mFetchPhotoTask.setFetchPhotoFromMemoryResult(bitmap);
    }
}
