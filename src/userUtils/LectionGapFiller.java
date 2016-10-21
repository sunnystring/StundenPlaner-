/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userUtils;

import core.Database;
import core.Profile;
import core.StudentDay;
import java.util.ArrayList;
import java.util.Map;
import scheduleData.DayColumnData;
import scheduleData.LectionData;
import studentListData.StudentListData;
import utils.Time;

/**
 *
 * Beim Klick auf ein nicht eingeteiltes, genug grosses Zeitintervall im
 * Stundenplan werden alle verfügbaren Zeiten angezeigt (als Lection oder
 * StudentDay)
 */
public class LectionGapFiller {

    private final Database database;
    private final DayColumnData dayColumn;
    private final StudentListData studentListData;
    private static LectionGap gap;

    public LectionGapFiller(Database database, DayColumnData dayColumn, StudentListData studentListData) {
        this.database = database;
        this.dayColumn = dayColumn;
        this.studentListData = studentListData;
        gap = new LectionGap();
    }

    public void clear() {
        gap.reset();
        clearAllMarks();
    }

    public void showAvailableTimes(Time selectedTime, int dayIndex) {
        clearAllMarks();
        if (locateGap(selectedTime, dayIndex)) {
            ArrayList<StudentDay> dayList = database.getSortedStudentDayListAt(dayIndex);
            for (int i = 0; i < database.getNumberOfStudents(); i++) {
                StudentDay studentDay = dayList.get(i);
                int studentID = database.getStudentID(dayIndex, studentDay);
                Profile profile = database.getProfile(studentID);
                LectionData lection = database.getLectionByID(studentID);
                if (gap.matchesTimeOf(studentDay, profile, isGapAdjacentTo(lection, dayIndex))) {
                    if (profile.isAllocated()) {
                        if (lection != null && lection.getProfileID() == studentID) {
                            lection.setGapFillerMarkEnabled(true);
                        }
                    } else {
                        studentListData.getValueAt(studentID, dayIndex + 1).setLectionGapFiller(true);
                    }
                }
            }
        }
    }

    private boolean isGapAdjacentTo(LectionData lection, int dayIndex) {
        if (lection == null) {
            return false;
        } else {
            return lection.start().equals(gap.end().plus(5)) && lection.getDayIndex() == dayIndex;
        }
    }

    private void clearAllMarks() {
        for (int dayIndex = 0; dayIndex < database.getNumberOfDays(); dayIndex++) {
            for (Map.Entry<Time, LectionData> entry : database.getLectionMapAt(dayIndex).entrySet()) {
                entry.getValue().setGapFillerMarkEnabled(false);
            }
            for (int studentID = 0; studentID < database.getNumberOfStudents(); studentID++) {
                studentListData.getValueAt(studentID, dayIndex + 1).setLectionGapFiller(false);
            }
        }
    }

    private boolean locateGap(Time selectedTime, int dayIndex) {
        LectionGap newGap = createGap(selectedTime, dayIndex);
        if (newGap.equals(gap)) {
            gap.reset();
            return false;
        } else {
            gap = newGap;
            return true;
        }
    }

    private LectionGap createGap(Time selectedTime, int dayIndex) {
        Time start = selectedTime.clone();
        Time end = selectedTime.clone();
        int fieldIndex = dayColumn.getFieldIndexAt(selectedTime);
        while (fieldIndex > 0) {
            if (dayColumn.getFieldDataAt(fieldIndex).isLectionAllocated()) {
                start.inc();
                break;
            }
            start.dec();
            fieldIndex--;
        }
        fieldIndex = dayColumn.getFieldIndexAt(selectedTime);
        while (fieldIndex < dayColumn.getTotalNumberOfFields()) {
            if (dayColumn.getFieldDataAt(fieldIndex).isLectionAllocated()) {
                end.dec();
                break;
            }
            end.inc();
            fieldIndex++;
        }
        return new LectionGap(start, end, dayIndex);
    }
}
