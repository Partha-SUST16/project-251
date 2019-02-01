package com.koushik.health_kit;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class EmergencyNearbyPlaces extends AsyncTask<Object, String,String> {

    String googlePlaceData,url;
    GoogleMap mMap;

    @Override
    protected String doInBackground(Object... objects) {

        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        DownloadUrl downloadUrl = new DownloadUrl();

        try
        {
            googlePlaceData =  downloadUrl.ReadTheUrl(url);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return googlePlaceData;
    }

    @Override
    protected void onPostExecute(String s) {

        List<HashMap<String,String>> nearbyPlaceList = null;
        DataPasser dataPasser = new DataPasser();
        nearbyPlaceList = dataPasser.parse(s);

        DisplayEmergencyPlaces(nearbyPlaceList);
    }

    private void DisplayEmergencyPlaces(List<HashMap<String,String>> nearbyPlaceList)
    {
        for(int i=0;i<nearbyPlaceList.size();i++)
        {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String,String> googleNearbyPlace = nearbyPlaceList.get(i);
            String NameofPlace = googleNearbyPlace.get("place_name");
            String Vicinity = googleNearbyPlace.get("vicinity");
            double lat = Double.parseDouble( googleNearbyPlace.get("lat"));
            double lng = Double.parseDouble(googleNearbyPlace.get("lng"));

            LatLng latLng = new LatLng(lat,lng);
            markerOptions.position(latLng);
            markerOptions.title(NameofPlace+":"+Vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.addMarker(markerOptions);
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            //mMap.animateCamera(CameraUpdateFactory.zoomBy(10));



        }
    }
}
