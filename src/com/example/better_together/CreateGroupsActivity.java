package com.example.better_together;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.better_together.storage.SharedPrefStorage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by ssdd on 10/17/14.
 */
public class CreateGroupsActivity extends Activity {
    private static final String TAG = CreateGroupsActivity.class.getName();
    private static final String ON_GROUP_CREATION_SUCCESSFUL_TEXT = "Group created successfully";
    private static final String ON_GROUP_CREATION_FAILED_TEXT = "Group creation FAILED";

    private Button mBtnSave;
    private Button mBtnClear;
    private Button mBtnBack;

    private EditText mEdTxtGroupName;
    private Context mActivity;
    private SharedPrefStorage mSharedPrefStorage;
    private AlertDialog mAlertDialog;

    private String mErrorMsg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"in onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_groups);
        mActivity = this;
        mSharedPrefStorage = new SharedPrefStorage(this,Context.MODE_PRIVATE);
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
                else{
                    showErrorDialog(mErrorMsg);
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

    private void showErrorDialog(String mErrorMsg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Error");
        alertDialogBuilder.setMessage(mErrorMsg);
        alertDialogBuilder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 mAlertDialog.dismiss();
            }
        });
        mAlertDialog = alertDialogBuilder.create();
        mAlertDialog.show();
    }

    private boolean createNewGroup(String iGroupName) {
        boolean successful = false;
        if(TextUtils.isEmpty(iGroupName)){
           mErrorMsg = "group name cannot be empty!";
            return false;
        }

        try{
            String groupsAsString = mSharedPrefStorage.readString(BTConstants.SHARED_PREF_KEY_GROUPS);
            JSONArray groupsJSONArray = new JSONArray();

            //first time
            if(!TextUtils.isEmpty(groupsAsString)){
                groupsJSONArray = new JSONArray(groupsAsString);
            }

            // check groupName does not already exist.
            for(int i =0; i < groupsJSONArray.length(); i++) {
                JSONObject groupJSON = groupsJSONArray.getJSONObject(i);
                Iterator<String> keys = groupJSON.keys();
                while (keys.hasNext()) {
                    String groupName = keys.next();
                    if(groupName.equals(iGroupName)){
                        mErrorMsg = "Group name already exist.";
                        return false;
                    }
                }
            }

            groupsJSONArray.put(new JSONObject().put(iGroupName,new JSONArray()));
            successful = mSharedPrefStorage.writeString(BTConstants.SHARED_PREF_KEY_GROUPS,groupsJSONArray.toString());
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
