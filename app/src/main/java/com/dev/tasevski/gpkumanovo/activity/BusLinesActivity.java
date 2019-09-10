package com.dev.tasevski.gpkumanovo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dev.tasevski.gpkumanovo.MapsActivity;
import com.dev.tasevski.gpkumanovo.R;
import com.dev.tasevski.gpkumanovo.extra.LinesAdapter;
import com.dev.tasevski.gpkumanovo.extra.OnItemClickListener;
import com.dev.tasevski.gpkumanovo.extra.TextChangedListener;
import com.dev.tasevski.gpkumanovo.model.BusLine;
import com.dev.tasevski.gpkumanovo.repository.BusLinesRepository;

import java.util.ArrayList;
import java.util.List;

public class BusLinesActivity  extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected BottomNavigationView navigationView;
    RecyclerView listBusLines;
    EditText textSearch;
    List<BusLine> lines;
    DrawerLayout mDrawerLayout;
    Button buttonDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

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
                            startActivity(new Intent(BusLinesActivity.this, AboutActivity.class));
                        } else if (itemId == R.id.nav_policy) {
                            startActivity(new Intent(BusLinesActivity.this, PrivacyPolicyActivity.class));
                        } else if (itemId == R.id.nav_contact) {
                            startActivity(new Intent(BusLinesActivity.this, ContactActivity.class));
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

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        textSearch = findViewById(R.id.textSearch);
        listBusLines = findViewById(R.id.listBusLines);
        listBusLines.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listBusLines.setLayoutManager(mLayoutManager);

        lines = BusLinesRepository.getLines();
        populateBusLinesList();


        textSearch.addTextChangedListener(new TextChangedListener<EditText>(textSearch) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                if (target.getText().toString().trim().equals("")) {
                    lines = BusLinesRepository.getLines();
                    populateBusLinesList();
                }
                else {
                    List<BusLine> possibleLines = new ArrayList<>();
                    for (BusLine line : lines) {
                        if (line.getName().toLowerCase().contains((target.getText().toString().trim().toLowerCase())))
                            possibleLines.add(line);
                    }
                    populateSearched(possibleLines);
                }
            }
        });
    }

    private void populateSearched(List<BusLine> possibleLines){
        lines = possibleLines;
        populateBusLinesList();
    }

    private void populateBusLinesList() {
        RecyclerView.Adapter mAdapter = new LinesAdapter(lines, new OnItemClickListener<BusLine>() {
            @Override
            public void onItemClick(BusLine item) {
                Intent intent = new Intent(getApplicationContext(),BusStopsForLineActivity.class);
                intent.putExtra("stops",item.getId());
                startActivity(intent);
            }
        });
        listBusLines.setAdapter(mAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        updateNavigationBarState();
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

    public int getContentViewId() {
        return R.layout.activity_bus_lines;
    }

    public int getNavigationMenuItemId() {
        return R.id.menuLine;
    }
}
