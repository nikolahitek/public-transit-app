package com.dev.tasevski.gpkumanovo.model;

import com.dev.tasevski.gpkumanovo.repository.DaysOffRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BusLine {

    private List<StopAndTime> stopsWithTime;
    private List<StartingTime> startingTimes;
    private String name;
    private int id;

    public BusLine(int id, String name, List<StopAndTime> stopsWithTime, List<StartingTime> startingTimes) {
        this.stopsWithTime = stopsWithTime;
        this.name = name;
        this.id = id;
        this.startingTimes = startingTimes;
    }

    public List<StopAndTime> getStopsWithTime() {
        return stopsWithTime;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<StartingTime> getStartingTimes() {
        List<StartingTime> list = new ArrayList<>();
        if (DaysOffRepository.isSaturday()) {
            for(StartingTime time : startingTimes){
                if (time.isWorkingOnSaturday())
                    list.add(time);
            }
            return list;
        }
        if (DaysOffRepository.isSunday() || DaysOffRepository.containsDay()) {
            for(StartingTime time : startingTimes) {
                if(time.isWorkingOnSunday())
                    list.add(time);
            }
            return list;
        }
        return startingTimes;
    }

    public StopAndTime getStopAndTime(int id){
        for(StopAndTime stop : stopsWithTime) {
            if(stop.getStop().getId() == id)
                return stop;
        }
        return new StopAndTime(new BusStop(),-1);
    }

    public int getClosestTime(int id) {
        List<StartingTime> startingTimesChecked = getStartingTimes();
        Calendar rightNow = Calendar.getInstance();
        int minutes = rightNow.get(Calendar.HOUR_OF_DAY)*60+rightNow.get(Calendar.MINUTE);
        StopAndTime stop= getStopAndTime(id);
        if(stop == null)
            return Integer.MIN_VALUE;
        for(StartingTime time : startingTimesChecked) {
            if(time.getTimeInMins()+stop.getTimeApart()>minutes)
                return time.getTimeInMins()+stop.getTimeApart()-minutes;
        }
        if(minutes>startingTimes.get(0).getTimeInMins()+stop.getTimeApart())
            return 1440-minutes+startingTimes.get(0).getTimeInMins()+stop.getTimeApart();
        return startingTimes.get(0).getTimeInMins()+stop.getTimeApart()-minutes;
    }

    public int getClosestStartingTime(int id) {
        List<StartingTime> startingTimesChecked = getStartingTimes();
        Calendar rightNow = Calendar.getInstance();
        StopAndTime stop = getStopAndTime(id);
        int minutes = rightNow.get(Calendar.HOUR_OF_DAY)*60+rightNow.get(Calendar.MINUTE);
        for(StartingTime time : startingTimesChecked) {
            if(minutes<time.getTimeInMins()+stop.getFinishingTime())
                return time.getTimeInMins();
        }
        return startingTimes.get(0).getTimeInMins();
    }

}

