package com.dev.tasevski.gpkumanovo.model;

import java.util.Locale;

public class StartingTime {
    private int h;
    private int min;
    private boolean workingOnSaturday;
    private boolean workingOnSunday;

    public StartingTime(int h, int min, boolean workingOnSaturday, boolean workingOnSunday) {
        this.h = h;
        this.min = min;
        this.workingOnSaturday = workingOnSaturday;
        this.workingOnSunday = workingOnSunday;
    }

    public int getTimeInMins() {
        return h*60 + min;
    }

    public boolean isWorkingOnSaturday() {
        return workingOnSaturday;
    }

    public boolean isWorkingOnSunday() {
        return workingOnSunday;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),"%02d:%02d",h,min);
    }
}
