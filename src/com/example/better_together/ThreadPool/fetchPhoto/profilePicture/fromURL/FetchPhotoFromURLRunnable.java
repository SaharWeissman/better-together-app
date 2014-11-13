package com.example.better_together.ThreadPool.fetchPhoto.profilePicture.fromURL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Process;
import android.util.Log;
import com.example.better_together.BTConstants;
import com.example.better_together.Utils.FileHelper;
import com.example.better_together.Utils.SharedPrefHelper;
import com.example.better_together.network.HttpRequestHelper;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by ssdd on 10/23/14.
 */
public class FetchPhotoFromURLRunnable implements Runnable {

    private static final String TAG = FetchPhotoFromURLRunnable.class.getName();

    private final ITaskFetchPhotoFromURLMethods mFetchPhotoTask;
    private final HttpRequestHelper mHttpRequestHelper;

    public FetchPhotoFromURLRunnable(ITaskFetchPhotoFromURLMethods fetchPhotoTask){
        this.mFetchPhotoTask = fetchPhotoTask;
        this.mHttpRequestHelper = new HttpRequestHelper();
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        mFetchPhotoTask.setFetchPhotoFromURLThread(Thread.currentThread());

        URL fetchPhotoURL = mFetchPhotoTask.getFetchPhotoURL();
        if(fetchPhotoURL == null){
            throw new IllegalArgumentException("fetch photo url cannot be null");
        }
        FetchPhotoFromURLTask.PhotoType photoType = mFetchPhotoTask.getPhotoType();
        Bitmap fetchPhotoBitmap = null;
        switch (photoType){
            case PROFILE_PIC:{
                Log.d(TAG,"in PROFILE_PIC case");
                fetchPhotoBitmap = mHttpRequestHelper.fetchPhotoFromURL(fetchPhotoURL);
                if (fetchPhotoBitmap == null) {
                    Log.d(TAG, "got null bitmap");
                } else {
                    Log.d(TAG, "successfully fetched image from URL");
                }
                break;
            }
            case USER_PHOTO:{
//                Log.d(TAG,"in USER_PHOTO case");
                // first check if image from url already exist in memory
//                boolean picExistInMemory = false;
//                Set<String> fetchedPicsUrlsSet = SharedPrefHelper.getInstance().readStringSet(BTConstants.KEY_FETCHED_PIC_URLS);
//                if(fetchedPicsUrlsSet != null && !fetchedPicsUrlsSet.isEmpty()){
//                    Iterator<String> iter = fetchedPicsUrlsSet.iterator();
//                    while(iter.hasNext()){
//                        String savedFileName = iter.next();
//                        String path = fetchPhotoURL.getPath();
//                        String fileName = path.substring(path.lastIndexOf(File.separator)+1,path.length());
//                        if(savedFileName.equals(fileName)){
//                            picExistInMemory = true;
//                            break;
//                        }
//                    }
//                }

//                if(!picExistInMemory) {
                    fetchPhotoBitmap = mHttpRequestHelper.fetchPhotoFromURL(fetchPhotoURL);
//                    savePicture(fetchPhotoBitmap, fetchPhotoURL,fetchedPicsUrlsSet);
//                    if (fetchPhotoBitmap == null) {
//                        Log.d(TAG, "got null bitmap");
//                    } else {
//                        Log.d(TAG, "successfully fetched image from URL");
//                    }
//                }else{
                    // fetch from memory
//                    String picPath = fetchPhotoURL.getPath();
//                    String fileName = picPath.substring(picPath.lastIndexOf(File.separator)+1,picPath.length());
//                    String path = "/data/data/com.example.better_together/app_" + BTConstants.FETCHED_USER_PICTURE_DIRECTORY + "/" + fileName + ".jpg";
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                    fetchPhotoBitmap = BitmapFactory.decodeFile(path, options);
//                }
                break;
            }
            default:{
                Log.d(TAG,"in default case");
                break;
            }
        }
        mFetchPhotoTask.setFetchPhotoFromURLResult(fetchPhotoBitmap);
    }

    private void savePicture(Bitmap picBitmap, URL fetchPhotoURL,Set<String> fetchedUrlsSet) {

        // write to internal memory
        String path = fetchPhotoURL.getPath();
        String fileName = path.substring(path.lastIndexOf(File.separator)+1,path.length());
        FileHelper.writeFileToDirectory(picBitmap,BTConstants.FETCHED_USER_PICTURE_DIRECTORY,fileName, FileHelper.FileFormat.JPEG,Context.MODE_PRIVATE);

        // write fileName to sharedPref
        if(fetchedUrlsSet == null){
            fetchedUrlsSet = new HashSet<String>();
        }
        fetchedUrlsSet.add(fileName);
        SharedPrefHelper.getInstance().writeStringSet(BTConstants.KEY_FETCHED_PIC_URLS,fetchedUrlsSet);
    }
}
