package com.sugarspicedance.sugarspice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressWarnings("unused")
    public void openTimer(View view) {
        Intent intent = new Intent(this, TimerActivity.class);

        switch (view.getId()) {
            case R.id.mins_4:
                intent.putExtra("minutes", 4);
                break;
            case R.id.mins_8:
                intent.putExtra("minutes", 8);
                break;
            case R.id.mins_12:
                intent.putExtra("minutes", 12);
                break;
            case R.id.mins_10:
                intent.putExtra("minutes", 10);
                break;
            case R.id.mins_15:
                intent.putExtra("minutes", 15);
                break;
            case R.id.mins_30:
                intent.putExtra("minutes", 30);
                break;
            case R.id.mins_60:
                intent.putExtra("minutes", 60);
                break;
        }

        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}