package com.artofcodeapps.locationalarm.app.domain;
import java.util.List;

/**
 * Created by Pete on 9.4.2014.
 */
public interface Dao {
    public List getAll();
    public boolean insert(Object data);
    public boolean update(Object newData);
    public boolean remove(Object data);
    public boolean removeAll();
}
