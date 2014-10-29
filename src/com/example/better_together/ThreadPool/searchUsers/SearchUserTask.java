package com.example.better_together.ThreadPool.searchUsers;

import com.example.better_together.ThreadPool.ThreadPoolManager;
import com.example.better_together.Views.adapters.UsersAdapter;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by ssdd on 10/22/14.
 */
public class SearchUserTask implements ITaskSearchUserMethods{
    private ThreadPoolManager mUserManager;
    private final SearchUserRunnable mSearchUserRunnable;
    private Thread mCurrentThread;
    private JSONObject mResponseJSON;
    private URL mSearchUserURL;
    private UsersAdapter mResultsListAdapter;

    public SearchUserTask(){
        mSearchUserRunnable = new SearchUserRunnable(this);
        mUserManager = ThreadPoolManager.getInstance();
    }


    @Override
    public void setUserSearchResponse(JSONObject responseAsJSON) {
        this.mResponseJSON = responseAsJSON;
        mUserManager.handleSearchUserTaskResponse(this);
    }

    @Override
    public void setSearchUserThread(Thread thread) {
        mCurrentThread = thread;
    }

    @Override
    public URL getSearchUserURL() {
        return mSearchUserURL;
    }

    public void initSearchUserTask(URL searchUserURL,UsersAdapter resultsListAdapter){
        mSearchUserURL = searchUserURL;
        mResultsListAdapter = resultsListAdapter;
    }

    public Runnable getSearchUserRunnable(){
        return this.mSearchUserRunnable;
    }

    public JSONObject getResponseJSON(){
        return this.mResponseJSON;
    }

    public UsersAdapter getResultsListAdapter(){return this.mResultsListAdapter;}
}
