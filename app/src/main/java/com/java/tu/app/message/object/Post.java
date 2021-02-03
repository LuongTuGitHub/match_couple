package com.java.tu.app.message.object;

public class Post {
    private String author;
    private String key;
    private long time;
    private String body_text;
    private String body_image;
    private String rule;

    public Post() {
    }

    public Post(String author, String key, long time, String body_text, String body_image, String rule) {
        this.author = author;
        this.key = key;
        this.time = time;
        this.body_text = body_text;
        this.body_image = body_image;
        this.rule = rule;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getBody_text() {
        return body_text;
    }

    public void setBody_text(String body_text) {
        this.body_text = body_text;
    }

    public String getBody_image() {
        return body_image;
    }

    public void setBody_image(String body_image) {
        this.body_image = body_image;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
