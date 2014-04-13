package com.ristaurants.ristaurants.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.toolbox.NetworkImageView;
import com.parse.ParseUser;
import android.widget.*;
import android.view.View.*;
import android.support.v4.app.*;
import com.parse.*;

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

			// set user name
			TextView mTvUsername = (TextView) view.findViewById(R.id.tv_profile_username);
			mTvUsername.setText(ParseUser.getCurrentUser().getUsername());

        } else {
			// handle login
			TextView mTvLogin = (TextView) view.findViewById(R.id.tv_profile_login);
			mTvLogin.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// login user
						ParseUser.logInInBackground("cs15", "ric", new LogInCallback() {
								public void done(ParseUser user, ParseException e) {
									if (user != null) {
										// let user know everything went well
										Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_LONG).show();
										
									} else {
										// Signup failed. Look at the ParseException to see what happened.
										Toast.makeText(getActivity(), "Login not successful\n\nError: " + e.getMessage(), Toast.LENGTH_LONG).show();
									}
								}
							});
					}
				});
				
			// handle sign up
			TextView mTvSignUp = (TextView) view.findViewById(R.id.tv_profile_sign_up);
			mTvSignUp.setOnClickListener(new OnClickListener(){
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
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
