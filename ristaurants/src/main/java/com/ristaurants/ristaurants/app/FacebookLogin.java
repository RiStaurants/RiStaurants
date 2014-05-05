package com.ristaurants.ristaurants.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.ristaurants.ristaurants.misc.HelperClass;
import com.ristaurants.ristaurants.misc.Singleton;

import java.util.Arrays;
import java.util.List;

/**
 * Facebook login page.
 */
public class FacebookLogin extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setTitle(HelperClass.setActionbarTitle(this, "Logging in to facebook..."));

        // set of facebook permissions
        List<String> permissions = Arrays.asList("user_about_me",
                "user_relationships", "user_birthday", "user_location");

        // login user
        ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (err == null) {
                    if (user == null) {
                        Log.i(Singleton.FACEBOOK_LOGIN_TAG, "Uh oh. The user cancelled the Facebook login.");
                    } else if (user.isNew()) {
                        Log.i(Singleton.FACEBOOK_LOGIN_TAG, "User signed up and logged in through Facebook!");
                    } else {
                        Log.i(Singleton.FACEBOOK_LOGIN_TAG, "User logged in through Facebook! ");
                    }

                    // set user data
                    setUserData();
                } else {
                    // log error
                    Log.e("Facebook login Error", "Error: " + err.getMessage());
                    Toast.makeText(FacebookLogin.this, err.getMessage(), Toast.LENGTH_LONG).show();

                    // destroy activity
                    finish();
                }
            }
        });
    }

    private void setUserData() {
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                // check if user is not null
                if (user != null) {
                    // message
                    getActionBar().setTitle(HelperClass.setActionbarTitle(FacebookLogin.this, "Saving user information..."));

                    // store user data
                    //ParseUser.getCurrentUser().put("avatarUrl", "");
                    ParseUser.getCurrentUser().put("name", user.getName());
                }

                // save data
                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            // destroy activity
                            finish();
                        }
                    }
                });
            }
        });

        // execute request
        request.executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }
}
