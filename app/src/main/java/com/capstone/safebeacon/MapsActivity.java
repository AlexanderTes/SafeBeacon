package com.capstone.safebeacon;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    ArrayList<Report> reports = new ArrayList<>();

    private int FIGHTING = 1;
    private int THEFT = 2;
    private int BURGLAR = 3;
    private int MINOR_ACCIDENT = 4;
    private int SEVERE_ACCIDENT = 5;
    private int CRIME = 6;
    private int WEIGHT_NUMBER = 20;

    HashMap<Integer, String> accTypeStr;

    BitmapDescriptor fightingBitmap;
    BitmapDescriptor burglarBitmap;
    BitmapDescriptor theftBitmap;
    BitmapDescriptor crimeBitmap;
    BitmapDescriptor minorAccidentBitmap;
    BitmapDescriptor severeAccidentBitmap;

    private static final String TAG = "Testing";

    LocationManager locationManager;

    LocationListener locationListener;
    Location currentLocation;

    private Marker currLocMarker;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    //widgets
    private EditText searchText;

    private Switch switchView;
    BitmapDescriptor myLocationIC;


    ArrayList<WeightedLatLng> weightedLatLngs = new ArrayList<>();

//    private static final int CAMERA_REQUEST = 1888;
//    private static final int MY_CAMERA_PERMISSION_CODE = 100;


    public void zoomToCurrentLocation(Location location) {
        if(currLocMarker != null) {
            currLocMarker.remove();
        }
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

        Marker marker = mMap.addMarker(new MarkerOptions().position(userLocation).title("My Location").icon(myLocationIC));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,14));

        currLocMarker = marker;
    }

    public void moveToLocation(Location location, int zoom){
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(userLocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,zoom));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                    Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    zoomToCurrentLocation(lastLocation);
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

    public void addMarker(ArrayList<Report> reports) {
        for (int i = 0; i < reports.size(); i++) {
            Report report = reports.get(i);
            if (report.getAccidentType() == FIGHTING) {
                mMap.addMarker(new MarkerOptions().position(report.getLatLng())
                        .title(accTypeStr.get(FIGHTING) + "\nDate: " + report.getTimeStamp() + "\n" + geoLocateByLatLng(report.getLatLng()).toString())
                        .icon(fightingBitmap));
            } else if (report.getAccidentType() == THEFT) {
                mMap.addMarker(new MarkerOptions().position(report.getLatLng())
                        .title(accTypeStr.get(THEFT) + "\nDate: " + report.getTimeStamp() + "\n" + geoLocateByLatLng(report.getLatLng()).toString())
                        .icon(theftBitmap));
            } else if (report.getAccidentType() == BURGLAR) {
                mMap.addMarker(new MarkerOptions().position(report.getLatLng())
                        .title(accTypeStr.get(BURGLAR) + "\nDate: " + report.getTimeStamp() + "\n" + geoLocateByLatLng(report.getLatLng()).toString())
                        .icon(burglarBitmap));
            } else if (report.getAccidentType() == MINOR_ACCIDENT) {
                mMap.addMarker(new MarkerOptions().position(report.getLatLng())
                        .title(accTypeStr.get(MINOR_ACCIDENT) + "\nDate: " + report.getTimeStamp() + "\n" + geoLocateByLatLng(report.getLatLng()).toString())
                        .icon(minorAccidentBitmap));
            } else if (report.getAccidentType() == SEVERE_ACCIDENT) {
                mMap.addMarker(new MarkerOptions().position(report.getLatLng())
                        .title(accTypeStr.get(SEVERE_ACCIDENT) + "\nDate: " + report.getTimeStamp() + "\n" + geoLocateByLatLng(report.getLatLng()).toString())
                        .icon(severeAccidentBitmap));
            } else {
                mMap.addMarker(new MarkerOptions().position(report.getLatLng())
                        .title(accTypeStr.get(CRIME) + "\nDate: " + report.getTimeStamp() + "\n" + geoLocateByLatLng(report.getLatLng()).toString())
                        .icon(crimeBitmap));
            }
        }
        Log.d(TAG,"AddMarkers");
    }

    public void loadNote() {
        db.collection("police_reports")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            reports.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                List<Double> group = (List<Double>) document.get("location");
                                String id = document.getId();
                                Double lat = Double.parseDouble(document.get("latitude").toString());
                                Double lng = Double.parseDouble(document.get("longtitude").toString());
                                String time = document.get("time_stamp").toString();
                                Integer type = Integer.parseInt(document.get("type").toString());
                                reports.add(new Report(id,new LatLng(lat,lng),time, type));

                                weightedLatLngs.add(new WeightedLatLng(new LatLng(lat,lng),type*WEIGHT_NUMBER));

//                                Log.d(TAG, "LoadNote: " + reports.size());
                            }
                        } else {
                            Log.d(TAG, "Error getting document: ", task.getException());

                        }
                        mMap.clear();
                        if(switchView.isChecked())
                            addMarker(reports);
                        else
                            addHeatMap(weightedLatLngs);
                        Log.d(TAG, "LoadNote-inloop: " + reports.size());
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        searchText = findViewById(R.id.input_search);
        switchView = findViewById(R.id.switchView);
        final ImageButton currLocButton = findViewById(R.id.currLocButton);

        final RelativeLayout reLay2 = findViewById(R.id.relativeLayout2);
        final ImageButton photoButton = findViewById(R.id.photoButton);

        switchView.setChecked(true);

        accTypeStr = new HashMap<Integer, String>();
        accTypeStr.put(FIGHTING,"Fighting");
        accTypeStr.put(THEFT,"Theft");
        accTypeStr.put(BURGLAR,"Burglar");
        accTypeStr.put(MINOR_ACCIDENT,"Minor Accident");
        accTypeStr.put(SEVERE_ACCIDENT,"Severe Accident");
        accTypeStr.put(CRIME,"Crime");

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
                    loadNote();

                } else {
                    reLay2.setVisibility(View.VISIBLE);
                    photoButton.setVisibility(View.INVISIBLE);
                    loadNote();
                }
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                startActivity(intent);
            }
        });

        currLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomToCurrentLocation(currentLocation);
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

    public Address geoLocateByLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> addressList = new ArrayList<>();

        try {
            addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocateByLatLng: IOException:" + e.getMessage());
        }

        if (addressList.size() > 0) {
            return addressList.get(0);
        }
        return null;
    }

    private void addHeatMap(ArrayList<WeightedLatLng> list) {
        // Create the gradient.
        int[] colors = {
//                Color.rgb(102, 225, 0), // green
//                Color.rgb(255, 0, 0)    // red
                Color.GREEN,    // green
                Color.YELLOW,    // yellow
                Color.RED              //red
        };

        float[] startPoints = {
                0.2f, 0.6f, 1f
        };

        Gradient gradient = new Gradient(colors, startPoints);

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        mProvider = new HeatmapTileProvider.Builder()
                .weightedData(list)
                .gradient(gradient)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
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

//    protected String getRandomString(int length_of_string) {
//        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
//        StringBuilder salt = new StringBuilder();
//        Random rnd = new Random();
//        while (salt.length() < length_of_string) { // length of the random string.
//            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
//            salt.append(SALTCHARS.charAt(index));
//        }
//        String saltStr = salt.toString();
//        return saltStr;
//    }
//
//    private String TIME_FORMAT_FOR_ID = "yyyyMMddHHmmZ";
//    public String parseTime(Date date, String format){
//        SimpleDateFormat output = new SimpleDateFormat(format);
//        return output.format(date);
//    }
//    CollectionReference policeReportRef = db.collection("police_reports");
//    public void setPoliceReport(String doc_name, LatLng location, Date time_stamp, int type) {
//        // [START set_document]
//        Map<String, Object> spot = new HashMap<>();
//        spot.put("latitude", location.latitude);
//        spot.put("longtitude", location.longitude);
//        spot.put("time_stamp", time_stamp);
//        spot.put("type", type);
//
//        policeReportRef.document(doc_name)
//                .set(spot)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error writing document", e);
//                    }
//                });
//    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        fightingBitmap = BitmapDescriptorFactory.fromResource(R.drawable.fighting);
        burglarBitmap = BitmapDescriptorFactory.fromResource(R.drawable.burglar);
        theftBitmap = BitmapDescriptorFactory.fromResource(R.drawable.theft);
        crimeBitmap = BitmapDescriptorFactory.fromResource(R.drawable.crime);
        minorAccidentBitmap = BitmapDescriptorFactory.fromResource(R.drawable.minor_accident);
        severeAccidentBitmap = BitmapDescriptorFactory.fromResource(R.drawable.severe_accident);
        myLocationIC = BitmapDescriptorFactory.fromResource(R.drawable.mylocation);

        ArrayList<LatLng> latLngs = new ArrayList<>();

        loadNote();
        Log.d(TAG, "LoadNote: " + reports.size());

        mMap.getUiSettings().setZoomControlsEnabled(true);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

//                if(switchView.isChecked())
//                    zoomToCurrentLocation(location);
                currentLocation = location;

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
                zoomToCurrentLocation(lastLocation);
            }
        }


//        latLngs.add(new LatLng(33.556242, -101.801492));
//        latLngs.add(new LatLng(33.541580, -101.900734));
//        latLngs.add(new LatLng(33.573734, -101.926059));
//        latLngs.add(new LatLng(33.605888, -101.870524));
//        latLngs.add(new LatLng(33.591225, -101.814989));
//        latLngs.add(new LatLng(33.533765, -101.840314));
//        latLngs.add(new LatLng(33.608716, -101.939556));
//        latLngs.add(new LatLng(33.551256, -101.884021));
//        latLngs.add(new LatLng(33.583410, -101.909346));
//        latLngs.add(new LatLng(33.568747, -101.853811));
//        latLngs.add(new LatLng(33.600901, -101.879136));
//        latLngs.add(new LatLng(33.543441, -101.823601));
//        latLngs.add(new LatLng(33.528779, -101.922843));
//        latLngs.add(new LatLng(33.560933, -101.793391));
//        latLngs.add(new LatLng(33.546270, -101.892633));
//        latLngs.add(new LatLng(33.578424, -101.837099));
//        latLngs.add(new LatLng(33.610578, -101.862423));
//        latLngs.add(new LatLng(33.595915, -101.806888));
//        latLngs.add(new LatLng(33.538455, -101.832213));
//        latLngs.add(new LatLng(33.570609, -101.931455));
//
//
//        for (int i = 0; i < latLngs.size(); i++) {
//            Date date = new Date();
//            if (i % 6 == FIGHTING) {
//                mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("Marker: Theft " + i).icon(fightingBitmap));
//                weightedLatLngs.add(new WeightedLatLng(latLngs.get(i),FIGHTING*WEIGHT_NUMBER));
////                setPoliceReport(getRandomString(10),latLngs.get(i),date,FIGHTING);
//            } else if (i % 6 == THEFT) {
//                mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("Marker: Fighting " + i).icon(theftBitmap));
//                weightedLatLngs.add(new WeightedLatLng(latLngs.get(i),THEFT*WEIGHT_NUMBER));
////                setPoliceReport(getRandomString(10),latLngs.get(i),date,THEFT);
//            } else if (i % 6 == BURGLAR) {
//                mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("Marker: Burglar " + i).icon(burglarBitmap));
//                weightedLatLngs.add(new WeightedLatLng(latLngs.get(i),BURGLAR *WEIGHT_NUMBER));
////                setPoliceReport(getRandomString(10),latLngs.get(i),date,BURGLAR);
//            } else if (i % 6 == MINOR_ACCIDENT) {
//                mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("Marker: Suspect " + i).icon(minorAccidentBitmap));
//                weightedLatLngs.add(new WeightedLatLng(latLngs.get(i),MINOR_ACCIDENT*WEIGHT_NUMBER));
////                setPoliceReport(getRandomString(10),latLngs.get(i),date,MINOR_ACCIDENT);
//            } else if (i % 6 == SEVERE_ACCIDENT) {
//                mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("Marker: minor Accident " + i).icon(severeAccidentBitmap));
//                weightedLatLngs.add(new WeightedLatLng(latLngs.get(i),SEVERE_ACCIDENT*WEIGHT_NUMBER));
////                setPoliceReport(getRandomString(10),latLngs.get(i),date,SEVERE_ACCIDENT);
//            } else {
//                mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("Marker: severe Accident " + i).icon(crimeBitmap));
//                weightedLatLngs.add(new WeightedLatLng(latLngs.get(i),CRIME*WEIGHT_NUMBER));
////                setPoliceReport(getRandomString(10),latLngs.get(i),date,CRIME);
//            }
//        }


//        mMap2.setMyLocationEnabled(true);

    }


}
