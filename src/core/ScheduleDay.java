/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import util.Time;

/**
 *
 * Einheit eines Unterrichtstages mit den Unterrichtszeiten (vom Lehrer
 * vorgegeben)
 */
public class ScheduleDay {

    private String day;
    private Time[] timeSlots;

    public ScheduleDay() {
        timeSlots = new Time[2];
        for (int i = 0; i < 2; i++) {
            timeSlots[i] = new Time();
        }
    }

    public void setTimeSlot(String time, int slot) throws IllegalArgumentException {
        slot = slot - 1; // ohne 1. Spalte
        if (isInvalidTimeSlot(time, slot)) {
            throw new IllegalArgumentException(" Unkorrekte Eingabe!");
        }
        timeSlots[slot].setTime(time);
    }

    private boolean isInvalidTimeSlot(String time, int i) {
        switch (i) {
            case 1: // beide oder keine Zeiten eingeben
                return (timeSlots[0].toString().trim().isEmpty() && !time.trim().isEmpty()) || (timeSlots[0].greaterEqualsThan(new Time(time)));
            default:
                return false;
        }
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
}
