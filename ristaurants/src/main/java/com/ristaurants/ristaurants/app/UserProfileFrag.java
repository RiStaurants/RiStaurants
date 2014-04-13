package com.ristaurants.ristaurants.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.util.Date;

/**
 * Fragment to display user profile
 */
public class UserProfileFrag extends Fragment {
    // instance variables
    private NetworkImageView mIvProfileImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate view
        View view = inflater.inflate(R.layout.frag_user_profile, null);

        // get user data
        if (ParseUser.getCurrentUser() != null) {
            // hide sign up container
            LinearLayout mLlLoginContainer = (LinearLayout) view.findViewById(R.id.ll_login_sign_up);
            mLlLoginContainer.setVisibility(View.GONE);

            // set user image
            mIvProfileImage = (NetworkImageView) view.findViewById(R.id.niv_profile_image);

            // display user information
            displayUserInfo(view);

        } else {
            // handle login
            TextView mTvLogin = (TextView) view.findViewById(R.id.tv_profile_login);
            mTvLogin.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // login user
                    loginUser();
                }
            });

            // handle sign up
            TextView mTvSignUp = (TextView) view.findViewById(R.id.tv_profile_sign_up);
            mTvSignUp.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // display login fragment
                    SignUpFrag frag = new SignUpFrag();
                    FragmentTransaction mFragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                    mFragTrans.setCustomAnimations(R.anim.anim_slide_in_right, android.R.anim.fade_out);
                    mFragTrans.replace(R.id.fl_drawer, frag);
                    mFragTrans.addToBackStack(null);
                    mFragTrans.commit();
                }
            });
        }

        // return view
        return view;
    }

    /**
     * try to login user to account
     */
    private void loginUser() {
        // dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // inflate and set the layout for the dialog
        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_login, null));

        // handle ok button
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // views
                EditText mEtUsername = (EditText) ((AlertDialog) dialog).findViewById(R.id.et_login_username);
                EditText mEtPassword = (EditText) ((AlertDialog) dialog).findViewById(R.id.et_login_password);

                // login user
                ParseUser.logInInBackground(mEtUsername.getText().toString(), mEtPassword.getText().toString(), new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            // let user know everything went well
                            Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_LONG).show();
                        } else {
                            // Sign up failed. Look at the ParseException to see what happened.
                            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // handle cancel button
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // close dialog
                dialog.cancel();
            }
        });

        // show dialog
        builder.create().show();
    }

    /**
     * display user information
     */
    private void displayUserInfo(View view) {
        // get user data
        ParseUser user = ParseUser.getCurrentUser();

        // set user name
        TextView mTvUsername = (TextView) view.findViewById(R.id.tv_profile_username);
        mTvUsername.setText(user.getUsername());

        // set full name
        TextView mTvFullName = (TextView) view.findViewById(R.id.tv_profile_full_name);
        mTvFullName.setAutoLinkMask(Linkify.ALL);
        String fullName = user.getString("firstName") + " " + user.getString("lastName");
        mTvFullName.setText(mTvFullName.getText().toString() + " " + fullName);

        // set location
        TextView mTvLocation = (TextView) view.findViewById(R.id.tv_profile_location);
        mTvLocation.setText(mTvLocation.getText().toString() + " " + user.getString("place"));

        // set member since
        Date date = user.getCreatedAt();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
        TextView mTvMemberSince = (TextView) view.findViewById(R.id.tv_profile_member_since);
        mTvMemberSince.setText(mTvMemberSince.getText().toString() + " " + dateFormat.format(date));

        // set bio
        TextView mTvBio = (TextView) view.findViewById(R.id.tv_profile_bio);
        mTvBio.setText(mTvBio.getText().toString() + " " + user.getString("bio"));
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_user_profile).setVisible(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().supportInvalidateOptionsMenu();
    }
}
