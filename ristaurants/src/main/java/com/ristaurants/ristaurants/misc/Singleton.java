package com.ristaurants.ristaurants.misc;

import android.app.Application;

/**
 * Instantiate all singleton class
 */
public class Singleton extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // instantiate volley instances
        SingletonVolley.initantiate(this);
    }
}
