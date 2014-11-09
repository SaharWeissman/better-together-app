package com.example.better_together.ThreadPool.fetchPhoto.recentMedia;

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
    private URL mUserPhotoURL;
    private String mUserPhotosCaption;
    private Date mUserPhotosCreationDate;
    private int mIndex;
    private String mUserPhotoLikesNum;

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
    public void setGetUserRecentMediaResponse(URL photoURL, String caption, Date creationDate, String likesNum) {
        this.mUserPhotoURL = photoURL;
        this.mUserPhotosCaption = caption;
        this.mUserPhotosCreationDate = creationDate;
        this.mUserPhotoLikesNum = likesNum;
        mUserManager.handleGetUserRecentMediaTaskResponse(this);
    }

    public ArrayAdapter getArrayAdapter(){
        return this.mUserPhotosAdapter;
    }

    public URL getUserPhotoURL(){
        return this.mUserPhotoURL;
    }

    public String getUserPhotoCaption(){return this.mUserPhotosCaption;}

    public Date getUserPhotoCreationDate(){return this.mUserPhotosCreationDate;}

    public String getUserPhotoLikesNum(){return  this.mUserPhotoLikesNum;}

    public UserPhoto getUserPhoto(){
        return this.mUserPhoto;
    }

    public Runnable getUserRecentMediaTaskRunnable(){
        return this.mGetUserRecentMediaRunnable;
    }
}
