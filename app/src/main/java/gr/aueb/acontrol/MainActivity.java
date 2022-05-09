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

import android.widget.ImageButton;
import android.widget.TextView;

import gr.aueb.acontrol.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    int temp = 24;

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
    }

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