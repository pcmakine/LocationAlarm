package com.artofcodeapps.locationalarm.app.domain;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;

import com.artofcodeapps.locationalarm.app.services.Database;
import com.artofcodeapps.locationalarm.app.services.DbContract;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Pete on 9.4.2014.
 */
public class ReminderDAO implements Dao, Serializable {
    private List reminders;
    private Database db;

    //todo move the db.getreadabledatabase call to asynctask
    public ReminderDAO(Database db){
        this.db = db;
        reminders = db.getAll();
    }
    public boolean noReminders(){
        return reminders == null || reminders.size()== 0;

    }

    public ContentValues values(Reminder reminder){
        if(reminder.getContent().isEmpty()){
            return null;
        }
        ContentValues vals = new ContentValues();
        vals.put(DbContract.ReminderEntry.COLUMN_NAME_CONTENT, reminder.getContent());
        return vals;
    }

    private void updateReminderAndList(Reminder r, long id){
        r.setId(id);
        reminders.add(r);
    }

    @Override
    public List getAll() {
        return reminders;
    }

    @Override
    public boolean insert(Object r) {
        Reminder reminder = (Reminder) r;
        ContentValues vals = values(reminder);
        if(vals == null){
            return false;
        }
        long id = db.insert(vals, DbContract.ReminderEntry.TABLE_NAME);
        updateReminderAndList(reminder, id);
        return id != -1;
    }

    @Override
    public boolean update(Object d, Object newData) {
        return false;
    }

    @Override
    public boolean remove(Object d) {
        Reminder reminder = (Reminder) d;
        db.deleteReminder(reminder.getId(), DbContract.ReminderEntry.TABLE_NAME);
        return false;
    }

    @Override
    public boolean removeAll() {
        return false;
    }

}
