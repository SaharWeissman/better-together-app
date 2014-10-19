package com.example.better_together;

import org.json.JSONObject;

import java.net.URL;

/**
 * Created by ssdd on 10/18/14.
 */
public interface IHttpRequestHelper {
    public JSONObject makeGetRequest(URL url);
}
