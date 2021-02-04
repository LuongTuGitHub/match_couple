
package com.android.database.local.no_sql.module;

import com.android.database.local.no_sql.NotNull;
import com.android.database.local.no_sql.exception.NullTarget;

public class Database {

    private String _root = "database:/";

    public Database getReference() {
        return new Database();
    }

    public Database getReference(@NotNull String path) {
        return new Database().child(path);
    }


    /**
     * @return Database;
     */


    public Database child(@NotNull String target) {
        if (target != null) {
            this._root += ("/" + target);
        } else {
            throw new NullTarget("target null");
        }
        return this;
    }


    public String getPath() {
        return this._root;
    }
}
