/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userUtils;

import core.Student;
import core.StudentDay;
import utils.Time;

/**
 *
 * Eine Stundenplanlücke (= nicht eingeteiltes Zeitintervall), die von {@link LectionGapFiller} benutzt wird
 */
public class Gap {

    private Time start, end;
    private int gapLength;
    private int dayIndex;

    public Gap(Time start, Time end, int dayIndex) {

        this.start = start;
        this.end = end;
        gapLength = end.minus(start).getNumberOfFields(Time.ROUND_DOWN) + 1;
        this.dayIndex = dayIndex;
    }

    public Gap() {
        reset();
    }

    public void reset() {
        start = new Time();
        end = new Time();
        dayIndex = -1;
        gapLength = 0;
    }

    public boolean matchesTimeOf(StudentDay studentDay, Student student) {
        if (studentDay.isEmpty()) {
            return false;
        }
        int lectionLength = student.getLectionLength();
        if (gapLength < lectionLength) {
            return false;
        }
        boolean outOfBounds = studentDay.latestEnd().plusTimeOf(lectionLength).lessThan(start)
                || studentDay.earliestStart().greaterThan(end);
        if (outOfBounds) {
            return false;
        } else {
            boolean match = false;
            for (StudentDay.ValidTimes validTimes : studentDay.getValidTimes()) {
                boolean within = validTimes.start().plusTimeOf(lectionLength).lessEqualsThan(end)
                        && validTimes.end().greaterEqualsThan(start);
                if (within) {
                    match = true;
                }
            }
            return match;
        }
    }

    public Time start() {
        return start;
    }

    public Time end() {
        return end;
    }

    public void setStart(Time start) {
        this.start = start;
    }

    public void setEnd(Time end) {
        this.end = end;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
    }

    @Override
    public boolean equals(Object obj) {
        Gap gap = (Gap) obj;
        return start.equals(gap.start) && end.equals(gap.end()) && this.dayIndex == gap.getDayIndex();
    }
}
