package com.artofcodeapps.locationalarm.app.Views;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    }

    public void buildDynamicViews(){
        LinearLayout contentHolder = (LinearLayout) findViewById(R.id.reminderHolder);
        contentHolder.removeAllViews();
        if(reminders.noReminders()){
            showNoRemindersText(contentHolder);
        }else{
            listReminders(contentHolder);
        }
    }

    public void listReminders(LinearLayout listHolder){
        List<Reminder> reminderList = reminders.getAll();
        int index = 0;
        for(Reminder r: reminderList){
            listHolder.addView(makeRow(r));
            if(!lastEntryInList(reminderList.size(), index)){
              listHolder.addView(makeSeparator());
            }
            index++;
        }
    }

    public void showNoRemindersText(LinearLayout contentHolder){
        contentHolder.addView(makeTextView(getString(R.string.zero_reminders_notification)));
        contentHolder.addView(makeAddViewLinkButton());
    }

    public boolean lastEntryInList(int listSize, int index){
        if(index == listSize - 1) return true;
        return false;
    }

    private LinearLayout.LayoutParams wrapContentParams(){
        LinearLayout.LayoutParams params;
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
       //params.gravity = Gravity.CENTER;
        return params;
    }

    private TextView makeTextView(String text){
        TextView tw = new TextView(this);
        tw.setText(text);
        LinearLayout.LayoutParams params;
        params = wrapContentParams();
        tw.setLayoutParams(params);
        tw.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        params.setMargins(1,1, 1, 1);
        return tw;
    }

    private Button makeAddViewLinkButton(){
        Button btn = new Button(this);
        btn.setLayoutParams(wrapContentParams());
        btn.setText(R.string.add_reminder_btn);
        btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                startAddActivity(v);
            }
        });

        return btn;
    }

    private View makeSeparator(){
        View view = new View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, convertDPtoPX(1));
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.DKGRAY);
        return view;
    }

    private LinearLayout makeRow(Reminder r){
        LinearLayout row = new LinearLayout(this);
        LinearLayout.LayoutParams params = wrapContentParams();
        row.setLayoutParams(params);
        TextView tw = makeTextView(r.getContent());
        LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        Button btn = makeRemoveReminderButton(r);
        tw.setLayoutParams(childParams);
        tw.setEllipsize(TextUtils.TruncateAt.END);
        tw.setSingleLine();

        btn.setLayoutParams(childParams);
        row.addView(tw);
        row.addView(btn);
        return row;
    }

    private RelativeLayout.LayoutParams getRelativeLayoutParams(int alignment, int width, int height){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.addRule(alignment);
        return params;
    }

    private Button makeRemoveReminderButton(final Reminder r){
        Button btn = new Button(this);
//        btn.setLayoutParams(getRelativeLayoutParams(RelativeLayout.ALIGN_PARENT_RIGHT,
    //            RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        btn.setText(R.string.remove_reminder);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminders.remove(r);
                onBackPressed();
            }
        });
        return btn;
    }

    public int convertDPtoPX(int dp){
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dp*scale + 0.5f);
    }

    public void startAddActivity(View view){
        Intent i = new Intent(this, AddActivity.class);
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
