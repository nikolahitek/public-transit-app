package com.dev.tasevski.gpkumanovo.repository;

import com.dev.tasevski.gpkumanovo.model.BusLine;
import com.dev.tasevski.gpkumanovo.model.StartingTime;
import com.dev.tasevski.gpkumanovo.model.StopAndTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BusLinesRepository {
    private static List<BusLine> lines = Arrays.asList(new BusLine(6, "Режановце - Куманово",
            Arrays.asList(
                    new StopAndTime(BusStopsRepository.getBusStopForId(1), 0),
                    new StopAndTime(BusStopsRepository.getBusStopForId(2), 5),
                    new StopAndTime(BusStopsRepository.getBusStopForId(3), 7),
                    new StopAndTime(BusStopsRepository.getBusStopForId(4), 10),
                    new StopAndTime(BusStopsRepository.getBusStopForId(5), 13),
                    new StopAndTime(BusStopsRepository.getBusStopForId(6), 15),
                    new StopAndTime(BusStopsRepository.getBusStopForId(7), 18),
                    new StopAndTime(BusStopsRepository.getBusStopForId(8), 20)),
            Arrays.asList(
                    new StartingTime(5, 15, true, false),
                    new StartingTime(6, 15, true, true),
                    new StartingTime(8, 0, true, true),
                    new StartingTime(9, 0, false, true),
                    new StartingTime(9, 50, true, true),
                    new StartingTime(11, 15, false, false),
                    new StartingTime(12, 30, true, true),
                    new StartingTime(13, 30, true, true),
                    new StartingTime(14, 25, true, true),
                    new StartingTime(15, 25, true, true),
                    new StartingTime(16, 25, false, false),
                    new StartingTime(17, 25, true, false),
                    new StartingTime(18, 50, false, false),
                    new StartingTime(19, 45, true, false),
                    new StartingTime(21, 15, false, false))
    ), new BusLine(3, "Демо Старт - Демо Крај",
            Arrays.asList(
                    new StopAndTime(BusStopsRepository.getBusStopForId(1), 0),
                    new StopAndTime(BusStopsRepository.getBusStopForId(4), 3),
                    new StopAndTime(BusStopsRepository.getBusStopForId(5), 3, 12),
                    new StopAndTime(BusStopsRepository.getBusStopForId(8), 12)),
            Arrays.asList(
                    new StartingTime(6, 15, true, true),
                    new StartingTime(8, 0, true, true),
                    new StartingTime(9, 0, false, true),
                    new StartingTime(9, 50, true, true),
                    new StartingTime(11, 15, false, false),
                    new StartingTime(12, 30, true, true),
                    new StartingTime(13, 30, true, true),
                    new StartingTime(14, 25, true, true),
                    new StartingTime(15, 25, true, true),
                    new StartingTime(16, 25, false, false),
                    new StartingTime(17, 25, true, false),
                    new StartingTime(18, 50, false, false),
                    new StartingTime(19, 45, true, false),
                    new StartingTime(21, 15, false, false))
    ));

    public static List<BusLine> getLines() {
        return lines;
    }

    public static BusLine getLineForId(int id) {
        for (BusLine line : lines) {
            if (line.getId() == id)
                return line;
        }
        return new BusLine(-1, "NON", null, null);
    }

    public static List<StopAndTime> getStopsWithTimeForLine(int id) {
        for (BusLine line : lines) {
            if (line.getId() == id)
                return line.getStopsWithTime();
        }
        return new ArrayList<>();
    }

    public static List<StartingTime> getStartingTimes(int id) {
        for (BusLine line : lines) {
            if (line.getId() == id)
                return line.getStartingTimes();
        }
        return new ArrayList<>();
    }

    public static List<String> getLineNamesForBusStop(int busId) {
        ArrayList<String> list = new ArrayList<>();
        for (BusLine line : lines) {
            StopAndTime stop = line.getStopAndTime(busId);
            if (!stop.getStop().getName().equals("NON")) {
                list.add(line.getName());
            }
        }
        return list;
    }

    public static List<Integer> getLineIdForBusStop(int busId) {
        ArrayList<Integer> list = new ArrayList<>();
        for (BusLine line : lines) {
            StopAndTime stop = line.getStopAndTime(busId);
            if (!stop.getStop().getName().equals("NON")) {
                list.add(line.getId());
            }
        }
        return list;
    }

    public static List<String> getClosestTimesForBusStop(int busId) {
        ArrayList<String> list = new ArrayList<>();
        for (BusLine line : lines) {
            StopAndTime stop = line.getStopAndTime(busId);
            if (!stop.getStop().getName().equals("NON")) {
                int time;
                if (stop.getTimeApart() == -2) {
                    Calendar rightNow = Calendar.getInstance();
                    int minutes = rightNow.get(Calendar.HOUR_OF_DAY) * 60 + rightNow.get(Calendar.MINUTE);
                    time = line.getClosestStartingTime(busId);
                    if (minutes > time)
                        minutes = 1440 - minutes + time;
                    else
                        minutes = time - minutes;
                    int startingTime = stop.getStartingTime() + minutes;
                    int finishingTime = stop.getFinishingTime() + minutes;
                    if (startingTime < 0) startingTime = 0;
                    list.add(String.format(Locale.getDefault(), "За %d до %d мин.", startingTime, finishingTime));
                } else {
                    time = line.getClosestTime(busId);
                    if (time != Integer.MIN_VALUE) {
                        if (time != -1)
                            if (time >= 60)
                                list.add(String.format(Locale.getDefault(), "За %d ч и %d мин.", time / 60, time % 60));
                            else
                                list.add(String.format(Locale.getDefault(), "За %d мин.", time));
                        else
                            list.add("Нема автобус за брзо време");
                    }
                }
            }
        }
        return list;
    }
}
