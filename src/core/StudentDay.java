/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import scheduleData.ScheduleTimeFrame;
import util.Time;

/**
 *
 * Einheit eines Unterrichtages mit den verfügbaren Schülerzeiten
 */
public class StudentDay {

    public static final int SLOTS = StudentTimes.COLUMNS - 1;
    private String dayName = "";
    private Time[] timeSlots;
    private boolean noStart1, noStart2, endSmallerStart1, endSmallerStart2, onlyStart1, onlyStart2;

    public StudentDay() {
        timeSlots = new Time[SLOTS];
        for (int i = 0; i < SLOTS; i++) {
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
            timeSlots[1] = timeSlots[0].clone();
        }
        if (onlyStart2) {
            timeSlots[3] = timeSlots[2].clone();
        }
    }

    public boolean isEmpty() {
        return getStartTime1().isEmpty() && getStartTime2().isEmpty() && getFavorite().isEmpty();
    }

    public boolean isOutOfTimeFrame(ScheduleTimeFrame scheduleTimeFrame, int lectionLength) {
        Time absoluteEnd = scheduleTimeFrame.getAbsoluteEnd();
        Time absoluteStart = scheduleTimeFrame.getAbsoluteStart();
        Time end1 = getStartTime1().plusLengthOf(lectionLength);
        Time end2 = getStartTime2().plusLengthOf(lectionLength);
        Time favoriteEnd = getFavorite().plusLengthOf(lectionLength);
        boolean outOfUpperBound = end1.greaterThan(absoluteEnd) || favoriteEnd.greaterThan(absoluteEnd) || end2.greaterThan(absoluteEnd);
        boolean outOfTime1 = !getEndTime1().isEmpty() && getEndTime1().lessThan(absoluteStart);
        boolean outOfFavorite = !getFavorite().isEmpty() && getFavorite().lessThan(absoluteStart);
        boolean outOfTime2 = !getEndTime2().isEmpty() && getEndTime2().lessThan(absoluteStart);
        boolean outOfLowerBound = outOfTime1 || outOfFavorite || outOfTime2;
        return outOfUpperBound || outOfLowerBound;
    }

    public boolean isOutOfValidEnd(ScheduleTimes scheduleTimes) {
        Time validScheduleEnd = new Time("23.55");
        ScheduleDay scheduleDay = scheduleTimes.getMatchingScheduleDayOf(this);
        if (scheduleDay != null) {
            validScheduleEnd = scheduleDay.getValidEnd();
        }
        return getStartTime1().greaterThan(validScheduleEnd) || getFavorite().greaterEqualsThan(validScheduleEnd);
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
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
