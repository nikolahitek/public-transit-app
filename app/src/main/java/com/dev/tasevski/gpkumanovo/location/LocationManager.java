package com.dev.tasevski.gpkumanovo.location;

import android.location.Location;

public interface LocationManager {

    void getLastKnownLocation(Location lastLocation);

    void onLocationChanged(Location location);

}