package com.android.database.local.no_sql.module;

import android.content.Context;

import com.android.database.local.no_sql.annotation.NotNull;
import com.android.database.local.no_sql.exception.NoSqlException;

public class NoSql {

    private Context context;
    private String path;

    public NoSql() {
    }

    private NoSql(Context context) {
        this.context = context;
    }

    public NoSql getReference(@NotNull Context context) {
        return new NoSql(context);
    }

    public NoSql getReference(@NotNull Context context, @NotNull String path) {
        return new NoSql(context).addPath(path);
    }

    public Node child(@NotNull String path) {
        if (path == null) {
            throw new NoSqlException("null path");
        }
        if (this.path != null) {
            this.path += path;
            return new Node(context, new Path().child(this.path));
        }
        return new Node(context, new Path().child(path));
    }

    private NoSql addPath(@NotNull String path) {
        this.path += path;
        return this;
    }
}
