package com.example.mymaps;

import android.location.Location;
import android.location.LocationListener;
import androidx.annotation.NonNull;

public class MyPosition implements LocationListener {

    MainActivity mainActivity;
    public static double latitud, longitud, altitud;
    public static boolean statusGPS;

    public MainActivity getMainActivity(){
        return mainActivity;
    }
    public void setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {

        latitud = location.getLatitude();
        longitud = location.getLongitude();
        altitud = location.getAltitude();

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        statusGPS = true;
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        statusGPS = false;
    }
}
