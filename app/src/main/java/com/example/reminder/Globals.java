package com.example.reminder;

import android.app.Application;

public class Globals extends Application {

    public static String MAIN_ACTIVTY_FLAG = "MainActivity";
    public static String EDIT_ACTIVTY_FLAG = "EditReminderActivity";
    public static String LIST_ITEM_POSITION = "ListItemPosition";
    public static String LIST_ITEM_ID = "ListItemID";
    public static String ACTIVITY_NAME = "ActivityName";

    public static DBHelper dbHelper ;
//    public String getSomeVariable() {
//        return someVariable;
//    }
//
//    public void setSomeVariable(String someVariable) {
//        this.someVariable = someVariable;
//    }
}