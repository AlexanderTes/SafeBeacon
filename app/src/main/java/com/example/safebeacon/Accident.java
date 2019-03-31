package com.example.safebeacon;

public class Accident implements reportInterface {
    private String name;
    private int type;

    Accident() {
        name = "";
        type = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void report(){

    }
    public void takePhotos(){

    }
}
