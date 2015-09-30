/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import util.Time;

/**
 *
 * @author Mathias
 */

/*  Daten für DayColumn-Instanzen*/
public class ScheduleDay {

    private String day;
    private Time[] timeSlots;

    public ScheduleDay() {

        timeSlots = new Time[2];

        for (int i = 0; i < 2; i++) {  // timeSlots initialisieren
            timeSlots[i] = new Time();
        }
    }

    public void setScheduleTime(String time, int i) throws IllegalArgumentException { // i = 1,2
        if (invalidSlot(time, i - 1)) {
            throw new IllegalArgumentException(" Unkorrekte Eingabe!");
        }
        timeSlots[i - 1].setTime(time);
    }

    public Time getValidStart() {
        return timeSlots[0];
    }

    public Time getValidEnd() {
        return timeSlots[1];
    }

    public void setDayName(String day) {
        this.day = day;
    }

    public String getDayName() {
        return day;
    }

    /* beide oder keine Zeit eingeben */
    private boolean invalidSlot(String time, int i) {

        switch (i) {
            case 1:
                return (timeSlots[0].toString().trim().isEmpty() && !time.trim().isEmpty()) || (timeSlots[0].greaterEqualsThan(new Time(time)));
            default:
                return false;
        }
    }
}
