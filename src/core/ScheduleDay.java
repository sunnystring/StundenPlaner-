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
        createEmptyTimeSlots();
    }

    private void createEmptyTimeSlots() {
        for (int i = 0; i < 2; i++) {
            timeSlots[i] = new Time();
        }
    }

    public void setTimeSlot(String time, int slot) {
        slot = slot - 1; // ohne 1. Spalte
        timeSlots[slot].setTime(time);
    }

    public boolean hasInvalidTimeSlots() {
        boolean emptyAndNotEqual = getValidStart().isEmpty() != getValidEnd().isEmpty();
        boolean startGreaterEndOrEqual = !getValidStart().isEmpty() && getValidStart().greaterEqualsThan(getValidEnd());
        return (emptyAndNotEqual || startGreaterEndOrEqual);
    }

    public void cleanInvalidTimeSlots() {
        createEmptyTimeSlots();
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
