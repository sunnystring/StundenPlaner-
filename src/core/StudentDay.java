/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import scheduleData.ScheduleTimeFrame;
import utils.Time;
import static core.StudentTimes.COLUMNS;
import java.util.ArrayList;
import scheduleData.DayColumnData;

/**
 *
 * Einheit eines Unterrichtages mit den verfügbaren Schülerzeiten
 */
public class StudentDay implements Comparable<StudentDay> {

    public static final int SLOTS = COLUMNS - 1;
    private String dayName = "";
    private final Time[] timeSlots;
    private boolean isEmpty;
    private boolean noStart1, noStart2, endSmallerStart1, endSmallerStart2, onlyStart1, onlyStart2;
    private Time earliestStart, latestStart, earliestEnd, latestEnd; // Timebounds
    private final ArrayList<ValidTimePairs> validTimes; // für IncompatibleStudentTimes (K = startTime, V = endTime)

    public StudentDay() {
        timeSlots = new Time[SLOTS];
        for (int i = 0; i < SLOTS; i++) {
            timeSlots[i] = new Time();
        }
        earliestStart = new Time();
        latestStart = new Time();
        earliestEnd = new Time();
        latestEnd = new Time();
        validTimes = new ArrayList<>();
        isEmpty = true;
    }

    public void setTimeSlot(String timeString, int slot) {
        slot = slot - 1; // ohne 1. Spalte
        timeSlots[slot].setTime(timeString);
    }

    public void setSelectionState() {
        isEmpty = start1().isEmpty() && start2().isEmpty() && favorite().isEmpty();
    }

    public void setSingleLections() {
        if (onlyStart1) {
            timeSlots[1] = timeSlots[0].clone();
        }
        if (onlyStart2) {
            timeSlots[3] = timeSlots[2].clone();
        }
    }

    public void setLowestAndHighestBounds() {
        if (!isEmpty) {
            setEarliestStartAndEnd();
            setLatestStartAndEnd();
        }
    }

    private void setEarliestStartAndEnd() {
        earliestStart = new Time("23.55");
        earliestEnd = new Time("23.55");
        for (int i = 0; i < SLOTS; i++) {
            if (!timeSlots[i].isEmpty()) {
                if (i != 1 && i != 3) {
                    if (timeSlots[i].lessEqualsThan(earliestStart)) {
                        earliestStart = timeSlots[i];
                    }
                }
                if (i != 0 && i != 2) {
                    if (timeSlots[i].lessEqualsThan(earliestEnd)) {
                        earliestEnd = timeSlots[i];
                    }
                }
            }
        }
    }

    private void setLatestStartAndEnd() {
        for (int i = 0; i < SLOTS; i++) {
            if (!timeSlots[i].isEmpty()) {
                if (i != 1 && i != 3) {
                    if (timeSlots[i].greaterEqualsThan(latestStart)) {
                        latestStart = timeSlots[i];
                    }
                }
                if (i != 0 && i != 2) {
                    if (timeSlots[i].greaterEqualsThan(latestEnd)) {
                        latestEnd = timeSlots[i];
                    }
                }
            }
        }
    }

    public void setValidTimes() {
        validTimes.clear();
        if (!start1().isEmpty()) {
            validTimes.add(new ValidTimePairs(start1(), end1()));
        }
        if (!start2().isEmpty()) {
            validTimes.add(new ValidTimePairs(start2(), end2()));
        }
        if (!favorite().isEmpty()) {
            validTimes.add(new ValidTimePairs(favorite(), favorite()));
        }
    }

    public boolean validateTimeSlots() {
        noStart1 = start1().isEmpty() && !end1().isEmpty();
        noStart2 = start2().isEmpty() && !end2().isEmpty();
        endSmallerStart1 = end1().lessThan(start1());
        endSmallerStart2 = end2().lessThan(start2());
        onlyStart1 = !start1().isEmpty() & end1().isEmpty();
        onlyStart2 = !start2().isEmpty() & end2().isEmpty();
        return (noStart1 || noStart2 || endSmallerStart1 && !onlyStart1 || endSmallerStart2 && !onlyStart2);
    }

    public void correctInvalidTimeSlots() {
        if (noStart1 || endSmallerStart1) {
            timeSlots[0].reset();
            timeSlots[1].reset();
        }
        if (noStart2 || endSmallerStart2) {
            timeSlots[2].reset();
            timeSlots[3].reset();
        }
    }

    public boolean matches(String dayname) {
        return this.dayName.equals(dayname);
    }

    public boolean outOfTimeFrame(ScheduleTimeFrame scheduleTimeFrame, int lectionLength) {
        Time absoluteEnd = scheduleTimeFrame.getAbsoluteEnd();
        Time absoluteStart = scheduleTimeFrame.getAbsoluteStart();
        boolean outOfUpperBound = !earliestStart.isEmpty() && earliestStart.plusTimeOf(lectionLength).greaterThan(absoluteEnd);
        boolean outOfLowerBound = !latestEnd.isEmpty() && latestEnd.lessThan(absoluteStart);
        return outOfUpperBound || outOfLowerBound;
    }

    public boolean outOfValidEndOf(ScheduleDay scheduleDay) {
        Time validScheduleEnd = new Time("23.55");
        if (scheduleDay != null) {
            validScheduleEnd = scheduleDay.getValidEnd();
        }
        return earliestStart.greaterEqualsThan(validScheduleEnd);
    }

    public boolean isWithin(StudentDay refDay, int refLection) { // this = searchDay
        if (isEmpty) {
            return false;
        }
        Time refEnd = refDay.getLatestEnd().plusTimeOf(refLection);
        return earliestStart.lessEqualsThan(refEnd);
    }

    public boolean isIncompatibleTo(StudentDay refDay, int refLection, int searchLection) { // this = searchDay
        boolean isIncompatible = true;
        Time searchEnd, refEnd;
        for (int i = 0; i < SLOTS; i++) {
            if (i != 0 && i != 2) {
                searchEnd = timeSlots[i];
                refEnd = refDay.getTimeAt(i);
                if (!searchEnd.isEmpty()) {  // 1. check: search incompatible to ref
                    for (ValidTimePairs timePairs : refDay.getValidTimes()) {
                        Time refStart = timePairs.start();
                        Time refLength = refStart.plusTimeOf(refLection);
                        if (searchEnd.greaterEqualsThan(refLength)) {
                            isIncompatible = false;
                        }
                    }
                }
                if (isIncompatible && !refEnd.isEmpty()) { // 2. check: ref incompatible to search
                    for (ValidTimePairs timePairs : validTimes) {
                        Time searchStart = timePairs.start();
                        Time searchLength = searchStart.plusTimeOf(searchLection);
                        if (refEnd.greaterEqualsThan(searchLength)) {
                            isIncompatible = false;
                        }
                    }
                }
            }
        }
        return isIncompatible;
    }

    public boolean isBlocked(DayColumnData dayColumn, int searchLectionLength) {
        boolean isBlocked = true;
        for (ValidTimePairs timePairs : validTimes) {
            Time startTime = timePairs.start();
            Time endTime = timePairs.end();
            int fieldIndex = dayColumn.getFieldIndexAt(startTime);
            int lastFieldIndex = dayColumn.getFieldIndexAt(endTime);
            int lectionFieldCount = 0;
            while (fieldIndex < lastFieldIndex + searchLectionLength) {
                lectionFieldCount++;
                if (dayColumn.getFieldDataAt(fieldIndex).isLectionAllocated()) {
                    lectionFieldCount = 0;
                }
                if (lectionFieldCount > searchLectionLength - 1) {
                    isBlocked = false;
                }
                fieldIndex++;
            }
        }
        return isBlocked;
    }

    @Override
    public int compareTo(StudentDay d) {
        if (earliestStart.greaterThan(d.getEarliestStart())) {
            return 1;
        }
        if (earliestStart.lessThan(d.getEarliestStart())) {
            return -1;
        }
        return 0;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public Time getTimeAt(int i) {
        return timeSlots[i];
    }

    public Time start1() {
        return timeSlots[0];
    }

    public Time end1() {
        return timeSlots[1];
    }

    public Time start2() {
        return timeSlots[2];
    }

    public Time end2() {
        return timeSlots[3];
    }

    public Time favorite() {
        return timeSlots[4];
    }

    public Time getEarliestStart() {
        return earliestStart;
    }

    public Time getEarliestEnd() {
        return earliestEnd;
    }

    public Time getLatestStart() {
        return latestStart;
    }

    public Time getLatestEnd() {
        return latestEnd;
    }

    public ArrayList<ValidTimePairs> getValidTimes() {
        return validTimes;
    }

    @Override
    public String toString() {
        String endString1, endString2;
        endString1 = end1().toString().trim().isEmpty() ? end1().toString() : "-" + end1().toString();
        endString2 = end2().toString().trim().isEmpty() ? end2().toString() : "-" + end2().toString();
        return " " + start1() + endString1 + " " + start2() + endString2 + " ";
    }

    private class ValidTimePairs {

        private Time start, end;

        public ValidTimePairs(Time start, Time end) {
            this.start = start;
            this.end = end;
        }

        public Time start() {
            return start;
        }

        public Time end() {
            return end;
        }
    }
}
