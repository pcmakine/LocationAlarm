package com.artofcodeapps.locationalarm.app.services;

import android.provider.BaseColumns;

/**
 * Created by Pete on 9.4.2014.
 */
public class DbContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_REMINDERS =
            "CREATE TABLE " + ReminderEntry.TABLE_NAME + " (" +
                    ReminderEntry._ID + " INTEGER PRIMARY KEY autoincrement" + COMMA_SEP  +
                    ReminderEntry.COLUMN_NAME_CONTENT + TEXT_TYPE +" )";

    public static final String SQL_CREATE_LOCATIONS =
            "CREATE TABLE " + LocationEntry.TABLE_NAME + " (" +
                    LocationEntry._ID + " INTEGER PRIMARY KEY autoincrement" + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LAT + REAL_TYPE  + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LONG + REAL_TYPE + " )";

    public DbContract(){

    }

    public static abstract class ReminderEntry implements BaseColumns {
        public static final String TABLE_NAME = "reminders";
        public static final String COLUMN_NAME_CONTENT = "content";
    }

    public static abstract class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "locations";
        public static final String COLUMN_NAME_LAT = "lat";
        public static final String COLUMN_NAME_LONG = "long";

    }

}
