package com.capstone.safebeacon;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;


public class Incidents {
    public static class IncidentType{
        public int FIGHTING = 1;
        public int THEFT = 2;
        public int BURGLAR = 3;
        public int MINOR_ACCIDENT = 4;
        public int SEVERE_ACCIDENT = 5;
        public int CRIME = 6;
    }
    public static class Incident {
        private int type;
        private String name;
        private BitmapDescriptor icon;

        Incident(int type, String name, BitmapDescriptor icon){
            this.type = type;
            this.icon = icon;
            this.name = name;
        }

        Incident(int type){
            IncidentType incType = new IncidentType();
            if (type == incType.FIGHTING) {
                this.type = incType.FIGHTING;
                this.name = "Fighting";
                this.icon = BitmapDescriptorFactory.fromResource(R.drawable.fighting);
            } else if (type == incType.BURGLAR) {
                this.type = incType.BURGLAR;
                this.name = "Burglar";
                this.icon = BitmapDescriptorFactory.fromResource(R.drawable.burglar);
            } else if (type == incType.THEFT) {
                this.type = incType.THEFT;
                this.name = "Theft";
                this.icon = BitmapDescriptorFactory.fromResource(R.drawable.theft);
            } else if (type == incType.MINOR_ACCIDENT) {
                this.type = incType.MINOR_ACCIDENT;
                this.name = "Minor Accident";
                this.icon = BitmapDescriptorFactory.fromResource(R.drawable.minor_accident);
            } else if (type == incType.SEVERE_ACCIDENT) {
                this.type = incType.SEVERE_ACCIDENT;
                this.name = "Severe Accident";
                this.icon = BitmapDescriptorFactory.fromResource(R.drawable.severe_accident);
            } else {
                this.type = incType.CRIME;
                this.name = "Crime";
                this.icon = BitmapDescriptorFactory.fromResource(R.drawable.crime);
            }
        }

        Incident(String name){
            IncidentType incType = new IncidentType();
            this.name = name;
            if (name.equals("Fighting")) {
                this.type = incType.FIGHTING;
                this.icon = BitmapDescriptorFactory.fromResource(R.drawable.fighting);
            } else if (name.equals("Burglar")) {
                this.type = incType.BURGLAR;
                this.icon = BitmapDescriptorFactory.fromResource(R.drawable.burglar);
            } else if (name.equals("Theft")) {
                this.type = incType.THEFT;
                this.icon = BitmapDescriptorFactory.fromResource(R.drawable.theft);
            } else if (name.equals("Minor Accident")) {
                this.type = incType.MINOR_ACCIDENT;
                this.icon = BitmapDescriptorFactory.fromResource(R.drawable.minor_accident);
            } else if (name.equals("Severe Accident")) {
                this.type = incType.SEVERE_ACCIDENT;
                this.icon = BitmapDescriptorFactory.fromResource(R.drawable.severe_accident);
            } else {
                this.type = incType.CRIME;
                this.icon = BitmapDescriptorFactory.fromResource(R.drawable.crime);
            }
        }

        public int getType() {
            return type;
        }

        public BitmapDescriptor getIcon() {
            return icon;
        }

        public String getName() {
            return name;
        }
    }
}
