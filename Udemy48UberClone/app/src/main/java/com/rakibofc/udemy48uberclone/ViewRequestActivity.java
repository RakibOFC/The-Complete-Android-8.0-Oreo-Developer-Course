package com.rakibofc.udemy48uberclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class ViewRequestActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    LocationManager locationManager;
    LocationListener locationListener;
    Location lastKnownLocation;
    double driverLatitude;
    double driverLongitude;
    ArrayList<Double> riderLatitude;
    ArrayList<Double> riderLongitude;
    ArrayList<String> riderRequest;
    ArrayList<String> riderKey;
    ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.driver_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.logout:
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
                finish();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.i("Permission Result", requestCode + "");

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


                    driverLatitude = lastKnownLocation.getLatitude();
                    driverLongitude = lastKnownLocation.getLongitude();
                    // Log.i("After Wait, Latitude", lastKnownLocation.getLatitude() + ", Longitude: " + lastKnownLocation.getLongitude());
                }
            }
        }
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {

        Log.e("shouldShowReq...", permission);

        return super.shouldShowRequestPermissionRationale(permission);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);
        this.setTitle("View Request");

        mAuth = FirebaseAuth.getInstance();

        ListView listViewViewRequest = findViewById(R.id.listViewViewRequest);

        riderRequest = new ArrayList<>();
        riderLatitude = new ArrayList<>();
        riderLongitude = new ArrayList<>();
        riderKey = new ArrayList<>();

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, riderRequest);
        listViewViewRequest.setAdapter(arrayAdapter);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("anonymous");

        // Update driver Database Reference
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference updateDriverDBRef = FirebaseDatabase.getInstance().getReference().child("anonymous").child(currentUser);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                driverLatitude = location.getLatitude();
                driverLongitude = location.getLongitude();

                try {
                    updateDriverDBRef.child("driverLatLon").setValue(driverLatitude + "," + driverLongitude);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                arrayAdapter.clear();
                updateRequestList();
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
            // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1);

        } else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLocation != null) {

                try {

                    driverLatitude = lastKnownLocation.getLatitude();
                    driverLongitude = lastKnownLocation.getLongitude();
                    Log.e("Last Location, Latitude", lastKnownLocation.getLatitude() + ", Longitude" + lastKnownLocation.getLongitude());

                } catch (NullPointerException e) {

                    e.printStackTrace();
                    Log.e("Info", "Something went wrong");
                }
            } else {

                Log.e("Info", "No Location");
            }
        }

        // https://www.google.com/maps/dir/23.7901961,90.3764219/23.810407,90.3714629
        // https://www.google.com/maps/dir/23.81033166666667,90.41251666666668

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                try {
                    if (snapshot.child("riderOrDriver").getValue().equals("rider")) {

                        addValueInList(snapshot);
                    }

                } catch (Exception e) {

                    Log.e("Error", e.getMessage() + "");
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.child("latLan").getValue() == null) {

                    for (int i = 0; i < riderKey.size(); i++) {

                        if (Objects.equals(snapshot.getKey(), riderKey.get(i))) {

                            riderKey.remove(i);
                            riderLatitude.remove(i);
                            riderLongitude.remove(i);
                        }
                    }
                } else {

                    addValueInList(snapshot);
                }

                arrayAdapter.clear();
                updateRequestList();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.e("Info", "Removed");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("Info", "Moved");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Info", "Canceled");
            }
        });

        listViewViewRequest.setOnItemClickListener((parent, view, position, id) -> {

            // Click in request
            Intent viewRequestIntent = new Intent(ViewRequestActivity.this, DriverLocationActivity.class);

            String driverKey = MainActivity.sharedPreferences.getString("currentUser", "");

            viewRequestIntent.putExtra("driverKey", driverKey);
            viewRequestIntent.putExtra("riderKey", riderKey.get(position));
            viewRequestIntent.putExtra("driverLatitude", driverLatitude);
            viewRequestIntent.putExtra("driverLongitude", driverLongitude);
            viewRequestIntent.putExtra("riderLatitude", riderLatitude.get(position));
            viewRequestIntent.putExtra("riderLongitude", riderLongitude.get(position));

            startActivity(viewRequestIntent);
        });
    }

    private void addValueInList(DataSnapshot snapshot) {

        String latLanStr = snapshot.child("latLan").getValue().toString();
        String[] latLanArr = latLanStr.split(",");

        double latitude = Double.parseDouble(latLanArr[0]);
        double longitude = Double.parseDouble(latLanArr[1]);

        riderKey.add(snapshot.getKey());
        riderLatitude.add(latitude);
        riderLongitude.add(longitude);

        float[] results = new float[1];
        Location.distanceBetween(driverLatitude, driverLongitude, latitude, longitude, results);
        riderRequest.add(results[0] / 1000 + " KM");
    }

    private void updateRequestList() {

        for (int i = 0; i < riderLatitude.size(); i++) {

            float[] results = new float[1];
            Location.distanceBetween(driverLatitude, driverLongitude, riderLatitude.get(i), riderLongitude.get(i), results);
            riderRequest.add(results[0] / 1000 + " KM");
        }
        arrayAdapter.notifyDataSetChanged();
    }
}