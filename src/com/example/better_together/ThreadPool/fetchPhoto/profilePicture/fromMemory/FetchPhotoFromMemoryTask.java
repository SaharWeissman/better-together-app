package com.example.better_together.ThreadPool.fetchPhoto.profilePicture.fromMemory;

import android.graphics.Bitmap;
import android.widget.BaseAdapter;
import com.example.better_together.ThreadPool.ThreadPoolManager;
import com.example.better_together.Views.models.User;

/**
 * Created by ssdd on 10/29/14.
 */
public class FetchPhotoFromMemoryTask implements ITaskFetchPhotoFromMemoryMethods{

    private final FetchPhotoFromMemoryRunnable mFetchPhotoRunnable;
    private final ThreadPoolManager mPhotosManager;
    private String mPhotoFilePath;
    private User mUser;
    private BaseAdapter mUsersAdapter;
    private Thread mCurrentThread;
    private Bitmap mFetchPhotoResult;

    public FetchPhotoFromMemoryTask(){
        mFetchPhotoRunnable = new FetchPhotoFromMemoryRunnable(this);
        mPhotosManager = ThreadPoolManager.getInstance();
    }

    public void initFetchPhotoTask(String photoFilePath,User user,BaseAdapter adapter){
        this.mPhotoFilePath = photoFilePath;
        this.mUser = user;
        this.mUsersAdapter = adapter;
    }

    @Override
    public void setFetchPhotoFromMemoryThread(Thread thread) {
        this.mCurrentThread = thread;
    }

    @Override
    public void setFetchPhotoFromMemoryResult(Bitmap result) {
        this.mFetchPhotoResult = result;
        this.mPhotosManager.handleFetchPhotoFromMemoryTaskResponse(this);
    }

    @Override
    public String getFetchPhotoPath() {
        return this.mPhotoFilePath;
    }

    @Override
    public Bitmap getFetchPhotoResponse() {
        return mFetchPhotoResult;
    }

    public User getUser(){
        return mUser;
    }

    public BaseAdapter getUsersAdapter(){
        return mUsersAdapter;
    }

    public Runnable getFetchPhotoFromMemorRunnable(){
        return this.mFetchPhotoRunnable;
    }
}
