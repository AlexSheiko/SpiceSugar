package com.sugarspicedance.sugarspice;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TimerActivity extends AppCompatActivity {

    CountDownTimer mTimer;
    private long mSecondsLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        int minutes = getIntent().getIntExtra("minutes", -1);

        mSecondsLeft = (long) (minutes * 60);
        TextView counterView = (TextView) findViewById(R.id.counterView);
        counterView.setText(mSecondsLeft + "");
    }

    public void selectAnotherTime(View view) {
        onBackPressed();
    }

    public void startTimer(View view) {
        startCountdown(mSecondsLeft);

        findViewById(R.id.logoImageView).setAlpha(0.2f);

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
        mTimer.cancel();

        findViewById(R.id.logoImageView).setAlpha(0.7f);

        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setText("R e s u m e");

        startButton.setBackground(getResources().getDrawable(R.drawable.circular_button_selector));

        startButton.setTextColor(getResources().getColor(R.color.white_700));

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer(view);
            }
        });
    }

    private void startCountdown(long seconds) {
        final TextView counterView = (TextView) findViewById(R.id.counterView);

        mTimer = new CountDownTimer(seconds * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;

                counterView.setText(secondsLeft + "");
                mSecondsLeft = secondsLeft;
            }

            public void onFinish() {
                counterView.setText("done!");
            }
        };
        mTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
    }
}
