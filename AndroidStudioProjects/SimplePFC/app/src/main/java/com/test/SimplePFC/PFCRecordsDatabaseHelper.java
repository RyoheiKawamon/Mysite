package com.test.SimplePFC;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PFCRecordsDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pfcRecords.db";
    private static final int DATABASE_VERSION = 1;

    public PFCRecordsDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE pfcRecords(");
        sb.append("_id INTEGER PRIMARY KEY,");
        sb.append("date DATETIME,");
        sb.append("time DATETIME,");
        sb.append("comment STRING,");
        sb.append("protein DOUBLE,");
        sb.append("fat DOUBLE,");
        sb.append("carb DOUBLE");
        sb.append(");");
        String sql = sb.toString();
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
