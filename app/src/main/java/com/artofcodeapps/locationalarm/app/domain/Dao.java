package com.artofcodeapps.locationalarm.app.domain;
import android.database.SQLException;

import java.util.List;

/**
 * Created by Pete on 9.4.2014.
 */
public interface Dao {
    public List getAll();
    public Object getOne(Long id);
    public long insert(Object data) throws SQLException;
    public boolean update(Object newData);
    public boolean remove(Object data);
    public boolean removeAll();
}
