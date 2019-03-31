package com.example.safebeacon;

public class Report extends Location implements reportInterface {
    private User user;
    private Accident accident;
    private Location location;
    private String reportId;
    private String comment;
    private String timeStamp;

    Report() {
        this.user = null;
        this.accident = null;
        this.location = null;
        this.reportId = "";
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
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
