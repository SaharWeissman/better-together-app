package com.example.better_together.Views.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.better_together.R;
import com.example.better_together.Views.IViewItemClickListener;
import com.example.better_together.Views.models.User;

import java.util.ArrayList;

/**
 * Created by ssdd on 10/22/14.
 */
public class UsersAdapter extends ArrayAdapter<User>{
    private static final String ALERT_DIALOG_TITLE = "Confirm";
    private static final String TAG = UsersAdapter.class.getName();
    private final IViewItemClickListener mUserItemClickListener;
    private AlertDialog mAlertDialog;

    public UsersAdapter(Activity callingActivity, ArrayList<User> users,IViewItemClickListener userItemClickListner){
        super(callingActivity, 0, users);
        mUserItemClickListener = userItemClickListner;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }
        // Lookup view for data population
        TextView tvUserID = (TextView)convertView.findViewById(R.id.txtV_user_id);
        TextView tvUserName = (TextView)convertView.findViewById(R.id.txtV_username);
        TextView tvUserFullName = (TextView)convertView.findViewById(R.id.txtV_user_full_name);
        TextView tvUserBio = (TextView)convertView.findViewById(R.id.txtV_user_bio);
        TextView tvUserWebsite = (TextView)convertView.findViewById(R.id.txtV_user_website);
        ImageView imgvProfilePic = (ImageView)convertView.findViewById(R.id.imgv_profile_pic);

        // Populate the data into the template view using the data object
        tvUserID.setText("ID: " +user.getID());
        tvUserName.setText(user.getUserName());
        tvUserFullName.setText(user.getFullName());
        tvUserBio.setText(user.getUserBio());
        tvUserWebsite.setText(user.getUserWebsite());
        imgvProfilePic.setImageBitmap(user.getProfilePic());
        // Return the completed vie
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final String groupName = user.getGroupName();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle(ALERT_DIALOG_TITLE);
                alertDialogBuilder.setMessage(String.format("Add user: \"%s\" to group: \"%s\"?", user.getUserName(), groupName));
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        addUserToGroup(user, groupName);
                        mUserItemClickListener.onListItemLongClick(user);
                    }
                });
                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAlertDialog.dismiss();
                    }
                });
                mAlertDialog = alertDialogBuilder.create();
                mAlertDialog.show();
                return true;
            }
        });
        return convertView;
    }

}
