package com.example.better_together;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import com.example.better_together.ThreadPool.ThreadPoolManager;
import com.example.better_together.Utils.ViewsHelper;
import com.example.better_together.Views.IViewItemClickListener;
import com.example.better_together.Views.OnScrollListeners.EndlessScrollListener;
import com.example.better_together.Views.adapters.GroupsAdapter;
import com.example.better_together.Views.adapters.UsersAdapter;
import com.example.better_together.Views.models.Group;
import com.example.better_together.Views.models.ViewItem;
import com.example.better_together.Views.models.User;
import com.example.better_together.storage.SharedPrefStorage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ssdd on 10/25/14.
 */
public class ShowExistingGroupsActivity extends Activity implements IViewItemClickListener {
    private static final String TAG = ShowExistingGroupsActivity.class.getName();
    private ListView mExistingGroupsList;
    private SharedPrefStorage mSharedPrefHelper;
    private int mCurrentContentView;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_existing_groups);
        initUIComponents();
        mSharedPrefHelper = new SharedPrefStorage(this, Context.MODE_PRIVATE);
        ViewsHelper.populateListWithGroups(mExistingGroupsList,mSharedPrefHelper.readString(BTConstants.SHARED_PREF_KEY_GROUPS));
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mCurrentContentView = layoutResID;
    }

    @Override
    public void onBackPressed() {
        if(mCurrentContentView == R.layout.users_in_group){
            setContentView(R.layout.show_existing_groups);
            initUIComponents();
            ViewsHelper.populateListWithGroups(mExistingGroupsList, mSharedPrefHelper.readString(BTConstants.SHARED_PREF_KEY_GROUPS));
        }else{
            super.onBackPressed();
        }
    }

    private void initUIComponents() {
        mExistingGroupsList = (ListView)findViewById(R.id.listV_exisitng_groups);
        ArrayList<Group> groups = new ArrayList<Group>();
        final GroupsAdapter groupsAdapter = new GroupsAdapter(this,groups,this);
        mExistingGroupsList.setAdapter(groupsAdapter);
        mExistingGroupsList.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadGroupsToAdapter(groupsAdapter, totalItemsCount);
            }
        });
    }

    private void loadGroupsToAdapter(GroupsAdapter groupsAdapter, int totalItemsCount) {
        //TODO
    }

    @Override
    public boolean onListItemLongClick(final ViewItem item) {
        final Group group = (Group)item;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Group Options");
        alertDialogBuilder.setMessage("choose option:");
        alertDialogBuilder.setPositiveButton("Edit name", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                showEditGroupNameLayout();
            }
        });

        alertDialogBuilder.setNeutralButton("Delete Group", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteGroup(group);
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mAlertDialog != null && mAlertDialog.isShowing()) {
                    mAlertDialog.dismiss();
                }
            }
        });
        mAlertDialog = alertDialogBuilder.create();
        mAlertDialog.show();
        return true;
    }

    private void deleteGroup(Group group) {

    }

    private void showEditGroupNameLayout() {

    }

    @Override
    public void onListItemClick(ViewItem item) {
        if(item instanceof Group) {
            Group group = (Group) item;
            setContentView(R.layout.users_in_group);
            TextView groupName = (TextView)findViewById(R.id.txtV_users_group_name_header);
            groupName.setText(group.getGroupName());
            ListView userInGroup = (ListView) findViewById(R.id.listV_users_in_group);
            ArrayList<User> usersInGroupArrayList = new ArrayList<User>();
            UsersAdapter adapter = new UsersAdapter(this, usersInGroupArrayList,this);
            userInGroup.setAdapter(adapter);

            JSONArray usersInGroupJSONArray = group.getUsersInGroup();
            for(int i = 0; i < usersInGroupJSONArray.length(); i++){
                try{
                    JSONObject userJSON = usersInGroupJSONArray.getJSONObject(i);
                    User user = new User(userJSON.getString(BTConstants.JSON_ATTR_USERNAME),userJSON.getString(BTConstants.JSON_ATTR_USER_FULL_NAME),userJSON.getString(BTConstants.JSON_ATTR_USER_BIO),
                            userJSON.getString(BTConstants.JSON_ATTR_USER_WEBSITE),userJSON.getString(BTConstants.JSON_ATTR_USER_PROFILE_PIC_URL),userJSON.getString(BTConstants.JSON_ATTR_USER_ID)
                            ,group.getGroupName(),null);
                    adapter.add(user);
                    ThreadPoolManager.fetchUserProfilePicFromMemory(BTConstants.sAppProfilePicDirectoryPrefixPath +
                    user.getProfilePicURL() + ".png",user,adapter);
                }catch(JSONException e){
                    Log.e(TAG,"unable to read user json",e);
                    continue;
                }
            }
        }
    }
}
