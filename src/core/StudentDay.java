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
public class StudentDay {

    private Time[] timeSlots;

    public StudentDay(int slots) {
        timeSlots = new Time[slots];
        for (int i = 0; i < slots; i++) {
            timeSlots[i] = new Time();
        }
    }

    public void setTimeSlot(String timeString, int slot) throws IllegalArgumentException {
        slot = slot - 1; // ohne 1. Spalte
        if (slot == 1 || slot == 3) {
            if (!timeString.trim().isEmpty() & timeSlots[slot - 1].toString().trim().isEmpty()) {
                throw new IllegalArgumentException(" Kein Anfangswert eingegeben!");
            }
        }
        timeSlots[slot].setTime(timeString);
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

    public void setSingleLections() {
        if (!getStartTime1().toString().trim().isEmpty() & getEndTime1().toString().trim().isEmpty()) {
            timeSlots[1] = getStartTime1();
        }
        if (!getStartTime2().toString().trim().isEmpty() & getEndTime2().toString().trim().isEmpty()) {
            timeSlots[3] = getStartTime2();
        }
    }

    @Override
    public String toString() {
        String endString1, endString2;
        endString1 = getEndTime1().toString().trim().isEmpty() ? getEndTime1().toString() : "-" + getEndTime1().toString();
        endString2 = getEndTime2().toString().trim().isEmpty() ? getEndTime2().toString() : "-" + getEndTime2().toString();
        return " " + getStartTime1() + endString1 + " " + getStartTime2() + endString2 + " ";
    }
}
