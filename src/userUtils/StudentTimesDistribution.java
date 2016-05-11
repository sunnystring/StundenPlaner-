/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userUtils;

import core.ScheduleDay;
import core.ScheduleTimes;
import java.awt.Color;
import java.util.ArrayList;
import java.util.TreeMap;
import utils.Colors;
import utils.Time;

/**
 *
 * Die Schülerzeiten werden gemäss 5 verschiedenen Zeitbereichen (auf Basis der
 * Anfangszeit 1) mit Blautönen markiert
 *
 */
public class StudentTimesDistribution {

    public static final int COLOR_RANGE = 5;
    private ScheduleTimes scheduleTimes;
    private ArrayList<TreeMap> blueMaps, redMaps, purpleMaps;
    private Time inc, timeMark;
    private Color color;

    public StudentTimesDistribution(ScheduleTimes scheduleTimes) {
        this.scheduleTimes = scheduleTimes;
        blueMaps = new ArrayList<>();
        redMaps = new ArrayList<>();
        purpleMaps = new ArrayList<>();
    }

    public void init() {
        createColorMaps();
    }

    public void update() {
        blueMaps.clear();
        redMaps.clear();
        purpleMaps.clear();
        createColorMaps();
    }

    private void createColorMaps() {
        for (int i = 0; i < scheduleTimes.getNumberOfValidDays(); i++) {
            setTimeComponents(scheduleTimes.getValidScheduleDayAt(i));
            TreeMap<Time, Color> blueMap = new TreeMap<>();
            TreeMap<Time, Color> redMap = new TreeMap<>();
            TreeMap<Time, Color> purpleMap = new TreeMap<>();
            for (int j = 0; j < COLOR_RANGE; j++) {
                timeMark = timeMark.plus(inc);
                color = Colors.getBlue(j);
                blueMap.put(timeMark, color);
                color = Colors.getRed(j);
                redMap.put(timeMark, color);
                color = Colors.getPurple(j);
                purpleMap.put(timeMark, color);
            }
            blueMaps.add(blueMap);
            redMaps.add(redMap);
            purpleMaps.add(purpleMap);
        }
    }

    private void setTimeComponents(ScheduleDay scheduleDay) {
        timeMark = new Time();
        timeMark = scheduleDay.getValidStart().clone();
        inc = new Time();
        inc = (scheduleDay.getValidEnd().minus(scheduleDay.getValidStart())).divBy(COLOR_RANGE, Time.ROUND_UP);
    }

    public Color getBlue(Time time, int dayIndex) {
        TreeMap<Time, Color> colorMap = blueMaps.get(dayIndex);
        return colorMap.get(colorMap.ceilingKey(time));
    }

    public Color getRed(Time time, int dayIndex) {
        TreeMap<Time, Color> colorMap = redMaps.get(dayIndex);
        return colorMap.get(colorMap.ceilingKey(time));
    }

    public Color getPurple(Time time, int dayIndex) {
        TreeMap<Time, Color> colorMap = purpleMaps.get(dayIndex);
        return colorMap.get(colorMap.ceilingKey(time));
    }
}
