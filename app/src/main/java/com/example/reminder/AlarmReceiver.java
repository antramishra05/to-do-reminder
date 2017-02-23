package com.example.reminder;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by antramishra on 2/21/17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context c, Intent i) {
//        Toast.makeText(c, "Rise and Shine!", Toast.LENGTH_LONG).show();

        Bundle b = i.getExtras();
        String head = b.getString(DBHelper.KEY_HEAD);
        String content = b.getString(DBHelper.KEY_TEXT);

//        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(c)
//                        .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
//                        .setContentTitle()
//                        .setContentText("Hello World!").setSound(soundUri);
//        int mNotificationId = 001;
//        NotificationManager mNotifyMgr = (NotificationManager) c.getSystemService(NOTIFICATION_SERVICE);
//        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        MediaPlayer mp = MediaPlayer.create(c, notification);
        mp.start();
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage(content).setTitle(head);
        builder.create();
    }
}