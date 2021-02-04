package com.android.database.local.no_sql.module;

import com.android.database.local.no_sql.annotation.NotNull;
import com.android.database.local.no_sql.exception.ObjectNullException;

public class JSON {

    @NotNull
    public String convert(@NotNull Object object) {
        if (object == null) {
            throw new ObjectNullException("object null");
        }
        StringBuilder result = new StringBuilder().append("{");


        return result.toString();
    }
}
