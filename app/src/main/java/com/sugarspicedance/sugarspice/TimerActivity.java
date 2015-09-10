package com.sugarspicedance.sugarspice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class TimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
    }

    public void selectAnotherTime(View view) {
        onBackPressed();
    }
}
