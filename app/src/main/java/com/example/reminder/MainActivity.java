package com.example.reminder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static DBHelper dbHelper;
    private static Context context;
    private static ImageButton btnAddReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appToolbar);
        setSupportActionBar(toolbar);
        Log.d("MainActivity","Starting main");
        context = getApplicationContext();
        initializeDB();
        populateList();
        btnAddReminder = (ImageButton) findViewById(R.id.btnAddReminder);
        ButtonListener buttonListener = new ButtonListener(R.id.btnAddReminder);
        btnAddReminder.setOnClickListener(buttonListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.menuActionSettings:
                break;
            case R.id.menuActionFaceBook:
                break;
        }
        return true;
    }

    private void initializeDB(){
        Globals.dbHelper = DBHelper.getDbHelper(context);
        dbHelper = Globals.dbHelper;
        dbHelper.open();
    }

    private void populateList(){
        ListView listView = (ListView) findViewById(R.id.list_item_reminders);
        listView.setAdapter(DBAdapter.getDbAdapter(this,dbHelper.getAllReminder()));
        listView.setOnItemLongClickListener(new ListItemClickListener());
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        finish();
        dbHelper.close();
    }


    class ButtonListener implements Button.OnClickListener {
        private int buttonId;

        public ButtonListener(int id){
            buttonId = id;
        }

        @Override
        public void onClick(View v) {
            switch (buttonId){
                case R.id.btnAddReminder:
                    addReminder(v);
                    break;
            }
        }

        private void addReminder(View v){
            Intent intent = new Intent(MainActivity.this, EditReminderActivity.class);
            intent.putExtra(Globals.ACTIVITY_NAME,Globals.EDIT_ACTIVTY_FLAG);
            startActivity(intent);
        }
    }


    class ListItemClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this,EditReminderActivity.class);
//            Toast.makeText(getApplicationContext(),"Clicked "+String.valueOf(id),Toast.LENGTH_LONG).show();
            intent.putExtra(Globals.ACTIVITY_NAME,Globals.MAIN_ACTIVTY_FLAG);
            intent.putExtra(Globals.LIST_ITEM_ID,id);
            intent.putExtra(Globals.LIST_ITEM_POSITION,position);
            startActivity(intent);
            return true;
        }
    }
}
