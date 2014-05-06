package com.artofcodeapps.locationalarm.app.Views;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.artofcodeapps.locationalarm.app.R;
import com.artofcodeapps.locationalarm.app.domain.ReminderLocation;
import com.artofcodeapps.locationalarm.app.domain.Reminder;
import com.artofcodeapps.locationalarm.app.domain.ReminderDAO;
import com.artofcodeapps.locationalarm.app.services.Database;

import java.util.List;

public class EditActivity extends ActionBarActivity {
    private Reminder reminderToEdit;
    private ReminderDAO reminders;
    private EditText content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent = getIntent();
        Long id = intent.getLongExtra("reminderID", -1);

        reminders = new ReminderDAO(new Database(this));
        reminderToEdit = (Reminder) reminders.getOne(id);
        content = (EditText) findViewById(R.id.content);
        TextView locationText = (TextView) findViewById(R.id.locationText);
        List<ReminderLocation> locs = reminderToEdit.getReminderLocations();
        if(!locs.isEmpty()){
            locationText.setText(locs.get(0).toString());
        }

        if(reminderToEdit == null){
            showErrorMessage();
        }else{
            content.setText(reminderToEdit.getContent());
        }
    }

    public void showErrorMessage(){
        RelativeLayout root = (RelativeLayout) findViewById(R.id.editRoot);
        root.removeAllViews();
        TextView tw = new TextView(this);
        tw.setText(R.string.general_error + " " + R.string.reminder_not_retrieved);
        root.addView(tw);
    }

    public void saveChanges(View view){
        reminderToEdit.setContent(content.getText().toString());
        if(reminders.update(reminderToEdit)){
            Toast.makeText(this, R.string.successfully_edited, Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(this, R.string.general_error + " " + R.string.reminder_not_edited,
                    Toast.LENGTH_LONG).show();
        }
        onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit, menu);
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
