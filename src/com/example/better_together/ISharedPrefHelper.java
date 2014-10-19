package com.example.better_together;

import org.json.JSONObject;

/**
 * Created by ssdd on 10/19/14.
 */
public interface ISharedPrefHelper {
    public boolean writeString(String key, String value);
    public JSONObject readJSON(String key);
}
