package com.capstone.safebeacon;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class Report {
    private String reportID;
    private LatLng latLng;
    private Date timeStamp;
    private Integer incidentType;

    public Report(){}

    public Report(String id, LatLng ltlg, Date time, int type){
        reportID = id;
        latLng = ltlg;
        timeStamp = time;
        incidentType = type;
    }

    public String getReportID() {
        return reportID;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public Integer getIncidentType() {
        return incidentType;
    }

    public String getStringDate() {
        return this.timeStamp.toString();
    }
}
