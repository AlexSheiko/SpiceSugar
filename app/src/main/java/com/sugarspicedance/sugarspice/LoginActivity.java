package com.sugarspicedance.sugarspice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
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
                    String booth = "Booth " + number;

                    ParseUser user = new ParseUser();
                    user.setUsername(booth);
                    user.setPassword(booth);

                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
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

    private int getNextBoothNumber(List<ParseUser> users) {
        int index = 1;

        for (ParseUser user : users) {
            int number = Integer.parseInt(user.getUsername().replace("Booth ", ""));
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
        builder.setMessage(e.getMessage());
        builder.create().show();
    }
}
