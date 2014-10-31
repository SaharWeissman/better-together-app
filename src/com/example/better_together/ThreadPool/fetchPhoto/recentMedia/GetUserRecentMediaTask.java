package com.example.better_together.ThreadPool.fetchPhoto.recentMedia;

import android.graphics.Bitmap;
import android.widget.ArrayAdapter;
import com.example.better_together.ThreadPool.ThreadPoolManager;
import com.example.better_together.Views.models.UserPhoto;

import java.net.URL;
import java.util.Date;

/**
 * Created by ssdd on 10/29/14.
 */
public class GetUserRecentMediaTask implements ITaskGetUserRecentMediaMethods {

    private final GetUserRecentMediaRunnable mGetUserRecentMediaRunnable;
    private final ThreadPoolManager mUserManager;
    private URL mGetUserRecentMediaURL;
    private Thread mCurrentThread;
    private ArrayAdapter mUserPhotosAdapter;
    private UserPhoto mUserPhoto;
    private Bitmap mUserPhotosBitmap;
    private String mUserPhotosCaption;
    private Date mUserPhotosCreationDate;
    private int mIndex;

    public GetUserRecentMediaTask(){
        this.mGetUserRecentMediaRunnable = new GetUserRecentMediaRunnable(this);
        this.mUserManager = ThreadPoolManager.getInstance();
    }

    public void initGetUserRecentMediaTask(URL getUserRecentMediaURL,UserPhoto userPhoto,ArrayAdapter adapter, int index){
        this.mGetUserRecentMediaURL = getUserRecentMediaURL;
        this.mUserPhotosAdapter = adapter;
        this.mUserPhoto = userPhoto;
        this.mIndex = index;
    }

    @Override
    public void setGetUserRecentMediaThread(Thread thread) {
        this.mCurrentThread = thread;
    }

    @Override
    public URL getUserRecentMediaURL() {
        return mGetUserRecentMediaURL;
    }

    @Override
    public int getIndex() {
        return mIndex;
    }

    @Override
    public void setGetUserRecentMediaResponse(Bitmap photo,String caption,Date creationDate) {
        this.mUserPhotosBitmap = photo;
        this.mUserPhotosCaption = caption;
        this.mUserPhotosCreationDate = creationDate;
        mUserManager.handleGetUserRecentMediaTaskResponse(this);
    }

    public ArrayAdapter getArrayAdapter(){
        return this.mUserPhotosAdapter;
    }

    public Bitmap getUserPhotosBitmap(){
        return this.mUserPhotosBitmap;
    }

    public String getUserPhotosCaption(){return this.mUserPhotosCaption;}

    public Date getUserPhotosCreationDate(){return this.mUserPhotosCreationDate;}

    public UserPhoto getUserPhoto(){
        return this.mUserPhoto;
    }

    public Runnable getUserRecentMediaTaskRunnable(){
        return this.mGetUserRecentMediaRunnable;
    }
}
