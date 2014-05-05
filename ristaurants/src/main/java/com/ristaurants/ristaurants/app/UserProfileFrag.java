package com.ristaurants.ristaurants.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.ristaurants.ristaurants.misc.HelperClass;
import com.ristaurants.ristaurants.misc.Singleton;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Fragment to display user profile
 */
public class UserProfileFrag extends Fragment {
    // instance variables
    private ImageView mBtFacebookLogin;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate view
        view = inflater.inflate(R.layout.frag_user_profile, null);

        // set-up action bar
        getActivity().getActionBar().setTitle(HelperClass.setActionbarTitle(getActivity(), getResources().getString(R.string.ab_title_profile)));

        // get user data
        if (ParseUser.getCurrentUser() != null && ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())) {
            // display user information
            displayUserInfo();
        } else {
            // handle facebook login
            mBtFacebookLogin = (ImageView) view.findViewById(R.id.iv_fb_login);
            mBtFacebookLogin.setVisibility(View.VISIBLE);
            mBtFacebookLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // set of facebook permissions
                    List<String> permissions = Arrays.asList("user_about_me",
                            "user_relationships", "user_birthday", "user_location", "email");

                    // login user
                    ParseFacebookUtils.logIn(permissions, getActivity(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException err) {
                            if (err == null) {
                                if (user == null) {
                                    Log.i(Singleton.FACEBOOK_LOGIN_TAG, "Uh oh. The user cancelled the Facebook login.");
                                } else if (user.isNew()) {
                                    // set user data
                                    setUserData();
                                    Log.i(Singleton.FACEBOOK_LOGIN_TAG, "User signed up and logged in through Facebook!");
                                } else {
                                    // set user data
                                    setUserData();
                                    Log.i(Singleton.FACEBOOK_LOGIN_TAG, "User logged in through Facebook! ");
                                }

                            } else {
                                // log error
                                Log.e("Facebook login Error", "Error: " + err.getMessage());
                                Toast.makeText(getActivity(), err.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        }

        // return view
        return view;
    }

    /**
     * Store user data on parse
     */
    private void setUserData() {
        //ParseFacebookUtils.getSession().requestNewPublishPermissions(new Session.NewPermissionsRequest(getActivity(), "publish_actions"));
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                // check if user is not null
                if (user != null) {
                    // store user data
                    ParseUser.getCurrentUser().put("facebookId", user.getId());
                    ParseUser.getCurrentUser().put("name", user.getName());
                    ParseUser.getCurrentUser().put("email", user.asMap().get("email").toString());
                }

                // save data
                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            reloadFragment();
                        }
                    }
                });
            }
        });

        // execute request
        request.executeAsync();
    }

    /**
     * Display user information
     */
    private void displayUserInfo() {
        // display image
        ProfilePictureView mIvAvatar = (ProfilePictureView) view.findViewById(R.id.ppv_profile_image);
        mIvAvatar.setProfileId(ParseUser.getCurrentUser().getString("facebookId"));

        // display user name
        TextView mTvUsername = (TextView) view.findViewById(R.id.tv_profile_full_name);
        mTvUsername.setText(ParseUser.getCurrentUser().getString("name"));

        // display post totals
        TextView mTvPostTotal = (TextView) view.findViewById(R.id.tv_profile_post_count);
        mTvPostTotal.setText(mTvPostTotal.getText() + " " + String.valueOf(ParseUser.getCurrentUser().getInt("postTotal")));

        // display user email
        TextView mTvEmail = (TextView) view.findViewById(R.id.tv_profile_email);
        mTvEmail.setText(" " + ParseUser.getCurrentUser().getString("email"));

        // display member since
        Date date = ParseUser.getCurrentUser().getCreatedAt();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
        TextView mTvMemberSince = (TextView) view.findViewById(R.id.tv_profile_member_since);
        mTvMemberSince.setText(" " + dateFormat.format(date));
    }

    /**
     * Reload fragment
     */
    private void reloadFragment() {
        /**
         // reload fragment
         Fragment frag = getActivity().getSupportFragmentManager().findFragmentByTag("UserProfileFrag");
         FragmentTransaction fragTrans = getActivity().getSupportFragmentManager().beginTransaction();
         fragTrans.detach(frag);
         fragTrans.attach(frag);
         fragTrans.commit();
         */

        // reload activity
        HelperClass.reloadActivity(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().supportInvalidateOptionsMenu();
        setHasOptionsMenu(true);
    }
}
