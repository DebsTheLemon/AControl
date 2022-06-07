package gr.aueb.acontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;
import java.util.Locale;

import gr.aueb.acontrol.databinding.ActivityTimerBinding;

public class TimerActivity extends AppCompatActivity {
    ImageView StartSwitch, StopSwitch;
    TextToSpeech audio;
    String feedback;

    private ActivityTimerBinding binding;
    private MaterialTimePicker picker;
    private Calendar calendar = Calendar.getInstance();
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_timer);
        createNotificationChannel();


        audio = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    audio.setLanguage(Locale.US);
                }
            }
        });

        binding.StartTimeVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        StartSwitch = (ImageView) findViewById(R.id.StartSwitch);
        binding.StartSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSwitch.setActivated(!StartSwitch.isActivated());
                if (StartSwitch.isActivated()){
                    setAlarm();
                    toastMsg("Start timer set.");
                    feedback ="Start timer enabled.";
                }else{
                    cancelAlarm();
                    toastMsg("Start timer unset.");
                    feedback = "Start timer disabled.";
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
                    toastMsg("Stop timer set.");
                    feedback ="Stop timer enabled.";
                }else{
                    toastMsg("Stop timer unset.");
                    feedback = "Stop timer disabled.";
                }
                audio.speak(feedback, TextToSpeech.QUEUE_FLUSH, null);
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

    private void setAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Log.i("SET SET", "SET SET");
    }

    private void cancelAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        if (alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
        Log.i("UNSET UNSET", "UNSET UNSET");
    }

    private void showTimePicker() {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Set Timer")
                .build();

        picker.show(getSupportFragmentManager(), "alarm");

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.StartTimeVal.setText(String.format("%02d", picker.getHour())+":"+String.format("%02d", picker.getMinute()));
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                calendar.set(Calendar.MINUTE, picker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            }
        });
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "AControl Timer";
            String description = "Channel Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel ("alarm", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}