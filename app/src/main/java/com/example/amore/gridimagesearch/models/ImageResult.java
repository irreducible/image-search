package com.example.amore.gridimagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by amore on 10/17/15.
 */
public class ImageResult implements Serializable {
    public String fullUrl;
    public String thumbUrl;
    public String title;

    public ImageResult(JSONObject jsonObject) {
        try {
            fullUrl = jsonObject.getString("url");
            thumbUrl = jsonObject.getString("tbUrl");
            title = jsonObject.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ImageResult> fromJSONArray(JSONArray array) {
        ArrayList<ImageResult> imageResults = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                imageResults.add(new ImageResult(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return imageResults;
    }
}
