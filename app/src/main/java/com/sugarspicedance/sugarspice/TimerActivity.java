package com.sugarspicedance.sugarspice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

public class TimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
    }

    public void selectAnotherTime(View view) {
        onBackPressed();
    }

    public void openTimer(View view) {
        findViewById(R.id.logoImageView).setAlpha(0.1f);

        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setText("P a u s e");

        TypedValue outValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.selectableItemBackground, outValue, true);
        startButton.setBackgroundResource(outValue.resourceId);

        startButton.setTextColor(getResources().getColor(R.color.indigo_300));

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
            }
        });
    }

    private void pauseTimer() {
        findViewById(R.id.logoImageView).setAlpha(0.7f);

        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setText("R e s u m e");

        startButton.setBackground(getResources().getDrawable(R.drawable.circular_button_selector));

        startButton.setTextColor(getResources().getColor(R.color.white_700));

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimer(view);
            }
        });
    }
}
