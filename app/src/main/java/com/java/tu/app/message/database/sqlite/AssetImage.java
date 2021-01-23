package com.java.tu.app.message.database.sqlite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import org.jetbrains.annotations.NotNull;

public class AssetImage extends SQLiteOpenHelper {

    private final static String DB_IMAGE = "image.db";
    private final static int VERSION = 1;

    public AssetImage(@NotNull Context context) {
        super(context, DB_IMAGE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS IMAGE(ID INTEGER PRIMARY KEY AUTOINCREMENT,UUID VARCHAR(255),BYTES BLOG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE TABLE IF NOT EXISTS IMAGE(ID INTEGER PRIMARY KEY AUTOINCREMENT,UUID VARCHAR(255),BYTES BLOG)");
    }

    public boolean checkExist(@NotNull String key) {
        SQLiteDatabase database = getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = database.rawQuery("SELECT UUID FROM IMAGE WHERE UUID LIKE ?", new String[]{key});
        if(cursor==null){
            database.close();
            return false;
        }
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.close();
            database.close();
            return true;
        }
        database.close();
        cursor.close();
        return false;
    }

    public void add(@NotNull String key, @NotNull byte[] bytes) {
        String sql = "INSERT INTO IMAGE VALUES(null,?,?)";
        SQLiteStatement statement = getWritableDatabase().compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, key);
        statement.bindBlob(2, bytes);
        statement.executeInsert();
        statement.close();
    }

    public byte[] getImage(String key) {
        byte[] bytes = null;
        try {
            SQLiteDatabase database = getReadableDatabase();
            @SuppressLint("Recycle") Cursor cursor = database.rawQuery("SELECT BYTES FROM IMAGE WHERE UUID LIKE ?", new String[]{key});
            cursor.moveToFirst();
            bytes = cursor.getBlob(0);
            database.close();
            cursor.close();
            if (bytes != null) {
                return bytes;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return bytes;
    }
}
