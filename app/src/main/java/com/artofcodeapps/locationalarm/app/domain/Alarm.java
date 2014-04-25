package com.artofcodeapps.locationalarm.app.domain;

import android.media.RingtoneManager;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pete on 24.4.2014.
 */
public class Alarm {
    private List<Trigger> triggers;

    public Alarm(Trigger trigger){
        triggers = new ArrayList();
        triggers.add(trigger);
    }

    public void addTrigger(Trigger trigger){
        triggers.add(trigger);
    }

    public void soundAlarm(){
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if(alert == null){
            // alert is null, using backup
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // I can't see this ever being null (as always have a default notification)
            // but just incase
            if(alert == null) {
                // alert backup is null, using 2nd backup
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
    }
}
