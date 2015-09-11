package com.sugarspicedance.sugarspice;

import android.app.Application;

import com.parse.Parse;

public class SugarApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "HCJZabkKVVJfGQ9TX18kUOrvV3S4qyJwocuiKcML", "gIncYlqSus33jH7pVKHVDHaopAxqehbSwt0lckH7");
    }
}
