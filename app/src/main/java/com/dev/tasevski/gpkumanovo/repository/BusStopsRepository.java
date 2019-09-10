package com.dev.tasevski.gpkumanovo.repository;

import com.dev.tasevski.gpkumanovo.model.BusStop;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BusStopsRepository {
    private static List<BusStop> stops = Arrays.asList(
            new BusStop("Село Режановце",1,new LatLng(42.157873, 21.676013), true),
            new BusStop("ФЗЦ 2",2,new LatLng(42.14753889,21.68138889)),
            new BusStop("Бедиње Касарна",3,new LatLng(42.13960556,21.70055556)),
            new BusStop("Болница",4,new LatLng(42.13603889,21.70972222)),
            new BusStop("Зик",5,new LatLng(42.13368611,21.70888889)),
            new BusStop("Серава",6,new LatLng(42.13269722,21.71472222)),
            new BusStop("Уред",7,new LatLng(42.13506667,21.71750000)),
            new BusStop("Табакана",8,new LatLng(42.13932222,21.72000000))
    );

    public static List<BusStop> getBusStops() {
        return stops;
    }

    public static void putMarkersForBusStops(GoogleMap map){
        for(BusStop stop : stops) {
            if(stop.getLocation()!=null) {
            map.addMarker(
                    new MarkerOptions().position(stop.getLocation()).title(stop.getName())
            );}
        }
    }

    public static String getNameForBusStopId(int id) {
        for(BusStop stop: stops) {
            if(stop.getId()==id)
                return stop.getName();
        }
        return "Invalid";
    }

    public static BusStop getBusStopForId(int id){
       for(BusStop stop : stops){
           if (stop.getId()==id)
               return stop;
       }
       return new BusStop();
    }

    public static List<BusStop> getBusStopsByCloseness(LatLng userPosition) {
        for(BusStop stop : stops){
            stop.setDistanceFromUser(userPosition);
        }
        List<BusStop> list = stops;
        Collections.sort(list);
        return list;
    }
}
