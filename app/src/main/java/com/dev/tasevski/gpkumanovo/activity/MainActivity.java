package com.dev.tasevski.gpkumanovo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dev.tasevski.gpkumanovo.extra.BusStopsAdapter;
import com.dev.tasevski.gpkumanovo.extra.OnItemClickListener;
import com.dev.tasevski.gpkumanovo.location.LocationHelper;
import com.dev.tasevski.gpkumanovo.location.LocationManager;
import com.dev.tasevski.gpkumanovo.MapsActivity;
import com.dev.tasevski.gpkumanovo.R;
import com.dev.tasevski.gpkumanovo.extra.TextChangedListener;
import com.dev.tasevski.gpkumanovo.model.BusStop;
import com.dev.tasevski.gpkumanovo.repository.BusStopsRepository;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, LocationManager {

    EditText textSearch;
    List<BusStop> stops;
    Location location;
    protected BottomNavigationView navigationView;
    private LocationHelper locationHelper;
    private RecyclerView mRecyclerView;
    DrawerLayout mDrawerLayout;
    Button buttonDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bus_stops);

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        buttonDrawer = findViewById(R.id.buttonDrawer);

        NavigationView navigationViewDrawer = findViewById(R.id.nav_view);
        navigationViewDrawer.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(false);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        int itemId = menuItem.getItemId();
                        if (itemId == R.id.nav_about) {
                            startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        } else if (itemId == R.id.nav_policy) {
                            startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        } else if (itemId == R.id.nav_contact) {
                            startActivity(new Intent(MainActivity.this, ContactActivity.class));
                        }
                        return true;
                    }
                });

        buttonDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mRecyclerView = findViewById(R.id.listBusStops);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        textSearch = findViewById(R.id.textSearch);
        textSearch.addTextChangedListener(new TextChangedListener<EditText>(textSearch) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                if (textSearch.getText().toString().trim().equals("")) {
                    if (location!=null) {
                        populateIfLocationOk();
                    } else {
                        stops = BusStopsRepository.getBusStops();
                        populateWithoutGps();
                    }
                }
                else {
                    List<BusStop> possibleStops = new ArrayList<>();
                    if (location != null) {
                        stops = BusStopsRepository.getBusStopsByCloseness(new LatLng(location.getLatitude(),location.getLongitude()));
                    } else {
                        stops = BusStopsRepository.getBusStops();
                        for(BusStop stop : stops){
                            stop.setDistance(-1);
                        }
                    }
                    for (BusStop stop : stops) {
                        if (stop.getName().toLowerCase().contains((textSearch.getText().toString().trim().toLowerCase())))
                            possibleStops.add(stop);
                    }
                    populateSearched(possibleStops);

                }
            }
        });
        Log.i("1","onTextChange");
    }

    @Override
    public void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 10);
        } else {
            locationHelper = new LocationHelper(MainActivity.this, this);
            locationHelper.startLocationUpdates();
        }
        if (location!=null) populateIfLocationOk();
        else {
            stops = BusStopsRepository.getBusStops();
            populateWithoutGps();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (locationHelper != null) {
            locationHelper.stopLocationUpdates();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menuStop) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (itemId == R.id.menuLine) {
            startActivity(new Intent(this, BusLinesActivity.class));
        } else if (itemId == R.id.menuMap) {
            startActivity(new Intent(this, MapsActivity.class));
        }
        overridePendingTransition(0, 0);
        finish();
        return true;
    }

    private void updateNavigationBarState() {
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        Menu menu = navigationView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                item.setChecked(true);
                break;
            }
        }
    }

    public int getNavigationMenuItemId() {
        return R.id.menuStop;
    }

    @Override
    public void getLastKnownLocation(Location lastLocation) {
        if (lastLocation != null && textSearch.getText().toString().trim().equals("")) {
            this.location = lastLocation;
            populateIfLocationOk();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null && textSearch.getText().toString().trim().equals("")) {
            this.location = location;
            populateIfLocationOk();
        }
    }

    @SuppressLint("MissingPermission")
    private void populateIfLocationOk()
    {
        if (location != null) {
            stops = BusStopsRepository.getBusStopsByCloseness(new LatLng(location.getLatitude(),location.getLongitude()));
            populateBusStopsList();
        }
    }

    private void populateWithoutGps(){
        for(BusStop stop : stops){
            stop.setDistance(-1);
        }
        populateBusStopsList();
    }

    @SuppressLint("MissingPermission")
    private void populateSearched(List<BusStop> possibleStops){
        stops = possibleStops;
        populateBusStopsList();
    }

    private void populateBusStopsList(){
        RecyclerView.Adapter mAdapter = new BusStopsAdapter(stops, new OnItemClickListener<BusStop>() {
            @Override
            public void onItemClick(BusStop item) {
                Intent intent = new Intent(getApplicationContext(),TimeFrameActivity.class);
                intent.putExtra("stop",item.getId());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationHelper = new LocationHelper(MainActivity.this, this);
                    locationHelper.startLocationUpdates();
                    populateIfLocationOk();
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
