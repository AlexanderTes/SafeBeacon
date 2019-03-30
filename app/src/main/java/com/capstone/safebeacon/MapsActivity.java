package com.capstone.safebeacon;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = "Testing";

    //widgets
    private EditText searchText;

//    public void goBack(View view) {
//        Intent intent = new Intent(getApplicationContext(),WelcomeActivity.class);
//
//        startActivity(intent);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        searchText = (EditText)findViewById(R.id.input_search);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        init();

    }

    private void init() {
        Log.d(TAG,"init: initializing");

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {

                    // execute method of searching
                    geoLocate();
                }

                return false;
            }
        });
    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = searchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> addressList  = new ArrayList<>();

        try {
            addressList = geocoder.getFromLocationName(searchString,1);
        }catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException:" + e.getMessage());
        }

        if(addressList.size() > 0) {
            Address address = addressList.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
//            Toast.makeText(this, address.toString(),Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        BitmapDescriptor fighting = BitmapDescriptorFactory.fromResource(R.drawable.fighting);
        BitmapDescriptor burglar = BitmapDescriptorFactory.fromResource(R.drawable.burglar);
        BitmapDescriptor theft = BitmapDescriptorFactory.fromResource(R.drawable.theft);
        BitmapDescriptor crime = BitmapDescriptorFactory.fromResource(R.drawable.crime);
        BitmapDescriptor minorAccident = BitmapDescriptorFactory.fromResource(R.drawable.minor_accident);
        BitmapDescriptor severeAccident = BitmapDescriptorFactory.fromResource(R.drawable.severe_accident);


        ArrayList<LatLng> latLngs = new ArrayList<>();

        latLngs.add(new LatLng(33.556242,-101.801492));
        latLngs.add(new LatLng(33.541580,-101.900734));
        latLngs.add(new LatLng(33.573734,-101.926059));
        latLngs.add(new LatLng(33.605888,-101.870524));
        latLngs.add(new LatLng(33.591225,-101.814989));
        latLngs.add(new LatLng(33.533765,-101.840314));
        latLngs.add(new LatLng(33.608716,-101.939556));
        latLngs.add(new LatLng(33.551256,-101.884021));
        latLngs.add(new LatLng(33.583410,-101.909346));
        latLngs.add(new LatLng(33.568747,-101.853811));
        latLngs.add(new LatLng(33.600901,-101.879136));
        latLngs.add(new LatLng(33.543441,-101.823601));
        latLngs.add(new LatLng(33.528779,-101.922843));
        latLngs.add(new LatLng(33.560933,-101.793391));
        latLngs.add(new LatLng(33.546270,-101.892633));
        latLngs.add(new LatLng(33.578424,-101.837099));
        latLngs.add(new LatLng(33.610578,-101.862423));
        latLngs.add(new LatLng(33.595915,-101.806888));
        latLngs.add(new LatLng(33.538455,-101.832213));
        latLngs.add(new LatLng(33.570609,-101.931455));

        // Add a marker in Sydney and move the camera
//        LatLng liberty = new LatLng(location.getLatitude(), location.getLongitude());
//        String name = "Texas Tech University";
//        mMap.addMarker(new MarkerOptions().position(liberty).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(liberty,10));
        for (int i = 0; i < latLngs.size(); i++) {
            if(i % 6 == 1) {
                mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("Marker " + i).icon(theft));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngs.get(i)));
            }
            else if(i % 6 == 2) {
                mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("Marker " + i).icon(fighting));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngs.get(i)));
            }
            else if(i % 6 == 3) {
                mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("Marker " + i).icon(burglar));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngs.get(i)));
            }
            else if(i % 6 == 4) {
                mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("Marker " + i).icon(crime));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngs.get(i)));
            }
            else if(i % 6 == 5) {
                mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("Marker " + i).icon(minorAccident));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngs.get(i)));
            }
            else {
                mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("Marker " + i).icon(severeAccident));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngs.get(i)));
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(11),11));
    }
}
