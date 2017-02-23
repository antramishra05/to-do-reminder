package com.example.reminder;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by antramishra on 2/17/17.
 */

public class DBAdapter extends CursorAdapter {
    private static DBAdapter cursorAdapter = null;

    private DBAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public static DBAdapter getDbAdapter(Context context,Cursor cursor){
        if(cursorAdapter == null){
            cursorAdapter = new DBAdapter(context,cursor);
        }
        return cursorAdapter;
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_reminders, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView reminderContent = (TextView) view.findViewById(R.id.tvReminderContent);
        Long date = cursor.getLong(cursor.getColumnIndex(DBHelper.KEY_DATE));
        Date reminderDate = new Date(date);
        String text = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_HEAD))
                + "\n"+reminderDate.toString();
        reminderContent.setText(text);
    }
}
