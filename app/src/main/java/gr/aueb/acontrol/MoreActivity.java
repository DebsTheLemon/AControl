package gr.aueb.acontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
    int speed = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        RadioButton turbo, economy;
        turbo = (RadioButton) findViewById(R.id.Turbo);
        economy = (RadioButton) findViewById(R.id.Economy);

        turbo.setOnCheckedChangeListener(onRadioButtonCheckedListener);
        economy.setOnCheckedChangeListener(onRadioButtonCheckedListener);

        ImageButton homeBtn = (ImageButton) findViewById(R.id.Home);
        ImageButton timerBtn = (ImageButton) findViewById(R.id.Timer);

        infoSwing = (ImageView) findViewById(R.id.infoSwing);
        infoFan = (ImageView) findViewById(R.id.infoFan);
        infoModes = (ImageView) findViewById(R.id.infoModes);

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

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        timerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),TimerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        infoSwing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoBox("Swing Adjustment", "To adjust the air's direction press START, " +
                        "when the flap has reached your desired angle, press STOP.");
            }
        });

        infoFan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoBox("Fan Speed Controls", "Use the arrow buttons to adjust the fan Speed between 1-3.");
            }
        });

        infoModes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoBox("Turbine Modes Info", "Select your desired turbine mode, " +
                        "the current turbine mode is being displayed above the turbine mode buttons.");
            }
        });

        infoModes = (ImageView) findViewById(R.id.infoModes);
        timerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),TimerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

    }

    public void toastMsg(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

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

    public void modeCool(View view){
        displayMode("TURBO");
    }
    public void modeFan(View view){
        displayMode("ECO");
    }

    private void displayMode(String mode) {
        TextView Mode = (TextView) findViewById(R.id.CurrentTurbineVal);
        Mode.setText("" + mode);
    }
    public void increaseSpeed(View view) {
        if (speed < 3) {
            speed = speed + 1;
        }
        displaySpeed(speed);
    }

    public void decreaseSpeed(View view) {
        if (speed > 1) {
            speed = speed - 1;
        }
        displaySpeed(speed);
    }

    private void displaySpeed(int speed) {
        TextView displayInteger = (TextView) findViewById (R.id.TxtCurrentFanVal);
        displayInteger.setText("" + speed);
    }
}
