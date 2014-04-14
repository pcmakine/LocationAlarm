package com.artofcodeapps.locationalarm.app.Views.textviewoptions;

import android.text.TextUtils;
import android.widget.TextView;

/**
 * Created by Pete on 14.4.2014.
 */
public class TwOptionsEllipsize extends TwOptions{

    public TwOptionsEllipsize(TextUtils.TruncateAt where, TextView tw) {
        tw.setEllipsize(where);
       }
}
