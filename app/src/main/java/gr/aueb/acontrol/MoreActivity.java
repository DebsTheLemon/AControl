package gr.aueb.acontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MoreActivity extends Activity {
    CompoundButton previousCheckedCompoundButton;
    ImageView swingSwitch, infoSwing, infoFan, infoModes;
    RadioButton turbo, economy;
    int speed = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        //Initiate Vibrator for haptic feedback
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Turbine Mode Image Radio Buttons
        //Turbo Mode Button
        turbo = (RadioButton) findViewById(R.id.Turbo);
        turbo.setOnCheckedChangeListener(onRadioButtonCheckedListener);
        //Eco Mode Button
        economy = (RadioButton) findViewById(R.id.Economy);
        economy.setOnCheckedChangeListener(onRadioButtonCheckedListener);

        //Swing Menu Info Box Button
        infoSwing = (ImageView) findViewById(R.id.infoSwing);
        infoSwing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoBox("Swing Adjustment", "To adjust the air's direction press START, " +
                        "when the flap has reached your desired angle, press STOP.");
            }
        });

        //Fan Speed Menu Info Box Button
        infoFan = (ImageView) findViewById(R.id.infoFan);
        infoFan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoBox("Fan Speed Controls", "Use the arrow buttons to adjust the fan Speed between 1-3.");
            }
        });

        //Turbine Mode Menu Info Box Button
        infoModes = (ImageView) findViewById(R.id.infoModes);
        infoModes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoBox("Turbine Modes Info", "Select your desired turbine mode, " +
                        "the current turbine mode is being displayed above the turbine mode buttons.");
            }
        });

        //Swing switch button
        swingSwitch = (ImageView) findViewById(R.id.SwingSwitch);
        swingSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swingSwitch.setActivated(!swingSwitch.isActivated());
                if (swingSwitch.isActivated()){
                    toastMsg("Swing is now ON.");
                }else{
                    toastMsg("Swing is now OFF.");
                }
            }
        });

        // Navigation Bar Buttons
        //Main Screen Button
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

        //Timer Screen Button
        ImageButton timerBtn = (ImageButton) findViewById(R.id.Timer);
        timerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.vibrate(50);
                Intent i = new Intent(getApplicationContext(),TimerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

    }

    //Used for functional image radio buttons in turbine mode selection
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

    //OnClick methods of each button
    public void modeCool(View view){
        displayMode("TURBO");
    }
    public void modeFan(View view){
        displayMode("ECO");
    }

    //Creates and displays toast of given message string
    public void toastMsg(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    //Created and displays infoBox of given title and info strings
    private void showInfoBox(String title, String info){
        AlertDialog infoBox = new AlertDialog.Builder(MoreActivity.this)
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

    //Updates current turbine mode TextView
    private void displayMode(String mode) {
        TextView Mode = (TextView) findViewById(R.id.CurrentTurbineVal);
        Mode.setText("" + mode);
    }

    //OnClick method of fan speed increase button
    public void increaseSpeed(View view) {
        if (speed < 3) {
            speed = speed + 1;
        }
        displaySpeed(speed);
    }

    //OnClick method of fan speed decrease button
    public void decreaseSpeed(View view) {
        if (speed > 1) {
            speed = speed - 1;
        }
        displaySpeed(speed);
    }

    //Updates current fan speed TextView
    private void displaySpeed(int speed) {
        TextView displayInteger = (TextView) findViewById (R.id.TxtCurrentFanVal);
        displayInteger.setText("" + speed);
    }
}
