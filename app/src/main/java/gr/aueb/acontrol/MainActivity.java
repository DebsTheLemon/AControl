package gr.aueb.acontrol;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;


import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.ImageView;


public class MainActivity extends Activity {

    private AppBarConfiguration appBarConfiguration;
    int temp = 24;
    CompoundButton previousCheckedCompoundButton;
    ImageView mIvToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIvToggle = (ImageView) findViewById(R.id.PowerBtn);
        mIvToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIvToggle.setActivated(!mIvToggle.isActivated());
            }
        });

        RadioButton cool, fan, dry, heat, auto;
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

    public void increaseInteger(View view) {
        if (temp < 32) {
            temp = temp + 1;
        }
        display(temp);
    }

    public void decreaseInteger(View view) {
        if (temp > 16) {
            temp = temp - 1;
        }
        display(temp);
    }

    private void display(int temp) {
        TextView displayInteger = (TextView) findViewById(
                R.id.CurrentTempNumber);
        displayInteger.setText("" + temp);
    }
}