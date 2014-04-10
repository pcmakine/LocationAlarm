package com.artofcodeapps.locationalarm.app.Views;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.artofcodeapps.locationalarm.app.R;
import com.artofcodeapps.locationalarm.app.domain.Reminder;
import com.artofcodeapps.locationalarm.app.domain.ReminderDAO;
import com.artofcodeapps.locationalarm.app.services.Database;

import java.util.List;

//todo make a dynamic ui builder class and take the methods from this class there
public class ListActivity extends ActionBarActivity {
    private ReminderDAO reminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        reminders = new ReminderDAO(new Database(this));
        listReminders();
    }

    public void listReminders(){
        LinearLayout contentHolder = (LinearLayout) findViewById(R.id.reminderHolder);

        if(reminders.noReminders()){
            addViewToParent(makeTextView(getString(R.string.zero_reminders_notification)), contentHolder);
            addViewToParent(makeButton(), contentHolder);

        }else{
            List<Reminder> reminderList = reminders.getAll();
            for(Reminder r: reminderList){
                String content = r.getContent();
                addViewToParent(makeTextView(content), contentHolder);
            }
        }
    }

    private LinearLayout.LayoutParams wrapContentParams(){
        LinearLayout.LayoutParams params;
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        return params;
    }

    private void addViewToParent(View view, LinearLayout parent){
        LinearLayout.LayoutParams params;
        params = wrapContentParams();
        view.setLayoutParams(params);
        parent.addView(view);
    }

    private TextView makeTextView(String text){
        TextView tw = new TextView(this);
        tw.setText(text);
        return tw;
    }

    private Button makeButton(){
        Button btn = new Button(this);
        btn.setLayoutParams(wrapContentParams());
        btn.setText("Add a new reminder");
        btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                startAddActivity(v);
            }
        });
        return btn;
    }

    public void startAddActivity(View view){
            Intent i = new Intent(this, AddActivity.class);
            this.startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list, menu);
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
