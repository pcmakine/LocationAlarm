package com.artofcodeapps.locationalarm.app.services;

import android.provider.BaseColumns;

/**
 * Created by Pete on 9.4.2014.
 */
public class DbContract {
    public static final String SQL_CREATE_REMINDERS =
            "CREATE TABLE " + ReminderEntry.TABLE_NAME + " (" +
                    ReminderEntry._ID + " INTEGER PRIMARY KEY autoincrement," +
                    ReminderEntry.COLUMN_NAME_CONTENT + " TEXT)";
/*                    FKEY + "(" + ReminderEntry._ID + ")" + REFS + ReminderLocationLinkEntry.TABLE_NAME + "(" +
                    ReminderLocationLinkEntry.COLUMN_NAME_REMINDERID +"))";*/

    public static final String SQL_CREATE_LOCATIONS =
            "CREATE TABLE " + LocationEntry.TABLE_NAME + " (" +
                    LocationEntry._ID + " INTEGER PRIMARY KEY autoincrement," +
                    LocationEntry.COLUMN_NAME_REMINDER_ID + " INTEGER, " +
                    LocationEntry.COLUMN_NAME_LAT +  " REAL," +
                    LocationEntry.COLUMN_NAME_LONG  + " REAL," +
                    " FOREIGN KEY (" + LocationEntry.COLUMN_NAME_REMINDER_ID + ") REFERENCES "
                    + ReminderEntry.TABLE_NAME + "(" +ReminderEntry._ID + ") ON DELETE CASCADE)";
/*                    "(" + LocationEntry._ID + ")" + REFS + ReminderLocationLinkEntry.TABLE_NAME + "(" +
                    ReminderLocationLinkEntry.COLUMN_NAME_LOCATIONID + "))";*/

/*
    public static final String SQL_CREATE_REMINDER_LOCATION_LINKS =
            "CREATE TABLE " + ReminderLocationLinkEntry.TABLE_NAME + " (" +
                    ReminderLocationLinkEntry.COLUMN_NAME_LOCATIONID + INTEGER_TYPE  + COMMA_SEP +
                    ReminderLocationLinkEntry.COLUMN_NAME_REMINDERID + INTEGER_TYPE + COMMA_SEP +
                    FKEY + "(" + ReminderLocationLinkEntry.COLUMN_NAME_LOCATIONID + ")" +
                    REFS + LocationEntry.TABLE_NAME + "(" + LocationEntry._ID + ")" + COMMA_SEP +
                    FKEY + "(" + ReminderLocationLinkEntry.COLUMN_NAME_REMINDERID + ")" +
                    REFS + ReminderEntry.TABLE_NAME + "(" + ReminderEntry._ID + ")" + COMMA_SEP +
                    PKEY + "("  + ReminderLocationLinkEntry.COLUMN_NAME_LOCATIONID + COMMA_SEP +
                    ReminderLocationLinkEntry.COLUMN_NAME_REMINDERID +") )";
*/

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
        public static final String COLUMN_NAME_REMINDER_ID = "reminderID";
    }

    public static abstract class ReminderLocationLinkEntry implements BaseColumns{
        public static final String TABLE_NAME = "links";
        public static final String COLUMN_NAME_REMINDERID = "reminder_id";
        public static final String COLUMN_NAME_LOCATIONID = "location_id";
    }

}
