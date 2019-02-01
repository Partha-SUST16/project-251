package com.koushik.health_kit;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class Emergency extends AppCompatActivity {
    private static final String TAG = "Emergency";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        getLocationPermission();
    }

    void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                getDeviceLocation();
            }
        });
    }


    private void getDeviceLocation(){

        if (mLocationPermissionGranted) {

            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            gMap.setMyLocationEnabled(true);

            gMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    return false;
                }
            });

            gMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {

                }
            });
            gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    gMap.clear();
                    MarkerOptions markerPosition = new MarkerOptions();
                    markerPosition.position(latLng);
                    markerPosition.title("Current Position:");

                    markerPosition.snippet("Latitude: "+(int)latLng.latitude+" & Longitude: "+(int)latLng.longitude);
                    gMap.addMarker(markerPosition);
                }
            });
        }
    }


    private void getLocationPermission()
    {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted=true;
                initMap();
            }
            else
            {
                ActivityCompat.requestPermissions(this,
                        permission,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else
        {
            ActivityCompat.requestPermissions(this,
                    permission,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted=false;
        switch(requestCode)
        {
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length>0)
                {
                    for(int i=0;i<grantResults.length;i++)
                    {
                        if(grantResults[i]!= PackageManager.PERMISSION_GRANTED)
                        {
                            mLocationPermissionGranted=false;
                            return;
                        }
                    }
                    mLocationPermissionGranted=true;
                    initMap();
                }
            }
        }
    }
}
