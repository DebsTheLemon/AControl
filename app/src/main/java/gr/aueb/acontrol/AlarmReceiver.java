package gr.aueb.acontrol;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.security.cert.CertPathBuilder;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){

        NotificationCompat.Builder startBuilder = new NotificationCompat.Builder(context, "start")
                .setSmallIcon(android.R.color.transparent)
                .setContentTitle("AControl")
                .setContentText("AC has been turned ON.")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat startManager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder stopBuilder = new NotificationCompat.Builder(context, "stop")
                .setSmallIcon(android.R.color.transparent)
                .setContentTitle("AControl")
                .setContentText("AC has been turned OFF.")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat stopManager = NotificationManagerCompat.from(context);

        if (TimerActivity.StartSet){
            startManager.notify(1, startBuilder.build());
            MainActivity.getInstance().powerON();
            Log.i("IN ON RECEIVE","START");
            TimerActivity.StartSet = false;
            TimerActivity.StartSwitch.setActivated(false);
        }else if (TimerActivity.StopSet) {
            stopManager.notify(2, stopBuilder.build());
            MainActivity.getInstance().powerOFF();
            Log.i("IN ON RECEIVE","STOP");
            TimerActivity.StopSet = false;
            TimerActivity.StopSwitch.setActivated(false);
        }
    }
}
