package com.example.better_together;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ssdd on 10/19/14.
 */
public class SharedPrefHelper implements ISharedPrefHelper {

    private static final String TAG = SharedPrefHelper.class.getName();

    private SharedPreferences mSharedPref;
    private int mMode;
    private String mSharedPrefFileName;
    private Activity mActivity;

    public SharedPrefHelper(Activity activity,int mode){
        if(activity == null){
            Log.w(TAG,"activity or file name is null");
            return;
        }
        this.mActivity = activity;
        this.mMode = mode;
        this.mSharedPref = mActivity.getPreferences(mode);
    }

    @Override
    public boolean writeString(String key, String value) {
        boolean ret = false;
        if(key == null || value == null){
            Log.w(TAG,"key or value is null,cannot write");
        }
        else{
            try {
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString(key, value);
                editor.commit();
                ret = true;
            }catch (Exception e){
                Log.e(TAG,"error while writing to sharedPref",e);
                ret = false;
            }
        }
        return ret;
    }

    @Override
    public JSONObject readJSON(String key) {
        JSONObject ret = null;
        String jsonAsString = mSharedPref.getString(key,null);
        if(jsonAsString != null){
            try{
                ret = new JSONObject(jsonAsString);
            }catch (JSONException e){
                Log.e(TAG,"unable to parse string to json: " + jsonAsString,e);
                ret = null;
            }
        }
        return ret;
    }
}
