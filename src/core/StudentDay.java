/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import util.Time;

/**
 *
 * Einheit eines Unterrichtages mit den verfügbaren Schülerzeiten
 */
public class StudentDay {

    private Time[] timeSlots;
    boolean noStart1, noStart2, endSmallerStart1, endSmallerStart2, onlyStart1, onlyStart2;

    public StudentDay(int slots) {
        timeSlots = new Time[slots];
        for (int i = 0; i < slots; i++) {
            timeSlots[i] = new Time();
        }
    }

    public void setTimeSlot(String timeString, int slot) {
        slot = slot - 1; // ohne 1. Spalte
        timeSlots[slot].setTime(timeString);
    }

    public boolean hasInvalidTimeSlots() {
        noStart1 = getStartTime1().isEmpty() && !getEndTime1().isEmpty();
        noStart2 = getStartTime2().isEmpty() && !getEndTime2().isEmpty();
        endSmallerStart1 = getEndTime1().lessThan(getStartTime1());
        endSmallerStart2 = getEndTime2().lessThan(getStartTime2());
        onlyStart1 = !getStartTime1().isEmpty() & getEndTime1().isEmpty();
        onlyStart2 = !getStartTime2().isEmpty() & getEndTime2().isEmpty();
        return (noStart1 || noStart2 || endSmallerStart1 && !onlyStart1 || endSmallerStart2 && !onlyStart2);
    }

    public void correctInvalidTimeSlots() {
        if (noStart1 || endSmallerStart1) {
            timeSlots[0] = new Time();
            timeSlots[1] = new Time();
        }
        if (noStart2 || endSmallerStart2) {
            timeSlots[2] = new Time();
            timeSlots[3] = new Time();
        }
    }

    public void setSingleLections() {
        if (onlyStart1) {
            timeSlots[1] = timeSlots[0];
        }
        if (onlyStart2) {
            timeSlots[3] = timeSlots[2];
        }
    }

    // ToDo....
    public boolean isEmpty() {
        return false;
    }

    public Time getStartTime1() {
        return timeSlots[0];
    }

    public Time getEndTime1() {
        return timeSlots[1];
    }

    public Time getStartTime2() {
        return timeSlots[2];
    }

    public Time getEndTime2() {
        return timeSlots[3];
    }

    public Time getFavorite() {
        return timeSlots[4];
    }

    @Override
    public String toString() {
        String endString1, endString2;
        endString1 = getEndTime1().toString().trim().isEmpty() ? getEndTime1().toString() : "-" + getEndTime1().toString();
        endString2 = getEndTime2().toString().trim().isEmpty() ? getEndTime2().toString() : "-" + getEndTime2().toString();
        return " " + getStartTime1() + endString1 + " " + getStartTime2() + endString2 + " ";
    }
}
