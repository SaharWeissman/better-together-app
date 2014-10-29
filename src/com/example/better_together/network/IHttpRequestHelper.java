package com.example.better_together.network;

import android.graphics.Bitmap;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by ssdd on 10/23/14.
 */
public interface IHttpRequestHelper {
    public JSONObject makeGetRequest(URL url);
    public Bitmap fetchPhotoFromURL(URL url);
}
