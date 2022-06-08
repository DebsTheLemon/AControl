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
import java.util.Date;
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
//      setContentView(R.layout.activity_timer);
        createNotificationChannel();

        currentTime = Calendar.getInstance();
        simple = new SimpleDateFormat("HH:mm");
        CurrentTimeVal = (TextView) binding.CurrentTimeVal;

        final Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                currentTime = Calendar.getInstance();
                CurrentTimeVal.setText(String.format(simple.format(currentTime.getTime())));
                h.postDelayed(this, 5000);
            }
        });

        audio = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    audio.setLanguage(Locale.US);
                }
            }
        });

        StartVal = (TextView) binding.StartTimeVal;
        StartVal.setText(String.format(simple.format(currentTime.getTime())));
        StartVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStartTimePicker();
            }
        });

        StopVal = (TextView) binding.StopTimeVal;
        StopVal.setText(String.format(simple.format(currentTime.getTime())));
        StopVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStopTimePicker();
            }
        });

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
                audio.speak(feedback, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

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
                audio.speak(feedback, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        infoStart = (ImageView) findViewById(R.id.infoStart);
        infoStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoBox("Start Timer", "Tap on the time display below to select when " +
                        "you want your Air Conditioner to turn ON, then tap SET.");
            }
        });

        infoStop = (ImageView) findViewById(R.id.infoStop);
        infoStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoBox("Stop Timer", "Tap on the time display below to select when " +
                        "you want your Air Conditioner to turn OFF, then tap SET.");
            }
        });

        ImageButton homeBtn = (ImageButton) findViewById(R.id.Home);
        ImageButton moreBtn = (ImageButton) findViewById(R.id.More);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MoreActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
    }

    public void toastMsg(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void setStartAlarm() {
        startAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        startAlarmManager.set(AlarmManager.RTC_WAKEUP, startCalendar.getTimeInMillis(), pendingIntent);
        Log.i("SET SET", "SET SET");
    }

    private void setStopAlarm() {
        stopAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);
        stopAlarmManager.set(AlarmManager.RTC_WAKEUP, stopCalendar.getTimeInMillis(), pendingIntent);
        Log.i("SET SET", "SET SET");
    }

    private void cancelStartAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        if (startAlarmManager == null){
            startAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        startAlarmManager.cancel(pendingIntent);
        Log.i("START", "UNSET");
    }

    private void cancelStopAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);
        if (stopAlarmManager == null){
            stopAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        stopAlarmManager.cancel(pendingIntent);
        Log.i("STOP", "UNSET");
    }


    private void showStartTimePicker() {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(startCalendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(startCalendar.get(Calendar.MINUTE))
                .setTitleText("Set Start Timer")
                .build();

        picker.show(getSupportFragmentManager(), "start");

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

    private void showStopTimePicker() {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(stopCalendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(stopCalendar.get(Calendar.MINUTE))
                .setTitleText("Set Stop Timer")
                .build();

        picker.show(getSupportFragmentManager(), "stop");

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

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel startChannel = new NotificationChannel ("start", "AControl Start Timer", NotificationManager.IMPORTANCE_HIGH);
            startChannel.setDescription("Channel Alarm Manager");

            NotificationManager startManager = getSystemService(NotificationManager.class);
            startManager.createNotificationChannel(startChannel);

            NotificationChannel stopChannel = new NotificationChannel ("stop", "AControl Stop Timer", NotificationManager.IMPORTANCE_HIGH);
            startChannel.setDescription("Channel Alarm Manager");

            NotificationManager stopManager = getSystemService(NotificationManager.class);
            stopManager.createNotificationChannel(stopChannel);
        }
    }

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