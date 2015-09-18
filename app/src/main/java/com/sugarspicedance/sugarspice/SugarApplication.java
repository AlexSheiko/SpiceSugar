package com.sugarspicedance.sugarspice;

import android.app.Application;

import com.parse.Parse;

public class SugarApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "OC8ApDFh9cc4PJDTRBuqmiUG6YUxaJxgc61IbeF8", "OeSxciiBuXYjFxM82Q0Lgz31e0MT6Reb5ZfoCEzb");
    }
}
