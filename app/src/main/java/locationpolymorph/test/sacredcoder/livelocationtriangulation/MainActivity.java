package locationpolymorph.test.sacredcoder.livelocationtriangulation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void showMyLocation(View view) {
        startActivity(new Intent(getApplicationContext(),MapsActivity.class));
    }

    public void showMyLocationText(View view) {
        startActivity(new Intent(getApplicationContext(),textLocationUpdate.class));
    }

    public void showMyLocationPoly(View view) {
        startActivity(new Intent(getApplicationContext(),PolyMap.class));
    }
}
