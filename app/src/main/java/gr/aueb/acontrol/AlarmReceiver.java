package gr.aueb.acontrol;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){

        //Creates new notification for when the AC has been turned ON.
        NotificationCompat.Builder startBuilder = new NotificationCompat.Builder(context, "start")
                .setSmallIcon(android.R.color.transparent)
                .setContentTitle("AControl")
                .setContentText("AC has been turned ON.")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        //Notification manager for start timer.
        NotificationManagerCompat startManager = NotificationManagerCompat.from(context);

        //Creates new notification for when the AC has been turned ON.
        NotificationCompat.Builder stopBuilder = new NotificationCompat.Builder(context, "stop")
                .setSmallIcon(android.R.color.transparent)
                .setContentTitle("AControl")
                .setContentText("AC has been turned OFF.")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        //Notification manager for stop timer.
        NotificationManagerCompat stopManager = NotificationManagerCompat.from(context);

        if (TimerActivity.StartSet){ //If the start timer has been set...
            startManager.notify(1, startBuilder.build()); //notify
            MainActivity.getInstance().powerON(); //Activate power button
            Log.i("IN ON RECEIVE","START");
            TimerActivity.StartSet = false; //Set value to false
            TimerActivity.StartSwitch.setActivated(false); //Disable start timer

        }else if (TimerActivity.StopSet) { //If the start timer has been set...
            stopManager.notify(2, stopBuilder.build()); //notify
            MainActivity.getInstance().powerOFF(); //Deactivate power button
            Log.i("IN ON RECEIVE","STOP");
            TimerActivity.StopSet = false; //Set value to false
            TimerActivity.StopSwitch.setActivated(false); //Disable stop timer
        }
    }
}
