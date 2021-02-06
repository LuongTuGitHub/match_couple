package com.android.database.local.no_sql.module;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.database.local.no_sql.annotation.NotNull;
import com.android.database.local.no_sql.exception.NullTarget;
import com.android.database.local.no_sql.exception.ObjectNullException;

import static com.android.database.local.no_sql.module.Database._root;

public class NoSql {
    private final Context context;
    private SharedPreferences refShared;
    private Database database;

    public NoSql(@NotNull Context context) {
        this.context = context;
    }

    public NoSql getInstance() {
        return new NoSql(this.context);
    }

    public NoSql getReference() {
        this.database = new Database().getReference();
        this.refShared = context.getSharedPreferences(_root, Context.MODE_PRIVATE);
        return this;
    }

    public NoSql getReference(@NotNull String path) {
        if (path == null) {
            throw new NullTarget("null path exception");
        }
        this.database = new Database().getReference(path);
        return this;
    }

    public NoSql child(@NotNull String path) {
        this.database.child(path);
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setValue(@NotNull Object object) {
        if (database == null || refShared == null) {
            throw new ObjectNullException("null not reference");
        }
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = refShared.edit();
        if (isEmpty()) {
            editor.clear();
        }
        try {
            editor.putString(database.getPath(), JSON.convertJSON(object));
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        editor.apply();
    }

    public void addEventChangeData(@NotNull EventDatabase.ValueChangeListener valueChangeListener) {
        new EventDatabase(refShared, database.getPath()).addEventChangeData(valueChangeListener);
    }

    public boolean isEmpty() {
        return refShared.contains(database.getPath());
    }


}
