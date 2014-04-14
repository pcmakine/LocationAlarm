package com.artofcodeapps.locationalarm.app.Views.textviewoptions;

import android.widget.TextView;

/**
 * Created by Pete on 14.4.2014.
 */
public class TwOptions {

    public TwOptions(){

    }

    public TwOptions(int type, int size, TextView tw){
        tw.setTextSize(type, size);
    }
}
