package com.ristaurants.ristaurants.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Sign up user fragment
 */
public class SignUpFrag extends Fragment {
    // instance variables
    private EditText mEtUsername;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private Button mBtSignUp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate view
        View view = inflater.inflate(R.layout.frag_signup, null);

        // views
        mEtUsername =(EditText) view.findViewById(R.id.et_username);
        mEtEmail = (EditText) view.findViewById(R.id.et_email);
        mEtPassword = (EditText) view.findViewById(R.id.et_password);
        mBtSignUp = (Button) view.findViewById(R.id.bt_signup);
        mBtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = new ParseUser();
                user.setUsername(mEtUsername.getText().toString().toLowerCase());
                user.setPassword(mEtPassword.getText().toString().toLowerCase());
                user.setEmail(mEtEmail.getText().toString().toLowerCase());
                user.put("firstName", "Christian");
                user.put("lastName", "Soler");
                user.put("dob", "03-21-1980");
                user.put("bio", "Bio Here!");
                user.put("place", "Providence, RI");

                // listener
                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Sign up was successful
                            Toast.makeText(getActivity(), "Please, check you email to verify your account.", Toast.LENGTH_LONG).show();

                            // invalidate menu
                            getActivity().supportInvalidateOptionsMenu();

                            // finish fragment
                            getActivity().onBackPressed();
                        } else {
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                            Toast.makeText(getActivity(), "Sign up was not successful.\n" + e.getMessage() , Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // return view
        return view;
    }
}
