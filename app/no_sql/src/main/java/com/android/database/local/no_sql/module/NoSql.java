package com.android.database.local.no_sql.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.database.local.no_sql.NotNull;
import com.android.database.local.no_sql.exception.NullTarget;

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
        this.refShared = context.getSharedPreferences("db_no_sql", Context.MODE_PRIVATE);
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


}
