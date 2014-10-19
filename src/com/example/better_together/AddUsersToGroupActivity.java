package com.example.better_together;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by ssdd on 10/19/14.
 */
public class AddUsersToGroupActivity extends Activity {

    public static final String EXTRA_GROUP_NAME = "group_name";
    private static final String TAG = AddUsersToGroupActivity.class.getName();

    private String mGroupName = "";
    private TextView mTxtGroupName;
    private Button mBtnSearch;
    private EditText mEditTxtUserName;
    private Button mBtnClear;
    private Button mBtnBack;
    private Context mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_users_to_group);
        Bundle extras = getIntent().getExtras();
        mGroupName = extras.getString(EXTRA_GROUP_NAME);
        mActivity = this;
        initUIComponents();

    }

    private void initUIComponents() {
        mTxtGroupName = (TextView)findViewById(R.id.txtV_group_name);
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
                Intent backToGroupCreate = new Intent(mActivity,CreateGroupsActivity.class);
                mActivity.startActivity(backToGroupCreate);
            }
        });

    }

    private void searchForUsername(String userName) {
        //TODO
    }
}
