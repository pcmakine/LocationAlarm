package com.artofcodeapps.locationalarm.app.domain;

/**
 * Created by Pete on 9.4.2014.
 */
public interface Dao {
    public List getAll();
    public boolean insert();
    public boolean update();
    public boolean remove();
    public boolean removeAll();
}
