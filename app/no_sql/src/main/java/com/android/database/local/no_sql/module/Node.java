package com.android.database.local.no_sql.module;

import com.android.database.local.no_sql.annotation.NotNull;

public class Node {

    private Database database;

    public Node() {
        database = Database.getReference();
    }

    public Node child(Node node, @NotNull String path) {
        if (node == null) {
            node = new Node();
            child(node, path);
        }
        database.child(path);
        return this;
    }
}
