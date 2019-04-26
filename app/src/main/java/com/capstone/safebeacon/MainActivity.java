package com.capstone.safebeacon;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "TESTING";
    private static final int LENGTH_OF_RANDOM_STRING = 20;

    private final int REQUEST_CODE = 20;
    private ImageView imageView1;
    private LatLng myLatLng;

    int accidentType;

    public void setReport(String doc_name, String comment, LatLng location, String photo, Date time_stamp, int type) {
        // [START set_document]
        Map<String, Object> city = new HashMap<>();
        city.put("comment", comment);
        city.put("location", location);
        city.put("photo", photo);
        city.put("time_stamp", time_stamp);
        city.put("type", type);

        db.collection("reports").document(doc_name)
                .set(city)
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

    public void setPreference(String doc_name, int alert_level, int alert_radius, String name, String phone_num) {
        // [START set_document]
        Map<String, Object> city = new HashMap<>();
        city.put("alert_level", alert_level);
        city.put("alert_radius", alert_radius);
        city.put("name", name);
        city.put("phone_num", phone_num);

        db.collection("user_preferences").document(doc_name)
                .set(city)
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
        // [END set_document]

//        Map<String, Object> data = new HashMap<>();

        // [START set_with_id]
        // db.collection("cities").document("new-city-id").set(data);
        // [END set_with_id]
    }

    public void getAllUsers() {
        // [START get_all_users]
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        // [END get_all_users]
    }

    public void deleteDocument(String var1) {
        // [START delete_document]
        db.collection("users").document(var1)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        // [END delete_document]
    }

    protected String getRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < LENGTH_OF_RANDOM_STRING) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Spinner spinner = findViewById(R.id.spinner);
        Button submitButton = findViewById(R.id.submitButton);
        Button cancelButton = findViewById(R.id.cancelButton);
        final EditText comment = findViewById(R.id.comment);
        final TextView locationText = findViewById(R.id.location);
        imageView1 = findViewById(R.id.imageView1);

        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(photoCaptureIntent, REQUEST_CODE);

        //add accident Types to spinner
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.accidentTypes, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

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

        //String doc_name, String comment, LatLng location, String photo, String time_stamp, int type
//        setReport(getRandomString(), "hello",new LatLng(122,-231),"photo",new Date(),2);
        //submit button onClickListener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReport(getRandomString(), comment.getText().toString(),myLatLng,"photo",new Date(),accidentType);

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

        if(this.REQUEST_CODE == requestCode && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            imageView1.setImageBitmap(bitmap);
            Log.d(TAG, "IMAGE VIEW HERE");
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
}
