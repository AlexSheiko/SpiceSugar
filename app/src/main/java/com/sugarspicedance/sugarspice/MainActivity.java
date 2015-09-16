package com.sugarspicedance.sugarspice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean mChangeUser = false;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.booth_list_item, new ArrayList<String>());
        final Spinner boothSpinner = (Spinner) findViewById(R.id.booth_spinner);
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
                    int spinnerPosition = adapter.getPosition(ParseUser.getCurrentUser().getUsername());
                    boothSpinner.setSelection(spinnerPosition);

                    boothSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> spinner, View view, int position, long id) {
                            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

                            final String booth = adapter.getItem(position);
                            if (ParseUser.getCurrentUser() == null || booth.equals(ParseUser.getCurrentUser().getUsername())) {
                                findViewById(R.id.progressBar).setVisibility(View.GONE);
                                return;
                            }
                            mPrefs.edit().putString("manual_booth", booth).apply();
                            mChangeUser = true;

                            ParseQuery<ParseObject> timerDeleteQuery = ParseQuery.getQuery("Timer");
                            timerDeleteQuery.whereEqualTo("booth", ParseUser.getCurrentUser().getUsername());
                            timerDeleteQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject timer, ParseException e) {
                                    if (e == null) {
                                        timer.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                ParseUser.getCurrentUser().deleteInBackground(new DeleteCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        ParseUser.logOutInBackground(new LogOutCallback() {
                                                            @Override
                                                            public void done(ParseException e) {
                                                                ParseUser.logInInBackground(booth, booth, new LogInCallback() {
                                                                    @Override
                                                                    public void done(ParseUser parseUser, ParseException e) {
                                                                        if (e == null) {
                                                                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                                                                            restartActivity();
                                                                        } else {
                                                                            ParseUser user = new ParseUser();
                                                                            user.setUsername(booth);
                                                                            user.setPassword(booth);

                                                                            user.signUpInBackground(new SignUpCallback() {
                                                                                public void done(ParseException e) {
                                                                                    if (e == null) {
                                                                                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                                                                                        setupDummyTimer();
                                                                                        restartActivity();
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            }
        });
    }

    private void setupDummyTimer() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Timer");
        query.whereEqualTo("booth", ParseUser.getCurrentUser().getUsername());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject timerObject, ParseException e) {
                ParseObject timer;
                if (timerObject != null) {
                    timer = timerObject;
                } else {
                    timer = new ParseObject("Timer");
                }
                timer.put("booth", ParseUser.getCurrentUser().getUsername());
                timer.put("secondsLeft", 0);
                timer.saveEventually();
            }
        });
    }

    private void restartActivity() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
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
        if (!mChangeUser) {
            if (ParseUser.getCurrentUser() != null) {
                ParseUser.getCurrentUser().deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        ParseUser.logOutInBackground();
                    }
                });
            }
        } else {
            mChangeUser = false;
        }
    }
}