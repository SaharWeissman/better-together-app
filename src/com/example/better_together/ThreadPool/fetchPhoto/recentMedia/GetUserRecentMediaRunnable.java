package com.example.better_together.ThreadPool.fetchPhoto.recentMedia;

import android.os.Process;
import android.util.Log;
import com.example.better_together.BTConstants;
import com.example.better_together.network.HttpRequestHelper;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by ssdd on 10/29/14.
 */
public class GetUserRecentMediaRunnable implements Runnable {

    private static final String TAG = GetUserRecentMediaRunnable.class.getName();
    private final ITaskGetUserRecentMediaMethods mUserRecentMediaTask;
    private final HttpRequestHelper mHttpRequestHelper;

    public GetUserRecentMediaRunnable(ITaskGetUserRecentMediaMethods userRecentMediaTask){
        this.mUserRecentMediaTask = userRecentMediaTask;
        this.mHttpRequestHelper = new HttpRequestHelper();
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        mUserRecentMediaTask.setGetUserRecentMediaThread(Thread.currentThread());

        URL userRecentMediaURL = mUserRecentMediaTask.getUserRecentMediaURL();
        if(userRecentMediaURL == null){
            throw new IllegalArgumentException("url cannot be null");
        }
        JSONObject responseAsJson = mHttpRequestHelper.makeGetRequest(userRecentMediaURL);
        int index = mUserRecentMediaTask.getIndex(); //index of image
        try {
            if(responseAsJson.optJSONArray(BTConstants.JSON_ATTR_DATA) != null) {
                JSONArray imagesJSONS = responseAsJson.optJSONArray(BTConstants.JSON_ATTR_DATA);
                JSONObject imageJSON = imagesJSONS.getJSONObject(index);
                URL imageURL = extractPhotoURLFromImageJSON(imageJSON);
                String caption = extractCaptionFromJSON(imageJSON);
                Date creationDate = extractCreationDateFromJSON(imageJSON);
                String likes = extractLikesNumFromJSON(imageJSON);
                mUserRecentMediaTask.setGetUserRecentMediaResponse(imageURL, caption, creationDate,likes);
            }else if(responseAsJson.getInt(BTConstants.HTTP_STATUS_CODE) == HttpStatus.SC_BAD_REQUEST){
                Log.d(TAG,"got bad request code");
                mUserRecentMediaTask.setGetUserRecentMediaResponse(null, "BAD REQUEST", null, null);
            }
        }catch (JSONException e) {
            Log.e(TAG, "cannot get image json");
            mUserRecentMediaTask.setGetUserRecentMediaResponse(null, "JSONException", null, null);
        }
    }

    private String extractLikesNumFromJSON(JSONObject imageJSON) {
        String likesNum;
        try{
            JSONObject likesFromJSON = imageJSON.getJSONObject(BTConstants.JSON_ATTR_LIKES);
            likesNum = likesFromJSON.getString(BTConstants.JSON_ATTR_LIKES_COUNT);
        }catch(JSONException e){
            Log.e(TAG,"unable to get likes number from json",e);
            likesNum = null;
        }
        return likesNum;
    }

    private Date extractCreationDateFromJSON(JSONObject imageJSON) {
        Date res = null;
        try{
            String createdTimeTimestamp = imageJSON.getString(BTConstants.JSON_ATTR_CREATED_TIME);
            res =new java.util.Date(Long.parseLong(createdTimeTimestamp)*1000);
        }catch(JSONException e){
            Log.e(TAG,"unable to extract image url from json",e);
        }
        return res;
    }

    private String extractCaptionFromJSON(JSONObject imageJSON) {
        String res = null;
        try{
            if(imageJSON != null) {
                JSONObject caption = imageJSON.optJSONObject(BTConstants.JSON_ATTR_CAPTION);
                if (caption != null) {
                    String text = caption.getString(BTConstants.JSON_ATTR_CAPTION_TEXT);
                    res = text;
                }
            }
        }catch(JSONException e){
            Log.e(TAG,"unable to extract caption text from json",e);
        }
        return res;
    }

    private URL extractPhotoURLFromImageJSON(JSONObject imageJSON) {
        URL res = null;
        try{
            JSONObject images = imageJSON.getJSONObject(BTConstants.JSON_ATTR_IMAGES);
            JSONObject standardResImage = images.getJSONObject(BTConstants.JSON_ATTR_LOW_RESOLUTION);
            String urlStr = standardResImage.getString(BTConstants.JSON_ATTR_URL);
            res = new URL(urlStr);
        }catch(JSONException e){
            Log.e(TAG,"unable to extract image url from json",e);
        } catch (MalformedURLException e) {
            Log.e(TAG,"unable to create url for image",e);
        }
        return res;
    }
}
