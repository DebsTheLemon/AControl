package gr.aueb.acontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class TimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

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
}