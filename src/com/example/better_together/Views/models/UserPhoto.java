package com.example.better_together.Views.models;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by ssdd on 10/29/14.
 */
public class UserPhoto {

    private User mUser;
    private Bitmap mPhoto;
    private String mCaption;
    private Date mCreationDate;

    public UserPhoto(User user, Bitmap photo, String caption, Date creationDate){
        this.mUser = user;
        this.mPhoto = photo;
        this.mCaption = caption;
        this.mCreationDate = creationDate;
    }

    public User getUser(){return this.mUser;}
    public Bitmap getPhoto(){return this.mPhoto;}
    public String getCaption(){return this.mCaption;}
    public Date getCreationDate(){return this.mCreationDate;}

    public void setUser(User user){
        this.mUser = user;
    }
    public void setPhoto(Bitmap photo){
        this.mPhoto = photo;
    }
    public void setCaption(String caption){this.mCaption = caption;}
    public void setCreationDate(Date creationDate){this.mCreationDate = creationDate;}
}
