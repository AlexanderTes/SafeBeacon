package com.example.safebeacon;

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
