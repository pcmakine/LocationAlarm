package com.artofcodeapps.locationalarm.app.Views;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Pete on 17.4.2014.
 */

//Maybe not a very useful class
public class ErrorMessage {
    private String msg;
    private int msgResource;

    public ErrorMessage(String msg){
        this.msg = msg;
    }

    public ErrorMessage(int msgResource){
        this.msgResource = msgResource;
    }

    public void show(Context ctx, int length){
        if(msg != null){
            Toast.makeText(ctx, msg, length);
        }else{
            Toast.makeText(ctx, ctx.getResources().getString(msgResource), length);
        }
    }
}
