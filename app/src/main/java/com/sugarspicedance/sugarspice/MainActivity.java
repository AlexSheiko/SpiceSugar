package com.sugarspicedance.sugarspice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView) findViewById(R.id.booth_name)).setText(ParseUser.getCurrentUser().getUsername());
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
            case R.id.mins_45:
                intent.putExtra("minutes", 45);
                break;
            case R.id.mins_60:
                intent.putExtra("minutes", 60);
                break;
        }

        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ParseUser.getCurrentUser().deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                ParseUser.logOutInBackground();
            }
        });
    }
}