package com.android.database.local.no_sql.module;

import com.android.database.local.no_sql.annotation.NotNull;
import com.android.database.local.no_sql.exception.NoSqlException;

public class Data {

    private final String value;

    public Data(String value) {
        this.value = value;
    }

    public <T> T parseValue(@NotNull Class<T> clsValue) {
        if (clsValue == null) {
            throw new NoSqlException("");
        }
        try {
            T resultValue = clsValue.newInstance();
            return resultValue;
        } catch (Exception exception) {
            exception.fillInStackTrace();
        }
        return null;
    }


    public boolean isNull() {
        return true;
    }
}
