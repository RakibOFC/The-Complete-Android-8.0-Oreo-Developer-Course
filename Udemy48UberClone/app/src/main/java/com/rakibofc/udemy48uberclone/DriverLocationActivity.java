package com.rakibofc.udemy48uberclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rakibofc.udemy48uberclone.databinding.ActivityDriverLocationBinding;

public class DriverLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityDriverLocationBinding binding;
    double driverLatitude;
    double driverLongitude;
    double riderLatitude;
    double riderLongitude;
    String driverKey;
    String riderKey;
    Marker marker;
    Button buttonAcceptRequest;

    public void acceptRequest(View view) {

        buttonAcceptRequest.setEnabled(false);

        FirebaseDatabase.getInstance().getReference().child("anonymous").child(riderKey).child("driver").setValue(driverKey);

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + driverLatitude + "," + driverLongitude + "&daddr=" + riderLatitude + "," + riderLongitude));
        startActivity(intent);

        startActivity(new Intent(getApplicationContext(), ViewRequestActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDriverLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        buttonAcceptRequest = findViewById(R.id.buttonAcceptRequest);

        Intent viewRequestIntent = getIntent();

        driverKey = viewRequestIntent.getStringExtra("driverKey");
        riderKey = viewRequestIntent.getStringExtra("riderKey");

        driverLatitude = viewRequestIntent.getDoubleExtra("driverLatitude", 0.0);
        driverLongitude = viewRequestIntent.getDoubleExtra("driverLongitude", 0.0);
        riderLatitude = viewRequestIntent.getDoubleExtra("riderLatitude", 0.0);
        riderLongitude = viewRequestIntent.getDoubleExtra("riderLongitude", 0.0);

        LatLng riderLatLon = new LatLng(riderLatitude, riderLongitude);
        mMap.addMarker(new MarkerOptions().position(riderLatLon).title("Request Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(riderLatLon));

        LatLng driverLatLon = new LatLng(driverLatitude, driverLongitude);
        marker = mMap.addMarker(new MarkerOptions().position(driverLatLon).position(driverLatLon).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(driverLatLon));

        // longitude and latitude
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("anonymous");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                try {
                    if (snapshot.child("riderOrDriver").getValue().equals("driver")) {

                        String latLanStr = snapshot.child("driverLatLon").getValue().toString();
                        String[] latLanArr = latLanStr.split(",");

                        driverLatitude = Double.parseDouble(latLanArr[0]);
                        driverLongitude = Double.parseDouble(latLanArr[1]);

                        LatLng driverLatLon = new LatLng(driverLatitude, driverLongitude);
                        marker.setPosition(driverLatLon);
                    }

                } catch (Exception e) {

                    Log.e("Error", e.getMessage());
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}