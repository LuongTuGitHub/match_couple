package com.android.database.local.no_sql.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.database.local.no_sql.annotation.NotNull;
import com.android.database.local.no_sql.exception.NoSqlException;

public class Node {

    private final static String _ROOT = "database";
    private final Path path;
    private final Context context;

    public Node(@NotNull Context context, @NotNull Path path) {
        this.context = context;
        this.path = path;
    }

    private Path getPath() {
        return path;
    }

    public Node child(@NotNull String path) {
        if (path == null) {
            throw new NoSqlException("null path");
        }
        Path _path = new Path().child(path);
        return new Node(context, _path);
    }

    public void setValue(@NotNull Object object) {
        String value = JSON.convertToJSON(object);
        String path = getPath().pathToString();
        SharedPreferences shared = context.getSharedPreferences(_ROOT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        if (path != null) {
            editor.remove(path);
            editor.putString(path, value);
            editor.apply();
        }
    }

    public void addValueDataChangeListener(@NotNull EventDatabase.ValueChangeDateListener valueChangeDateListener) {
        SharedPreferences shared = context.getSharedPreferences(_ROOT, Context.MODE_PRIVATE);
        shared.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                String path = getPath().pathToString();
                if (key.equals(path)) {
                    String value = sharedPreferences.getString(key, "");
                    Data data = new Data(value);
                    if (data.isNull()) {
                        valueChangeDateListener.onChange(null, key);
                    } else {
                        valueChangeDateListener.onChange(data, key);
                    }
                }
            }
        });
    }

}
