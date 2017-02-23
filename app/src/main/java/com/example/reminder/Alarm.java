package com.example.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by antramishra on 2/20/17.
 */

public class Alarm {
    BroadcastReceiver br;
    Context context;

    Alarm(Context c){
        this.context = c;
    }

    public void setAlarm(String head, String text,Long date){
        AlarmManager alrm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        final String TRIGGER_ALARM_RING = ".AlarmReceiver";
        IntentFilter intentFilter = new IntentFilter(TRIGGER_ALARM_RING);
        AlarmReceiver mReceiver = new AlarmReceiver();
        context.registerReceiver(mReceiver, intentFilter);

//        Intent intent2 = new Intent(context,AlarmReceiver.class);
        Intent intent2 = new Intent(TRIGGER_ALARM_RING);
        intent2.putExtra(DBHelper.KEY_HEAD,head);
        intent2.putExtra(DBHelper.KEY_TEXT,text);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent2, 0);
        int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
        alrm.set(AlarmManager.RTC_WAKEUP, date, pi);
    }
}


