package com.android.database.local.no_sql.module;

import android.content.SharedPreferences;

import com.android.database.local.no_sql.annotation.NotNull;

public class EventDatabase {

    private final SharedPreferences refShared;
    private final String path;

    public EventDatabase(SharedPreferences refShared, String path) {
        this.refShared = refShared;
        this.path = path;
    }

    public void addEventChangeData(@NotNull ValueChangeListener valueChangeListener) {
        refShared.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(path)) {
                    valueChangeListener.onChange();
                }
            }
        });
    }


    public interface ValueChangeListener {
        void onChange();
    }
}
