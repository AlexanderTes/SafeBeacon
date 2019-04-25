package com.capstone.safebeacon;

import com.google.android.gms.maps.model.LatLng;

public class Report {
    private String reportID;
    private LatLng latLng;
    private String timeStamp;
    private Integer accidentType;

    public Report(){}

    public Report(String id, LatLng ltlg, String time, int type){
        reportID = id;
        latLng = ltlg;
        timeStamp = time;
        accidentType = type;
    }

    public String getReportID() {
        return reportID;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public Integer getAccidentType() {
        return accidentType;
    }

}
