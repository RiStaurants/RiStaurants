package com.ristaurants.ristaurants.app;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.ristaurants.ristaurants.app.*;

import android.support.v4.app.Fragment;
import com.parse.*;
import com.ristaurants.ristaurants.misc.HelperClass;

/**
 *
 */
public class SettingsFrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate layout
        View view = inflater.inflate(R.layout.frag_settings, null);

        // set-up action bar
        getActivity().getActionBar().setTitle(HelperClass.setActionbarTitle(getActivity(), getResources().getString(R.string.ab_title_settings)));

		// logout user
		TextView mTvLogout = (TextView) view.findViewById(R.id.tv_profile_logout);
		mTvLogout.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// logout user
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.logout_user);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// logout user
								ParseUser.getCurrentUser().logOut();

								// let user know he/she is being logout
								Toast.makeText(getActivity(), "Logout successful", Toast.LENGTH_LONG).show();

                                // reload activity
                                HelperClass.reloadActivity(getActivity());
							}
						});
					builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// do thing
							}
						});

					if (ParseUser.getCurrentUser() != null) {
						// show dialog
						builder.show();
					}
				}
			});

		// return view
		return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
