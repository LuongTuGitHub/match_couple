package com.java.tu.app.message.asset;

import org.jetbrains.annotations.NotNull;

public class Key {
    public String getKey(@NotNull String person1, @NotNull String person2) {
        StringBuilder result = new StringBuilder();

        if (person1.compareTo(person2) >= 0) {
            String key1 = person1.hashCode() + "";
            String key2 = person2.hashCode() + "";
            result.append(key1);
            result.append(key2);
        } else {
            String key2 = person1.hashCode() + "";
            String key1 = person2.hashCode() + "";
            result.append(key1);
            result.append(key2);
        }

        return result.toString();
    }
}
