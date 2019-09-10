package com.dev.tasevski.gpkumanovo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.dev.tasevski.gpkumanovo.R;
import com.dev.tasevski.gpkumanovo.extra.BusStopsForLineAdapter;
import com.dev.tasevski.gpkumanovo.model.StartingTime;
import com.dev.tasevski.gpkumanovo.model.StopAndTime;
import com.dev.tasevski.gpkumanovo.repository.BusLinesRepository;

import java.util.ArrayList;
import java.util.List;

public class BusStopsForLineActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView listBusStops;
    List<StopAndTime> stops;
    List<StartingTime> startingTimes;
    TextView lineName;
    Button backButton;
    int minutes = 0;
    DrawerLayout mDrawerLayout;
    Button buttonDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops_for_line);
        listBusStops = findViewById(R.id.listBusStops);
        listBusStops.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listBusStops.setLayoutManager(mLayoutManager);

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
                            startActivity(new Intent(BusStopsForLineActivity.this, AboutActivity.class));
                        } else if (itemId == R.id.nav_policy) {
                            startActivity(new Intent(BusStopsForLineActivity.this, PrivacyPolicyActivity.class));
                        } else if (itemId == R.id.nav_contact) {
                            startActivity(new Intent(BusStopsForLineActivity.this, ContactActivity.class));
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

        lineName = findViewById(R.id.lineName);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle extra = getIntent().getExtras();
        if (extra != null){
            stops = BusLinesRepository.getStopsWithTimeForLine((int) extra.get("stops"));
            startingTimes = BusLinesRepository.getStartingTimes((int) extra.get("stops"));
            lineName.setText(BusLinesRepository.getLineForId((int) extra.get("stops")).getName());
            populateBusStopsList();
        }

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        List<String> times = new ArrayList<>();
        for(int i=0;i<startingTimes.size();i++) {
            times.add(startingTimes.get(i).toString());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_open, times);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(dataAdapter);
    }

    private void populateBusStopsList() {
        if (stops==null) return;
        RecyclerView.Adapter mAdapter = new BusStopsForLineAdapter(stops, minutes);
        listBusStops.setAdapter(mAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String[] parts = parent.getItemAtPosition(position).toString().split(":");
        minutes = Integer.parseInt(parts[0])*60+Integer.parseInt(parts[1]);
        populateBusStopsList();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        minutes = 0;
    }
}