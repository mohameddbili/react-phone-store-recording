package com.example.mygps;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.SettingInjectorService;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GPSLocationTracker extends Service implements LocationListener {

    //Déclaration des variables et constantes
    private final Context context;
    boolean isGPSEnable = false;
    boolean canGetLocation = false;



    boolean isNetworkEnabled = false;
    Location location;
    double latitude;
    double longitude;
    private static final long min_distance_change_for_updates = 10;
    private static final long min_time_bw_updates = 1000*60*1;
    protected LocationManager locationManager;

    //Constructeur
    public GPSLocationTracker(Context context)
    {
        this.context = context;
        getLocation();
    }

    private Location getLocation()
    {
        try
        {
            locationManager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGPSEnable && !isNetworkEnabled)
            {

            }
            else
            {
                this.canGetLocation = true;
                if(isNetworkEnabled)
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,min_time_bw_updates,min_distance_change_for_updates,this);
                    if(locationManager !=null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location  !=null)
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                if(isGPSEnable)
                {
                    if(location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, min_time_bw_updates, min_distance_change_for_updates, this);
                        if (locationManager != null)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if(location !=null)
                            {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }
        catch(Exception ex)
        {
            ex.getStackTrace();
        }
        return location;
    }

    public void stopUsingGPS()
    {
        if(locationManager !=null)
        {
            locationManager.removeUpdates(GPSLocationTracker.this);
        }
    }
    public boolean isCanGetLocation() {
        return canGetLocation;
    }

    public double getLatitude() {
        if(location != null)
        {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if(location !=null)
        {
            longitude = location.getLongitude();
        }
        return longitude;
    }

public void showSettingsAlert()
{
    AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
    alertdialogbuilder.setTitle("Configuration du GPS");
    alertdialogbuilder.setMessage("Le GPS n'est pas actif. Pour l'activer voulez-allez au menu de configuration? ");
    alertdialogbuilder.setPositiveButton("Configuration", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent (Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
        }
    });
    alertdialogbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    });
    alertdialogbuilder.show();
}


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}//End of class GPSLocationTracker
