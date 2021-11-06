package com.example.copyversion;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class SortPlacesSelling implements Comparator<SellerInfo> {

    private double currentLongitude,currentLatitude;

    public SortPlacesSelling(double currentLatitude,double currentLongitude){
        this.currentLatitude=currentLatitude;
        this.currentLongitude=currentLongitude;
    }


    public double distance(double fromLat, double fromLon, double toLat, double toLon) {
        double radius = 6371;   // approximate Earth radius, *in meters*
        double deltaLat = toLat-fromLat;
        double deltaLon = toLon-fromLon;
        double angle = 2 * Math.asin( Math.sqrt(
                Math.pow(Math.sin(deltaLat/2), 2) +
                        Math.cos(fromLat) * Math.cos(toLat) *
                                Math.pow(Math.sin(deltaLon/2), 2) ) );

        return radius * angle;
    }



    @Override
    public int compare(SellerInfo o1, SellerInfo o2) {
        double lat1 = o1.getLatitude();
        double lon1 = o1.getLongitude();
        double lat2 = o2.getLatitude();
        double lon2 = o2.getLongitude();

        double distanceToPlace1 = distance(currentLatitude, currentLongitude, lat1, lon1);
        double distanceToPlace2 = distance(currentLatitude, currentLongitude, lat2, lon2);
        Log.v("fg",""+ ((int)(distanceToPlace1- distanceToPlace2)));
        return (int)(distanceToPlace1- distanceToPlace2);


    }
}