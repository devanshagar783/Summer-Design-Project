package com.sdp.remotehealthcareapp.Activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.remotehealthcareapp.R;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private static final String TAG = "MapsActivityStarted";
    private GoogleMap mMap;

    private LocationManager locationManager;
    private String provider;
    double lat, lng;
    private List<String> array_title = new ArrayList<>();

    private Marker previousMarker;
    private Marker newMarker;

    String docName;
    String st = "";
    String et = "";
    LatLng location;
    String day = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null)
                onLocationChanged(location);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            locationManager.requestLocationUpdates(provider, 60000, 1, this);
            if (previousMarker != null)
                previousMarker.remove();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Bundle bundle = getIntent().getExtras();
        docName = bundle.getString("Name");

        mMap = googleMap;

        if (previousMarker != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(previousMarker.getPosition().latitude, previousMarker.getPosition().longitude), 10));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        }


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            mMap.setMyLocationEnabled(true);
        }
//        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(LatLng latLng) {
//                if (newMarker != null)
//                    newMarker.remove();
//                newMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("New Marker"));
//            }
//        });


        LocalTime localTime = LocalTime.now(); //current time
        Calendar calendar = Calendar.getInstance();
        int dayint = calendar.get(Calendar.DAY_OF_WEEK); //todays day
        switch (dayint) {
            case 1:
                day = "Sunday";
                break;
            case 2:
                day = "Monday";
                break;
            case 3:
                day = "Tuesday";
                break;
            case 4:
                day = "Wednesday";
                break;
            case 5:
                day = "Thursday";
                break;
            case 6:
                day = "Friday";
                break;
            case 7:
                day = "Saturday";
                break;
        }
        FirebaseFirestore.getInstance()
                .collection("users")
                .document("Doctors")
                .collection("Names")
                .document(docName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            assert doc != null;
                            String clinics = doc.getString("Visits");
                            Log.d(TAG, "onMapComplete: " + clinics);

                            try {
                                Log.d(TAG, "onMapComplete: regex vala");
                                array_title = Arrays.asList(clinics.split(","));
                                Log.d(TAG, "onMapComplete: " + array_title.size());

                            } catch (NullPointerException e) {
                                Log.d(TAG, "onMapComplete: catch vala");
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            Log.d(TAG, "onMapComplete: last print");
                            getMarkers(day, localTime);
                        }
                    }
                });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MapsActivity.this, "Now move to book appointments activity", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getMarkers(String day, LocalTime localTime) {

        LatLng hospital1 = new LatLng(26.797776, 80.886892);
        LatLng hospital2 = new LatLng(26.795472, 80.890678);
        LatLng hospital3 = new LatLng(26.796456, 80.892670);
        LatLng hospital4 = new LatLng(26.797819, 80.900112);

        for (int i = 0; i < array_title.size(); ++i) {
            String clinicName = array_title.get(i);
            switch (clinicName) {
                case "The Vitality Visit":
                    location = hospital1;
                    break;
                case "Treatment Solutions":
                    location = hospital2;
                    break;
                case "The Minute Medical":
                    location = hospital3;
                    break;
                default:
                    location = hospital4;
                    break;
            }
            setMarkers(clinicName, localTime, location);
        }
    }

    public void setMarkers(String clinicName, LocalTime localTime, LatLng latLng) {
        final int[] flag = {0};
        FirebaseFirestore.getInstance()
                .collection("users")
                .document("Doctors")
                .collection("Names")
                .document(docName)
                .collection(clinicName)
                .document(day)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            assert doc != null;
                            st = doc.getString("startTime");
                            et = doc.getString("endTime");
                            Log.d(TAG, "onMapComplete: " + clinicName + "  " + st + "       " + et);
                            try {
                                if (localTime.isAfter(LocalTime.parse(st)) && localTime.isBefore(LocalTime.parse(et))) {
                                    mMap.addMarker(new MarkerOptions()
                                            .position(latLng)
                                            .title(clinicName)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                            .snippet(docName)
                                    );
                                    Log.d(TAG, "onComplete: map marker g " + clinicName);
                                }
                                if (localTime.isBefore(LocalTime.parse(st)) && localTime.isBefore(LocalTime.parse(et))) {
                                    mMap.addMarker(new MarkerOptions()
                                            .position(latLng)
                                            .title(clinicName)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                                            .snippet(docName)
                                    );
                                    Log.d(TAG, "onComplete: map marker y " + clinicName);
                                }
                                if (localTime.isAfter(LocalTime.parse(st)) && localTime.isAfter(LocalTime.parse(et))) {
                                    mMap.addMarker(new MarkerOptions()
                                            .position(latLng)
                                            .title(clinicName)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                            .snippet(docName)
                                    );
                                    Log.d(TAG, "onComplete: map marker r " + clinicName);
                                }
                            } catch (NullPointerException e) {
                                Toast.makeText(MapsActivity.this, "Doctor is not coming", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onComplete: " + e);
                            }
                        } else {
                            Toast.makeText(MapsActivity.this, "Not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MapsActivity.this, "The doctor doesn't visit today", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.action_hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.action_satellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.action_terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.action_none:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
        }
        return true;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();

        if (mMap != null) {
            LatLng position = new LatLng(lat, lng);
            previousMarker = mMap.addMarker(new MarkerOptions()
                    .title("Your Location")
                    .position(position));
        }
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

}