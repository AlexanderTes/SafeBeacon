package com.capstone.safebeacon;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = "Testing";

    LocationManager locationManager;

    LocationListener locationListener;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    //widgets
    private EditText searchText;

    private Switch switchView;
    BitmapDescriptor myLocationIC;

//    private static final int CAMERA_REQUEST = 1888;
//    private static final int MY_CAMERA_PERMISSION_CODE = 100;


    public void updateMap(Location location) {
        mMap.clear();
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.addMarker(new MarkerOptions().position(userLocation).title("My Location").icon(myLocationIC));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,14));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                    Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    updateMap(lastLocation);
                }
            }
        }
//        if (requestCode == MY_CAMERA_PERMISSION_CODE)
//        {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
//            {
//                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
//            }
//            else
//            {
//                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
//            }
//        }
//        protected void onActivityResult(int requestCode, int resultCode, Intent data)
//        {
//            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
//            {
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                imageView.setImageBitmap(photo);
//            }
//        }

    }

    public void loadNote(Location location) {

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> addressList = new ArrayList<>();
        String city = "";

        try {
            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException:" + e.getMessage());
        }

        if (addressList.size() > 0) {
            Address address = addressList.get(0);
            city = address.getLocality();
        }
        db.collection("reports")
                .whereEqualTo("city",city)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "Load Note: " + document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting document: ", task.getException());

                        }
                    }
                });
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                        if(documentSnapshot.exists()) {
////                            String comment = documentSnapshot.getString("comment");
////                            String locationSTr = documentSnapshot.getString("location");
//
////                            Log.d(TAG, "Location: " + locationSTr);
//
//                            Map<String, Object> note = documentSnapshot.getData();
//                            Log.d(TAG,"Load Note: " + note.toString());
//                        }
//                        else
//                            Toast.makeText(MapsActivity.this,"Document does not exist",Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        searchText = findViewById(R.id.input_search);
        switchView = findViewById(R.id.switchView);

        final RelativeLayout reLay2 = findViewById(R.id.relativeLayout2);
        final ImageButton photoButton = findViewById(R.id.photoButton);

        switchView.setChecked(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        loadNote("Duckwater"); // Testing

        init();

        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    reLay2.setVisibility(View.INVISIBLE);
                    photoButton.setVisibility(View.VISIBLE);

                    mMap.getUiSettings().setZoomControlsEnabled(false);

                } else {
                    reLay2.setVisibility(View.VISIBLE);
                    photoButton.setVisibility(View.INVISIBLE);

                    mMap.getUiSettings().setZoomControlsEnabled(true);
                }
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                startActivity(intent);
//                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//                {
//                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
//                }
//                else
//                {
//                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
//                }
            }
        });

    }

    private void init() {
        Log.d(TAG, "init: initializing");

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
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
        List<Address> addressList = new ArrayList<>();

        try {
            addressList = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException:" + e.getMessage());
        }

        if (addressList.size() > 0) {
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());


            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
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

        BitmapDescriptor fighting = BitmapDescriptorFactory.fromResource(R.drawable.fighting);
        BitmapDescriptor burglar = BitmapDescriptorFactory.fromResource(R.drawable.burglar);
        BitmapDescriptor theft = BitmapDescriptorFactory.fromResource(R.drawable.theft);
        BitmapDescriptor crime = BitmapDescriptorFactory.fromResource(R.drawable.crime);
        BitmapDescriptor minorAccident = BitmapDescriptorFactory.fromResource(R.drawable.minor_accident);
        BitmapDescriptor severeAccident = BitmapDescriptorFactory.fromResource(R.drawable.severe_accident);
        myLocationIC = BitmapDescriptorFactory.fromResource(R.drawable.mylocation);

        ArrayList<LatLng> latLngs = new ArrayList<>();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if(switchView.isChecked())
                    updateMap(location);
                loadNote(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLocation != null) {
                updateMap(lastLocation);
            }
        }


        latLngs.add(new LatLng(33.556242, -101.801492));
        latLngs.add(new LatLng(33.541580, -101.900734));
        latLngs.add(new LatLng(33.573734, -101.926059));
        latLngs.add(new LatLng(33.605888, -101.870524));
        latLngs.add(new LatLng(33.591225, -101.814989));
        latLngs.add(new LatLng(33.533765, -101.840314));
        latLngs.add(new LatLng(33.608716, -101.939556));
        latLngs.add(new LatLng(33.551256, -101.884021));
        latLngs.add(new LatLng(33.583410, -101.909346));
        latLngs.add(new LatLng(33.568747, -101.853811));
        latLngs.add(new LatLng(33.600901, -101.879136));
        latLngs.add(new LatLng(33.543441, -101.823601));
        latLngs.add(new LatLng(33.528779, -101.922843));
        latLngs.add(new LatLng(33.560933, -101.793391));
        latLngs.add(new LatLng(33.546270, -101.892633));
        latLngs.add(new LatLng(33.578424, -101.837099));
        latLngs.add(new LatLng(33.610578, -101.862423));
        latLngs.add(new LatLng(33.595915, -101.806888));
        latLngs.add(new LatLng(33.538455, -101.832213));
        latLngs.add(new LatLng(33.570609, -101.931455));


        for (int i = 0; i < latLngs.size(); i++) {
            if (i % 6 == 1) {
                mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("Marker: Theft " + i).icon(theft));
            } else if (i % 6 == 2) {
                mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("Marker: Fighting " + i).icon(fighting));
            } else if (i % 6 == 3) {
                mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("Marker: Burglar " + i).icon(burglar));
            } else if (i % 6 == 4) {
                mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("Marker: Suspect " + i).icon(crime));
            } else if (i % 6 == 5) {
                mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("Marker: minor Accident " + i).icon(minorAccident));
            } else {
                mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("Marker: severe Accident " + i).icon(severeAccident));
            }
        }


//        mMap2.setMyLocationEnabled(true);

    }


}
