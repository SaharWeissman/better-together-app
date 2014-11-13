package com.example.better_together.storage;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import com.example.better_together.BTConstants;
import com.example.better_together.Utils.SharedPrefHelper;

import java.util.Set;

/**
 * Created by ssdd on 10/19/14.
 */
public class SharedPrefStorage implements IKeyValueStorage{

    private static final String TAG = SharedPrefStorage.class.getName();

    private SharedPreferences mSharedPref;
    private Activity mActivity;

    public SharedPrefStorage(Activity activity, int mode){
        if(activity == null){
            Log.w(TAG,"activity or file name is null");
            return;
        }
        this.mActivity = activity;
        this.mSharedPref = mActivity.getSharedPreferences(BTConstants.SHARED_PREF_FILE_NAME,mode);
    }

    @Override
    public boolean writeString(String key, String value) {
        Log.d(TAG,String.format("writing string: key: %s, value: %s",key,value));
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
    public boolean writeStringSet(String key, Set<String> values) {
        Log.d(TAG,String.format("writing string set: key= %s, value= %s",key,values));
        boolean ret = false;
        if(key == null || values == null || values.isEmpty()){
            Log.w(TAG,"key or values are null");
        }else{
            try{
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putStringSet(key,values);
                editor.commit();
                ret = true;
            }catch(Exception e){
                Log.e(TAG,"error writing to sharedPref",e);
                ret = false;
            }
        }
        return ret;
    }

    @Override
    public String readString(String key) {
        Log.d(TAG,String.format("readString: %s",key));
        return mSharedPref.getString(key,null);
    }

    @Override
    public Set<String> readStringSet(String key) {
        Log.d(TAG,"read string set: " + key);
        return mSharedPref.getStringSet(key,null);
    }
}
