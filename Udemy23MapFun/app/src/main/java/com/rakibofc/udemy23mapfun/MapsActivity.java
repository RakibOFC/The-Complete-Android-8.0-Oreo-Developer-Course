package com.rakibofc.udemy23mapfun;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rakibofc.udemy23mapfun.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

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

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // Add a marker in Dhaka and move the camera
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng dhaka = new LatLng(23.790201270182013, 90.37639717961262);
        mMap.addMarker(new MarkerOptions().position(dhaka).title("East Shewrapara").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dhaka)); */

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {

                Log.i("Location", location.toString());

                // Toast.makeText(getApplicationContext(), "Boom!", Toast.LENGTH_SHORT).show();

                mMap.clear();
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Location"));

                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {

                    List<Address> addressList  = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (addressList != null && addressList.size() > 0) {

                        String address = "";

                        if (addressList.get(0).getLocale() != null) {

                            address += addressList.get(0).getLocale() + " ";
                        }

                        if (addressList.get(0).getPostalCode() != null) {

                            address += addressList.get(0).getPostalCode() + " ";
                        }
                        if (addressList.get(0).getAdminArea() != null) {

                            address += addressList.get(0).getAdminArea() + " ";
                        }

                        if (addressList.get(0).getCountryName() != null) {

                            address += addressList.get(0).getCountryName();

                        }

                        Toast.makeText(MapsActivity.this, address, Toast.LENGTH_SHORT).show();
                        Log.i("Address", address);


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
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            mMap.clear();
            LatLng currentLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        }
    }
}