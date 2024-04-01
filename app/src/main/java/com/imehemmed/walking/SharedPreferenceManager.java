package com.imehemmed.walking;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {

    //constants for SharedPreference
    private final int PRIVATE_MODE = 0;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    public SharedPreferenceManager(Context context) {
        preferences = context.getSharedPreferences(context.getPackageName(), PRIVATE_MODE);
        editor = preferences.edit();

        this.context = context;
    }

    public boolean isContained(String s) {
        return preferences.contains(s);
    }

    public void setValue(Object key, Object value) {
        if (value instanceof String) {
            editor.putString(((String) key).toLowerCase(), value.toString()); // value to store
            editor.commit();
        } else if (value instanceof Boolean) {
            editor.putBoolean(((String) key).toLowerCase(), (Boolean) value); // value to store
            editor.commit();
        } else if (value instanceof Integer) {
            editor.putInt(((String) key).toLowerCase(), (Integer) value); // value to store
            editor.commit();
        } else if (value instanceof Long) {
            editor.putLong(((String) key).toLowerCase(), (Long) value); // value to store
            editor.commit();
        }
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key.toLowerCase(), defValue);
    }

    public Boolean getBoolean(String key, Boolean defValue) {
        return preferences.getBoolean(key.toLowerCase(), defValue);
    }

    public Integer getInt(String key, Integer defValue) {
        return preferences.getInt(key.toLowerCase(), defValue);
    }

    public Long getLong(String key, Long defValue) {
        return preferences.getLong(key.toLowerCase(), defValue);
    }
}

