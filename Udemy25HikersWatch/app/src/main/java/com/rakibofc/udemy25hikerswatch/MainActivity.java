package com.rakibofc.udemy25hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView latitudeTextView, longitudeTextView, accuracyTextView, altitudeTextView, addressTextView;

    String addressLine = "";

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        accuracyTextView  =findViewById(R.id.accuracyTextView);
        altitudeTextView = findViewById(R.id.altitudeTextView);
        addressTextView = findViewById(R.id.addressTextView);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                Toast.makeText(MainActivity.this, "Keep going...", Toast.LENGTH_SHORT).show();
                
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                latitudeTextView.setText("Latitude: " + location.getLatitude());
                longitudeTextView.setText("Longitude: " + location.getLongitude());
                accuracyTextView.setText("Accuracy: " + location.getAccuracy());
                altitudeTextView.setText("Altitude: " + location.getAltitude());

                try {

                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    Log.i("Address", addressList.toString());

                    if (addressList.get(0).getAddressLine(0) != null) {

                        addressLine = addressList.get(0).getAddressLine(0);
                    }

                    addressTextView.setText("Address:\n" + addressLine);

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
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            latitudeTextView.setText("Latitude: " + lastKnownLocation.getLatitude());
            longitudeTextView.setText("Longitude: " + lastKnownLocation.getLongitude());
            accuracyTextView.setText("Accuracy: " + lastKnownLocation.getAccuracy());
            altitudeTextView.setText("Altitude: " + lastKnownLocation.getAltitude());
        }

    }
}