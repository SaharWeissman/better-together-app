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
import com.example.better_together.Views.models.UserPhoto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ssdd on 10/29/14.
 */
public class UsersPhotosAdapter extends ArrayAdapter<UserPhoto> {

    private static final String TAG = UsersPhotosAdapter.class.getName();
    private final Context mCallingActivity;
    private final IViewItemClickListener mUserPhotosClickListener;
    private final int mUsersNum;

    public UsersPhotosAdapter(Activity callingActivity, ArrayList<UserPhoto> usersPhotos,int usersNum,IViewItemClickListener userPhotosItemClickListner){
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
        final UserPhoto userPhoto = getItem(position);
        User user = userPhoto.getUser();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user_photo, parent, false);
        }

        TextView userName = (TextView)convertView.findViewById(R.id.txtV_user_photo_username);
        TextView photoCaption = (TextView)convertView.findViewById(R.id.txtV_user_photo_caption);
        TextView photoCreateDate = (TextView)convertView.findViewById(R.id.txtV_user_photo_create_time);
        TextView photoLikesNum = (TextView)convertView.findViewById(R.id.txtV_user_photo_likes_count);
        ImageView userProfilePic = (ImageView)convertView.findViewById(R.id.imgV_user_photo_profile_pic);
        ImageView userImage = (ImageView)convertView.findViewById(R.id.imgV_user_photo);
        userName.setText(userPhoto.getUser().getUserName());
        photoCaption.setText(userPhoto.getCaption());
        photoLikesNum.setText(userPhoto.getLikesNum());
        userProfilePic.setImageBitmap(user.getProfilePic());
        userImage.setImageBitmap(userPhoto.getPhoto());

        // photo create date
        String createDateStr = "";
        Date createDate = userPhoto.getCreationDate();
        if(createDate != null){
            DateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            createDateStr = format.format(createDate);
        }
        photoCreateDate.setText(createDateStr);

        return convertView;
    }
}
