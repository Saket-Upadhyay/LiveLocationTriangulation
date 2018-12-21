package locationpolymorph.test.sacredcoder.livelocationtriangulation;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import static locationpolymorph.test.sacredcoder.livelocationtriangulation.MapsActivity.REQUEST_LOCATION_PERMISSION;
import static locationpolymorph.test.sacredcoder.livelocationtriangulation.MapsActivity.REQUEST_LOCATION_PERMISSION;

public class textLocationUpdate extends AppCompatActivity {

    private static final String CHANNEL_ID = "1";
    TextView latitudestat;
    TextView longitudestat;
    TextView providerstat;
    TextView statusstat;
    LocationManager locationManager;
    LocationListener locationListener;
    String groupId = "my_group_01";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_location_update);
createNotificationChannel();
        latitudestat=findViewById(R.id.latitudestat);
        longitudestat=findViewById(R.id.longitudestat);
        providerstat=findViewById(R.id.provider);
        statusstat=findViewById(R.id.ta);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng  latLng = new LatLng(latitude, longitude);;
                Geocoder geocoder = new Geocoder(getApplicationContext());

                try {
                    List<Address> addresses =
                            geocoder.getFromLocation(latitude, longitude, 1);
                    String result = addresses.get(0).getLocality()+":";
                    result += addresses.get(0).getCountryName();


                    latitudestat.setText(latLng.toString());
                    longitudestat.setText(result);
                    statusstat.setText("Currently Extracting Information from:\n"+locationManager.getBestProvider(new Criteria(),true));


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {


            }

            @Override
            public void onProviderEnabled(String s) {
providerstat.setText("GPS Provider Enabled");
            }

            @Override
            public void onProviderDisabled(String s) {
providerstat.setText("GPS Provider Disabled");
            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, locationListener);

    }




    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void showNotification(LatLng latLng)
    {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_gps_not_fixed_black_24dp)
                .setContentTitle("Location Updated")
                .setContentText(latLng.toString())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,mBuilder.build());
    }
}
