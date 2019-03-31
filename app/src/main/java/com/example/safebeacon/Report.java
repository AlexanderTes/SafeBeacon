package com.example.safebeacon;

import java.util.Calendar;
import java.util.Date;

public class Report extends Location implements reportInterface {
    private User user;
    private Accident accident;
    private Location location;
    private String reportId;
    private String comment;
    //private Date timeStamp;

    Report() {
        this.user = null;
        this.accident = null;
        this.location = null;
        this.reportId = "";
    }

    public Date getTimeStamp() {
        return Calendar.getInstance().getTime();
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }

    public void setAccident(Accident accident) {
        this.accident = accident;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setReportId() {

        //this.reportId = ;
    }

    public Accident getAccident() {
        return accident;
    }

    public Location getLocation() {
        return location;
    }

    public String getReportId() {
        return reportId;
    }

    public void report() {
        //
    }
    public void takePhotos() {
        //
    }
}
