package com.ristaurants.ristaurants.misc;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.PushService;
import com.ristaurants.ristaurants.app.BaseActivity;
import com.ristaurants.ristaurants.app.R;

/**
 * Instantiate all singleton class
 */
public class Singleton extends Application {

    public static final String FACEBOOK_LOGIN_TAG = "Facebook Login";

    @Override
    public void onCreate() {
        super.onCreate();

        // instantiate volley instances
        SingletonVolley.initantiate(this);

        // instantiate parse database
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_app_secret));

        // notification
        PushService.setDefaultPushCallback(this, BaseActivity.class);

        // save the current Installation to Parse.
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
