package com.example.better_together.Views.models;

import android.graphics.Bitmap;
import com.example.better_together.BTConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ssdd on 10/22/14.
 */
public class User extends ViewItem {
    private String mGroupName;
    private String mUserName;
    private String mFullName;
    private String mBio;
    private String mWebsite;

    private String mProfilePictureURL;
    private String mID;

    private Bitmap mProfilePic;
    private String[] mRecentMediaURLArray;

    public User(String username,String fullName,String bio,String website,String profilePicURL,String id,String groupName,String[] recentMediaURLArray){
        mUserName = username;
        mFullName = fullName;
        mBio = bio;
        mWebsite = website;
        mProfilePictureURL = profilePicURL;
        mID = id;
        mGroupName = groupName;
        mRecentMediaURLArray = recentMediaURLArray;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getUserBio() {
        return mBio;
    }

    public String getUserWebsite() {
        return mWebsite;
    }

    public String getProfilePicURL() {
        return mProfilePictureURL;
    }

    public String getID() {
        return mID;
    }

    public String getGroupName(){ return mGroupName;}

    public Bitmap getProfilePic(){return mProfilePic;}

    public String[] getRecentMediaURLArray(){return mRecentMediaURLArray;}

    public void setProfilePic(Bitmap profilePic){
        this.mProfilePic = profilePic;
    }

    public void setRecentMediaURLArray(String[] recentMediaURLArray){
        this.mRecentMediaURLArray = recentMediaURLArray;
    }

    public static User createFromJSON(JSONObject userJSON) {
        try {
            String username = userJSON.getString(BTConstants.JSON_ATTR_USERNAME);
            String userFullName = userJSON.getString(BTConstants.JSON_ATTR_USER_FULL_NAME);
            String userID = userJSON.getString(BTConstants.JSON_ATTR_USER_ID);
            String userBio = userJSON.getString(BTConstants.JSON_ATTR_USER_BIO);
            String userWebsite = userJSON.getString(BTConstants.JSON_ATTR_USER_WEBSITE);
            String userProfilePicUrl = userJSON.getString(BTConstants.JSON_ATTR_USER_PROFILE_PIC_URL);
            String userGroupName = userJSON.optString(BTConstants.JSON_ATTR_USER_GROUP_NAME);
            return new User(username, userFullName, userBio, userWebsite, userProfilePicUrl, userID, userGroupName, null);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
