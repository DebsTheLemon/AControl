package gr.aueb.acontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class MoreActivity extends Activity {
    CompoundButton previousCheckedCompoundButton;

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
