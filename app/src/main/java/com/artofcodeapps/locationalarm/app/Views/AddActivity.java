package com.artofcodeapps.locationalarm.app.Views;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.artofcodeapps.locationalarm.app.R;
import com.artofcodeapps.locationalarm.app.domain.Reminder;
import com.artofcodeapps.locationalarm.app.domain.ReminderDAO;
import com.artofcodeapps.locationalarm.app.services.Database;
import com.google.android.gms.maps.model.LatLng;

public class AddActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LatLng location = getIntent().getParcelableExtra("location");
        if(location != null){
            Toast.makeText(this, "Location is " + location.toString(), Toast.LENGTH_SHORT).show();
        }
        setContentView(R.layout.activity_add);
    }

    public void add(View view){
        ReminderDAO reminders = new ReminderDAO(new Database(this));

        String content  = getEditTextContent(R.id.contentToSave);

        Reminder r = new Reminder(content);
        if(reminders.insert(r)){
            Toast toast = Toast.makeText(this, R.string.successfully_added, Toast.LENGTH_LONG);
            toast.show();
        }
        onBackPressed();
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
