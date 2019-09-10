package com.dev.tasevski.gpkumanovo.model;

public class StopAndTime {
    private BusStop stop;
    private int timeApart;
    private int startingTime;
    private int finishingTime;

    public StopAndTime(BusStop stop, int time) {
        this.stop = stop;
        this.timeApart = time;
        startingTime = -1;
        finishingTime = -1;
    }

    public StopAndTime(BusStop stop, int startingTime, int finishingTime) {
        this.stop = stop;
        this.timeApart = -2;
        this.startingTime = startingTime;
        this.finishingTime = finishingTime;
    }

    public BusStop getStop() {
        return stop;
    }

    public int getTimeApart() {
        return timeApart;
    }


    public int getStartingTime() {
        return startingTime;
    }

    public int getFinishingTime() {
        return finishingTime;
    }
}
