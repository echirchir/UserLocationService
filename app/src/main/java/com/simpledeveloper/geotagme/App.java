package com.simpledeveloper.geotagme;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;


public class App extends Application {

    private static double mLatitude;
    private static double mLongitude;
    private LocationManager locationManager;
    private LocationListener listener;

    @Override
    public void onCreate(){

        super.onCreate();

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
    }

    public void acquireCustomerCoordinates(){

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager
                        .PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){

            listener = new GpsListener();

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);

        }else {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                listener = new GpsListener();

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);

            }
        }

    }

    private class GpsListener implements LocationListener {

        public void onLocationChanged(Location location) {
            if (location != null) {

                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager
                                .PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();

                String locationName = getLocationAddress(getApplicationContext(), mLatitude, mLongitude);

                EventBus.getDefault().post(new UserLocationAcquiredEvent(true, mLatitude, mLongitude));

                try {
                    if (listener != null)
                        locationManager.removeUpdates(listener);
                } catch (Exception e) { e.getMessage(); }

                locationManager = null;

            }else {
                EventBus.getDefault().post(new UserLocationAcquiredEvent(true, 0.0, 0.0));
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    private String getLocationAddress(Context context, double latitude, double longitude){

        String location = "";

        Geocoder gcd = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0){
                location = addresses.get(0).getFeatureName();
                String locationAddress = addresses.get(0).getLocality();
            }

        } catch (IOException e) {
            e.getMessage();
        }
        return location;
    }
}
