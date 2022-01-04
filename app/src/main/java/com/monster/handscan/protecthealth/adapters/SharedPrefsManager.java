package com.monster.handscan.protecthealth.adapters;

import android.content.Context;
import android.content.SharedPreferences;

import com.monster.handscan.protecthealth.activity.MainActivity;

public class SharedPrefsManager {
    private static final String PREFS_NAME = "share_prefs";
    private static SharedPrefsManager mInstance;
    private SharedPreferences mSharedPreferences;

    private SharedPrefsManager() {
        mSharedPreferences = MainActivity.self().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPrefsManager getInstance() {
        if (mInstance == null) {
            mInstance = new SharedPrefsManager();
        }
        return mInstance;
    }

    public boolean getBoolean(String key) {
        try {
            return mSharedPreferences.getBoolean(key, false);
        } catch (Exception e) {
            return false;
        }
    }

    public int getInt(String key) {
        try {
            return mSharedPreferences.getInt(key, 0);
        } catch (Exception e) {
            return 0;
        }
    }

    public String getString(String key) {
        try {
            return mSharedPreferences.getString(key, "");
        } catch (Exception e) {
            return "";
        }
    }

    public long getLong(String key) {
        try {
            return mSharedPreferences.getLong(key, 0);
        } catch (Exception e) {
            return 0;
        }
    }

    public float getFloat(String key) {
        try {
            return mSharedPreferences.getFloat(key, 0);
        } catch (Exception e) {
            return 0;
        }
    }

//    public Gson getJson(String key) {
//        try {
//            return GameActivity.self().getGSon().fromJson(mSharedPreferences.getString(key, ""), null);
//        } catch (Exception e) {
//            return getJson("");
//        }
//    }
//
//    public <T> void putJson(String key, T data) {
//        try {
//            SharedPreferences.Editor editor = mSharedPreferences.edit();
//            editor.putString(key, GameActivity.self().getGSon().toJson(data)).apply();
//        } catch (Exception e) {
//
//        }
//    }

    public void putString(String key, String data) {
        try {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(key, data).apply();
        } catch (Exception e) {

        }
    }

    public void putInt(String key, int data) {
        try {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putInt(key, data).apply();
        } catch (Exception e) {

        }
    }

    public void putBoolean(String key, boolean data) {
        try {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(key, data).apply();
        } catch (Exception e) {

        }
    }

    public void putFloat(String key, float data) {
        try {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putFloat(key, data).apply();
        } catch (Exception e) {

        }
    }

    public void putLong(String key, long data) {
        try {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putLong(key, data).apply();
        } catch (Exception e) {

        }
    }

    public void clear() {
        mSharedPreferences.edit().clear().apply();
    }
}
