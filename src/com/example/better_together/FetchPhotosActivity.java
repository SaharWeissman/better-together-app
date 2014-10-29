package com.example.better_together;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;
import com.example.better_together.Utils.ViewsHelper;
import com.example.better_together.Views.IViewItemClickListener;
import com.example.better_together.Views.adapters.GroupsAdapter;
import com.example.better_together.Views.adapters.UsersPhotosAdapter;
import com.example.better_together.Views.models.Group;
import com.example.better_together.Views.models.ViewItem;
import com.example.better_together.storage.SharedPrefStorage;
import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by ssdd on 10/29/14.
 */
public class FetchPhotosActivity extends Activity implements IViewItemClickListener {
    private static final String TAG = FetchPhotosActivity.class.getName();
    private int mCurrentlayoutID;
    private ListView mChooseFromGroupsList;
    private SharedPrefStorage mSharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fetch_photos_groups);
        initUIComponents();
        mSharedPrefHelper = new SharedPrefStorage(this, Context.MODE_PRIVATE);
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
        GridView usersPhotosGridView = (GridView)findViewById(R.id.gridV_users_photos);
        usersPhotosGridView.setAdapter(new UsersPhotosAdapter(this,usersInGroup));
    }
}
