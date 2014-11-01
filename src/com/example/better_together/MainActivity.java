package com.example.better_together;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.better_together.storage.SharedPrefStorage;

import java.io.File;

public class MainActivity extends Activity {

    //============== CONSTANTS ============================
    private static final String TAG = MainActivity.class.getName();
    private static final String CLIENT_ID = "7575571c75b34969ac40ae01c2ac1e6d";
    private static final String REDIRECT_URI = "bettertogether://test/";
    private static final String INSTAGRAM_AUTHENTICATION_URL = String.format("https://instagram.com/oauth/authorize/?client_id=%s&redirect_uri=%s&response_type=token",CLIENT_ID,REDIRECT_URI);

    private static final int MENU_ITEM_CLEAR_GROUPS = 0;

    private static Activity mActivity;
    private AlertDialog mAlertDialog;

    //============== Members ============================
    private String mAccessToken = null;
    private Button mBtnCreateGroups;
    private Button mBtnShowExistingGroups;
    private Button mBtnFetchPhotos;
    private SharedPrefStorage mSharedPrefStorage;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "in onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mActivity = this;
        mSharedPrefStorage = new SharedPrefStorage(mActivity,Context.MODE_PRIVATE);
        Intent intent = getIntent();
        BTConstants.setAppProfilePicDirectoryPrefix("/data/data/" + getApplicationContext().getPackageName() + "/app_profilePictures/");
        initUIComponents();
        if(intent.getData() != null){
            handleAccessToken(intent);
        }
        if(!accessTokenExistsInSharedPref()) {
            getAccessToken();
        }
    }

    private boolean accessTokenExistsInSharedPref() {
        String accessToken = mSharedPrefStorage.readString(BTConstants.SHARED_PREF_KEY_ACCESS_TOKEN);
        return accessToken != null;
    }

    private void getAccessToken() {
        Log.d(TAG,"in getAccessToken");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(INSTAGRAM_AUTHENTICATION_URL)).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(browserIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,MENU_ITEM_CLEAR_GROUPS,0,"Clear All Groups");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case MENU_ITEM_CLEAR_GROUPS:{
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Confirm");
                alertDialogBuilder.setMessage("Are you sure?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearAllGroups();
                        Toast.makeText(mActivity,"cleared all groups",Toast.LENGTH_SHORT).show();
                        mAlertDialog.dismiss();
                    }
                });
                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAlertDialog.dismiss();
                    }
                });
                mAlertDialog = alertDialogBuilder.create();
                mAlertDialog.show();
                break;
            }
            default:{
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearAllGroups() {
        mSharedPrefStorage.writeString(BTConstants.SHARED_PREF_KEY_GROUPS, "");
        mSharedPrefStorage.writeString(BTConstants.SHARED_PREF_KEY_PROFILE_PICS_MAPPING, "");
        deleteAllProfilePics();
        Log.d(TAG,"cleared all groups");
    }

    private void deleteAllProfilePics() {
        File directory = getDir(BTConstants.PROFILE_PICTURES_DIRECTORY, Context.MODE_PRIVATE);
        for (File profilePicFile : directory.listFiles()){
            profilePicFile.delete();
        }
    }

    private void handleAccessToken(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            String accessToken = "";
            if (uri.getFragment() != null) {
                accessToken = uri.getFragment().replace("access_token=", "");
                Log.d(TAG, "Found access token: " + accessToken);
                mSharedPrefStorage.writeString(BTConstants.SHARED_PREF_KEY_ACCESS_TOKEN, accessToken);
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
                Log.d(TAG,"clicked button 'create new groups'");
                Intent intentCreateGroups = new Intent(mActivity,CreateGroupsActivity.class);
                mActivity.startActivity(intentCreateGroups);
            }
        });

        mBtnShowExistingGroups = (Button)findViewById(R.id.btn_show_existing_groups);
        mBtnShowExistingGroups.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG,"clicked button 'show existing groups'");
                Intent showExistingGroupsIntent = new Intent(mActivity,ShowExistingGroupsActivity.class);
                mActivity.startActivity(showExistingGroupsIntent);
            }
        });

        mBtnFetchPhotos = (Button)findViewById(R.id.btn_fetch_photos);
        mBtnFetchPhotos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG,"clicked button fetch photos!");
                Intent fetchPhotosIntent = new Intent(mActivity,FetchPhotosActivity.class);
                mActivity.startActivity(fetchPhotosIntent);
            }
        });
    }
}
