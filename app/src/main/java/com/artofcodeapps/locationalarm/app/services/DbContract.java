package com.artofcodeapps.locationalarm.app.services;

import android.provider.BaseColumns;

/**
 * Created by Pete on 9.4.2014.
 */
public class DbContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ReminderEntry.TABLE_NAME + " (" +
                    ReminderEntry._ID + " INTEGER PRIMARY KEY autoincrement" + COMMA_SEP  +
                    ReminderEntry.COLUMN_NAME_CONTENT + TEXT_TYPE +" )";

    public DbContract(){

    }

    public static String createEntries(){
        return SQL_CREATE_ENTRIES;
    }
    public static abstract class ReminderEntry implements BaseColumns {
        public static final String TABLE_NAME = "reminders";
        public static final String COLUMN_NAME_ENTRY_ID = "reminderID";
        public static final String COLUMN_NAME_CONTENT = "content";

    }

}
