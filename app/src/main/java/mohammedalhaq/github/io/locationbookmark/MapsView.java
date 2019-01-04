package mohammedalhaq.github.io.locationbookmark;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsView extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    String name;
    String locationText;
    String notes;
    private ShareActionProvider sap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");
        setTitle(name);
        TextView location = findViewById(R.id.location);
        locationText = extras.getString("location");
        location.setText(locationText);

        //adds a back button to action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //searches map for the location to dispplay
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocationName(locationText, 1);
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //if user presses edit fab formactivity launches to edit values
    public void editLoc(View view){
        Intent intent = new Intent(MapsView.this, FormActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("location", locationText);
        intent.putExtra("notes", notes);
        startActivity(intent);
    }

    //creates search bar on action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share, menu);

        MenuItem item = menu.findItem(R.id.share);
        sap = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        //share button
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, locationText);
        sap.setShareIntent(shareIntent);

        return true;
    }

    //search button handler
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent(MapsView.this, MainActivity.class);
            startActivity(intent);
        } else {
            //share
        }
        return super.onOptionsItemSelected(item);
    }

    private void setShareIntent(Intent shareIntent) {
        if (sap != null) {
            sap.setShareIntent(shareIntent);
        }
    }

}
