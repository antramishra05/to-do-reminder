package com.example.reminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EditReminderActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    Button btnSaveReminder,btnCancelReminder,btnTime;
    Button btnDate;
//    ImageButton btnDate;
    int m_date, m_month, m_year,hourOfDay,minute;

    private String activityFlag;
    private DBHelper dbHelper = Globals.dbHelper;
    long listItemId = 0;

    private DatePickerDialog.OnDateSetListener dateSetListener
            = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            m_date = dayOfMonth;
            m_month = month;
            m_year = year;
            TextView tvDate = (TextView) findViewById(R.id.tvDate);
            tvDate.setText(new Date(m_year,m_month,m_date).toString());
        }
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener
            = new TimePickerDialog.OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            EditReminderActivity.this.hourOfDay = hourOfDay;
            EditReminderActivity.this.minute = minute;
            TextView tvTime = (TextView) findViewById(R.id.tvDate);
            tvTime.setText(hourOfDay+" : "+minute);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appToolbar);
        setSupportActionBar(toolbar);


        Calendar c = Calendar.getInstance();
        hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        datePickerDialog = new DatePickerDialog(this,0);
        datePickerDialog.setOnDateSetListener(dateSetListener);
        timePickerDialog = new TimePickerDialog(this,timeSetListener,hourOfDay,minute,true);

//        btnDate = (ImageButton) findViewById(R.id.btnDate);
//        btnDate.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                datePickerDialog.show();
//            }
//        });
        btnDate = (Button) findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditReminderActivity.this,"Clicked Date",Toast.LENGTH_LONG);
                datePickerDialog.show();
            }
        });

        btnTime = (Button) findViewById(R.id.btnTime);
        btnTime.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

        btnSaveReminder = (Button) findViewById(R.id.btnSaveReminder);
        btnCancelReminder = (Button) findViewById(R.id.btnCancelReminder);
        btnSaveReminder = (Button) findViewById(R.id.btnSaveReminder);
        ButtonListener buttonListener = new ButtonListener(R.id.btnSaveReminder);
        btnSaveReminder.setOnClickListener(buttonListener);
        btnCancelReminder.setOnClickListener(new ButtonListener(R.id.btnCancelReminder));

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        activityFlag = bundle.getString(Globals.ACTIVITY_NAME);
        if(activityFlag.equals(Globals.MAIN_ACTIVTY_FLAG)){
            // update view using previous data
            int position = bundle.getInt(Globals.LIST_ITEM_POSITION);
            long id = bundle.getLong(Globals.LIST_ITEM_ID);
            listItemId = id;
            getCurrentReminder(position,id);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_edit_reminder,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.action_edit_settings:
                break;
            case R.id.action_edit_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditReminderActivity.this);
                builder.setMessage("Delete this reminder?");
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbHelper.deleteReminder(listItemId);
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
                break;
        }
        return true;
    }

    private void getCurrentReminder(int position,long id){
        Cursor c = dbHelper.getReminderById(id);
        EditText head = (EditText) findViewById(R.id.etReminderHead);
        EditText text = (EditText) findViewById(R.id.etReminderContent);
        Toast.makeText(getApplicationContext(),String.valueOf(id),Toast.LENGTH_LONG).show();
        if(c.moveToFirst()) {
                head.setText(c.getString(c.getColumnIndex(DBHelper.KEY_HEAD)));
                text.setText(c.getString(c.getColumnIndex(DBHelper.KEY_TEXT)));
                Toast.makeText(getApplicationContext(),c.getString(c.getColumnIndex(DBHelper.KEY_HEAD)),Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), DatabaseUtils.dumpCursorToString(c),Toast.LENGTH_LONG).show();
        }
        if(c != null)
            c.close();

    }

    class ButtonListener implements Button.OnClickListener {
        private int buttonId;

        public ButtonListener(int id){
            buttonId = id;
        }

        @Override
        public void onClick(View v) {
            switch (buttonId){
                case R.id.btnSaveReminder:
                    saveReminder(v);
                    break;
                case R.id.btnCancelReminder:
                    finish();
                    break;
            }
        }

        private void saveReminder(View v){
            EditText head = (EditText) findViewById(R.id.etReminderHead);
            EditText text = (EditText) findViewById(R.id.etReminderContent);
            Date date = new GregorianCalendar(m_year,m_month,m_date,hourOfDay,minute).getTime();
            if(activityFlag.equals(Globals.EDIT_ACTIVTY_FLAG)) {
                dbHelper.saveReminder(head.getText().toString(), text.getText().toString(), date);
            }else if(activityFlag.equals(Globals.MAIN_ACTIVTY_FLAG)){
                //update the DB
                dbHelper.updateReminder(head.getText().toString(), text.getText().toString(), date,listItemId);

            }
            Alarm alrm = new Alarm(getApplicationContext());
            alrm.setAlarm(head.getText().toString(), text.getText().toString(),date.getTime());
            finish();
        }

    }


}
