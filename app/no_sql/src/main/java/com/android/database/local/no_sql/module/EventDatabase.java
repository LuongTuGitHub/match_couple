package com.android.database.local.no_sql.module;

import com.android.database.local.no_sql.annotation.NotNull;

public class EventDatabase {

    public interface ValueChangeDateListener {
        void onChange(@NotNull Data data, @NotNull String key);
    }
}
