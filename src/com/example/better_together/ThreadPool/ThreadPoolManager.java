package com.example.better_together.ThreadPool;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import com.example.better_together.BTConstants;
import com.example.better_together.ThreadPool.fetchPhoto.profilePicture.fromMemory.FetchPhotoFromMemoryTask;
import com.example.better_together.ThreadPool.fetchPhoto.profilePicture.fromURL.FetchPhotoFromURLTask;
import com.example.better_together.ThreadPool.fetchPhoto.recentMedia.GetUserRecentMediaTask;
import com.example.better_together.ThreadPool.searchUsers.SearchUserRunnable;
import com.example.better_together.ThreadPool.searchUsers.SearchUserTask;
import com.example.better_together.Views.adapters.UsersAdapter;
import com.example.better_together.Views.models.User;
import com.example.better_together.Views.models.UserPhotos;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by ssdd on 10/22/14.
 */
public class ThreadPoolManager {

    private static final String TAG = SearchUserRunnable.class.getName();
    private static final String API_URL_PREFIX = "https://api.instagram.com/v1";

    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static final ThreadPoolManager sInstance;
    private static final int KEEP_ALIVE_TIME = 1; // time for each thread to keep alive when idle
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS; // time unit for keep alive
    private static String sAccessToken = null;
    private static boolean mFinishedLoadingUsers;
    private static ProgressDialog mSearchUsersProgressDialog;
    private static String mCurrentGroupName;

    private final BlockingQueue<Runnable> mSearchUsersRunnablesWorkQueue;
    private final BlockingQueue<Runnable> mFetchPhotosRunnablesWorkQueue;
    private final Handler mHandler;
    private final ThreadPoolExecutor mSearchUsersThreadPool;
    private final ThreadPoolExecutor mFetchPhotosThreadPool;
    private Queue<SearchUserTask> mSearchUserTaskWorkQueue;
    private Queue<FetchPhotoFromURLTask> mFetchUserProfilePicsFromURLWorkQueue;
    private Queue<FetchPhotoFromMemoryTask> mFetchUserProfilePicsFromMemoryWorkQueue;
    private Queue<GetUserRecentMediaTask> mFetchUserRecentMediaPhotosWorkQueue;
    private JSONObject mLastSearchUserResult;

    static {
        sInstance = new ThreadPoolManager();
    }

    private ThreadPoolManager(){
        // to handle messages and communicating with UI Thread
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message){
                int state = message.what;

                // hide dialog if showing
                if(mSearchUsersProgressDialog != null && mSearchUsersProgressDialog.isShowing()){
                    mSearchUsersProgressDialog.dismiss();
                }
                switch (state){
                    case BTConstants.MESSAGE_SEARCH_USER_NO_RESULTS_FOUND:{
                        Log.d(TAG,"in case MESSAGE_SEARCH_USER_NO_RESULTS_FOUND");
                        SearchUserTask task = (SearchUserTask)message.obj;
                        UsersAdapter resultsListAdapter = task.getResultsListAdapter();
                        resultsListAdapter.clear();
                        User empty = new User("No Results Found :(","","","",null,"","",null);
                        resultsListAdapter.add(empty);
                        break;
                    }
                    case BTConstants.MESSAGE_FOUND_RESULTS:{
                        Log.d(TAG,"in case MESSAGE_FOUND_RESULTS");
                        SearchUserTask task = (SearchUserTask)message.obj;
                        int offset = message.arg1;
                        int count = message.arg2;
                        mLastSearchUserResult = task.getResponseJSON();
                        Log.d(TAG,"ret= " + mLastSearchUserResult.toString());
                        UsersAdapter resultsListAdapter = task.getResultsListAdapter();
                        populateListWithResults(resultsListAdapter,mLastSearchUserResult,offset,count);
                        break;
                    }
                    case BTConstants.MESSAGE_NO_PROFILE_PIC_FROM_URL_FOUND:{
                        Log.d(TAG,"in case MESSAGE_NO_PROFILE_PIC_FROM_URL_FOUND");
                        break;
                    }
                    case BTConstants.MESSAGE_FOUND_PROFILE_PIC_FROM_URL:{
                        Log.d(TAG,"in case MESSAGE_FOUND_PROFILE_PIC_FROM_URL");
                        FetchPhotoFromURLTask task = (FetchPhotoFromURLTask)message.obj;
                        User user = task.getUser();
                        UsersAdapter adapter = task.getUsersAdapter();
                        user.setProfilePic(task.getFetchPhotoResponse());
                        adapter.notifyDataSetChanged();
                        break;
                    }
                    case BTConstants.MESSAGE_FOUND_PROFILE_PIC_FROM_MEMORY:{
                        Log.d(TAG,"in case MESSAGE_FOUND_PROFILE_PIC_FROM_MEMORY");
                        FetchPhotoFromMemoryTask task = (FetchPhotoFromMemoryTask)message.obj;
                        User user = task.getUser();
                        BaseAdapter adapter = task.getUsersAdapter();
                        user.setProfilePic(task.getFetchPhotoResponse());
                        adapter.notifyDataSetChanged();
                        break;
                    }
                    case BTConstants.MESSAGE_FOUND_USER_RECENT_MEDIA:{
                        Log.d(TAG,"in case MESSAGE_FOUND_USER_RECENT_MEDIA");
                        GetUserRecentMediaTask task = (GetUserRecentMediaTask)message.obj;
                        Bitmap[] photosBitmapArray = task.getUserPhotosBitmapArray();
                        String[] photosCaptions = task.getUserPhotosCaptionsArray();
                        String[] photosCreationDates = task.getUserPhotosCreationDatesArray();
                        UserPhotos userPhotos = task.getUserPhotos();
                        userPhotos.setPhotos(photosBitmapArray);
                        userPhotos.setCaptions(photosCaptions);
                        userPhotos.setCreationDates(photosCreationDates);
                        ArrayAdapter adapter = task.getArrayAdapter();
                        adapter.notifyDataSetChanged();
                        break;
                    }
                    default:{
                        break;
                    }
                }
            }
        };

        mSearchUserTaskWorkQueue = new LinkedBlockingQueue<SearchUserTask>(); //queue for search user tasks
        mFetchUserProfilePicsFromURLWorkQueue = new LinkedBlockingQueue<FetchPhotoFromURLTask>(); //queue for fetch profile pics tasks
        mFetchUserProfilePicsFromMemoryWorkQueue = new LinkedBlockingQueue<FetchPhotoFromMemoryTask>(); //queue for fetch profile pics tasks
        mFetchUserRecentMediaPhotosWorkQueue = new LinkedBlockingQueue<GetUserRecentMediaTask>();

        // SEARCH USERS
        // queue for runnable tasks
        mSearchUsersRunnablesWorkQueue = new LinkedBlockingQueue<Runnable>();
        mFetchPhotosRunnablesWorkQueue = new LinkedBlockingQueue<Runnable>();
        // Creates a thread pool manager
        mSearchUsersThreadPool = new ThreadPoolExecutor(
                NUMBER_OF_CORES,       // Initial pool size
                NUMBER_OF_CORES,       // Max pool size
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                mSearchUsersRunnablesWorkQueue);

        // FETCH PHOTOS
        //queue for runnable tasks
        mFetchPhotosThreadPool = new ThreadPoolExecutor(
                NUMBER_OF_CORES,
                NUMBER_OF_CORES,
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                mFetchPhotosRunnablesWorkQueue);
    }

    private static void populateListWithResults(UsersAdapter resultsListAdapter, JSONObject ret,int offest,int count) {
       // {"data":[{"id":"278657633","profile_picture":"http:\/\/images.ak.instagram.com\/profiles\/profile_278657633_75sq_1356896202.jpg","username":"ssdd1886","bio":"","website":"","full_name":"Sahar Weissman"}],"meta":{"code":200}}
        try {
            JSONArray dataArray = ret.getJSONArray(BTConstants.JSON_ATTR_DATA);
            for(int i = offest; i < count && i < dataArray.length(); i++){
                JSONObject userJSON = dataArray.getJSONObject(i);
                String username = userJSON.getString(BTConstants.JSON_ATTR_USERNAME);
                String userFullName = userJSON.getString(BTConstants.JSON_ATTR_USER_FULL_NAME);
                String userBio = userJSON.getString(BTConstants.JSON_ATTR_USER_BIO);
                String userWebsite = userJSON.getString(BTConstants.JSON_ATTR_USER_WEBSITE);
                String userProfilePicURL = userJSON.getString(BTConstants.JSON_ATTR_USER_PROFILE_PIC_URL);
                String userID = userJSON.getString(BTConstants.JSON_ATTR_USER_ID);
                User user = new User(username,userFullName,userBio,userWebsite,userProfilePicURL,userID,mCurrentGroupName,null);
                if(user.getProfilePic() == null) {
                    fetchUserProfilePicFromURL(userProfilePicURL, user, resultsListAdapter);
                }
                resultsListAdapter.add(user);
            }

        } catch (JSONException e) {
            Log.e(TAG,"caught json exception while parsing response",e);
            resultsListAdapter.add(new User("JSONException","","","",null,"-1","",null));
        } catch(ClassCastException e){
            Log.e(TAG,"cannot cast string to Bitmap",e);
            resultsListAdapter.add(new User("ClassCastException","","","",null,"-1","",null));
        }
    }

    public static ThreadPoolManager getInstance(){
        return sInstance;
    }

    public static SearchUserTask searchForUser(String username,String groupName,String accessToken,UsersAdapter resultsListAdapter,Context callingContext){
        mFinishedLoadingUsers = false;
        mCurrentGroupName = groupName;
        mSearchUsersProgressDialog = ProgressDialog.show(callingContext,"Searching for : \"" + username + "\"","Search in progress...",true);
        if (sAccessToken == null){
            sAccessToken = accessToken;
        }

        SearchUserTask searchUserTask = sInstance.mSearchUserTaskWorkQueue.poll();

        // if queue is empty
        if(searchUserTask == null){
            searchUserTask = new SearchUserTask();
        }
        URL url = buildURLForUserName(username);
        if(url == null){
          Log.d(TAG,"url is null");
        }else{
            searchUserTask.initSearchUserTask(url,resultsListAdapter);
            sInstance.mSearchUsersThreadPool.execute(searchUserTask.getSearchUserRunnable());
        }
        return searchUserTask;
    }

    public static void loadUsersToAdapter(final UsersAdapter adapter, final int offset){
        populateListWithResults(adapter,sInstance.mLastSearchUserResult,offset,offset + BTConstants.SEARCH_USERS_RESULTS_COUNT);
    }

    public static FetchPhotoFromURLTask fetchUserProfilePicFromURL(String profilePicURL, User user, UsersAdapter adapter){
        FetchPhotoFromURLTask fetchPhotoTask = sInstance.mFetchUserProfilePicsFromURLWorkQueue.poll();

        // if queue is empty
        if(fetchPhotoTask == null){
            fetchPhotoTask = new FetchPhotoFromURLTask();
        }
        URL url;
        try {
            url = new URL(profilePicURL);
        } catch (MalformedURLException e) {
            Log.e(TAG,"bad url for user profile pic",e);
            url = null;
        }

        if(url == null){
            Log.d(TAG,"url is null");
        }else{
            fetchPhotoTask.initFetchPhotoTask(url,user,adapter);
            sInstance.mFetchPhotosThreadPool.execute(fetchPhotoTask.getFetchPhotoRunnable());
        }
        return fetchPhotoTask;
    }

    public static FetchPhotoFromMemoryTask fetchUserProfilePicFromMemory(String profilePicPath, User user, ArrayAdapter adapter){
        FetchPhotoFromMemoryTask task = sInstance.mFetchUserProfilePicsFromMemoryWorkQueue.poll();

        if(task == null){
            task = new FetchPhotoFromMemoryTask();
        }

        if(profilePicPath == null){
            Log.d(TAG,"profile pic path is null");
        }else{
            task.initFetchPhotoTask(profilePicPath,user,adapter);
            sInstance.mFetchPhotosThreadPool.execute(task.getFetchPhotoFromMemorRunnable());
        }
        return task;
    }

    public static GetUserRecentMediaTask FetchUserRecentMediaPhotos(String userID, UserPhotos userPhotos,ArrayAdapter adapter,String accessToken){
        GetUserRecentMediaTask task = sInstance.mFetchUserRecentMediaPhotosWorkQueue.poll();
        if(task == null){
            task = new GetUserRecentMediaTask();
        }
        task.initGetUserRecentMediaTask(buildURLForUserRecentMedia(userID,accessToken),userPhotos,adapter);
        sInstance.mFetchPhotosThreadPool.execute(task.getUserRecentMediaTaskRunnable());
        return task;
    }

    public void handleSearchUserTaskResponse(SearchUserTask task) {
        if(task.getResponseJSON() == null){
            Log.d(TAG,"response is null");
            Message noResultsFoundMessage = mHandler.obtainMessage(BTConstants.MESSAGE_SEARCH_USER_NO_RESULTS_FOUND,null);
            noResultsFoundMessage.sendToTarget();
        }else{
            Log.d(TAG, "got response: " + task.getResponseJSON().toString());
            Message resultsFoundMessage = mHandler.obtainMessage(BTConstants.MESSAGE_FOUND_RESULTS,0,BTConstants.SEARCH_USERS_RESULTS_COUNT,task);
            resultsFoundMessage.sendToTarget();
        }
    }

    public void handleFetchPhotoFromURLTaskResponse(FetchPhotoFromURLTask task){
        if(task.getFetchPhotoResponse() == null){
            Log.d(TAG, "response is null");
        }else{
            Log.d(TAG,"found profile picture");
            Message fetchProfilePicMessage = mHandler.obtainMessage(BTConstants.MESSAGE_FOUND_PROFILE_PIC_FROM_URL,task);
            fetchProfilePicMessage.sendToTarget();
        }
    }

    public void handleFetchPhotoFromMemoryTaskResponse(FetchPhotoFromMemoryTask task){
        if(task.getFetchPhotoResponse() == null){
            Log.d(TAG, "cannot find profile pic in memory");
        }else{
            Log.d(TAG,"found profile picture in memory");
            Message foundProfilePicInMemoryMsg = mHandler.obtainMessage(BTConstants.MESSAGE_FOUND_PROFILE_PIC_FROM_MEMORY,task);
            foundProfilePicInMemoryMsg.sendToTarget();
        }
    }

    public void handleGetUserRecentMediaTaskResponse(GetUserRecentMediaTask task){
        if(task.getUserPhotosBitmapArray() == null){
            Log.d(TAG,"cannot fetch user recent media");
        }else{
            Log.d(TAG,"found user recent media");
            Message foundUserRecentMediaMsg = mHandler.obtainMessage(BTConstants.MESSAGE_FOUND_USER_RECENT_MEDIA,task);
            foundUserRecentMediaMsg.sendToTarget();
        }
    }

    private static URL buildURLForUserName(String username) {
        URL res;
        try {
            res = new URL(String.format("%s/users/search?q=%s&access_token=%s",API_URL_PREFIX,username,sAccessToken));
        } catch (MalformedURLException e) {
            Log.e(TAG,"bad url",e);
            res = null;
        }
        return res;
    }

    private static URL buildURLForUserRecentMedia(String userID,String accessToken){
        URL res;
        try{
            res = new URL(String.format("%s/users/%s/media/recent?access_token=%s",API_URL_PREFIX,userID,accessToken));
        }catch(MalformedURLException e){
            Log.d(TAG,"bad url",e);
            res = null;
        }
        return res;
    }
}