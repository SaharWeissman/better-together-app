package com.example.better_together;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.example.better_together.ThreadPool.ThreadPoolManager;
import com.example.better_together.Views.IViewItemClickListener;
import com.example.better_together.Views.OnScrollListeners.EndlessScrollListener;
import com.example.better_together.Views.adapters.UsersAdapter;
import com.example.better_together.Views.models.ViewItem;
import com.example.better_together.Views.models.User;
import com.example.better_together.storage.SharedPrefStorage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by ssdd on 10/19/14.
 */
public class AddUsersToGroupActivity extends Activity implements IViewItemClickListener {

    public static final String EXTRA_GROUP_NAME = "group_name";
    private static final String TAG = AddUsersToGroupActivity.class.getName();

    private String mGroupName = "";
    private TextView mTxtGroupName;
    private Button mBtnSearch;
    private EditText mEditTxtUserName;
    private Button mBtnClear;
    private Button mBtnBack;
    private Activity mActivity;
    private SharedPrefStorage mSharedPrefHelper;
    private String mAccessToken;

    private AlertDialog mAlertDialog;
    private int mCurrentContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_users_to_group);
        Bundle extras = getIntent().getExtras();
        mGroupName = extras.getString(EXTRA_GROUP_NAME);
        mActivity = this;
        mSharedPrefHelper = new SharedPrefStorage(mActivity,Context.MODE_PRIVATE);
        mAccessToken = mSharedPrefHelper.readString(BTConstants.SHARED_PREF_KEY_ACCESS_TOKEN);
        initUIComponents();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        this.mCurrentContentView = layoutResID;
    }

    @Override
    public void onBackPressed() {
        if(mCurrentContentView == R.layout.users_search_result_list){
            setContentView(R.layout.add_users_to_group);
            initUIComponents();
        }else{
            super.onBackPressed();
        }
    }

    private void initUIComponents() {
        mTxtGroupName = (TextView)findViewById(R.id.txtV_enter_group_name);
        mTxtGroupName.setText(mGroupName);

        mEditTxtUserName = (EditText)findViewById(R.id.edTxt_add_user);

        mBtnSearch = (Button)findViewById(R.id.btn_search_user);
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mEditTxtUserName.getText().toString();
                if(!TextUtils.isEmpty(userName)){
                    searchForUsername(userName);
                }else{
                    Log.d(TAG,"username is empty");
                }
            }
        });

        mBtnClear = (Button)findViewById(R.id.btn_clear_username);
        mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditTxtUserName.setText("");
            }
        });

        mBtnBack = (Button)findViewById(R.id.btn_back_user);
        mBtnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent backToGroupCreate = new Intent(mActivity,MainActivity.class);
                mActivity.startActivity(backToGroupCreate);
            }
        });

    }

    private void searchForUsername(String userName) {
        JSONObject args = new JSONObject();
        try{
            hideKeyboard();
            this.setContentView(R.layout.users_search_result_list);
            args.put(BTConstants.KEY_USERNAME,userName);
            ListView resultsList = (ListView)findViewById(R.id.listv_users_search_result);
            ArrayList<User> users = new ArrayList<User>();
            final UsersAdapter adapter = new UsersAdapter(this,users,this);
            resultsList.setAdapter(adapter);
            resultsList.setOnScrollListener(new EndlessScrollListener() {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    ThreadPoolManager.loadUsersToAdapter(adapter, totalItemsCount);
                }
            });
            ThreadPoolManager.searchForUser(userName, mGroupName, mAccessToken, adapter, this);
        }catch(JSONException e){
            Log.e(TAG,"caught jsonException cannot search for username: " + userName,e);
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * Groups are stored in shared pref as follows:
     * {"Groups": [{"group1":[{"user1":{"username":...}},...]},{"group2":[...]}]}
     * @param item
     */
    @Override
    public boolean onListItemLongClick(ViewItem item) {
        try {
            User user = (User)item;
            String groupsAsString = mSharedPrefHelper.readString(BTConstants.SHARED_PREF_KEY_GROUPS);
            JSONArray groupsArray = new JSONArray(groupsAsString);

            // find specific group
            for(int i =0; i < groupsArray.length(); i++){
                JSONObject groupJSON = groupsArray.getJSONObject(i);
                Iterator<String> keys = groupJSON.keys();
                while (keys.hasNext()){
                    String groupKey = keys.next();
                    if(groupKey.equals(mGroupName)){
                        Log.d(TAG,"found group: " + mGroupName);
                        JSONArray usersInGroup = groupJSON.getJSONArray(mGroupName);

                        // check if user does not already exist in group already
                        if(!userAlreadyExistInGroup(usersInGroup,user)) {
                            JSONObject userJSON = new JSONObject();
                            userJSON.put(BTConstants.JSON_ATTR_USERNAME, user.getUserName());
                            userJSON.put(BTConstants.JSON_ATTR_USER_FULL_NAME, user.getFullName());
                            userJSON.put(BTConstants.JSON_ATTR_USER_BIO, user.getUserBio());
                            userJSON.put(BTConstants.JSON_ATTR_USER_WEBSITE, user.getUserWebsite());
                            userJSON.put(BTConstants.JSON_ATTR_USER_ID, user.getID());
                            userJSON.put(BTConstants.JSON_ATTR_USER_PROFILE_PIC_URL, saveAndGetUUIDForProfilePic(user));
                            usersInGroup.put(userJSON);
                            mSharedPrefHelper.writeString(BTConstants.SHARED_PREF_KEY_GROUPS,groupsArray.toString());
                            Toast.makeText(this, "User added successfully to group", Toast.LENGTH_SHORT).show();

                            // switch layout back to search user
                            this.setContentView(R.layout.add_users_to_group);
                            initUIComponents();
                        }else{
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                            alertDialogBuilder.setTitle("Alert");
                            alertDialogBuilder.setMessage("User already exist in group!");
                            alertDialogBuilder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAlertDialog.dismiss();
                                }
                            });
                            mAlertDialog = alertDialogBuilder.create();
                            mAlertDialog.show();
                        }
                    }else{
                        break;
                    }
                }
            }
        }catch (JSONException e){
            Log.e(TAG,"caught jsonException while entering user to group",e);
        }
        return true;
    }

    @Override
    public void onListItemClick(ViewItem item) {

    }

    private boolean userAlreadyExistInGroup(JSONArray usersInGroup, User user) {
        boolean ret = false;
        for(int i =0; i < usersInGroup.length();i++){
            try{
                JSONObject userJSON = usersInGroup.getJSONObject(i);
                if(userJSON.getString(BTConstants.JSON_ATTR_USERNAME).equals(user.getUserName())){
                    Log.d(TAG,String.format("user \"%s\" already exist in group \"%s\"",user.getUserName(),mGroupName));
                    ret = true;
                    break;
                }
            }catch(JSONException e){
                Log.e(TAG,"userAlreadyExistInGroup: unable to read userJSON",e);
                ret = false;
                break;
            }
        }
        return ret;
    }


    /**
     * the idea here is to create a mapping of : [{"username":<username>,"profile_pic_path":<profile_pic_path>}...]
     * so that there will only be one profile pic saved in app internal memory for each user
     */
    private String saveAndGetUUIDForProfilePic(User user) {
        String filePathName = null;
        try{
            boolean needToGenerateUUIDAndSave = true;
            String username = user.getUserName();
            String profilePicsMappingsAsString = mSharedPrefHelper.readString(BTConstants.SHARED_PREF_KEY_PROFILE_PICS_MAPPING);
            JSONArray profilePicsMappings = new JSONArray();
            if(!TextUtils.isEmpty(profilePicsMappingsAsString)){
                profilePicsMappings = new JSONArray(profilePicsMappingsAsString);
            }
            for(int i =0; i < profilePicsMappings.length();i++){
                JSONObject userToPicMapping = profilePicsMappings.getJSONObject(i);
                if(userToPicMapping.getString(BTConstants.KEY_USERNAME).equals(username)){
                    needToGenerateUUIDAndSave = false;
                    filePathName = userToPicMapping.getString(BTConstants.KEY_PROFILE_PICTURE_PATH);
                    break;
                }
            }

            if(needToGenerateUUIDAndSave){
                String profilePicFileName = UUID.randomUUID().toString();
                Bitmap profilePic = user.getProfilePic();

                File directory = getDir(BTConstants.PROFILE_PICTURES_DIRECTORY, Context.MODE_PRIVATE);
                File profilePicFile = new File(directory,profilePicFileName + ".png");
                FileOutputStream fos = null;
                try {

                    fos = new FileOutputStream(profilePicFile);
                    // Use the compress method on the BitMap object to write image to the OutputStream
                    profilePic.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                    filePathName = profilePicFileName;
                    JSONObject userToPicMapping = new JSONObject();
                    userToPicMapping.put(BTConstants.KEY_USERNAME,user.getUserName());
                    userToPicMapping.put(BTConstants.KEY_PROFILE_PICTURE_PATH,filePathName);
                    profilePicsMappings.put(userToPicMapping);
                    mSharedPrefHelper.writeString(BTConstants.SHARED_PREF_KEY_PROFILE_PICS_MAPPING,profilePicsMappings.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    filePathName = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    filePathName = null;
                }
            }
        }catch(JSONException e){
            Log.e(TAG,"cannot save profile pic",e);
            return null;
        }
        return filePathName;
    }
}
