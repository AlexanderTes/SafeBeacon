package com.capstone.safebeacon;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection("reports");
    private static final String TAG = "TESTING";
//    private static final int LENGTH_OF_RANDOM_STRING = 20;

    private static final int LENGTH_OF_REPORT_ID = 20;
    private String TIME_FORMAT_FOR_ID = "yyyyMMddHHmmZ";
    private String TIME_FORMAT_FOR_DATE = "MM/dd/yyyy HH:mm z";

    private final int REQUEST_IMAGE_CAPTURE_CODE = 100;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private int atImage = 1;
    private LatLng myLatLng;
    private String cityName;
    private String userId;

    int accidentType;

    public String parseTime(Date date, String format){
        SimpleDateFormat output = new SimpleDateFormat(format);

        return output.format(date);
    }

    public void setReport(String doc_name, String id, String comment, String city, LatLng location, String photo, Date time_stamp, int type) {
        // [START set_document]
        Map<String, Object> spot = new HashMap<>();
        spot.put("userId",id);
        spot.put("comment", comment);
        spot.put("city", city);
        spot.put("latitude", location.latitude);
        spot.put("longitude", location.longitude);
        spot.put("photo", photo);
        spot.put("time_stamp", time_stamp);
        spot.put("type", type);

        collectionReference.document(doc_name)
                .set(spot)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
        Toast.makeText(this, "Your report is submitted", Toast.LENGTH_SHORT).show();
        // [END set_document]

//        Map<String, Object> data = new HashMap<>();

        // [START set_with_id]
        // db.collection("cities").document("new-city-id").set(data);
        // [END set_with_id]
    }

//    public void getAllUsers() {
//        // [START get_all_users]
//        db.collection("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//        // [END get_all_users]
//    }

    protected String getRandomString(int length_of_string) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length_of_string) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Spinner spinner = findViewById(R.id.spinner);
        Button submitButton = findViewById(R.id.submitButton);
        Button cancelButton = findViewById(R.id.cancelButton);
        final EditText comment = findViewById(R.id.comment);
        final TextView locationText = findViewById(R.id.location);
        final TextView userIdTextView = findViewById(R.id.userId);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);

        //Open camera Intent and get result
        Intent photoCaptureIntent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(photoCaptureIntent1, REQUEST_IMAGE_CAPTURE_CODE);

        userId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        //add accident Types to spinner
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.accidentTypes, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        userIdTextView.setText(userId);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Geocoder geocoder = new Geocoder(MainActivity.this);
                List<Address> addressList = new ArrayList<>();

                try {
                    addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    Log.e(TAG, "geoLocate: IOException:" + e.getMessage());
                }

                if (addressList.size() > 0) {
                    Address address = addressList.get(0);
                    cityName = address.getLocality();
                    String loc = address.getAddressLine(0);
                    locationText.setText(loc);
                    myLatLng = new LatLng(address.getLatitude(),address.getLongitude());
                    Log.d(TAG, "ADDRESS: " + loc);

                }
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

    //        int requestCode = 1;
    //
    //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    //            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA)  != PackageManager.PERMISSION_GRANTED) {
    //                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA},requestCode);
    //            }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                setReport(parseTime(date,TIME_FORMAT_FOR_ID),userId, comment.getText().toString(),cityName,myLatLng,"photo",date,accidentType);

                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);

                startActivity(intent);

            }
        });

        //cancel button onClickListener
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(this.REQUEST_IMAGE_CAPTURE_CODE == requestCode && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            if(atImage == 1){
                imageView1.setImageBitmap(bitmap);
            } else if (atImage == 2){
                imageView2.setImageBitmap(bitmap);
            } else {
                imageView3.setImageBitmap(bitmap);
            }
        }

    }

    public static String getFirstChar(String s) {
        return (s == null || s.length() == 0)
                ? null
                : (s.substring(0,1));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        accidentType = Integer.parseInt(getFirstChar(adapterView.getItemAtPosition(i).toString()));

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    void imageViewClick(View v) {
        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(photoCaptureIntent, REQUEST_IMAGE_CAPTURE_CODE);
        switch (v.getId()){
            case R.id.imageView1:
                //Set to change image
                atImage = 1;
                break;
            case R.id.imageView2:
                //Set to change image
                atImage = 2;
                break;
            case R.id.imageView3:
                //Set to change image
                atImage = 3;
                break;
        }
    }


}
