package com.ristaurants.ristaurants.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Display about team information
 */
public class AboutFrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate view
        View view = inflater.inflate(R.layout.frag_about, null);

        // views
        TextView mTvFontBender = (TextView) view.findViewById(R.id.tv_font_bender);
        mTvFontBender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://jimdore.com"));
                startActivity(intent);
            }
        });

        // return
        return view;
    }
}
