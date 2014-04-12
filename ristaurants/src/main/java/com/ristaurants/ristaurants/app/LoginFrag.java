package com.ristaurants.ristaurants.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Login user fragment
 */
public class LoginFrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate view
        View view = inflater.inflate(R.layout.frag_login, null);

        Button bt = (Button) view.findViewById(R.id.bt_signup);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // display login fragment
                SignUpFrag frag = new SignUpFrag();
                FragmentTransaction mFragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                mFragTrans.setCustomAnimations(R.anim.anim_in, R.anim.anim_out);
                mFragTrans.replace(R.id.fl_drawer, frag);
                mFragTrans.addToBackStack(null);
                mFragTrans.commit();
            }
        });

        Toast.makeText(getActivity(), "Login Fragment!", Toast.LENGTH_LONG).show();

        // return
        return view;
    }
}
