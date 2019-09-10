package com.dev.tasevski.gpkumanovo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dev.tasevski.gpkumanovo.R;
import com.dev.tasevski.gpkumanovo.extra.BusStopTimesAdapter;
import com.dev.tasevski.gpkumanovo.repository.BusLinesRepository;
import com.dev.tasevski.gpkumanovo.repository.BusStopsRepository;

import java.util.List;

public class TimeFrameActivity extends AppCompatActivity {

    TextView lineName;
    Button backButton;
    private List<String> stopNames;
    private List<Integer> idList;
    private List<String> closestTimes;
    private RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView subName;
    Bundle extra;
    DrawerLayout mDrawerLayout;
    Button buttonDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeframe);
        mRecyclerView = findViewById(R.id.listStopAndTime);
        mRecyclerView.setHasFixedSize(true);

        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        subName = findViewById(R.id.subName);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

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
                            startActivity(new Intent(TimeFrameActivity.this, AboutActivity.class));
                        } else if (itemId == R.id.nav_policy) {
                            startActivity(new Intent(TimeFrameActivity.this, PrivacyPolicyActivity.class));
                        } else if (itemId == R.id.nav_contact) {
                            startActivity(new Intent(TimeFrameActivity.this, ContactActivity.class));
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

        extra = getIntent().getExtras();
        if (extra != null) {
            int busStopID = (int) extra.get("stop");
            if (BusStopsRepository.getBusStopForId(busStopID).isRegion()){
                subName.setText("РЕГИОН");
            } else {
                subName.setText("ПОСТОЈКА");
            }
            stopNames = BusLinesRepository.getLineNamesForBusStop(busStopID);
            idList = BusLinesRepository.getLineIdForBusStop(busStopID);
            lineName.setText(BusStopsRepository.getNameForBusStopId(busStopID));
            closestTimes = BusLinesRepository.getClosestTimesForBusStop(busStopID);
            populateBusStopsList();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (extra != null) {
                    int busStopID = (int) extra.get("stop");
                    stopNames = BusLinesRepository.getLineNamesForBusStop(busStopID);
                    idList = BusLinesRepository.getLineIdForBusStop(busStopID);
                    lineName.setText(BusStopsRepository.getNameForBusStopId(busStopID));
                    closestTimes = BusLinesRepository.getClosestTimesForBusStop(busStopID);
                    populateBusStopsList();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void populateBusStopsList() {
        if (stopNames == null) return;
        RecyclerView.Adapter mAdapter = new BusStopTimesAdapter(stopNames, idList, closestTimes);
        mRecyclerView.setAdapter(mAdapter);

    }
}
