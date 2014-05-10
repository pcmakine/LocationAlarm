package com.artofcodeapps.locationalarm.app.Views;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.artofcodeapps.locationalarm.app.R;
import com.artofcodeapps.locationalarm.app.domain.ReminderDAO;
import com.artofcodeapps.locationalarm.app.domain.ReminderLocation;
import com.artofcodeapps.locationalarm.app.domain.Reminder;
import com.artofcodeapps.locationalarm.app.services.DataManager;
import com.artofcodeapps.locationalarm.app.services.Database;
import com.google.android.gms.maps.model.LatLng;

public class EditAddActivity extends ActionBarActivity {
    private LatLng location;
    private String reminderText;
    private TextView contentToSaveTW;
    private Switch mySwitch;
    private TextView locationText;
    private Reminder reminder;
    private boolean editMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        locationText = (TextView) findViewById(R.id.location);
        contentToSaveTW = (TextView) findViewById(R.id.contentToSave);
        mySwitch = (Switch) findViewById(R.id.myswitch);
        Intent intent = getIntent();
        if(intent.getLongExtra("reminderID", -1) == -1){
            makeNewReminder();
        }else{
            long id = intent.getLongExtra("reminderID", -1);
            ReminderDAO reminders = new ReminderDAO(new Database(this));
            Reminder reminder = (Reminder) reminders.getOne(id);
            useExistingReminder(reminder);
        }
        if(location != null){
            locationText.setText(location.toString());
        }
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    reminder.turnOn();
                }

            }
        });
    }

    private void makeNewReminder(){
        this.reminder = new Reminder("");
        this.location = getIntent().getParcelableExtra("location");
        if(location != null){
            reminder.setReminderLocation(new ReminderLocation(location));
        }
        mySwitch.setChecked(true);
        editMode = false;
    }

    private void useExistingReminder(Reminder reminder){
        this.reminder = reminder;
        this.location = reminder.getLocation().getLatLng();
        contentToSaveTW.setText(reminder.getContent());
        mySwitch.setChecked(reminder.isOn());
        editMode = true;
    }

    public void add(View view){
/*        ReminderDAO reminders = new ReminderDAO(new Database(this));
        LocationDAO locations = new LocationDAO(new Database(this));
        String content  = getEditTextContent(R.id.contentToSave);
        Reminder r = new Reminder(content);
        boolean remdinerInsertSuccess = reminders.insert(r);*/
        reminderText  = getEditTextContent(R.id.contentToSave);
        reminder.setContent(reminderText);
        if(editMode){
            updateReminder();
        }else{
            addReminder();
        }

    }

    private void updateReminder(){
        ReminderDAO rdao = new ReminderDAO(new Database(this));
        if(rdao.update(reminder)){
            Toast toast = Toast.makeText(this, R.string.successfully_edited, Toast.LENGTH_LONG);
            toast.show();
            onBackPressed();
        }else{
            Toast toast = Toast.makeText(this, R.string.reminder_not_edited, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void addReminder(){
        if(DataManager.saveReminder(reminder, new ReminderLocation(location), this)){
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
