package com.example.better_together.Utils;

import android.text.TextUtils;
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
        String groupsAsString = iGroupsAsString;
        GroupsAdapter adapter = (GroupsAdapter)list.getAdapter();
        if(TextUtils.isEmpty(groupsAsString)){
            Log.d(TAG, "no existing groups");
            adapter.add(new Group("NO EXISTING GROUPS",-1,null));
            return;
        }
        try{
            JSONArray groupsJSONArray = new JSONArray(groupsAsString);
            for(int i =0; i < groupsJSONArray.length(); i++){
                JSONObject groupJSON = groupsJSONArray.getJSONObject(i); //specific group
                Iterator<String> groupJSONKeys = groupJSON.keys();
                while(groupJSONKeys.hasNext()){
                    String groupName = groupJSONKeys.next();
                    JSONArray usersInGroup = groupJSON.getJSONArray(groupName);
                    Group group = new Group(groupName,usersInGroup.length(),usersInGroup);
                    adapter.add(group);
                    break;
                }
            }
        }catch(JSONException e){
            Log.e(TAG,"unable to parse groups string to JSON");
        }
    }
}
