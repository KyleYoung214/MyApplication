package com.example.kyle.forgradle.com.example.kyle.retrofit.gson;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nick.yang on 2017/7/19.
 */

public class Result {
    @SerializedName("_id")
    public String id;
    public String createdAt;
    public String desc;
    public String[] images;
    public String publishedAt;
    public String source;
    public String type;
    public String url;
    public boolean used;
    public String who;

    @Override
    public String toString() {
        String imagesStr = "";
        if (null != images) {
            for (String image : images) {
                if (TextUtils.isEmpty(imagesStr)) {
                    imagesStr += image;
                } else {
                    imagesStr += "\n" + image;
                }
            }
        }
        return "_id: " + id + "\ncreatedAt: " + createdAt + "\ndesc: " + desc + "\nimages: " +
                imagesStr + "\npublishedAt: " + publishedAt + "\nsource: " + source + "\ntype: "
                + "" + type + "\nurl: " + url + "\nused: " + used + "\nwho: " + who;
    }
}
