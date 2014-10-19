package com.example.better_together;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ssdd on 10/17/14.
 */
public class CreateGroupsActivity extends Activity {
    private static final String TAG = CreateGroupsActivity.class.getName();
    private static final String SHARED_PREF_KEY_GROUPS = "GROUPS";
    private static final String ON_GROUP_CREATION_SUCCESSFUL_TEXT = "Group created successfully";
    private static final String ON_GROUP_CREATION_FAILED_TEXT = "Group creation FAILED";

    private Button mBtnSave;
    private Button mBtnClear;
    private Button mBtnBack;

    private EditText mEdTxtGroupName;
    private Context mActivity;
    private SharedPrefHelper mSharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"in onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_groups);
        mActivity = this;
        mSharedPrefHelper = new SharedPrefHelper(this,Context.MODE_PRIVATE);
        initUIComponents();
    }

    private void initUIComponents() {
        this.mEdTxtGroupName = (EditText)findViewById(R.id.edTxt_group_name);

        this.mBtnSave = (Button)findViewById(R.id.btn_save);
        this.mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"button save clicked!");
                String groupName = mEdTxtGroupName.getText().toString();
                Log.d(TAG,"group name: " + groupName);
                boolean operationSuccessful = createNewGroup(groupName);
                if(operationSuccessful){
                    Intent addUsersActivity = new Intent(mActivity,AddUsersToGroupActivity.class);
                    addUsersActivity.putExtra(AddUsersToGroupActivity.EXTRA_GROUP_NAME,groupName);
                    mActivity.startActivity(addUsersActivity);
                }
            }
        });

        this.mBtnClear = (Button)findViewById(R.id.btn_clear);
        this.mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"button clear clicked!");
                mEdTxtGroupName.setText("");
            }
        });

        this.mBtnBack = (Button)findViewById(R.id.btn_back);
        this.mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"button back clicked!");
                Intent backToMainIntent = new Intent(mActivity,MainActivity.class);
                mActivity.startActivity(backToMainIntent);
            }
        });
    }

    private boolean createNewGroup(String groupName) {
        boolean successful = false;
        try{
            JSONObject groups = mSharedPrefHelper.readJSON(SHARED_PREF_KEY_GROUPS);

            //first time
            if(groups == null){
                groups = new JSONObject();
                groups.put(SHARED_PREF_KEY_GROUPS, new JSONArray());
            }
            JSONArray groupsArray = groups.getJSONArray(SHARED_PREF_KEY_GROUPS);
            groupsArray.put(new JSONObject().put(groupName,new JSONArray()));
            successful = mSharedPrefHelper.writeString(SHARED_PREF_KEY_GROUPS,groups.toString());
        }catch(JSONException e){
            Log.e(TAG,"unable to create group",e);
            successful = false;
        }
        if(successful){
            Toast.makeText(mActivity,ON_GROUP_CREATION_SUCCESSFUL_TEXT,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mActivity,ON_GROUP_CREATION_FAILED_TEXT,Toast.LENGTH_SHORT).show();
        }
        return successful;
    }
}
