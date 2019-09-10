package com.dev.tasevski.gpkumanovo.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DaysOffRepository {
    private static List<Calendar> daysOff =  new ArrayList<>();

    public static List<Calendar> getDaysOff() {
        return daysOff;
    }

    public static boolean containsDay() {
        return daysOff.contains(GregorianCalendar.getInstance());
    }

    public static boolean isSaturday() {
        Calendar calendar = GregorianCalendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
    }

    public static boolean isSunday() {
        Calendar calendar = GregorianCalendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }
}
