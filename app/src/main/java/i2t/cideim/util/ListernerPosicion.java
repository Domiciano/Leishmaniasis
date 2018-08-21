package i2t.cideim.util;

import android.icu.text.NumberFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;

import java.text.DecimalFormat;

import i2t.cideim.leishmaniasis.CreatePatientActivity;

/**
 * Created by Domiciano on 13/02/2018.
 */
public class ListernerPosicion implements LocationListener {


    CreatePatientActivity root;


    public ListernerPosicion(CreatePatientActivity root){
        this.root = root;
    }

    boolean firstTime = true;

    @Override
    public void onLocationChanged(Location location) {
            java.text.NumberFormat nf = new DecimalFormat("#0.000");
            root.tv_location.setText("Coordenada: \nLat: " + nf.format(location.getLatitude()) + ", Lng: " + nf.format(location.getLongitude()) + "\n"
                    + "Exacto a " + location.getAccuracy() + " metros");
            firstTime = false;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if(status == LocationProvider.OUT_OF_SERVICE);

        if(status == LocationProvider.AVAILABLE);

        if(status == LocationProvider.TEMPORARILY_UNAVAILABLE);

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
