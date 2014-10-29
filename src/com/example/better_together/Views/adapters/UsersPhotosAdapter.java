package com.example.better_together.Views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.better_together.BTConstants;
import com.example.better_together.R;
import com.example.better_together.ThreadPool.ThreadPoolManager;
import com.example.better_together.Views.models.User;
import com.example.better_together.storage.SharedPrefStorage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ssdd on 10/29/14.
 */
public class UsersPhotosAdapter extends BaseAdapter {

    private static final String TAG = UsersPhotosAdapter.class.getName();
    private final Context mCallingActivity;
    private final JSONArray mUsers;

    public UsersPhotosAdapter(Activity callingActivity, JSONArray users) {
        this.mCallingActivity = callingActivity;
        this.mUsers = users;
    }


    @Override
    public int getCount() {
        return mUsers.length() * BTConstants.NUM_OF_PHOTOS_PER_USER;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mCallingActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;

        if (convertView == null) {
            gridView = new View(mCallingActivity);
            gridView = inflater.inflate(R.layout.item_user_photo, null);

            try {
                JSONObject userJSON = mUsers.getJSONObject(position % mUsers.length());
                User userFromJSON = User.createFromJSON(userJSON);
                TextView txtVUserName = (TextView)gridView.findViewById(R.id.txtV_user_photo_username);
                txtVUserName.setText(userFromJSON.getUserName());
                ImageView imgVUserPhoto = (ImageView)gridView.findViewById(R.id.imgV_user_photo_image);
                ImageView imgUserProfilePic = (ImageView)gridView.findViewById(R.id.imgV_user_photo_profile_pic);

                //TODO: fetch users profile pics from memory & fetch pictures for each user

            } catch (JSONException e) {
                Log.e(TAG,"cannot get username from json");
            }
        } else {
            gridView = (View) convertView;
        }
        return gridView;
    }
}
