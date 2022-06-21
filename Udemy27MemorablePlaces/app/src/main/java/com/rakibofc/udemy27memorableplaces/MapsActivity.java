package com.rakibofc.udemy27memorableplaces;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rakibofc.udemy27memorableplaces.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    LocationManager locationManager;
    LocationListener locationListener;

    public  void centerMapOnLocation(Location location, String title) {

        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
    }

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

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);

        Intent intent = getIntent();

        if (intent.getIntExtra("placeNumber", 0) == 0 ) {


            locationListener = new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {

                    centerMapOnLocation(location, "Your Location");

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

                centerMapOnLocation(lastKnownLocation, "Your Last Location");
            }

        } else {

            String tempMessage = "Place Size: " + MainActivity.places.size() + "\nLocation Size: " + MainActivity.location.size();
            Log.i("Info", tempMessage);

            Location placeLocation = new Location(LocationManager.GPS_PROVIDER);
            placeLocation.setLatitude(MainActivity.location.get(intent.getIntExtra("placeNumber", 0)).latitude);
            placeLocation.setLongitude(MainActivity.location.get(intent.getIntExtra("placeNumber", 0)).longitude);

            centerMapOnLocation(placeLocation, MainActivity.places.get(intent.getIntExtra("placeNumber",0)));
        }
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String address = "";

        try {

            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (addressList != null && addressList.size() > 0) {

                if (addressList.get(0).getThoroughfare() != null) {

                    if (addressList.get(0).getSubThoroughfare() != null) {

                        address += addressList.get(0).getSubThoroughfare() + " ";

                    }
                    address += addressList.get(0).getThoroughfare() + " ";
                }
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
        if (address.equals("")){

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm: yyyy-MM-dd");

            address += dateFormat.format(new Date());
        }

        mMap.addMarker(new MarkerOptions().position(latLng).title(address));

        MainActivity.places.add(address);
        MainActivity.arrayAdapter.notifyDataSetChanged();

        MainActivity.location.add(latLng);


        ArrayList<String> latitudes = new ArrayList<>();
        ArrayList<String> longitudes = new ArrayList<>();

        try {


            for (LatLng coord : MainActivity.location) {

                latitudes.add(Double.toString(coord.latitude));
                longitudes.add(Double.toString(coord.longitude));

            }

            MainActivity.sharedPreferences.edit().putString("places", ObjectSerializer.serialize(MainActivity.places)).apply();
            MainActivity.sharedPreferences.edit().putString("latitudes", ObjectSerializer.serialize(latitudes)).apply();
            MainActivity.sharedPreferences.edit().putString("longitudes", ObjectSerializer.serialize(longitudes)).apply();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Location Saved!", Toast.LENGTH_SHORT).show();

    }
}