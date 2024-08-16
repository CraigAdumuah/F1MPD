package com.example.f1mpdresit;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HashMap<String, String> raceDetailsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);  // Ensure this layout contains your map fragment

        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize race details map
        raceDetailsMap = new HashMap<>();
        raceDetailsMap.put("Monaco Grand Prix", "Details about Monaco Grand Prix");
        raceDetailsMap.put("British Grand Prix", "Details about British Grand Prix");
        // Add more race details here
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add race location markers
        LatLng monaco = new LatLng(43.7384, 7.4246);
        mMap.addMarker(new MarkerOptions().position(monaco).title("Monaco Grand Prix"));

        LatLng silverstone = new LatLng(52.0733, -1.0142);
        mMap.addMarker(new MarkerOptions().position(silverstone).title("British Grand Prix"));

        // Move the camera to the first location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(monaco, 5));

        // Set marker click listener to show race details
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showRaceDetails(marker.getTitle());
                return true;
            }
        });
    }

    private void showRaceDetails(String raceName) {
        String details = raceDetailsMap.get(raceName);

        new AlertDialog.Builder(this)
                .setTitle(raceName)
                .setMessage(details)
                .setPositiveButton("OK", null)
                .show();
    }
}
