package gr.aueb.acontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;


import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.ImageView;


public class MainActivity extends Activity {

    static int temp = 26;
    CompoundButton previousCheckedCompoundButton;
    ImageView power, infoTemp, infoMode;
    RadioButton cool, fan, dry, heat, auto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AControl);
        setContentView(R.layout.activity_main);

        power = (ImageView) findViewById(R.id.PowerBtn);
        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                power.setActivated(!power.isActivated());
                if (power.isActivated()){
                    toastMsg("AC is now ON.");
                }else{
                    toastMsg("AC is now OFF.");
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
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MoreActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        infoMode = (ImageView) findViewById(R.id.infoMode);
        timerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),TimerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        infoTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showInfoBox("Temperature Info", "Set your desired temperature through " +
                       "the arrow buttons and turn the AC on and off with the power button.");
            }
        });

        infoMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoBox("Modes Info", "Select your desired mode, the current mode " +
                        "is being displayed above the buttons.");
            }
        });
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

    public void modeCool(View view){
        displayMode("COOL");
    }
    public void modeFan(View view){
        displayMode("FAN");
    }
    public void modeDry(View view){
        displayMode("DRY");
    }
    public void modeHeat(View view){
        displayMode("HEAT");
    }
    public void modeAuto(View view){
        displayMode("AUTO");
    }

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
        displayInteger.setText("" + temp);
    }

    private void displayMode(String mode) {
        TextView Mode = (TextView) findViewById(R.id.CurrentModeVal);
        Mode.setText("" + mode);
    }
}