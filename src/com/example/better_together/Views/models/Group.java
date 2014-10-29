package com.example.better_together.Views.models;

import org.json.JSONArray;

/**
 * Created by ssdd on 10/28/14.
 */
public class Group extends ViewItem {

    private final String mGroupName;
    private final int mNumGroupMembers;
    private final JSONArray mUsersInGroup;

    public Group(String groupName,int numGroupMembers,JSONArray usersInGroup){
        this.mGroupName = groupName;
        this.mNumGroupMembers = numGroupMembers;
        this.mUsersInGroup = usersInGroup;
    }

    public String getGroupName(){
        return mGroupName;
    };

    public int getNumGroupMembers(){
        return mNumGroupMembers;
    }

    public JSONArray getUsersInGroup(){
        return mUsersInGroup;
    }
}
