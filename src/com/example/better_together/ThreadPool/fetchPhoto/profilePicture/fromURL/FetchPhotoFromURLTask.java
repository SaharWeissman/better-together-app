package com.example.better_together.ThreadPool.fetchPhoto.profilePicture.fromURL;

import android.graphics.Bitmap;
import android.widget.ArrayAdapter;
import com.example.better_together.ThreadPool.ThreadPoolManager;
import com.example.better_together.Views.adapters.UsersAdapter;
import com.example.better_together.Views.models.User;
import com.example.better_together.Views.models.UserPhoto;
import com.example.better_together.storage.SharedPrefStorage;

import java.net.URL;

/**
 * Created by ssdd on 10/28/14.
 */
public class FetchPhotoFromURLTask implements ITaskFetchPhotoFromURLMethods {

    // to distinguish later in thread pool whether this picture is profile pic. of user
    // or recent media picture of user
    public enum PhotoType{
        PROFILE_PIC,
        USER_PHOTO
    }

    private final FetchPhotoFromURLRunnable mFetchPhotoRunnable;
    private final ThreadPoolManager mPhotosManager;
    private Thread mCurrentThread;
    private Bitmap mFetchPhotoResult;
    private URL mFetchPhotoURL;
    private User mUser;
    private ArrayAdapter mUsersAdapter;
    private PhotoType mPhotoType;
    private UserPhoto mUserPhoto;

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

    @Override
    public User getUser(){
        return this.mUser;
    }

    public Runnable getFetchPhotoRunnable(){
        return this.mFetchPhotoRunnable;
    }

    public void initFetchPhotoTask(URL fetchPhotoURL,User user,ArrayAdapter adapter,PhotoType type){
        this.mFetchPhotoURL = fetchPhotoURL;
        this.mUser = user;
        this.mUsersAdapter = adapter;
        this.mPhotoType = type;
    }

    public void initFetchPhotoTask(URL fetchPhotoURL,User user,ArrayAdapter adapter,PhotoType type, UserPhoto userPhoto){
        this.mUserPhoto = userPhoto;
        initFetchPhotoTask(fetchPhotoURL,user,adapter,type);
    }

    public ArrayAdapter getUsersAdapter(){
        return this.mUsersAdapter;
    }

    public PhotoType getPhotoType(){
        return mPhotoType;
    }

    public UserPhoto getUserPhoto(){
        return mUserPhoto;
    }
}
