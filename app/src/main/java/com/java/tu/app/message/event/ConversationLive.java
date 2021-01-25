package com.java.tu.app.message.event;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class ConversationLive {
    private final ArrayList<String> value;
    private final ArrayList<String> key;

    public ConversationLive() {
        value = new ArrayList<>();
        key = new ArrayList<>();
    }

    public void add(@NotNull String key, String value) {
        this.key.add(key);
        this.value.add(value);
    }

    public void change(@NotNull String key, @NotNull String value) {
        for (int i = 0; i < this.key.size(); i++) {
            if (key.equals(this.key.get(i))) {
                this.value.set(i, value);
            }
        }
    }

    public ArrayList<String> getKey() {
        return key;
    }

    public ArrayList<String> getValue() {
        return value;
    }

    public int size() {
        return key.size();
    }

    public void remove(@NotNull String key) {
        for (int i = 0; i < this.key.size(); i++) {
            if (this.key.get(i).equals(key)) {
                this.key.remove(i);
                this.value.remove(i);
                break;
            }
        }
    }

}
