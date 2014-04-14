package com.artofcodeapps.locationalarm.app.domain;

import android.content.ContentValues;

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

    @Override
    public Object getOne(Long id) {
        return db.getReminder(id);
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
        if(!reminder.hasContent()){
            return false;
        }
        ContentValues vals = values(reminder);
        long id = db.insert(vals, DbContract.ReminderEntry.TABLE_NAME);
        updateReminderAndList(reminder, id);
        return id != -1;
    }

    public ContentValues values(Reminder reminder){
        ContentValues vals = new ContentValues();
        vals.put(DbContract.ReminderEntry.COLUMN_NAME_CONTENT, reminder.getContent());
        return vals;
    }

    @Override
    public boolean update(Object r) {
        Reminder reminder = (Reminder) r;
        if(!reminder.hasContent()){
            return false;
        }
        ContentValues vals = values(reminder);
        int numOfRowsAffected = db.update(vals, DbContract.ReminderEntry.TABLE_NAME, DbContract.ReminderEntry._ID, String.valueOf(reminder.getId()));
        return numOfRowsAffected > 0;
    }

    @Override
    public boolean remove(Object d) {
        Reminder reminder = (Reminder) d;
        return db.deleteReminder(reminder.getId(), DbContract.ReminderEntry.TABLE_NAME);
    }

    @Override
    public boolean removeAll() {
        return false;
    }

}
