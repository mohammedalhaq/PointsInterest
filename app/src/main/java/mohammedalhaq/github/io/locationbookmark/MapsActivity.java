package mohammedalhaq.github.io.locationbookmark;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    EditText locationSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Button confirm = findViewById(R.id.confirmButton);
        locationSearch = findViewById(R.id.editText);


        locationSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onMapSearch();
                    return true;
                }
                return false;
            }
        });

        //TODO to open the map on the users current location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, this);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},0);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));

        if (getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();
            String source = bundle.getString("source");
            String locationEdit = bundle.getString("location");
            locationSearch.setText(locationEdit);
        }

        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16f));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //searches map for the specified location
    public void onMapSearch() {
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
                Address address = addressList.get(0);
                LatLng searchedLoc = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(searchedLoc));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchedLoc, 17.0f));

            } catch (Exception e) {
                Toast badAddr = Toast.makeText(this, "Address does not exist.", Toast.LENGTH_LONG);
                badAddr.show();
                e.printStackTrace();
            }

        }

    }

    //confirms entry
    public void confirm(View view){
        Intent intent = new Intent(MapsActivity.this, FormActivity.class);
        intent.putExtra("location", locationSearch.getText().toString());
        startActivity(intent);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                //lstLatLngs.add(point);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(point));

                getAddr(point);
            }
        });

    }

    public void focusLocation(View view){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, this);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},0);
            return;
        }

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
        LatLng point = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point,17.0f));

        getAddr(point);
        Toast.makeText(this, "Set to current Location", Toast.LENGTH_SHORT).show();
    }

    //gets the address from a latlng object
    public void getAddr(LatLng point){
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        try {
            List<Address> addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
            String tappedAddr = addresses.get(0).getAddressLine(0);
            locationSearch.setText(tappedAddr);
        } catch (IOException e) {
            locationSearch.setText(point.latitude +", " + point.longitude);
            e.printStackTrace();
        }
    }
}
