package com.java.tu.app.message.object;

import java.util.ArrayList;

public class Conversation {
    private ArrayList<String> persons;
    private String user_create;
    private int type;
    private String message;
    private String name;
    private String image;

    public Conversation() {
    }

    public Conversation(ArrayList<String> persons, String user_create, int type, String message) {
        this.persons = persons;
        this.user_create = user_create;
        this.type = type;
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<String> persons) {
        this.persons = persons;
    }

    public String getUser_create() {
        return user_create;
    }

    public void setUser_create(String user_create) {
        this.user_create = user_create;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
