package com.example.better_together.ThreadPool.fetchPhoto.profilePicture.fromURL;

import android.graphics.Bitmap;
import com.example.better_together.ThreadPool.ThreadPoolManager;
import com.example.better_together.Views.adapters.UsersAdapter;
import com.example.better_together.Views.models.User;

import java.net.URL;

/**
 * Created by ssdd on 10/28/14.
 */
public class FetchPhotoFromURLTask implements ITaskFetchPhotoFromURLMethods {

    private final FetchPhotoFromURLRunnable mFetchPhotoRunnable;
    private final ThreadPoolManager mPhotosManager;
    private Thread mCurrentThread;
    private Bitmap mFetchPhotoResult;
    private URL mFetchPhotoURL;
    private User mUser;
    private UsersAdapter mUsersAdapter;

    public FetchPhotoFromURLTask(){
        mFetchPhotoRunnable = new FetchPhotoFromURLRunnable(this);
        mPhotosManager = ThreadPoolManager.getInstance();
    }

    @Override
    public void setFetchPhotoFromURLThread(Thread thread) {
        this.mCurrentThread = thread;
    }

    @Override
    public void setFetchPhotoFromURLResult(Bitmap result) {
        this.mFetchPhotoResult = result;
        mPhotosManager.handleFetchPhotoFromURLTaskResponse(this);
    }

    @Override
    public URL getFetchPhotoURL() {
        return this.mFetchPhotoURL;
    }

    @Override
    public Bitmap getFetchPhotoResponse() {
        return this.mFetchPhotoResult;
    }

    public Runnable getFetchPhotoRunnable(){
        return this.mFetchPhotoRunnable;
    }

    public void initFetchPhotoTask(URL fetchPhotoURL,User user,UsersAdapter adapter){
        this.mFetchPhotoURL = fetchPhotoURL;
        this.mUser = user;
        this.mUsersAdapter = adapter;
    }

    public User getUser(){
        return this.mUser;
    }

    public UsersAdapter getUsersAdapter(){
        return this.mUsersAdapter;
    }
}
