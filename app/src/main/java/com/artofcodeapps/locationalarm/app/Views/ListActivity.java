package com.artofcodeapps.locationalarm.app.Views;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.artofcodeapps.locationalarm.app.R;
import com.artofcodeapps.locationalarm.app.Views.textviewoptions.TwOptionsBuilder;
import com.artofcodeapps.locationalarm.app.domain.Reminder;
import com.artofcodeapps.locationalarm.app.domain.ReminderDAO;
import com.artofcodeapps.locationalarm.app.services.Database;

import java.util.List;

public class ListActivity extends ActionBarActivity {
    private ReminderDAO reminders;
    private LinearLayout listHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }

    public void buildDynamicViews(){
        listHolder = (LinearLayout) findViewById(R.id.reminderHolder);
        listHolder.removeAllViews();
        if(reminders.noReminders()){
            showNoRemindersText(listHolder);
        }else{
            listReminders(listHolder);
        }
    }

    public void showNoRemindersText(LinearLayout contentHolder){
        contentHolder.addView(makeTextView(getString(R.string.zero_reminders_notification)));
        contentHolder.addView(makeAddViewLinkButton());
    }

    public void listReminders(LinearLayout listHolder){
        List<Reminder> reminderList = reminders.getAll();
        for(int i = 0; i < reminderList.size(); i++){
            Row row = new Row(this, new float[]{15, 1});
            TextView tw = row.insertText(reminderList.get(i).getContent(), LinearLayout.LayoutParams.WRAP_CONTENT, 0);
            setOnTouchListener(reminderList.get(i), tw);
            ImageButton btn = row.insertImageButton(R.drawable.ic_action_discard, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            setButtonListener(btn, reminderList.get(i), row);
            listHolder.addView(row);
        }
     //  Row row = new Row(this, new float[]{15, 1});
    //    TextView tw = row.insertText(String.valueOf("There are " + new Database(this).totalRows("locations")) + "locations in db", LinearLayout.LayoutParams.WRAP_CONTENT, 0);
      //  setOnTouchListener(reminderList.get(i), tw);
//        ImageButton btn = row.insertImageButton(R.drawable.ic_action_discard, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        //setButtonListener(btn, reminderList.get(i));
    //    listHolder.addView(row);
    }

    private void setButtonListener(final ImageButton btn, final Reminder r, final Row row){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(reminders.remove(r)){
                   Toast.makeText(getApplicationContext(), R.string.successfully_removed, Toast.LENGTH_LONG).show();
                   listHolder.removeView(row);
               }else{
                   Toast.makeText(getApplicationContext(), R.string.reminder_not_removed, Toast.LENGTH_LONG).show();
               }


              //  onBackPressed();
            }
        });
    }

    private void setOnTouchListener(final Reminder r, TextView tw){
        tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEdit(r);
            }
        });
    }

    private TextView makeTextView(String text){
        TwOptionsBuilder builder = new TwOptionsBuilder(text, this);
        builder.fontSize(TypedValue.COMPLEX_UNIT_SP, 20);
        TextView tw = builder.getTextView();
        tw.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return tw;
    }

    private Button makeAddViewLinkButton(){
        Button btn = new Button(this);
        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        btn.setText(R.string.add_reminder_btn);
        btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                startAddActivity(v);
            }
        });
        return btn;
    }

    public void startEdit(Reminder r){
        Intent intent = new Intent(this, EditAddActivity.class);
        intent.putExtra("reminderID", r.getId());
        startActivity(intent);
    }

    public void startAddActivity(View view){
        Intent i = new Intent(this, EditAddActivity.class);
        this.startActivity(i);
    }

    @Override
    public void onResume(){
        super.onResume();
        reminders = new ReminderDAO(new Database(this));
        buildDynamicViews();
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


/*
    private View makeSeparator(){
        View view = new View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, convertDPtoPX(1));
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.DKGRAY);
        return view;
    }*/
