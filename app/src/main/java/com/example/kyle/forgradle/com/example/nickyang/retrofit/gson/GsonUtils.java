package com.example.kyle.forgradle.com.example.kyle.retrofit.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by nick.yang on 2017/7/19.
 */

public class GsonUtils {
    private GsonUtils() {

    }

    public static <T> T parseObj(String json, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    public static <T> List<T> parseObjs(String json, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<T>>() {
        }.getType());
    }
}
