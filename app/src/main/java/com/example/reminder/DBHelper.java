package com.example.reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by antramishra on 2/17/17.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper dbHelper = null;
    private final Context context;
    protected static SQLiteDatabase db;
    private static int id = 0;

    private static final String DN_NAME = "Reminder";
    private static final String DB_TABLE_NAME = "Reminders";
    private static final int DB_VERSION = 4;

    public static final String KEY_ID = "_id";
    public static final String KEY_HEAD = "heading";
    public static final String KEY_TEXT = "content";
    public static final String KEY_DATE = "date";
    private static final String TAG = "DBHelper";

    private static final String DB_CREATE_TABLE = "CREATE TABLE  IF NOT EXISTS " + DB_TABLE_NAME + "( " +
            KEY_ID + " INTEGER PRIMARY KEY, "+ KEY_HEAD + " VARCHAR, " + KEY_TEXT + " VARCHAR, " + KEY_DATE + " LONG);";


    private DBHelper(Context context) {
        super(context, DN_NAME, null, DB_VERSION);
        this.context = context;
    }

    private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    public static DBHelper getDbHelper(Context context){
        if(dbHelper == null){
            dbHelper = new DBHelper(context);
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME );
    }

    public DBHelper open() throws SQLException {
        db = getWritableDatabase();
//        onUpgrade(db,0,0);
//        db.execSQL(DB_CREATE_TABLE);
        return this;
    }

    public void close() {
        if (db != null)
            db.close();
        if (dbHelper != null)
            dbHelper.close();
    }

    public Cursor getAllReminder(){
        String table = DB_TABLE_NAME;
        String[] columns = new String[]{KEY_ID,KEY_HEAD,KEY_TEXT,KEY_DATE};
        String selection = "";
        String[] selectionArgs  = null;
        String groupBy = null;
        String having = null;
        String orderBy = "";
        String limit = null;
        return db.query(true, table,columns , selection, selectionArgs,groupBy,having,orderBy,limit);
    }

    public Cursor getReminderById(long id){
        String table = DB_TABLE_NAME;
        String[] columns = new String[]{KEY_HEAD,KEY_TEXT,KEY_DATE};
        String selection = KEY_ID+ " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor c = db.query(true, table,columns,selection, selectionArgs,null,null,null,null);
        return c;
    }

    public void saveReminder(String head,String text,Date date){
        ContentValues values = new ContentValues();
        values.put(KEY_HEAD,head);
        values.put(KEY_TEXT,text);
        values.put(KEY_DATE,date.getTime());
        db.insert(DB_TABLE_NAME, null, values);
        DBAdapter.getDbAdapter(context,null).swapCursor(getAllReminder());
    }

    public void updateReminder(String head,String text,Date date, long id){
        ContentValues values = new ContentValues();
        values.put(KEY_HEAD,head);
        values.put(KEY_TEXT,text);
        values.put(KEY_DATE,date.getTime());
        db.update(DB_TABLE_NAME, values,KEY_ID+" = ?",new String[]{String.valueOf(id)});
        DBAdapter.getDbAdapter(context,null).swapCursor(getAllReminder());
    }

    public void deleteReminder(long id){
        db.delete(DB_TABLE_NAME,KEY_ID+" = ?",new String[]{String.valueOf(id)});
        DBAdapter.getDbAdapter(context,null).swapCursor(getAllReminder());
    }

}
