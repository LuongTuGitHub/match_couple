package com.android.database.local.no_sql.module;

import com.android.database.local.no_sql.annotation.NotNull;

public class Path {

    private final static String DEFAULT = "";

    private String path;


    @NotNull
    public Path child(@NotNull String _path) {
        if (this.path == null) {
            path = DEFAULT;
        }
        if (this.path.equals(DEFAULT)) {
            this.path += _path;
        } else {
            this.path += ("/" + _path);
        }
        return this;
    }


    public String pathToString() {
        if (path == null) {
            return DEFAULT;
        }
        return path;
    }
}
