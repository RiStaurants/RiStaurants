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

        // hide/show login container
        if (ParseUser.getCurrentUser() != null) {
            LinearLayout mLlLoginContainer = (LinearLayout) view.findViewById(R.id.ll_login_sign_up);
            mLlLoginContainer.setVisibility(View.GONE);

            // views
            mIvProfileImage = (NetworkImageView) view.findViewById(R.id.niv_profile_image);
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
