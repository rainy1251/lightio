package com.aiyaopai.lightio.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {

    private static SharedPreferences mSharedPreferences;

    public static SharedPreferences instance(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        }

        return mSharedPreferences;
    }

    //存储方式,存储位置：/data/data/<package name>/shared_prefs
    public static void save(String key,
                            Object value) {

        if (value instanceof String) {
            mSharedPreferences.edit().putString(key, (String) value).commit();
        } else if (value instanceof Boolean) {
            mSharedPreferences.edit().putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof Integer) {
            mSharedPreferences.edit().putInt(key, (Integer) value).commit();
        } else if (value instanceof Float) {
            mSharedPreferences.edit().putFloat(key, (Float) value).commit();
        } else if (value instanceof Long) {
            mSharedPreferences.edit().putLong(key, (Long) value).commit();
        }
    }


    public static void remove(String key) {
        mSharedPreferences.edit().remove(key).commit();
    }


    // 获取SharedPreferences数据指定key所对应的value，如果该key不存在，返回默认值false(boolen),""(string);
    public static Boolean getBoolean(
            String key) {

        return mSharedPreferences.getBoolean(key, false);
    }

    public static Boolean getBooleanDefTrue(
            String key) {

        return mSharedPreferences.getBoolean(key, true);
    }

    public static String getString(String key) {

        return mSharedPreferences.getString(key, "");
    }

    public static String getPxString(String key) {

        return mSharedPreferences.getString(key, Contents.Standard);
    }

    public static String getModeString(String key) {

        return mSharedPreferences.getString(key, Contents.DIRECT_UPLOAD);
    }

    public static int getInt(String key) {

        return mSharedPreferences.getInt(key, 0);
    }

    public static long getLong(String key) {

        return mSharedPreferences.getLong(key, 0);
    }

    /**
     * 清空所有数据
     *
     * @param
     * @param fileName
     */
    public static void removeFile(String fileName) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

}
