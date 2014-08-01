package com.artofcodeapps.locationalarm.app.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.artofcodeapps.locationalarm.app.domain.Data;
import com.artofcodeapps.locationalarm.app.domain.Reminder;
import com.artofcodeapps.locationalarm.app.domain.ReminderDAO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pete on 9.4.2014.
 */

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "locAlarmDB";
    private static final int DATABASE_VERSION = 12;

    public Database(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.SQL_CREATE_REMINDERS);
        db.execSQL(DbContract.SQL_CREATE_LOCATIONS);
        // db.execSQL(DbContract.SQL_CREATE_REMINDER_LOCATION_LINKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(Database.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data"
        );
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.ReminderEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.LocationEntry.TABLE_NAME);
      //  db.execSQL("DROP TABLE IF EXISTS " + DbContract.ReminderLocationLinkEntry.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        if(!db.isReadOnly()){
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
        super.onOpen(db);
    }

    /**
     * Inserts a new row into the database
     *
     * @param vals      The values to be inserted
     * @param tableName The table where the values are to be inserted
     * @return The id of the new row. If the operation was unsuccessful, -1 is returned
     */
    public long insert(ContentValues vals, String tableName) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        long newRowId = db.insertOrThrow(
                tableName,
                null,   //if there are no values in the values variable, the framework won't insert anything
                vals);
        db.close();
        return newRowId;
    }

    public int totalRows(String tablename){
        String countQuery = "SELECT  * FROM " + tablename;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public List getAll(String tableName, String sortOrder, Class c) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                tableName,
                null, //reads all the fields
                null,
                null,
                null,
                null,
                sortOrder);

        List list = entriesAsList(cursor, c);
        cursor.close();
        db.close();
        return list;
    }

    public List get(String tableName, String selection, String[] selectionArgs, String sortOrder, Class c) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                tableName,
                null, //reads all the fields
                selection,
                selectionArgs,
                null,
                null,
                sortOrder,
                null);

        List list = entriesAsList(cursor, c);
        cursor.close();
        db.close();
        return list;
    }


    public Object executeRaw(String query, Class c) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                query, null);
        int count = cursor.getCount();
        List list = entriesAsList(cursor, c);
        cursor.close();
        db.close();
        return list.get(0);
    }


    private List entriesAsList(Cursor cursor, Class c) {
        ArrayList list = new ArrayList();
        String cursorContent= "";
        if (cursor != null && cursor.moveToFirst()) {
            do {
            //    cursorContent = "ReminderId = " + cursorContent + cursor.
                Object data = makeOneEntry(cursor, c);
                if(data != null){
                    list.add(data);
                }
            } while (cursor.moveToNext());
        }
        return list;
    }

    private Object makeOneEntry(Cursor cursor, Class c) {
        Object data = null;
        try {
            Class[] cArg = new Class[1];
            cArg[0] = Cursor.class;
            Method m = c.getMethod("createOneEntry", Cursor.class);
            data = m.invoke(null, cursor);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return data;
    }

    public boolean deleteEntry(long id, String tableName, String entryIdColumnName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(tableName, entryIdColumnName + " =?",
                new String[]{id + ""});
        db.close();
        return rowsAffected != 0;
    }

    public int update(ContentValues vals, String tableName, String idRowName, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int success =  db.update(tableName, vals, idRowName + " = ?",
                new String[]{id});
        db.close();
        return success;
    }

    /**
     * For now returns one reminder. Needs to be generalized
     *
     * @return
     */
    public Object getEntry(String tableName, String entryIdColumnName, long id, Class c) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(tableName, null,   //null here returns all the columns
                entryIdColumnName + " = ?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor == null) {
            return null;
        }

        cursor.moveToFirst();
        Object data = makeOneEntry(cursor, c);
        cursor.close();
        db.close();
        return data;
    }


    /**
     * shows all the tables and columns of the database in the logcat
     */
    public void showAllTables() {
        SQLiteDatabase db = this.getReadableDatabase();
        String mySql = " SELECT name FROM sqlite_master " + " WHERE type='table'";

        Cursor c = db.rawQuery(mySql, null);
        c.moveToFirst();
        c.moveToPosition(2);  // may be two, not sure if the cursor starts at 0 or 1
        while (c.isAfterLast() == false) {
            String tblName = c.getString(0);
            Cursor table = db.rawQuery("SELECT * FROM " + tblName,null);
            table.moveToFirst();
            Log.e("table", tblName);
            logColumnNames(table.getColumnNames());
            c.moveToNext();
        }
    }

    private void logColumnNames(String[] names){
        for(String s: names){
            Log.e("column", s);
        }
/*        if (c.moveToFirst()) {
            do {
                todoItems.add(c.getString(0));

            } while (c.moveToNext());
        }
        if (todoItems.size() >= 0) {
            for (int i = 0; i < todoItems.size(); i++) {
                Log.e("TODOItems(" + i + ")", todoItems.get(i) + "");

            }
        }*/


    }

}
