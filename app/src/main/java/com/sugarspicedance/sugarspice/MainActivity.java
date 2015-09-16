package com.sugarspicedance.sugarspice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.booth_list_item, new ArrayList<String>());
        Spinner boothSpinner = (Spinner) findViewById(R.id.booth_spinner);
        boothSpinner.setAdapter(adapter);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("BoothNames");
        query.orderByAscending("name");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> boothes, ParseException e) {
                if (e == null) {
                    for (ParseObject booth : boothes) {
                        adapter.add(booth.getString("name"));
                    }
                }
            }
        });
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