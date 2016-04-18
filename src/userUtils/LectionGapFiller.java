/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userUtils;

import core.Database;
import core.Student;
import core.StudentDay;
import java.util.ArrayList;
import java.util.Map;
import scheduleData.DayColumnData;
import scheduleData.LectionData;
import studentListData.StudentListData;
import utils.Time;

/**
 *
 * Beim Klick auf ein nicht eingeteiltes, genug grosses Zeitintervall im Stundenplan
 * werden alle verfügbaren Zeiten angezeigt (als Lection oder StudentDay)
 */
public class LectionGapFiller {

    private final Database database;
    private final DayColumnData dayColumn;
    private final StudentListData studentListData;
    private static Gap gap;

    public LectionGapFiller(Database database, DayColumnData dayColumn, StudentListData studentListData) {
        this.database = database;
        this.dayColumn = dayColumn;
        this.studentListData = studentListData;
        gap = new Gap();
    }

    public void clear() {
        gap.reset();
        clearAllMarks();
    }

    public void showAvailableTimes(Time selectedTime, int dayIndex) {
        clearAllMarks();
        if (locateGap(selectedTime, dayIndex)) { // gap main switch  
            ArrayList<StudentDay> dayList = database.getSortedStudentDayListAt(dayIndex);
            for (int i = 0; i < database.getNumberOfStudents(); i++) {
                StudentDay studentDay = dayList.get(i);
                int studentID = database.getStudentID(dayIndex, studentDay);
                Student student = database.getStudent(studentID);
                if (gap.matchesTimeOf(studentDay, student)) {
                    if (student.isAllocated()) {
                        LectionData lection = database.getLection(studentID);
                        if (lection != null && lection.getStudentID() == studentID) {
                            lection.setGapFillerMarkEnabled(true);
                        }
                    } else {
                        studentListData.getValueAt(studentID, dayIndex + 1).setLectionGapFiller(true);
                    }
                }
            }
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
        Gap newGap = createGap(selectedTime, dayIndex);
        if (newGap.equals(gap)) {
            gap.reset();
            return false;
        } else {
            gap = newGap;
            return true;
        }
    }

    private Gap createGap(Time selectedTime, int dayIndex) {
        Time start = selectedTime.clone();
        Time end = selectedTime.clone();
        int fieldIndex = dayColumn.getFieldIndexAt(selectedTime);
        while (fieldIndex > dayColumn.getFieldCountStart()) {
            if (dayColumn.getFieldDataAt(fieldIndex).isLectionAllocated()) {
                start.inc();
                break;
            }
            start.dec();
            fieldIndex--;
        }
        fieldIndex = dayColumn.getFieldIndexAt(selectedTime);
        while (fieldIndex < dayColumn.getFieldCountEnd() - 1) {
            if (dayColumn.getFieldDataAt(fieldIndex).isLectionAllocated()) {
                end.dec();
                break;
            }
            end.inc();
            fieldIndex++;
        }
        return new Gap(start, end, dayIndex);
    }
}
