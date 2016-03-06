/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userUtil;

import core.Database;
import core.ScheduleDay;
import core.ScheduleTimes;
import java.awt.Color;
import java.util.ArrayList;
import java.util.TreeMap;
import util.Colors;
import util.Time;

/**
 *
 * @author mathiaskielholz
 */
public class ColoredStudentTimes {

    private Database database;
    private ScheduleTimes scheduleTimes;
    private ArrayList<TreeMap> colorMaps;
    public static final int COLOR_RANGE = 5;
    private Time inc, timeMark;
    private Color color;

    public ColoredStudentTimes(Database database) {
        this.database = database;
        colorMaps = new ArrayList<>();
        createMaps();
    }

    private void createMaps() {
        scheduleTimes = database.getScheduleTimes();
        for (int i = 0; i < scheduleTimes.getNumberOfValidDays(); i++) {
            setTimeComponents(scheduleTimes.getValidScheduleDay(i));
            TreeMap<Time, Color> colorMap = new TreeMap<>();
            for (int j = 0; j < COLOR_RANGE; j++) {
                createMappingAt(j);
                colorMap.put(timeMark, color);
            }
            colorMaps.add(colorMap);
        }
    }

    private void setTimeComponents(ScheduleDay scheduleDay) {
        timeMark = new Time();
        timeMark = scheduleDay.getValidStart().clone();
        inc = new Time();
        inc = (scheduleDay.getValidEnd().minus(scheduleDay.getValidStart())).divBy(COLOR_RANGE);
    }

    private void createMappingAt(int colorIndex) {
        switch (colorIndex) {
            case 0:
                color = Colors.BLUE_0;
                break;
            case 1:
                color = Colors.BLUE_DEFAULT;
                break;
            case 2:
                color = Colors.BLUE_2;
                break;
            case 3:
                color = Colors.BLUE_3;
                break;
            case 4:
                color = Colors.BLUE_4;
                break;
        }
        timeMark = timeMark.plus(inc);
    }

    public Color getColor(int studentID, int dayIndex) {
        Time studentTime = database.getStudent(studentID).getStudentTimes().getValidStudentDay(dayIndex).getStartTime1();
        if (studentTime.isEmpty()) {
            return Colors.BLUE_DEFAULT;
        } else {
            TreeMap<Time, Color> colorMap = colorMaps.get(dayIndex);
            return colorMap.get(colorMap.higherKey(studentTime));
        }
    }
}
