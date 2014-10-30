package com.example.better_together.ThreadPool.fetchPhoto.recentMedia;

import android.graphics.Bitmap;
import android.os.Process;
import android.util.Log;
import com.example.better_together.BTConstants;
import com.example.better_together.network.HttpRequestHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

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
        URL[] photosUrls = new URL[BTConstants.NUM_OF_PHOTOS_PER_USER];
        String[] captions = new String[BTConstants.NUM_OF_PHOTOS_PER_USER];
        String[] creationDates = new String[BTConstants.NUM_OF_PHOTOS_PER_USER];
        extractRecentMediaValues(responseAsJson,photosUrls,captions,creationDates);
        Bitmap[] userPhotos = new Bitmap[photosUrls.length];
        for(int i =0; i < photosUrls.length; i++){
            userPhotos[i] = mHttpRequestHelper.fetchPhotoFromURL(photosUrls[i]);
        }
        mUserRecentMediaTask.setGetUserRecentMediaResponse(userPhotos,captions,creationDates);
    }

    private void extractRecentMediaValues(JSONObject responseAsJson,URL[] photosURLS,String[] captions,String[] creationDates) {
//        {
//            "tags": [
//
//            ],
//            "location": null,
//                "link": "http:\/\/instagram.com\/p\/uwLKYwLF4L\/",
//                "user_has_liked": false,
//                "caption": {
//            "id": "842222224188136918",
//                    "created_time": "1414620734",
//                    "text": "❤️💐❤️",
//                    "from": {
//                "id": "26587838",
//                        "profile_picture": "http:\/\/photos-d.ak.instagram.com\/hphotos-ak-xfa1\/10684292_737635552973923_1352355841_a.jpg",
//                        "username": "danathersh",
//                        "full_name": "Dana Hershko"
//            }
//        },
//            "type": "image",
//                "id": "842222222602690059_26587838",
//                "likes": {
//            "data": [
//            {
//                "id": "183816500",
//                    "profile_picture": "http:\/\/images.ak.instagram.com\/profiles\/profile_183816500_75sq_1387202916.jpg",
//                    "username": "zoharshemesh",
//                    "full_name": "Zohar Shemesh"
//            },
//            {
//                "id": "43701540",
//                    "profile_picture": "http:\/\/photos-f.ak.instagram.com\/hphotos-ak-xap1\/924579_538957609538333_211316120_a.jpg",
//                    "username": "paulinamaydel",
//                    "full_name": "Paulina Maydel"
//            },
//            {
//                "id": "56084589",
//                    "profile_picture": "http:\/\/images.ak.instagram.com\/profiles\/profile_56084589_75sq_1346525818.jpg",
//                    "username": "sivanlopezberger",
//                    "full_name": "sivanlopezberger"
//            },
//            {
//                "id": "284109791",
//                    "profile_picture": "http:\/\/photos-b.ak.instagram.com\/hphotos-ak-xap1\/923823_932845006742801_1066598097_a.jpg",
//                    "username": "lironmeiri",
//                    "full_name": "Liron Meiri"
//            }
//            ],
//            "count": 9
//        },
//            "images": {
//            "low_resolution": {
//                "url": "http:\/\/scontent-a.cdninstagram.com\/hphotos-xaf1\/t51.2885-15\/10747852_341120916067068_937922566_a.jpg",
//                        "height": 306,
//                        "width": 306
//            },
//            "standard_resolution": {
//                "url": "http:\/\/scontent-a.cdninstagram.com\/hphotos-xaf1\/t51.2885-15\/10747852_341120916067068_937922566_n.jpg",
//                        "height": 640,
//                        "width": 640
//            },
//            "thumbnail": {
//                "url": "http:\/\/scontent-a.cdninstagram.com\/hphotos-xaf1\/t51.2885-15\/10747852_341120916067068_937922566_s.jpg",
//                        "height": 150,
//                        "width": 150
//            }
//        },
//            "users_in_photo": [
//
//            ],
//            "created_time": "1414620734",
//                "user": {
//            "id": "26587838",
//                    "profile_picture": "http:\/\/photos-d.ak.instagram.com\/hphotos-ak-xfa1\/10684292_737635552973923_1352355841_a.jpg",
//                    "username": "danathersh",
//                    "bio": "",
//                    "website": "",
//                    "full_name": "Dana Hershko"
//        },
//            "filter": "Amaro",
//                "comments": {
//            "data": [
//
//            ],
//            "count": 0
//        },
//            "attribution": null
//        }
        int numOfPhotosPerUser = BTConstants.NUM_OF_PHOTOS_PER_USER;
        try{
            JSONArray imagesJSONS = responseAsJson.getJSONArray(BTConstants.JSON_ATTR_DATA);
            for(int i = 0; i < numOfPhotosPerUser; i ++){
                JSONObject imageJSON = imagesJSONS.getJSONObject(i);

                // extract image standard res. url
                JSONObject images = imageJSON.getJSONObject(BTConstants.JSON_ATTR_IMAGES);
                JSONObject standardResolution = images.getJSONObject(BTConstants.JSON_ATTR_STANDARD_RESOLUTION);
                String url = standardResolution.getString(BTConstants.JSON_ATTR_URL);
                photosURLS[i] = new URL(url);

                // extract image caption text
                JSONObject caption = imageJSON.getJSONObject(BTConstants.JSON_ATTR_CAPTION);
                String text = caption.getString(BTConstants.JSON_ATTR_CAPTION_TEXT);
                captions[i] = text;

                // extract image creation date
                String createdTimestampStr = caption.getString(BTConstants.JSON_ATTR_CAPTION_CREATED_TIME);
                java.util.Date time=new java.util.Date((long)Integer.parseInt(createdTimestampStr)*1000);
                creationDates[i] = time.toString();
            }
        }catch(JSONException e){
            Log.e(TAG,"unable to extract images from data",e);
        } catch (MalformedURLException e) {
            Log.e(TAG, "unable to create standard resolution image url from string", e);
        }
    }
}
