package com.example.better_together;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by ssdd on 10/17/14.
 */
public class CreateGroupsActivity extends Activity {
    private static final String TAG = CreateGroupsActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"in onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_groups);
        initUIComponents();
    }

    private void initUIComponents() {
        //TODO
    }
}
