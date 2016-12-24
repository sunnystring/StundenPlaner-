/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import utils.Time;

/**
 *
 * Einheit eines Unterrichtstages mit dem vom Lehrer vorgegebenen Zeitrahmen
 *
 */
public class ScheduleDay implements Cloneable {

    private String dayName = "";
    private Time[] timeSlots;

    public ScheduleDay() {
        createTimeSlots();
    }

    public void createTimeSlots() {
        timeSlots = new Time[2];
        for (int i = 0; i < timeSlots.length; i++) {
            timeSlots[i] = new Time();
        }
    }

    public boolean hasInvalidTimeSlots() {
        boolean emptyAndNotEqual = getValidStart().isEmpty() != getValidEnd().isEmpty();
        boolean startGreaterEndOrEqual = !getValidStart().isEmpty() && getValidStart().greaterEqualsThan(getValidEnd());
        return (emptyAndNotEqual || startGreaterEndOrEqual);
    }

    public void cleanTimeSlots() {
        for (int i = 0; i < timeSlots.length; i++) {
            timeSlots[i].setTime("");
        }
    }

    public boolean matches(String dayname) {
        return this.dayName.equals(dayname);
    }

    public boolean isEmpty() {
        return getValidStart().isEmpty() && getValidEnd().isEmpty();
    }

    public void setTimeSlot(String s, int slot) {
        slot = slot - 1; // ohne 1. Spalte
        timeSlots[slot].setTime(s);
    }

    private void setTimeSlot(Time t, int slot) {
        timeSlots[slot].setTime(t);
    }

    public Time getTimeSlot(int slot) {
        return timeSlots[slot];
    }

    public Time getValidStart() {
        return timeSlots[0];
    }

    public Time getValidEnd() {
        return timeSlots[1];
    }

    public void setDayName(String day) {
        this.dayName = day;
    }

    public String getDayName() {
        return dayName;
    }

    @Override
    public ScheduleDay clone() {
        ScheduleDay scheduleDay = null;
        try {
            scheduleDay = (ScheduleDay) super.clone();
            scheduleDay.createTimeSlots();
            for (int i = 0; i < timeSlots.length; i++) {
                scheduleDay.setTimeSlot(getTimeSlot(i).clone(), i);
            }
        } catch (CloneNotSupportedException ex) {
        }
        return scheduleDay;
    }

    @Override
    public boolean equals(Object obj) {
        ScheduleDay scheduleDay;
        if (!(obj instanceof ScheduleDay)) {
            return false;
        }
        scheduleDay = (ScheduleDay) obj;
        if (!dayName.equals(scheduleDay.getDayName())) {
            return false;
        }
        for (int i = 0; i < timeSlots.length; i++) {
            if (!scheduleDay.getTimeSlot(i).equals(getTimeSlot(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int n = 0;
        for (int i = 0; i < timeSlots.length; i++) {
            n += getTimeSlot(i).getHour() + getTimeSlot(i).getMinute();
        }
        return n + dayName.hashCode();
    }
}
