package com.rakibofc.udemy48uberclone;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakibofc.udemy48uberclone.databinding.ActivityRiderBinding;

public class RiderActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseAuth mAuth;
    private ActivityRiderBinding binding;
    LocationManager locationManager;
    LocationListener locationListener;
    double riderLatitude, riderLongitude;
    Location lastKnownLocation;
    Button buttonCallAnUber;
    boolean isCallAnUberActive = false;
    DatabaseReference databaseReference;
    TextView textViewDriverLocation;
    Marker riderMarker, driverMarker;
    boolean isFirstTime = true;

    public void buttonLogout(View view) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        MainActivity.sharedPreferences.edit().putString("userType", "").apply();

        FirebaseDatabase.getInstance().getReference().child("anonymous").child(mAuth.getCurrentUser().getUid()).removeValue();

        mAuth.signOut();

        user.delete().addOnCompleteListener(task -> {

            if (!task.isSuccessful()) {

                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void CallAnUber(View view) {

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // longitude and latitude
        databaseReference = FirebaseDatabase.getInstance().getReference().child("anonymous").child(currentUser);

        if (!isCallAnUberActive) {

            isCallAnUberActive = true;
            buttonCallAnUber.setText("Cancel Uber");

            try {

                databaseReference.child("latLan").setValue(lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude());

            } catch (Exception e) {

                isCallAnUberActive = false;
                buttonCallAnUber.setText("Call An Uber");
                Toast.makeText(getApplicationContext(), "Please restart the app", Toast.LENGTH_SHORT).show();
            }

        } else {

            isCallAnUberActive = false;
            buttonCallAnUber.setText("Call An Uber");

            databaseReference.child("latLan").removeValue();
        }
    }

    public void updateMap(Location location) {

        riderLatitude = location.getLatitude();
        riderLongitude = location.getLongitude();

        LatLng riderLocation = new LatLng(riderLatitude, riderLongitude);
        riderMarker.setPosition(riderLocation);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.i("Permission Result", requestCode + "");

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                    lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    Log.i("After Wait, Latitude", lastKnownLocation.getLatitude() + ", Longitude: " + lastKnownLocation.getLongitude());
                    updateMap(lastKnownLocation);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRiderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mAuth = FirebaseAuth.getInstance();
        textViewDriverLocation = findViewById(R.id.textViewDriverLocation);

        // child(mAuth.getCurrentUser().getUid()).child("driver")
        FirebaseDatabase.getInstance().getReference().child("anonymous").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {
                    String driverKey = snapshot.child(mAuth.getCurrentUser().getUid()).child("driver").getValue().toString();

                    if (!driverKey.isEmpty()) {

                        buttonCallAnUber.setEnabled(false);
                        textViewDriverLocation.setVisibility(View.VISIBLE);

                        String driverLatLng = snapshot.child(driverKey).child("driverLatLon").getValue().toString();

                        String[] latLanArr = driverLatLng.split(",");

                        double driverLatitude = Double.parseDouble(latLanArr[0]);
                        double driverLongitude = Double.parseDouble(latLanArr[1]);

                        LatLng driverLatLon = new LatLng(driverLatitude, driverLongitude);

                        if (isFirstTime) {

                            isFirstTime = false;
                            driverMarker = mMap.addMarker(new MarkerOptions().position(driverLatLon).title("Driver Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                        }
                        driverMarker.setPosition(driverLatLon);

                        float[] results = new float[1];
                        Location.distanceBetween(driverLatitude, driverLongitude, riderLatitude, riderLongitude, results);
                        textViewDriverLocation.setText("The driver is " + results[0] / 1000 + " km away");

                        if (results[0] / 1000 < 0.04) {

                            /*mMap.clear();

                            isFirstTime = true;
                            buttonCallAnUber.setEnabled(true);
                            textViewDriverLocation.setVisibility(View.INVISIBLE);

                            isCallAnUberActive = false;
                            buttonCallAnUber.setText("Call An Uber"); */

                            Log.e("LatLan", databaseReference.child("latLan").toString());

                            databaseReference.child("latLan").removeValue();

                            startActivity(new Intent(getApplicationContext(), ViewRequestActivity.class));
                        }
                    }

                } catch (NullPointerException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buttonCallAnUber = findViewById(R.id.buttonCallAnUber);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                updateMap(location);
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
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLocation != null) {

                LatLng riderLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                riderMarker = mMap.addMarker(new MarkerOptions().position(riderLatLng).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(riderLatLng));

                try {
                    updateMap(lastKnownLocation);

                } catch (NullPointerException e) {

                    e.printStackTrace();
                    buttonCallAnUber.setEnabled(false);
                }
            } else {

                buttonCallAnUber.setEnabled(false);
            }
        }
    }
}