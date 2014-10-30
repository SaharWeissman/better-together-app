package com.example.better_together.Views.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.better_together.BTConstants;
import com.example.better_together.R;
import com.example.better_together.Views.IViewItemClickListener;
import com.example.better_together.Views.models.User;
import com.example.better_together.Views.models.UserPhotos;

import java.util.ArrayList;

/**
 * Created by ssdd on 10/29/14.
 */
public class UsersPhotosAdapter extends ArrayAdapter<UserPhotos> {

    private static final String TAG = UsersPhotosAdapter.class.getName();
    private final Context mCallingActivity;
    private final IViewItemClickListener mUserPhotosClickListener;
    private final int mUsersNum;

    public UsersPhotosAdapter(Activity callingActivity, ArrayList<UserPhotos> usersPhotos,int usersNum,IViewItemClickListener userPhotosItemClickListner){
        super(callingActivity,0,usersPhotos);
        this.mCallingActivity = callingActivity;
        this.mUserPhotosClickListener = userPhotosItemClickListner;
        this.mUsersNum = usersNum;
    }

    @Override
    public int getCount() {
        return mUsersNum * BTConstants.NUM_OF_PHOTOS_PER_USER;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UserPhotos userPhoto = getItem(position % mUsersNum);
        User user = userPhoto.getUser();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user_photo, parent, false);
        }

        TextView userName = (TextView)convertView.findViewById(R.id.txtV_user_photo_username);
        TextView photoCaption = (TextView)convertView.findViewById(R.id.txtV_user_photo_caption);
        TextView photoCreateDate = (TextView)convertView.findViewById(R.id.txtV_user_photo_create_time);
        ImageView userProfilePic = (ImageView)convertView.findViewById(R.id.imgV_user_photo_profile_pic);
        ImageView userImage = (ImageView)convertView.findViewById(R.id.imgV_user_photo);
        userName.setText(userPhoto.getUser().getUserName());
        photoCaption.setText(userPhoto.getCaptions()[position / mUsersNum]);
        photoCreateDate.setText(userPhoto.getCreationDates()[position / mUsersNum]);
        userProfilePic.setImageBitmap(user.getProfilePic());
        userImage.setImageBitmap(userPhoto.getPhotos()[position / mUsersNum]);
        return convertView;
    }
}
