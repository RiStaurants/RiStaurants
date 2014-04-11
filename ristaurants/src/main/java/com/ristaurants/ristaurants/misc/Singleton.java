package com.ristaurants.ristaurants.misc;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;
import com.ristaurants.ristaurants.app.BaseActivity;

/**
 * Instantiate all singleton class
 */
public class Singleton extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // instantiate volley instances
        SingletonVolley.initantiate(this);

        // instantiate Parse Database
        Parse.initialize(this, "WB3Th85cP3viS7jJ5zkXzkZ2MTsFagIg0AKQeBpQ", "EGZKA60G8Iy4vVCPPvBDjn2XoeBbqQ1rtWReRvRh");

        // notification
        //PushService.setDefaultPushCallback(this, BaseActivity.class);

        // save the current Installation to Parse.
        //ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
