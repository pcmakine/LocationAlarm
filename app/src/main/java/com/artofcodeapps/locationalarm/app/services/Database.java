package com.artofcodeapps.locationalarm.app.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pete on 9.4.2014.
 */
public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "locAlarmDB";
    private static final int DATABASE_VERSION = 1;

    public Database(Context c){
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.createEntries());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(Database.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data"
        );
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.ReminderEntry.TABLE_NAME);
        onCreate(db);
    }

    public List getReminders() {
        return new ArrayList();
    }
}
