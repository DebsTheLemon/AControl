package gr.aueb.acontrol;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;

import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;


import android.widget.Toast;
import android.widget.ImageView;
import android.speech.tts.TextToSpeech;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Locale;
import android.os.Vibrator;


public class MainActivity extends Activity {

    static int temp = 26;
    private static int MICROPHONE_PERMISSION_CODE = 200;
    CompoundButton previousCheckedCompoundButton;

    private static MainActivity instance;

    ImageView infoTemp, infoMode, btnSpeak;
    RadioButton cool, fan, dry, heat, auto;
    TextToSpeech audio;
    String feedback;
    static ImageView power;

    //private TextView txvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AControl);
        setContentView(R.layout.activity_main);
        instance = this;

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //txvResult = (TextView) findViewById(R.id.txvResult);
        audio = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    audio.setLanguage(Locale.US);
                }
            }
        });

        power = (ImageView) findViewById(R.id.PowerBtn);
        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                power.setActivated(!power.isActivated());
                v.vibrate(50);
                if (power.isActivated()){
                    powerON();
                }else{
                    powerOFF();
                }
            }
        });

        displayTemp(temp);

        cool = (RadioButton) findViewById(R.id.Cool);
        fan = (RadioButton) findViewById(R.id.Fan);
        dry = (RadioButton) findViewById(R.id.Dry);
        heat = (RadioButton) findViewById(R.id.Heat);
        auto = (RadioButton) findViewById(R.id.Auto);

        cool.setOnCheckedChangeListener(onRadioButtonCheckedListener);
        fan.setOnCheckedChangeListener(onRadioButtonCheckedListener);
        dry.setOnCheckedChangeListener(onRadioButtonCheckedListener);
        heat.setOnCheckedChangeListener(onRadioButtonCheckedListener);
        auto.setOnCheckedChangeListener(onRadioButtonCheckedListener);

        ImageButton moreBtn = (ImageButton) findViewById(R.id.More);
        ImageButton timerBtn = (ImageButton) findViewById(R.id.Timer);

        infoTemp = (ImageView) findViewById(R.id.infoTemp);
        infoMode = (ImageView) findViewById(R.id.infoMode);
        btnSpeak = (ImageView) findViewById(R.id.btnSpeak);

        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.vibrate(50);
                Intent i = new Intent(getApplicationContext(),MoreActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        timerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.vibrate(50);
                Intent i = new Intent(getApplicationContext(),TimerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        infoTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showInfoBox("Temperature Info", "Set your desired temperature through " +
                       "the arrow buttons and turn the Air Conditioner ON and OFF with the power button.");
            }
        });

        infoMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoBox("Modes Info", "Select your desired mode, the current mode " +
                        "is being displayed above the mode buttons.");
            }
        });

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.vibrate(50);
                getSpeechInput();
            }
        });
    }

    public static boolean getPowerState() {
        return power.isActivated();
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public void powerON() {
        power.setActivated(true);
        toastMsg("AC is now ON.");
        speaker("AC is now ON.");
    }

    public void powerOFF() {
        power.setActivated(false);
        toastMsg("AC is now OFF.");
       speaker("AC is now OFF.");
    }

    public void toastMsg(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void showInfoBox(String title, String info){
        AlertDialog infoBox = new AlertDialog.Builder(MainActivity.this)
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

    CompoundButton.OnCheckedChangeListener onRadioButtonCheckedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!isChecked) return;
            if (previousCheckedCompoundButton != null) {
                previousCheckedCompoundButton.setChecked(false);
                previousCheckedCompoundButton = buttonView;
            } else {
                previousCheckedCompoundButton = buttonView;
            }
        }
    };

    public void modeCool(View view){ displayMode("COOL"); }
    public void modeFan(View view){ displayMode("FAN"); }
    public void modeDry(View view){ displayMode("DRY"); }
    public void modeHeat(View view){ displayMode("HEAT"); }
    public void modeAuto(View view){ displayMode("AUTO"); }

    public void increaseInteger(View view) {
        if (temp < 32) {
            temp = temp + 1;
        }
        displayTemp(temp);
    }

    public void decreaseInteger(View view) {
        if (temp > 16) {
            temp = temp - 1;
        }
        displayTemp(temp);
    }

    private void displayTemp(int temp) {
        TextView displayInteger = (TextView) findViewById (R.id.CurrentTempNumber);
        displayInteger.setText(String.valueOf(temp));
    }

    private void displayMode(String mode) {
        TextView Mode = (TextView) findViewById(R.id.CurrentModeVal);
        Mode.setText("" + mode);
    }


    public void getSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        getMicPermission();
//        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
//        }else{
//            Toast.makeText(this, "Your device does not support speech input", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    String result = String.valueOf(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
                    String results[] = result.trim().split("\\s* \\s*");
                    Log.i("Speech recognition", "I heard you!");
                    if (result.contains("assistance")) {
                        listCommands();
                    } else if (result.contains("turn") && result.contains("on")) {
                        powerON();
                    } else if (result.contains("turn") && result.contains("off")) {
                        powerOFF();
                    } else if (result.contains("Celsius")) {
                        Log.i("Desired Temperature", String.valueOf(results[results.length - 2]));
                        temp = Integer.valueOf(results[results.length - 2]);
                        if (temp <= 32 && temp >= 16) {
                            displayTemp(temp);
                            speaker("Temperature has been set to " + String.valueOf(temp) + " degrees Celsius.");
                        } else if (temp > 32) {
                            speaker("Maximum temperature is 32 degrees Celsius, please try again.");
                        } else if (temp < 16) {
                            speaker("Minimum temperature is 16 degrees Celsius, please try again.");
                        }
                    } else if (result.contains("set") && result.contains("mode")) {
                        String mode = String.valueOf(results[results.length - 1]);
                        Log.i("Heard you say", mode);
                        if (result.contains("cool")) {
                            displayMode("COOL");
                            speaker("Mode set to Cool.");
                        } else if (result.contains("vent")) {
                            displayMode("FAN");
                            speaker("Mode set to Vent.");
                        } else if (result.contains("dry")) {
                            displayMode("DRY");
                            speaker("Mode set to Dry.");
                        } else if (result.contains("heat") || result.contains("warm")) {
                            displayMode("HEAT");
                            speaker("Mode set to Heat.");
                        } else if (result.contains("auto") || result.contains("automatic")) {
                            displayMode("AUTO");
                            speaker("Mode set to Auto.");
                        } else {
                            speaker("Available modes are: COOL, VENT, DRY, HEAT or AUTO. Please try again.");
                        }
                    } else if (result.contains("meow")) {
                        speaker("Thank you for using our app. OwO");
                    } else if (result.contains("joke")) {
                        speaker("What’s the difference between a piano and a fish? You can tune a piano, but you can’t tuna fish. HAHA HAHA HAHA");
                    }else{
                        speaker("Sorry, please try again");
                    }
                }
            break;
        }
    }

    private void speaker(String message){
        audio.speak(message, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void listCommands() {
        String commands =   "Say 'TURN ON' to turn the AC ON."+
                            "Say 'TURN OFF' to turn the AC OFF. "+
                            "To set your desired temperature say the temperature value followed by the word Celsius."+
                            "To change the mode say: 'Set mode to', and the name of the mode you want, COOL, VENT, DRY, HEAT or AUTO";

        audio.speak(commands, TextToSpeech.QUEUE_FLUSH, null);

    }

    private void getMicPermission () {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE);
        }
    }
}