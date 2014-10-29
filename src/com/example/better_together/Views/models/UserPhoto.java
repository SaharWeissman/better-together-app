package com.example.better_together.Views.models;

import android.graphics.Bitmap;

/**
 * Created by ssdd on 10/29/14.
 */
public class UserPhoto {

    private User mUser;
    private Bitmap mPhoto;

    public UserPhoto(User user,Bitmap photo){
        this.mUser = user;
        this.mPhoto = photo;
    }

    public User getUser(){return this.mUser;}
    public Bitmap getPhoto(){return this.mPhoto;}

    public void setUser(User user){
        this.mUser = user;
    }

    public void setPhoto(Bitmap photo){
        this.mPhoto = photo;
    }
}
