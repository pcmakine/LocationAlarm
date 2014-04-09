package com.artofcodeapps.locationalarm.app.domain;

import com.artofcodeapps.locationalarm.app.services.Database;

import java.util.List;

/**
 * Created by Pete on 9.4.2014.
 */
public class ReminderDAO implements Dao {
    private List reminders;
    private Database db;

    //todo move the db.getreadabledatabase call to asynctask
    public ReminderDAO(){
        this.db = db;
        reminders = db.getReminders();
    }

    public boolean noReminders(){
        return reminders == null;
    }

    @Override
    public List getAll() {
        return reminders;
    }

    @Override
    public boolean insert(Object r) {
        Reminder reminder = (Reminder) r;
        if(reminder.getContent().isEmpty()){
            return false;
        }
        return false;
    }

    @Override
    public boolean update(Object d, Object newData) {
        return false;
    }

    @Override
    public boolean remove(Object d) {
        return false;
    }

    @Override
    public boolean removeAll() {
        return false;
    }

}
