package com.ristaurants.ristaurants.app;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // if the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!super.getUserLearnedDrawer()) {
            openLeftDrawer();
        }
    }
}
