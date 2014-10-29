package com.example.better_together.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.example.better_together.BTConstants;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by ssdd on 10/18/14.
 */
public class HttpRequestHelper implements IHttpRequestHelper {
    private static final String TAG = HttpRequestHelper.class.getName();

    @Override
    public JSONObject makeGetRequest(URL url) {
        JSONObject res = null;
        if(url != null){
            try {
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                int responseCode = urlConnection.getResponseCode();
                Log.d(TAG,"got responseCode: " + responseCode);
                if(responseCode == HttpStatus.SC_OK) {
                    String responseString = readStream(urlConnection.getInputStream());
                    Log.d(TAG, "got response: " + responseString);
                    res = new JSONObject(responseString);
//                    tryFetchingProfilePics(res,0, BTConstants.SEARCH_USERS_RESULTS_COUNT);
                }
            } catch (IOException e) {
                Log.e(TAG,"caught IOException",e);
            } catch (JSONException e) {
                Log.e(TAG,"caught JSONException,returning null",e);
            }
        }
        else{
            Log.d(TAG,"url is null");
        }
        return res;
    }

    @Override
    public Bitmap fetchPhotoFromURL(URL url) {
        Bitmap result = null;
        if(url != null) {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                InputStream in = httpURLConnection.getInputStream();
                result = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
               Log.e(TAG,"caught IOException while trying to fetch photo from url: " + url,e);
               result = null;
            }
        }else{
            Log.d(TAG,"url is null");
        }
        return result;
    }

    public void tryFetchingProfilePics(JSONObject res,int offset,int count) {
        // {"data":[{"id":"278657633","profile_picture":"http:\/\/images.ak.instagram.com\/profiles\/profile_278657633_75sq_1356896202.jpg","username":"ssdd1886","bio":"","website":"","full_name":"Sahar Weissman"}],"meta":{"code":200}}
        try {
            JSONArray dataArray = res.getJSONArray(BTConstants.JSON_ATTR_DATA);
            for(int i =offset; i< count;i++){
                JSONObject userJSON = dataArray.getJSONObject(i);
                String profilePicUrlStr = userJSON.getString(BTConstants.JSON_ATTR_USER_PROFILE_PIC_URL);
                if(profilePicUrlStr != null){
                    Bitmap profilePic = getUserProfilePic(profilePicUrlStr);
                    if(profilePic != null){
                        userJSON.put(BTConstants.JSON_ATTR_USER_PROFILE_PIC_URL,profilePic);
                    }else{
                        Log.d(TAG,"could not fetch profile pic of user: " + userJSON.getString(BTConstants.JSON_ATTR_USERNAME));
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG,"unable to fetch profile pics - jsonException",e);
        }
    }

    private Bitmap getUserProfilePic(String profilePicUrlStr) {
        Bitmap profilePic = null;
        try {
            URL profilePicURL = new URL(profilePicUrlStr);
            InputStream inputStream = profilePicURL.openStream();
            profilePic = BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            Log.d(TAG,"cannot download profile picture - bad url",e);
            profilePic = null;
        } catch (IOException e) {
            Log.d(TAG, "cannot download profile picture - cannot open connection", e);
            profilePic = null;
        }
        return profilePic;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
