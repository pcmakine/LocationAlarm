package com.artofcodeapps.locationalarm.app.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.artofcodeapps.locationalarm.app.domain.Reminder;
import com.artofcodeapps.locationalarm.app.domain.ReminderDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pete on 9.4.2014.
 */

/*Todo figure out how to generalize the class, so that it would also work for other objects than just
    the reminders */
public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "locAlarmDB";
    private static final int DATABASE_VERSION = 2;

    public Database(Context c){
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.SQL_CREATE_REMINDERS);
        db.execSQL(DbContract.SQL_CREATE_LOCATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(Database.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data"
        );
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.ReminderEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.LocationEntry.TABLE_NAME);
        onCreate(db);
    }

    /**
     * Inserts a new row into the database
     * @param vals  The values to be inserted
     * @param tableName The table where the values are to be inserted
     * @return  The id of the new row. If the operation was unsuccessful, -1 is returned
     */
    public long insert(ContentValues vals, String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        long newRowId = db.insert(
                tableName,
                null,   //if there are no values in the values variable, the framework won't insert anything
                vals);
        db.close();
        return newRowId;
    }

    public Cursor getAll(String tableName, String sortOrder){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(
                tableName,
                null, //reads all the fields
                null,
                null,
                null,
                null,
                sortOrder);
        return c;
    }

    public boolean deleteEntry(long id, String tableName, String entryIdColumnName){
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(tableName, entryIdColumnName+ " =?",
                new String[] {id+""});
        db.close();
        return rowsAffected != 0;
    }

    public int update(ContentValues vals, String tableName, String idRowName, String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(tableName, vals, idRowName + " = ?",
                new String[] {id});
    }

    /**
     * For now returns one reminder. Needs to be generalized
     * @return
     */
    public Cursor getEntry(String tableName,String entryIdColumnName, long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(tableName, null,   //null here returns all the columns
                entryIdColumnName + " = ?",
                new String[] {String.valueOf(id)}, null, null, null, null);
        return cursor;
    }


}
