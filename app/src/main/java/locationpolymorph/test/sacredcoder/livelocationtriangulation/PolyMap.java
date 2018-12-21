package locationpolymorph.test.sacredcoder.livelocationtriangulation;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PolyMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker marker;
    Marker marker2;
    LocationListener locationListener;
    LocationManager locationManager;
    protected static final int REQUEST_LOCATION_PERMISSION = 1;

    //for line
    private Polyline polyline;
    private Activity activity;
    private Boolean flag=true;

    private List<LatLng> polylinePoints;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poly_map);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapp);
        mapFragment.getMapAsync(this);
        polylinePoints = new ArrayList<>();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
//polyline.setColor(Color.GREEN);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //if(location.hasAccuracy()) {
                  //  if (location.getAccuracy() < 20.0f) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        //get the location name from latitude and longitude
                        Geocoder geocoder = new Geocoder(getApplicationContext());
                        try {
                            List<Address> addresses =
                                    geocoder.getFromLocation(latitude, longitude, 1);
                            String result = addresses.get(0).getLocality() + ":";
                            result += addresses.get(0).getCountryName();
                            LatLng latLng = new LatLng(latitude, longitude);


                            if (flag) {
                                marker2 = mMap.addMarker(new MarkerOptions().position(latLng).title(result).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                            }


                            if (marker != null) {
                                marker.remove();

                                marker = mMap.addMarker(new MarkerOptions().position(latLng).title(result).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                mMap.setMaxZoomPreference(20);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));

                            } else {
                                marker = mMap.addMarker(new MarkerOptions().position(latLng).title(result).icon(BitmapDescriptorFactory.defaultMarker(120.0f)));
                                mMap.setMaxZoomPreference(20);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));
                            }

                            polylinePoints.add(latLng);


                            if (polyline != null) {
                                polyline.setPoints(polylinePoints);

                            } else {
                                polyline = mMap.addPolyline(new PolylineOptions().addAll(polylinePoints).color(Color.MAGENTA).jointType(JointType.ROUND).width(3.0f));
                            }

                            flag = false;


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                  //  }
                //}
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
    //TO MANAGE THE MAP AS ONE WHEN THE MAP VIEW IS AVAILABLE FOR THE USER.

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(mMap.MAP_TYPE_SATELLITE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    //CLOSE THE SESSION WHEN THE BACK IS PRESSED.
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        this.finish();
    }
}
