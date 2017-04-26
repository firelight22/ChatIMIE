package com.chatimie.arthurcouge.chatimie.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Quentin for ChatIMIE on 30/03/2017.
 */

public class PostsBDDHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "chat.db";
    private static final int DATABASE_VERSION = 1;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_POSTS_TABLE = "CREATE TABLE " + PostsBDDContract.PostsEntry.TABLE_NAME + " (" +
                PostsBDDContract.PostsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PostsBDDContract.PostsEntry.COLUMN_MESSAGE_NAME + " TEXT NOT NULL, " +
                PostsBDDContract.PostsEntry.COLUMN_PSEUDO_NAME + " TEXT NOT NULL, " +
                PostsBDDContract.PostsEntry.COLUMN_DATE_NAME + " TEXT NOT NULL" +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_POSTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PostsBDDContract.PostsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    public PostsBDDHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
