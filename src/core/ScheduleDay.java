/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import schedule.DayColumn;
import util.Time;

/**
 *
 * @author Mathias
 */

/*  Daten für DayColumn-Instanzen*/
public class ScheduleDay {

    private String day;

    private Time validStart;  // Unterrichtszeit pro Tag
    private Time validEnd;

    private int dayListIndex; // Index des Tages (zb. 0 = Montag)

    public ScheduleDay() {

        validStart = new Time("12.00");
        validEnd = new Time("12.00");

    }

//    private static Time absoluteStart = new Time("20.00"); // frühester Beginn aller Tage
//    private static Time absoluteEnd = new Time();   // spätestes Ende aller Tage
    public ScheduleDay(String day, Time validStart, Time validEnd) {

        this.day = day;
        this.validStart = validStart;
        this.validEnd = validEnd;

//        if (validStart.smallerThan(absoluteStart)) {
//            absoluteStart = validStart;
//        }
//        if (validEnd.greaterThan(absoluteEnd)) {
//            absoluteEnd = validEnd;
//        }
    }

    /* Getter */
//      public static Time getAbsoluteStart() {
//        return absoluteStart;
//    }
//    public static Time getAbsoluteEnd() {
//        return absoluteEnd;
//    }
     /* Getter, Setter */
    public void setTime(String time, int i) {
        switch (i) {
            case 1:
                validStart = new Time(time);
            case 2:
                validEnd = new Time(time);
        }
    }

    public Time getValidStart() {
        return validStart;
    }

    public Time getValidEnd() {
        return validEnd;
    }

    public String getDayName() {
        return day;
    }

    public void setDayIndex(int index) {
        dayListIndex = index;
    }

    public int getDayIndex() {
        return dayListIndex;
    }

//    public Time getTotalHours() {
//        return absoluteEnd.minus(absoluteStart);
//    }
}
