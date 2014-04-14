package com.artofcodeapps.locationalarm.app.Views.textviewoptions;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * Created by Pete on 14.4.2014.
 */
public class TwOptionsBuilder {
    private TwOptions options;
    private TextView tw;


    public TwOptionsBuilder(String content, Context ctx){
        TextView tw = new TextView(ctx);
        tw.setText(content);
        this.tw = tw;
        options = new TwOptions();
    }

    public TwOptions options(){
        return options;
    }

    public TwOptionsBuilder ellipsize(TextUtils.TruncateAt where){
        this.options = new TwOptionsEllipsize(where, tw);
        return this;
    }

    public TwOptionsBuilder fontSize(int type, int fontSize){
        this.options = new TwOptions(type, fontSize, tw);
        return this;
    }

    public TwOptionsBuilder singleLine(boolean single){
        this.options = new TwOptionsSingleLine(single, tw);
        return this;
    }

    public TextView getTextView(){
        return tw;
    }


}
