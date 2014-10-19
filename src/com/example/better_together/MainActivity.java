package com.example.better_together;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    //============== CONSTANTS ============================
    private static final String TAG = MainActivity.class.getName();
    private static final String CLIENT_ID = "7575571c75b34969ac40ae01c2ac1e6d";
    private static final String REDIRECT_URI = "bettertogether://test/";
    private static final String INSTAGRAM_AUTHENTICATION_URL = String.format("https://instagram.com/oauth/authorize/?client_id=%s&redirect_uri=%s&response_type=token",CLIENT_ID,REDIRECT_URI);

    private static Context mActivity;

    //============== Members ============================
    private boolean mGotAccessToken = false;
    private String mAccessToken = null;

    private Button mBtnCreateGroups;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "in onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mActivity = this;
        initUIComponents();
        if(!mGotAccessToken) {
            getAccessToken();
        }
    }

    private void getAccessToken() {
        Log.d(TAG,"in getAccessToken");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(INSTAGRAM_AUTHENTICATION_URL));
        this.startActivity(browserIntent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG,"in onNewIntent");
        super.onNewIntent(intent);
        handleAccessToken(intent);
    }

    private void handleAccessToken(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            String accessToken = "";
            if (uri.getFragment() != null) {
                accessToken = uri.getFragment().replace("access_token=", "");
                Log.d(TAG, "Found access token: " + accessToken);
                mAccessToken = accessToken;
                mGotAccessToken = true;
                //TODO:save access_token to sharedPref
            } else {
                Log.d(TAG, "Access token not found. URI: " + uri.toString());
            }
        }
    }

    private void initUIComponents() {
        Log.d(TAG,"in initUIComponents");
        mBtnCreateGroups = (Button)findViewById(R.id.btn_create_new_groups);
        mBtnCreateGroups.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG,"clicked button 'create new groups");
                Intent intentCreateGroups = new Intent(mActivity,CreateGroupsActivity.class);
                mActivity.startActivity(intentCreateGroups);
            }
        });
    }
}
