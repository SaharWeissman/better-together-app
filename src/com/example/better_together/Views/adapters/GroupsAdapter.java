package com.example.better_together.Views.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.better_together.R;
import com.example.better_together.Views.IViewItemClickListener;
import com.example.better_together.Views.models.Group;

import java.util.ArrayList;

/**
 * Created by ssdd on 10/29/14.
 */
public class GroupsAdapter extends ArrayAdapter<Group> {
    private final IViewItemClickListener mGroupItemClickListener;

    public GroupsAdapter(Activity callingActivity, ArrayList<Group> groups,IViewItemClickListener groupItemClickListener){
        super(callingActivity, 0, groups);
        this.mGroupItemClickListener = groupItemClickListener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Group group = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_group, parent, false);
        }
        // Lookup view for data population
        TextView tvGroupName = (TextView)convertView.findViewById(R.id.txtV_existing_group_name);
        TextView tvGroupMembersNum = (TextView)convertView.findViewById(R.id.txtV_group_members_num);

        // Populate the data into the template view using the data object
        int numMembers = group.getNumGroupMembers();
        String groupNamePrefix = numMembers < 0 ? "" : "Name:";
        String groupMembersNumStr = numMembers < 0 ? "" : "Members#: " + String.valueOf(group.getNumGroupMembers());
        tvGroupName.setText(String.format("%s \"%s\"",groupNamePrefix,group.getGroupName()));
        tvGroupMembersNum.setText(groupMembersNumStr);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                mGroupItemClickListener.onListItemClick(group);
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                return mGroupItemClickListener.onListItemLongClick(group);
            }
        });
        return convertView;
    }
}
