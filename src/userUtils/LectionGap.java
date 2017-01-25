/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userUtils;

import core.Profile;
import core.StudentDay;
import scheduleData.LectionData;
import utils.Time;

/**
 *
 * Eine Stundenplanlücke (= nicht eingeteiltes Zeitintervall), die von
 * {@link LectionGapFiller} benutzt wird
 */
public class LectionGap {

    private Time gapStart, gapEnd;
    private int gapLength;
    private int dayIndex;

    public LectionGap(Time start, Time end, int dayIndex) {
        this.gapStart = start;
        this.gapEnd = end;
        gapLength = end.minus(start).getNumberOfFields(Time.ROUND_DOWN) + 1;
        this.dayIndex = dayIndex;
    }

    public LectionGap() {
        reset();
    }

    public void reset() {
        gapStart = new Time();
        gapEnd = new Time();
        dayIndex = -1;
        gapLength = 0;
    }

    public boolean matchesTimeOf(StudentDay studentDay, Profile profile, LectionData lection, int dayIndex) {
        if (studentDay.isEmpty()) {
            return false;
        }
        boolean isAdjacentToLection = isAdjacentTo(lection, dayIndex);
        int lectionLength = profile.getLectionLengthInFields();
        if (gapLength < lectionLength && !isAdjacentToLection) {
            return false;
        }
        boolean outOfBounds = studentDay.latestEnd().plusLengthOf(lectionLength).lessThan(gapStart)
                || studentDay.earliestStart().greaterThan(gapEnd);
        if (outOfBounds) {
            return false;
        } else {
            boolean match = false;
            for (StudentDay.ValidTimes validTimes : studentDay.getValidTimes()) {
                Time validStart = validTimes.start().plusLengthOf(lectionLength - 1);
                Time validEnd = validTimes.end();
                boolean within = validStart.lessEqualsThan(gapEnd) && validEnd.greaterEqualsThan(gapStart);
                if (isAdjacentToLection) {
                    validStart = validTimes.start();
                    validEnd = validTimes.end().plusLengthOf(lectionLength - 1);
                    within = validStart.lessEqualsThan(gapEnd) && validEnd.greaterEqualsThan(gapStart);
                }
                if (within) {
                    match = true;
                }
            }
            return match;
        }
    }

    private boolean isAdjacentTo(LectionData lection, int dayIndex) {
        if (lection == null) {
            return false;
        } else if (lection.getDayIndex() != dayIndex) {
            return false;
        } else {
            return lection.start().equals(gapEnd.plus(5)) || lection.end().plus(5).equals(gapStart);
        }
    }

    public Time start() {
        return gapStart;
    }

    public Time end() {
        return gapEnd;
    }

    public void setStart(Time gapStart) {
        this.gapStart = gapStart;
    }

    public void setEnd(Time gapEnd) {
        this.gapEnd = gapEnd;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
    }

    @Override
    public boolean equals(Object obj) {
        LectionGap gap = (LectionGap) obj;
        return gapStart.equals(gap.start()) && gapEnd.equals(gap.end()) && this.dayIndex == gap.getDayIndex();
    }
}
