package com.example.googlemap_ex;

import android.Manifest;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_CODE_PERMISSIONS = 1000;
    FusedLocationProviderClient mFusedLocationProviderClient;
    Button getLocation_btn;
    TextView current_location;
    Geocoder mGeocoder;
    List<Address> list = null;
    MapFragment mapFragment;
    GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        current_location = findViewById(R.id.current_location);
        getLocation_btn = findViewById(R.id.get_current_location_button);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mGeocoder = new Geocoder(this);


        FragmentManager fragmentManager = getFragmentManager();
        mapFragment = (MapFragment) fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        getLocation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSIONS);


                    return;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case REQUEST_CODE_PERMISSIONS:
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "권한 거부됨", Toast.LENGTH_SHORT).show();
                }else{
                    mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());

                                try {

                                    list = mGeocoder.getFromLocation(myLocation.latitude, myLocation.longitude, 10);
                                    current_location.setText(list.get(0).toString());
                                    System.out.println(list.get(0).toString());

                                    /*MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(myLocation);
                                    markerOptions.title("현재위치");

                                    mGoogleMap.addMarker(markerOptions);
                                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));*/

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng SEOUL = new LatLng(37.56, 126.97);
        mGoogleMap = googleMap;
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");

        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }
}
