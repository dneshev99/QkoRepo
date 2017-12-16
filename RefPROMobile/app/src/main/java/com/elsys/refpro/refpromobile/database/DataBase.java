package com.elsys.refpro.refpromobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 30.11.2017 г..
 */

public class DataBase extends SQLiteOpenHelper {

    private static final String TAG = "DATABASE";

    private static final String TABLE_NAME = "match_info";
    private static final String COL1 = "ID";
    private static final String COL2 = "competition";
    private static final String COL3 = "venue";
    private static final String COL4 = "home";
    private static final String COL5 = "away";
    private static final String COL6 = "homeabbr";
    private static final String COL7 = "awayabbr";
    private static final String COL8 = "date";
    private static final String COL9 = "time";
    private static final String COL10 = "players";
    private static final String COL11 = "substitutes";
    private static final String COL12 = "length";
    private static final String COL13 = "MongoID";

    public DataBase(Context context) {
        super(context, TABLE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 +  " TEXT, "
                + COL3 +  " TEXT, " + COL4 +  " TEXT, " + COL5 +  " TEXT, " + COL6 +  " TEXT, " + COL7 +  " TEXT, "
                + COL8 +  " TEXT, " + COL9 +  " TEXT, " + COL10 +  " TEXT, " + COL11 +  " TEXT, " + COL12 +  " TEXT, " + COL13 + " TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String competition, String venue, String home, String away, String homeabbr,
                           String awayabbr, String date, String time, int players, int substitutes,
                           int length, String MongoID) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, competition);
        contentValues.put(COL3, venue);
        contentValues.put(COL4, home);
        contentValues.put(COL5, away);
        contentValues.put(COL6, homeabbr);
        contentValues.put(COL7, awayabbr);
        contentValues.put(COL8, date);
        contentValues.put(COL9, time);
        contentValues.put(COL10, players);
        contentValues.put(COL11, substitutes);
        contentValues.put(COL12, length);
        contentValues.put(COL13, MongoID);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1)
            return false;
        else
            return true;

    }

    public Cursor getData() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL8 + " ASC";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void delete(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'";
        db.execSQL(query);
    }

    public Cursor getRow(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL1 + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}
