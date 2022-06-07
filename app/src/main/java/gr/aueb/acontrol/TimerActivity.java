package gr.aueb.acontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Locale;

public class TimerActivity extends AppCompatActivity {
    ImageView StartSwitch, StopSwitch;
    TextToSpeech audio;
    String feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        audio = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    audio.setLanguage(Locale.US);
                }
            }
        });

        StartSwitch = (ImageView) findViewById(R.id.StartSwitch);
        StartSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSwitch.setActivated(!StartSwitch.isActivated());
                if (StartSwitch.isActivated()){
                    toastMsg("Start timer set.");
                    feedback ="Start timer enabled.";
                }else{
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
}