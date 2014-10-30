package com.example.better_together.ThreadPool.fetchPhoto.recentMedia;

import android.graphics.Bitmap;
import android.widget.ArrayAdapter;
import com.example.better_together.ThreadPool.ThreadPoolManager;
import com.example.better_together.Views.models.UserPhotos;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by ssdd on 10/29/14.
 */
public class GetUserRecentMediaTask implements ITaskGetUserRecentMediaMethods {

    private final GetUserRecentMediaRunnable mGetUserRecentMediaRunnable;
    private final ThreadPoolManager mUserManager;
    private URL mGetUserRecentMediaURL;
    private Thread mCurrentThread;
    private JSONObject mUserRecentMediaResponse;
    private ArrayAdapter mUserPhotosAdapter;
    private UserPhotos mUserPhotos;
    private Bitmap[] mUserPhotosBitmapArray;
    private String[] mUserPhotosCaptionsArray;
    private String[] mUserPhotosCreationDatesArray;

    public GetUserRecentMediaTask(){
        this.mGetUserRecentMediaRunnable = new GetUserRecentMediaRunnable(this);
        this.mUserManager = ThreadPoolManager.getInstance();
    }

    public void initGetUserRecentMediaTask(URL getUserRecentMediaURL,UserPhotos userPhotos,ArrayAdapter adapter){
        this.mGetUserRecentMediaURL = getUserRecentMediaURL;
        this.mUserPhotosAdapter = adapter;
        this.mUserPhotos = userPhotos;
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
    public void setGetUserRecentMediaResponse(Bitmap[] photos,String[] captions,String[] creationDates) {
        this.mUserPhotosBitmapArray = photos;
        this.mUserPhotosCaptionsArray = captions;
        this.mUserPhotosCreationDatesArray = creationDates;
        mUserManager.handleGetUserRecentMediaTaskResponse(this);
    }

    public ArrayAdapter getArrayAdapter(){
        return this.mUserPhotosAdapter;
    }

    public Bitmap[] getUserPhotosBitmapArray(){
        return this.mUserPhotosBitmapArray;
    }

    public String[] getUserPhotosCaptionsArray(){return this.mUserPhotosCaptionsArray;}

    public String[] getUserPhotosCreationDatesArray(){return this.mUserPhotosCreationDatesArray;}

    public UserPhotos getUserPhotos(){
        return this.mUserPhotos;
    }

    public Runnable getUserRecentMediaTaskRunnable(){
        return this.mGetUserRecentMediaRunnable;
    }
}
