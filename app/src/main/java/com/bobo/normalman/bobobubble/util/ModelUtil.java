package com.bobo.normalman.bobobubble.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * Created by xiaobozhang on 8/17/17.
 */

public class ModelUtil {
    static Gson gson = new Gson();
    static String SP_PREF = "models";

    public static void save(Context context, String key, Object obj) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SP_PREF, Context.MODE_PRIVATE);
        String val = gson.toJson(obj);
        sp.edit().putString(key, val).apply();
    }

    public static <T> T read(Context context, String key, TypeToken<T> token) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SP_PREF, Context.MODE_PRIVATE);
        try {
            return gson.fromJson(sp.getString(key, ""), token.getType());
        } catch (JsonSyntaxException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static <T> T toObject(String json, TypeToken<T> token) {
        return gson.fromJson(json, token.getType());
    }

    public static <T> String toString(T obj, TypeToken<T> token) {
        return gson.toJson(obj, token.getType());
    }
}
