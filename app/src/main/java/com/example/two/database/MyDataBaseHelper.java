package com.example.two.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by liuhuan1 on 2018/6/15.
 */

public class MyDataBaseHelper extends SQLiteOpenHelper {

    String CREATE_TABLE_PROVINCES="create table provinces("+
            "id integer primary key," +
            "name text)";

    String CREATE_TABLE_CITY="create table cities(" +
            "id integer primary key," +
            "fatherId int," +
            "name text)";

    String CREATE_TABLE_COUNTY="create table counties(" +
            "id integer primary key," +
            "fatherId int," +
            "name text," +
            "weather_id text)";

    public MyDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PROVINCES);
        db.execSQL(CREATE_TABLE_CITY);
        db.execSQL(CREATE_TABLE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
