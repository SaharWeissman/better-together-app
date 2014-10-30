package com.example.better_together.Views.models;

import android.graphics.Bitmap;
import com.example.better_together.BTConstants;

/**
 * Created by ssdd on 10/29/14.
 */
public class UserPhotos {

    private User mUser;
    private Bitmap[] mPhotos = new Bitmap[BTConstants.NUM_OF_PHOTOS_PER_USER];
    private String[] mCaptions = new String[BTConstants.NUM_OF_PHOTOS_PER_USER];
    private String[] mCreationDates = new String[BTConstants.NUM_OF_PHOTOS_PER_USER];

    public UserPhotos(User user, Bitmap[] photos,String[] captions, String[] creationDates){
        this.mUser = user;
        this.mPhotos = photos;
        this.mCaptions = captions;
        this.mCreationDates = creationDates;
    }

    public User getUser(){return this.mUser;}
    public Bitmap[] getPhotos(){return this.mPhotos;}
    public String[] getCaptions(){return this.mCaptions;}
    public String[] getCreationDates(){return this.mCreationDates;}

    public void setUser(User user){
        this.mUser = user;
    }
    public void setPhotos(Bitmap[] photos){
        this.mPhotos = photos;
    }
    public void setCaptions(String[] captions){this.mCaptions = captions;}
    public void setCreationDates(String[] creationDates){this.mCreationDates= creationDates;}
}
