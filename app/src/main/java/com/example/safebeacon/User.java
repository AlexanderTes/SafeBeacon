package com.example.safebeacon;


import com.example.safebeacon.MainActivity;
import java.util.UUID;


public class User implements userInterface, reportInterface {
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
        //String uniqueID = UUID.randomUUID().toString();
        return UUID.randomUUID().toString();
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
