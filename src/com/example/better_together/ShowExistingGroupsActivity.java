package com.example.better_together;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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
import org.json.JSONStringer;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ssdd on 10/25/14.
 */
public class ShowExistingGroupsActivity extends Activity implements IViewItemClickListener {
    private static final String TAG = ShowExistingGroupsActivity.class.getName();
    private ListView mExistingGroupsList;
    private SharedPrefStorage mSharedPrefHelper;
    private int mCurrentContentView;
    private AlertDialog mAlertDialog;
    private UsersAdapter mUsersInGroupAdapter;

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
        if(item instanceof Group) {
            final Group group = (Group) item;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Group Options");
            alertDialogBuilder.setMessage("choose option:");
            alertDialogBuilder.setPositiveButton("Edit name", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showEditGroupNameLayout(group);
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
                    if (mAlertDialog != null && mAlertDialog.isShowing()) {
                        mAlertDialog.dismiss();
                    }
                }
            });
            mAlertDialog = alertDialogBuilder.create();
            mAlertDialog.show();
        }
        else if(item instanceof User){
            final User user = (User)item;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Confirm");
            alertDialogBuilder.setMessage(String.format("Remove user \"%s\" from group?",user.getUserName()));
            alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    removeUserFromGroup(user,user.getGroupName());
                }
            });

            alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(mAlertDialog != null && mAlertDialog.isShowing()) {
                        mAlertDialog.dismiss();
                    }
                }
            });
            mAlertDialog= alertDialogBuilder.create();
            mAlertDialog.show();
        }
        return true;
    }

    private void removeUserFromGroup(User user, String iGroupName) {
        String groupsAsString = mSharedPrefHelper.readString(BTConstants.SHARED_PREF_KEY_GROUPS);
        if(!TextUtils.isEmpty(groupsAsString)){
            try{
                JSONArray groupsJSONArray = new JSONArray(groupsAsString);
                String userNameToRemove = user.getUserName();
                int indexGroupToRemove = -1;
                ArrayList<String> groupsArrayAsList = new ArrayList<String>(groupsJSONArray.length());

                //step#1 - transfer groups jsons to list + find index of group in list
                for(int i =0; i < groupsJSONArray.length();i++){
                    JSONObject groupJSON = groupsJSONArray.getJSONObject(i);
                    Iterator<String> iter = groupJSON.keys();
                    while(iter.hasNext()){
                        String groupName = iter.next();
                        if(groupName.equals(iGroupName)){
                            indexGroupToRemove = i;
                            break;
                        }
                    }
                    groupsArrayAsList.add(groupJSON.toString());
                }

                //step#2 -  save group aside & delete from list
                JSONObject groupToRemove = new JSONObject(groupsArrayAsList.get(indexGroupToRemove));
                JSONArray usersInGroupJSONArray = groupToRemove.getJSONArray(iGroupName);
                groupsArrayAsList.remove(indexGroupToRemove);

                //step#3 - transfer users into list + find index of user to remove
                int indexUserToRemove = -1;
                boolean foundUser = false;
                ArrayList<String> usersInGroupList = new ArrayList<String>(usersInGroupJSONArray.length());
                for(int i = 0; i < usersInGroupJSONArray.length(); i++){
                    JSONObject userJSON = usersInGroupJSONArray.getJSONObject(i);
                    usersInGroupList.add(userJSON.toString());
                    if(!foundUser && userJSON.getString(BTConstants.JSON_ATTR_USERNAME).equals(user.getUserName())){
                        indexUserToRemove = i;
                        foundUser = true;
                    }
                }

                //step#4 - remove user from list of users in group + set new group + new group array & save to shared pref
                usersInGroupList.remove(indexUserToRemove);
                JSONObject newGroup = new JSONObject();
                newGroup.put(iGroupName,new JSONArray(usersInGroupList.toString()));
                groupsArrayAsList.add(newGroup.toString());

                JSONArray newGroupArray = new JSONArray(groupsArrayAsList.toString());
                mSharedPrefHelper.writeString(BTConstants.SHARED_PREF_KEY_GROUPS,newGroupArray.toString());

                mUsersInGroupAdapter.remove(user);
                mUsersInGroupAdapter.notifyDataSetChanged();

            }catch(JSONException e){
                Log.e(TAG,"unable to convert groups json string to json array",e);
            }
        }
    }

    private void deleteGroup(Group group) {
        String groupsAsString = mSharedPrefHelper.readString(BTConstants.SHARED_PREF_KEY_GROUPS);
        String groupName = group.getGroupName();
        int indexToRemove = -1;
        try{
            // first turn json array to array list
            JSONArray groupsJSONArray = new JSONArray(groupsAsString);
            ArrayList<String> list = new ArrayList<String>(groupsJSONArray.length());
            for(int i =0; i < groupsJSONArray.length(); i++){
                list.add(i,groupsJSONArray.getString(i));
            }

            // then find it's position in json array
            for(int i =0; i < groupsJSONArray.length();i++){
                JSONObject groupJSON = groupsJSONArray.getJSONObject(i);
                Iterator<String> iter = groupJSON.keys();
                while(iter.hasNext()){
                    String name = iter.next();
                    if(name.equals(groupName)){
                        //found group
                        indexToRemove = i;
                        break;
                    }else{
                        continue;
                    }
                }
            }

            // remove from list
            list.remove(indexToRemove);
            GroupsAdapter adapter = (GroupsAdapter)mExistingGroupsList.getAdapter();

            // remove from list adapter
            for(int i = 0; i < groupsJSONArray.length(); i++){
                Group groupFromAdapter = adapter.getItem(i);
                if(groupFromAdapter.getGroupName() == groupName){
                    adapter.remove(groupFromAdapter);
                    break;
                }
            }
            adapter.notifyDataSetChanged();

            // then update groupsJSONArray and save to shared pref.
            groupsJSONArray = new JSONArray(list.toString());
            mSharedPrefHelper.writeString(BTConstants.SHARED_PREF_KEY_GROUPS,groupsJSONArray.toString());
        }catch(JSONException e){
            Log.e(TAG,"unable to parse groups string to json",e);
        }
    }

    private void showEditGroupNameLayout(final Group group) {
        LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.change_group_name_layout,null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogLayout);
        builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText newGroupNameEditText = (EditText)dialogLayout.findViewById(R.id.edTxt_new_group_name);
                String newGroupName = newGroupNameEditText.getText().toString();
                setGroupNewName(group,newGroupName);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAlertDialog.dismiss();
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    private void setGroupNewName(Group group, String newGroupName) {
        deleteGroup(group);
        String groupsAsString = mSharedPrefHelper.readString(BTConstants.SHARED_PREF_KEY_GROUPS);
        try{
            JSONArray groupsJSONArray = new JSONArray(groupsAsString);
            groupsJSONArray.put(new JSONObject().put(newGroupName,group.getUsersInGroup()));
            mSharedPrefHelper.writeString(BTConstants.SHARED_PREF_KEY_GROUPS,groupsJSONArray.toString());
            ViewsHelper.populateListWithGroups(mExistingGroupsList,mSharedPrefHelper.readString(BTConstants.SHARED_PREF_KEY_GROUPS));
        }catch (JSONException e){
            Log.e(TAG,"unable to set new group name",e);
        }
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
            mUsersInGroupAdapter= new UsersAdapter(this, usersInGroupArrayList,this);
            userInGroup.setAdapter(mUsersInGroupAdapter);

            JSONArray usersInGroupJSONArray = group.getUsersInGroup();
            for(int i = 0; i < usersInGroupJSONArray.length(); i++){
                try{
                    JSONObject userJSON = usersInGroupJSONArray.getJSONObject(i);
                    User user = new User(userJSON.getString(BTConstants.JSON_ATTR_USERNAME),userJSON.getString(BTConstants.JSON_ATTR_USER_FULL_NAME),userJSON.getString(BTConstants.JSON_ATTR_USER_BIO),
                            userJSON.getString(BTConstants.JSON_ATTR_USER_WEBSITE),userJSON.getString(BTConstants.JSON_ATTR_USER_PROFILE_PIC_URL),userJSON.getString(BTConstants.JSON_ATTR_USER_ID)
                            ,group.getGroupName(),null);
                    mUsersInGroupAdapter.add(user);
                    ThreadPoolManager.fetchUserProfilePicFromMemory(BTConstants.sAppProfilePicDirectoryPrefixPath +
                    user.getProfilePicURL() + ".png",user,mUsersInGroupAdapter);
                }catch(JSONException e){
                    Log.e(TAG,"unable to read user json",e);
                    continue;
                }
            }
        }
    }
}
