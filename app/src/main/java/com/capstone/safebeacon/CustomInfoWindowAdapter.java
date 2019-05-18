package com.capstone.safebeacon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;
    ArrayList<Report> reports;

    public CustomInfoWindowAdapter(Context mContext, ArrayList<Report> reports) {
        this.mContext = mContext;
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.info_window,null);
        this.reports = reports;
    }

    private void renderWindowText(Marker marker, View view){

        String title = marker.getTitle();
        TextView tvTitle = view.findViewById(R.id.title);

        if(!title.equals("")){
            tvTitle.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView tvSnippet = view.findViewById(R.id.snippet);

        if(!snippet.equals("")){
            tvSnippet.setText(snippet);
        }

        // Get image
        Incidents.Incident incident = new Incidents.Incident(title);
        Double lat = marker.getPosition().latitude;
        Double lng = marker.getPosition().longitude;
        String downloadImageString="";
        for (int i = 0; i < reports.size(); i++){
            if (reports.get(i).getIncidentType() == incident.getType())
                if (reports.get(i).getLatLng().latitude == lat)
                    if(reports.get(i).getLatLng().longitude == lng)
                        downloadImageString = reports.get(i).getReportID();
        }
        if(!downloadImageString.equals(""))
            downloadImage(downloadImageString.concat("/image1"),view);
    }



    public Bitmap downloadImage(String name,View view) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://safebeacon-2019.appspot.com").child(name);

        final Bitmap[] bitmap = {null};
        final ImageView imageView = view.findViewById(R.id.markerImageView);

        final long ONE_MEGABYTE = 1024 * 1024;

        //download file as a byte array
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
            }
        });
        return bitmap[0];
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker,mWindow);
        return mWindow;
    }
}
