package com.sugarspicedance.sugarspice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "SugarDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ParseUser.getCurrentUser() != null) {
            showMainScreen();
        } else {
            loginAutomatically();
        }
    }

    private void loginAutomatically() {

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending("username");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    int number = getNextBoothNumber(users);
                    String booth = getBoothName(number);

                    ParseUser user = new ParseUser();
                    user.setUsername(booth);
                    user.setPassword(booth);

                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                setupDummyTimer();
                                showMainScreen();
                            } else {
                                showErrorDialog(e);
                            }
                        }
                    });
                } else {
                    showErrorDialog(e);
                }
            }
        });
    }

    private String getBoothName(int number) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("BoothNames");
        query.orderByAscending("name");
        try {
            List<ParseObject> boothes = query.find();
            for (ParseObject booth : boothes) {
                if (booth.getString("name").contains(number+"")) {
                    return booth.getString("name");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "No more boothes";
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

    private int getNextBoothNumber(List<ParseUser> users) {
        int index = 1;

        for (ParseUser user : users) {
            int number = Integer.parseInt(user.getUsername().substring(0, 1));
            if (index != number) {
                return index;
            } else {
                index++;
            }
        }

        return index;
    }

    private void showMainScreen() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    private void showErrorDialog(ParseException e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Check your network connection and try again");
        builder.create().show();
    }
}
