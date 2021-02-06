
package com.android.database.local.no_sql.module;

import com.android.database.local.no_sql.annotation.NotNull;
import com.android.database.local.no_sql.exception.NullTarget;

public class Database {

    public static final String _root = "database";
    private String path = "";

    public static Database getReference() {
        return new Database();
    }

    public static Database getReference(@NotNull String path) {
        return new Database().child(path);
    }


    /**
     * @return Database;
     */


    public Database child(@NotNull String target) {
        if (target != null) {
            if (this.path.length() == 0) {
                this.path += target;
            } else {
                this.path += ("/" + target);
            }
        } else {
            throw new NullTarget("target null");
        }
        return this;
    }


    public String getPath() {
        return this.path;
    }


}
