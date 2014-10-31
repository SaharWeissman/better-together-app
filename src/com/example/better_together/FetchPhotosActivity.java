package com.example.better_together;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.better_together.ThreadPool.ThreadPoolManager;
import com.example.better_together.Utils.ViewsHelper;
import com.example.better_together.Views.IViewItemClickListener;
import com.example.better_together.Views.adapters.GroupsAdapter;
import com.example.better_together.Views.adapters.UsersPhotosAdapter;
import com.example.better_together.Views.models.Group;
import com.example.better_together.Views.models.User;
import com.example.better_together.Views.models.UserPhoto;
import com.example.better_together.Views.models.ViewItem;
import com.example.better_together.storage.SharedPrefStorage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ssdd on 10/29/14.
 */
public class FetchPhotosActivity extends Activity implements IViewItemClickListener {
    private static final String TAG = FetchPhotosActivity.class.getName();
    private int mCurrentlayoutID;
    private ListView mChooseFromGroupsList;
    private SharedPrefStorage mSharedPrefHelper;
    private String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fetch_photos_groups);
        initUIComponents();
        mSharedPrefHelper = new SharedPrefStorage(this, Context.MODE_PRIVATE);
        mAccessToken = mSharedPrefHelper.readString(BTConstants.SHARED_PREF_KEY_ACCESS_TOKEN);
        ViewsHelper.populateListWithGroups(mChooseFromGroupsList,mSharedPrefHelper.readString(BTConstants.SHARED_PREF_KEY_GROUPS));
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mCurrentlayoutID = layoutResID;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initUIComponents() {
        mChooseFromGroupsList = (ListView)findViewById(R.id.listv_fetch_photos_groups);
        ArrayList<Group> groups = new ArrayList<Group>();
        GroupsAdapter adapter = new GroupsAdapter(this,groups,this);
        mChooseFromGroupsList.setAdapter(adapter);
    }

    @Override
    public boolean onListItemLongClick(ViewItem item) {
        return false;
    }

    @Override
    public void onListItemClick(ViewItem item) {
        Group group = (Group)item;
        JSONArray usersInGroup = group.getUsersInGroup();
        setContentView(R.layout.users_photos);
        TextView groupName = (TextView)findViewById(R.id.txtV_group_name);
        groupName.setText(group.getGroupName());
        GridView usersPhotosGridView = (GridView)findViewById(R.id.gridV_users_photos);
        ArrayList<UserPhoto> userPhotos = new ArrayList<UserPhoto>();
        UsersPhotosAdapter adapter = new UsersPhotosAdapter(this,userPhotos,usersInGroup.length(),this);
        createUserPhotos(usersInGroup,userPhotos,adapter);
        usersPhotosGridView.setAdapter(adapter);
    }

    private void createUserPhotos(JSONArray usersInGroup, ArrayList<UserPhoto> userPhotos,ArrayAdapter adapter) {
        for(int i = 0; i < usersInGroup.length() * BTConstants.NUM_OF_PHOTOS_PER_USER; i++){
            try{
                JSONObject userJSON = usersInGroup.getJSONObject(i % usersInGroup.length());
                User user = User.createFromJSON(userJSON);
                UserPhoto userPhoto = new UserPhoto(user,null,null,null);
                userPhotos.add(userPhoto);
                ThreadPoolManager.fetchUserProfilePicFromMemory(BTConstants.sAppProfilePicDirectoryPrefixPath + user.getProfilePicURL() + ".png",user,adapter);
                ThreadPoolManager.FetchUserRecentMediaPhotos(user.getID(),userPhoto,adapter,mAccessToken,i / usersInGroup.length());
            }catch(JSONException e){
                Log.e(TAG,"unable to get user JSON",e);
            }
        }
    }
}
