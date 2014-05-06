package com.artofcodeapps.locationalarm.app.Views;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.artofcodeapps.locationalarm.app.R;
import com.artofcodeapps.locationalarm.app.domain.ReminderLocation;
import com.artofcodeapps.locationalarm.app.domain.Reminder;
import com.artofcodeapps.locationalarm.app.services.DataManager;
import com.google.android.gms.maps.model.LatLng;

public class AddActivity extends ActionBarActivity {
    private LatLng location;
    private String reminderText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.location = getIntent().getParcelableExtra("location");
        setContentView(R.layout.activity_add);
        TextView locationText = (TextView) findViewById(R.id.location);
        if(location != null){
            locationText.setText(location.toString());
        }
/*        if(location != null){
            Toast.makeText(this, "Location is " + location.toString(), Toast.LENGTH_SHORT).show();
        }*/

    }

    public void add(View view){
/*        ReminderDAO reminders = new ReminderDAO(new Database(this));
        LocationDAO locations = new LocationDAO(new Database(this));
        String content  = getEditTextContent(R.id.contentToSave);
        Reminder r = new Reminder(content);
        boolean remdinerInsertSuccess = reminders.insert(r);*/
        reminderText  = getEditTextContent(R.id.contentToSave);
        Reminder r = new Reminder(reminderText);

        if(DataManager.saveReminder(r, new ReminderLocation(location), this)){
            Toast toast = Toast.makeText(this, R.string.successfully_added, Toast.LENGTH_LONG);
            toast.show();
            onBackPressed();
        }else{
            Toast toast = Toast.makeText(this, R.string.reminder_not_added, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void onBackPressed(){
        Bundle bundle = new Bundle();
        bundle.putString("reminderText", reminderText);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        super.onBackPressed();
    }


    private String getEditTextContent(int viewID){
        return ((EditText) findViewById(viewID)).getText().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
