package com.example.better_together;

import android.util.Log;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ssdd on 10/18/14.
 */
public class HttpRequestHelper implements IHttpRequestHelper {
    private static final String TAG = HttpRequestHelper.class.getName();

    @Override
    public JSONObject makeGetRequest(URL url) {
        JSONObject res = null;
        if(url != null){
            try {
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                int responseCode = urlConnection.getResponseCode();
                Log.d(TAG,"got responseCode: " + responseCode);
                if(responseCode == HttpStatus.SC_OK) {
                    String responseString = readStream(urlConnection.getInputStream());
                    Log.d(TAG, "got response: " + responseString);
                    res = new JSONObject(responseString);
                }
            } catch (IOException e) {
                Log.e(TAG,"caught IOException",e);
            } catch (JSONException e) {
                Log.e(TAG,"caught JSONException,returning null",e);
            }
        }
        else{
            Log.d(TAG,"url is null");
        }
        return res;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
