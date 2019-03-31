package com.example.safebeacon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.Calendar;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button button;
    EditText location, comment;
    Spinner spinner;
    String accidentType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        location = findViewById(R.id.location);
        spinner = findViewById(R.id.spinner);
        comment = findViewById(R.id.comment);
        final Report report = new Report();
        final User user = new User();
        final Accident accident = new Accident();

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.accidentTypes,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report.getTimeStamp();
                report.setComment(comment.getText().toString());
                String id = user.generateUserId();
                String[] arr = accidentType.split("-");
                accident.setType(Integer.parseInt(arr[0]));
                accident.setName(arr[1]);
                report.setUser(user);
                report.setAccident(accident);
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        accidentType = parent.getItemAtPosition(position).toString();
        //Toast.makeText(this, accidentType, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
