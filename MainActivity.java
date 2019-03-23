package com.example.safebeacon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public class User implements userInterface, reportInterface{
        private String userId;
        private String name;
        private String phone;

        protected void setName(String str) {
            this.name = str;
        }
        protected void setPhone(String phone) {
            this.phone = phone;
        }
        protected String getName() {
            return this.name;
        }
        protected String getPhone() {
            return this.phone;
        }
        protected String generateUserId() {
            String uniqueID = UUID.randomUUID().toString();
            return uniqueID;
        }
        public User getUser(){
            User user = new User();
            //fill out user information here
            return user;
        }
        public Report getReport(){
            Report report = new Report();
            //
            return report;
        }
        public void openReportInterface() {

        }
        public void openBluetoothInterface() {

        }
        public void report(){

        }
        public void takePhotos(){

        }
    }

    public class Accident implements reportInterface{
        private String name;
        private int type;

        public void report(){

        }
        public void takePhotos(){

        }
    }

    public class Location implements reportInterface {
        private float longitude;
        private float latitude;

        public void report() {

        }
        public void takePhotos(){

        }
    }

    public class Report extends Location implements reportInterface {
        private User user;
        private Accident accident;
        private Location location;

        public void report() {
            //
        }
        public void takePhotos() {
            //
        }
    }

    public class Emergencies extends Location implements userInterface {
        private String name;
        private int type;
        private Location location;

        public User getUser(){
            User user = new User();
            //fill out user information here
            return user;
        }
        public Report getReport(){
            Report report = new Report();
            //
            return report;
        }
        public void openReportInterface() {

        }
        public void openBluetoothInterface() {

        }
    }

    interface userInterface extends googleMap {
        User getUser();
        Report getReport();
        void openReportInterface();
        void openBluetoothInterface();

    }
    interface bluetoothDetection extends Hardware {

        String BTaddress = "";

        String getAddress();
        void setAddress(String address);
    }

    interface reportInterface {
        void report();
        void takePhotos();
    }

    interface googleMap {
        //
    }
    interface Hardware {
        //
    }
}
