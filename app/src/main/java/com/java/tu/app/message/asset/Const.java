package com.java.tu.app.message.asset;

public class Const {
    public final static String SESSION = "session";
    public final static long TIME_SLASH = 1500;
    public final static int SIGN_UP = 9;
    public final static String STATUS = "status";
    public final static String ONLINE = "online";
    public final static String OFFLINE = "offline";
    public final static String ACCOUNT = "account";
    public final static String PROFILE = "profile";
    public final static String IMAGE = "image";
    public final static String KEY = ".png";
    public final static String NAME = "name";
    public final static String CONVERSATION = "conversation";
    public final static String TYPE = "type";
    public final static String CHAT = "chat";
    public final static String AVATAR = "avatar";
    public final static int PICK_IMAGE = 100;

    public static class Message {
        public final static int TEXT = 0;
        public final static int IMAGE = 1;
        public final static int VIDEO = 2;
        public final static int SOUND = 3;
        public final static int HIDE_TEXT = 4;
        public final static int HIDE_IMAGE = 5;
        public final static int HIDE_VIDEO = 6;
        public final static int HIDE_SOUND = 7;
        public final static int LIST_IMAGE = 8;
        public final static int LIST_VIDEO = 9;
        public final static int LIST_CUSTOM = 10;
        public final static int HIDE_LIST = 11;
        public final static int DELETE = 12;
    }

    public static class Conversation {
        public final static int NORMAl = 0;
        public final static int GROUP = 1;
        public final static int COUPLE = 2;
    }

}
