package com.ristaurants.ristaurants.app;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ristaurants.ristaurants.misc.HelperClass;

/**
 * Display the google map location
 */
public class MapLocation extends FragmentActivity {
    // instance variables
    private GoogleMap mGoogleMap;
    private LatLng mGeoPoints;
    private String mRestaurantName;
    private String mAddress;
    private float mZoomPerc = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_maps);

        // set action bar background color
        HelperClass.setActionBarBackground(this, R.color.menus_bg);

        // set-up action bar
        getActionBar().setTitle(HelperClass.setActionbarTitle(this, getResources().getString(R.string.ab_title_maps)));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.ic_action_navigation_expand);

        // get data from previous activity
        if (getIntent().getExtras() != null) {
            float latitude = (float)getIntent().getExtras().getDouble("mLatitude");
            float longitude = (float)getIntent().getExtras().getDouble("mLongitude");
            mGeoPoints = new LatLng(latitude, longitude);
            mRestaurantName = getIntent().getExtras().getString("mRestaurantName");
            mAddress = getIntent().getExtras().getString("mAddress");
        }

        // instantiate fragment
        mGoogleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gm_map)).getMap();
        mGoogleMap.setMyLocationEnabled(false);

        // zoom in to the geo point
        CameraUpdate center = CameraUpdateFactory.newLatLng(mGeoPoints);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(mZoomPerc);

        // set camera
        mGoogleMap.moveCamera(center);
        mGoogleMap.animateCamera(zoom);

        // mark location
        Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                .position(mGeoPoints)
                .title(mRestaurantName)
                .snippet(mAddress)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        marker.showInfoWindow();

        // information click listener
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                // open google maps with directions
                openGoogleMaps();
            }
        });

        // marker click listener
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // open google maps with directions
                openGoogleMaps();
                return false;
            }
        });
    }

    /**
     * open Google Maps app
     */
    private void openGoogleMaps() {
        // open google maps with directions
        Intent intent = new Intent(Intent.ACTION_VIEW, null);
        intent.setData(Uri.parse("http://maps.google.co.in/maps?q=" + mAddress));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_google_map, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // go back to previous screens
                onBackPressed();
                break;
            case R.id.menu_directions:
                // open google maps with directions
                openGoogleMaps();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // set activity animation
        this.overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
    }

}
