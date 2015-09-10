package com.sugarspicedance.sugarspice;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
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
    private ObjectAnimator colorAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        int minutes = getIntent().getIntExtra("minutes", -1);

//        mSecondsLeft = (long) (minutes * 60);
        mSecondsLeft = 16;
        TextView counterView = (TextView) findViewById(R.id.counterView);
        counterView.setText(formatTime(mSecondsLeft));
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

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
            }
        };
        startButton.setOnClickListener(listener);
        findViewById(R.id.counterView).setOnClickListener(listener);
        findViewById(R.id.remainingLabel).setOnClickListener(listener);
        findViewById(R.id.logoImageView).setOnClickListener(listener);
    }

    private void pauseTimer() {
        mTimer.cancel();

        findViewById(R.id.logoImageView).setAlpha(0.7f);

        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setText("R e s u m e");

        startButton.setBackground(getResources().getDrawable(R.drawable.circular_button_selector));

        startButton.setTextColor(getResources().getColor(R.color.white_700));

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer(view);
            }
        };
        startButton.setOnClickListener(listener);
        findViewById(R.id.counterView).setOnClickListener(listener);
        findViewById(R.id.remainingLabel).setOnClickListener(listener);
        findViewById(R.id.logoImageView).setOnClickListener(listener);
    }

    private void startCountdown(long seconds) {
        final TextView counterView = (TextView) findViewById(R.id.counterView);

        mTimer = new CountDownTimer(seconds * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;

                counterView.setText(formatTime(secondsLeft));
                mSecondsLeft = secondsLeft;

                if (mSecondsLeft == 15) {
                    startBlinkering(counterView);
                }
            }

            public void onFinish() {
                counterView.setText("00:00");
                counterView.setTextColor(Color.RED);
                colorAnim.cancel();
            }
        };
        mTimer.start();
    }

    private void startBlinkering(TextView textView) {
        colorAnim = ObjectAnimator.ofInt(textView, "textColor",
                getResources().getColor(R.color.white_700), Color.RED);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setDuration(500);
        colorAnim.setStartDelay(750);
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();
    }

    private String formatTime(long s) {
        if (s == 3600) {
            return "60:00";
        } else if (s == 1) {
            return "00:01";
        } else {
            return String.format("%02d:%02d", (s % 3600) / 60, (s % 60));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
    }
}
