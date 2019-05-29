package harkor.weather.Services;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import harkor.weather.Interfaces.GpsResultInterface;

public class MyLocationListener implements LocationListener {
    Context context;
    GpsResultInterface gpsResultInterface;

    public MyLocationListener(Context context, GpsResultInterface gpsResultInterface) {
        this.context = context;
        this.gpsResultInterface = gpsResultInterface;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("check", "localisation search");
        String cityName;
        List<Address> addresses;
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        Log.d("weather-test Latitude", "" + location.getLatitude());
        Log.d("weather-test Longitude", "" + location.getLongitude());
        try {
            addresses = gcd.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            if (addresses.size() > 0) {
                cityName = addresses.get(0).getLocality();
                Log.d("weather-test",cityName+" "+location.getLongitude()+" "+location.getLatitude());
                gpsResultInterface.setMainLocation(cityName,location.getLongitude(),location.getLatitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
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
