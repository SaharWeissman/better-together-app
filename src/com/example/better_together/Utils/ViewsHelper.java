package com.example.better_together.Utils;

import android.util.Log;
import android.widget.ListView;
import com.example.better_together.Views.adapters.GroupsAdapter;
import com.example.better_together.Views.models.Group;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by ssdd on 10/28/14.
 */
public class ViewsHelper {

    private static final String TAG = ViewsHelper.class.getName();

    public static void populateListWithGroups(ListView list,String iGroupsAsString){
        String groupsAsString = iGroupsAsString == null ? "" : iGroupsAsString;
        GroupsAdapter adapter = (GroupsAdapter)list.getAdapter();
        try{
            JSONArray groupsJSONArray = new JSONArray(groupsAsString);
            if(groupsJSONArray.length() == 0){
                adapter.add(new Group("No Groups Exist",-1,new JSONArray()));
            }else {
                for (int i = 0; i < groupsJSONArray.length(); i++) {
                    JSONObject groupJSON = groupsJSONArray.getJSONObject(i); //specific group
                    Iterator<String> groupJSONKeys = groupJSON.keys();
                    while (groupJSONKeys.hasNext()) {
                        String groupName = groupJSONKeys.next();
                        JSONArray usersInGroup = groupJSON.getJSONArray(groupName);
                        Group group = new Group(groupName, usersInGroup.length(), usersInGroup);
                        adapter.add(group);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }catch(JSONException e){
            Log.e(TAG,"unable to parse groups string to JSON");
        }
    }
}
