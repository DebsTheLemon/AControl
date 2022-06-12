package gr.aueb.acontrol;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import gr.aueb.acontrol.databinding.ActivityTimerBinding;

public class TimerActivity extends AppCompatActivity {

    TextView StartVal, StopVal, CurrentTimeVal;
    ImageView infoStart, infoStop;
    TextToSpeech audio;
    String feedback;

    static ImageView StartSwitch, StopSwitch;
    static boolean StartSet, StopSet;

    private ActivityTimerBinding binding;
    private MaterialTimePicker picker;
    private Calendar startCalendar = Calendar.getInstance();
    private Calendar stopCalendar = Calendar.getInstance();
    private Calendar currentTime;
    SimpleDateFormat simple;
    private AlarmManager startAlarmManager, stopAlarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Notification channel needed for Timer notifications
        createNotificationChannel();

        //Initiates vibrator for haptic feedback
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //Initiate calendar for current time clock
        currentTime = Calendar.getInstance();
        simple = new SimpleDateFormat("HH:mm");
        CurrentTimeVal = (TextView) binding.CurrentTimeVal;

        //Handler to update current time clock value every 5000 milliseconds
        final Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                currentTime = Calendar.getInstance();
                CurrentTimeVal.setText(String.format(simple.format(currentTime.getTime())));
                h.postDelayed(this, 5000);
            }
        });

        //Initiate audio for voice audio feedback
        audio = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    audio.setLanguage(Locale.US);
                }
            }
        });

        //Start timer TextView with onClick listener, initiated with current time value
        StartVal = (TextView) binding.StartTimeVal;
        StartVal.setText(String.format(simple.format(currentTime.getTime())));
        StartVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStartTimePicker();
            }
        });

        //Stop timer TextView with onClick listener, initiated with current time value
        StopVal = (TextView) binding.StopTimeVal;
        StopVal.setText(String.format(simple.format(currentTime.getTime())));
        StopVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStopTimePicker();
            }
        });

        //Start timer switch
        StartSwitch = (ImageView) findViewById(R.id.StartSwitch);
        binding.StartSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSwitch.setActivated(!StartSwitch.isActivated());
                if (StartSwitch.isActivated()){
                    setStartAlarm();
                    toastMsg("Start timer set.");
                    feedback ="Start timer enabled.";
                    StartSet = true;

                }else{
                    cancelStartAlarm();
                    toastMsg("Start timer unset.");
                    feedback = "Start timer disabled.";
                    StartSet = false;
                }
                speaker(feedback);
            }
        });

        //Stop timer switch
        StopSwitch = (ImageView) findViewById(R.id.StopSwitch);
        StopSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StopSwitch.setActivated(!StopSwitch.isActivated());
                if (StopSwitch.isActivated()){
                    setStopAlarm();
                    toastMsg("Stop timer set.");
                    feedback ="Stop timer enabled.";
                    StopSet = true;
                }else{
                    cancelStopAlarm();
                    toastMsg("Stop timer unset.");
                    feedback = "Stop timer disabled.";
                    StopSet = false;
                }
                speaker(feedback);
            }
        });

        //Start Timer Info Box Button
        infoStart = (ImageView) findViewById(R.id.infoStart);
        infoStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoBox("Start Timer", "Tap on the time display below to select when " +
                        "you want your Air Conditioner to turn ON, then tap SET.");
            }
        });

        //Stop Timer Info Box Button
        infoStop = (ImageView) findViewById(R.id.infoStop);
        infoStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoBox("Stop Timer", "Tap on the time display below to select when " +
                        "you want your Air Conditioner to turn OFF, then tap SET.");
            }
        });

        // Navigation Bar Buttons
        // Main Screen Button
        ImageButton homeBtn = (ImageButton) findViewById(R.id.Home);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.vibrate(50);
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        //Advanced Screen Button
        ImageButton moreBtn = (ImageButton) findViewById(R.id.More);
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.vibrate(50);
                Intent i = new Intent(getApplicationContext(),MoreActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
    }

    //Sets start timer alarm with pending Intent
    private void setStartAlarm() {
        startAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        startAlarmManager.set(AlarmManager.RTC_WAKEUP, startCalendar.getTimeInMillis(), pendingIntent);
        Log.i("Start Timer", "SET");
    }

    //Sets stop timer alarm with pending Intent
    private void setStopAlarm() {
        stopAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);
        stopAlarmManager.set(AlarmManager.RTC_WAKEUP, stopCalendar.getTimeInMillis(), pendingIntent);
        Log.i("Stop Timer", "SET");
    }

    //Cancels start timer alarm
    private void cancelStartAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        if (startAlarmManager == null){
            startAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        startAlarmManager.cancel(pendingIntent);
        Log.i("Start Timer", "UNSET");
    }

    //Cancels stop timer alarm
    private void cancelStopAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);
        if (stopAlarmManager == null){
            stopAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        stopAlarmManager.cancel(pendingIntent);
        Log.i("Stop Timer", "UNSET");
    }

    //Displays time picker of start timer
    private void showStartTimePicker() {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(startCalendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(startCalendar.get(Calendar.MINUTE))
                .setTitleText("Set Start Timer")
                .build();

        picker.show(getSupportFragmentManager(), "start");

        //OK button onClick listener updates start timer TextView and start calendar
        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartVal.setText(String.format("%02d", picker.getHour())+":"+String.format("%02d", picker.getMinute()));
                startCalendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                startCalendar.set(Calendar.MINUTE, picker.getMinute());
                startCalendar.set(Calendar.SECOND, 0);
                startCalendar.set(Calendar.MILLISECOND, 0);
            }
        });
    }

    //Displays time picker of stop timer
    private void showStopTimePicker() {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(stopCalendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(stopCalendar.get(Calendar.MINUTE))
                .setTitleText("Set Stop Timer")
                .build();

        picker.show(getSupportFragmentManager(), "stop");

        //OK button onClick listener updates stop timer TextView and start calendar
        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StopVal.setText(String.format("%02d", picker.getHour())+":"+String.format("%02d", picker.getMinute()));
                stopCalendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                stopCalendar.set(Calendar.MINUTE, picker.getMinute());
                stopCalendar.set(Calendar.SECOND, 0);
                stopCalendar.set(Calendar.MILLISECOND, 0);
            }
        });
    }

    //Creates Notification channels needed to send the start and stop timer notifications
    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //Start Timer channel
            NotificationChannel startChannel = new NotificationChannel ("start", "AControl Start Timer", NotificationManager.IMPORTANCE_HIGH);
            startChannel.setDescription("Channel Alarm Manager");
            //Start Timer notification manager
            NotificationManager startManager = getSystemService(NotificationManager.class);
            startManager.createNotificationChannel(startChannel);

            //Stop Timer channel
            NotificationChannel stopChannel = new NotificationChannel ("stop", "AControl Stop Timer", NotificationManager.IMPORTANCE_HIGH);
            startChannel.setDescription("Channel Alarm Manager");
            //Stop Timer notification manager
            NotificationManager stopManager = getSystemService(NotificationManager.class);
            stopManager.createNotificationChannel(stopChannel);
        }
    }

    //Speaker method for voice audio feedback
    private void speaker(String message){
        audio.speak(message, TextToSpeech.QUEUE_FLUSH, null);
    }

    //Creates and displays toast of given message string
    public void toastMsg(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    //Created and displays infoBox of given title and info strings
    private void showInfoBox(String title, String info){
        AlertDialog infoBox = new AlertDialog.Builder(TimerActivity.this)
                .setTitle(title)
                .setMessage(info)
                .setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface infoBox, int i) {
                        infoBox.dismiss();
                    }
                }).create();
        infoBox.show();
    }

}