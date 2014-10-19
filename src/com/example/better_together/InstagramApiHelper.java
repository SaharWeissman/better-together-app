package com.example.better_together;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ssdd on 10/18/14.
 */
public class InstagramApiHelper implements IApiHelper {

    //================== CONSTANTS ============================
    private static final String API_URL_PREFIX = "https://api.instagram.com/v1";
    private static final String API_COMMAND_SEARCH = "search";
    private static final String TAG = InstagramApiHelper.class.getName();
    private static final String ATTR_SEARCH_USERNAME = "username";

    //================== Members ============================
    private String mAccessToken;
    private HttpRequestHelper mHttpRequestHelper;

    public InstagramApiHelper(String accessToken){
        if(accessToken == null){
            Log.e(TAG,"cannot init with null accessToken");
            return;
        }
        this.mAccessToken = accessToken;
        this.mHttpRequestHelper = new HttpRequestHelper();
    }

    @Override
    public JSONObject execute(String apiCommand, JSONObject args) {
        JSONObject ret = null;
        try {

            if (apiCommand == API_COMMAND_SEARCH) {
                //            https://api.instagram.com/v1/users/search?q=ssdd1886&access_token=278657633.7575571.d97310a097d34a8cbe64a38e30c28e9e
                String query = args.getString(ATTR_SEARCH_USERNAME);
                URL url = new URL(String.format("%s/users/search?q=%s&&access_token=%s",API_URL_PREFIX,query,mAccessToken));
                ret = mHttpRequestHelper.makeGetRequest(url);
            }
        }catch(MalformedURLException e){
            Log.e(TAG,"bad url",e);
            ret = null;
        } catch (JSONException e) {
            Log.e(TAG,"cannot find attr. in json",e);
            ret = null;
        }
        return ret;
    }
}
