/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userUtils;

import core.Database;
import core.ScheduleTimes;
import core.StudentDay;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import scheduleData.DayColumnData;
import scheduleData.LectionData;
import scheduleData.ScheduleData;
import studentListData.StudentFieldData;
import studentListData.StudentListData;
import utils.Time;

/**
 *
 * Findet die Schülerzeiten zu denen nicht gleichzeitig eingeteilt werden kann
 * oder die durch eine Einteilung gesperrt sind, die entsprechenden
 * Felder/Schrift werden rot markiert
 *
 */
public class IncompatibleStudentTimes {

    private Database database;
    private StudentListData studentListData;
    private ScheduleTimes scheduleTimes;
    private ScheduleData scheduleData;

    public IncompatibleStudentTimes(Database database, StudentListData studentListData) {
        this.studentListData = studentListData;
        this.database = database;
        scheduleTimes = database.getScheduleTimes();
    }

    public void resetAllStudentFields() {
        for (int dayIndex = 0; dayIndex < scheduleTimes.getNumberOfValidDays(); dayIndex++) {
            resetStudentFieldsAt(dayIndex);
        }
    }

    private void resetStudentFieldsAt(int dayIndex) {
        for (int i = 0; i < database.getNumberOfStudents(); i++) {
            StudentFieldData studentFieldData = studentListData.getValueAt(i, dayIndex + 1);
            studentFieldData.setIncompatible(false);
            studentFieldData.setBlocked(false);
            studentFieldData.setUnallocatable(false);
        }
    }

    public void findBlockingScheduleTimes() {
        for (int dayIndex = 0; dayIndex < scheduleTimes.getNumberOfValidDays(); dayIndex++) {
            TreeMap<Time, LectionData> lectionMap = database.getLectionMapAt(dayIndex);
            if (!lectionMap.isEmpty()) {
                ArrayList<StudentDay> dayList = database.getSortedStudentDayListAt(dayIndex);
                DayColumnData dayColumn = scheduleData.getDayColumn(dayIndex);
                for (Map.Entry<Time, LectionData> entry : lectionMap.entrySet()) {
                    Time startSearchLection = entry.getKey();
                    Time endSearchLection = entry.getValue().end();
                    for (int i = 0; i < dayList.size(); i++) {
                        StudentDay searchDay = dayList.get(i);
                        if (!searchDay.isEmpty()) {
                            int searchStudentID = database.getStudentID(dayIndex, searchDay);
                            StudentFieldData searchField = studentListData.getValueAt(searchStudentID, dayIndex + 1);
                            if (!searchField.isStudentAllocated()) {
                                int searchLectionLength = database.getStudent(searchStudentID).getLectionLengthInFields();
                                boolean withinValidBounds = searchDay.earliestStart().lessEqualsThan(endSearchLection)
                                        || searchDay.latestEnd().plusLengthOf(searchLectionLength).greaterEqualsThan(startSearchLection);
                                if (withinValidBounds) {
                                    boolean blocked = searchDay.isBlocked(dayColumn, searchLectionLength);
                                    searchField.setBlocked(blocked);
                                    searchField.setIncompatible(blocked);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void findAll() {
        for (int dayIndex = 0; dayIndex < scheduleTimes.getNumberOfValidDays(); dayIndex++) {
            int refIndex = 0;
            ArrayList<StudentDay> dayList = database.getSortedStudentDayListAt(dayIndex);
            while (refIndex < dayList.size()) {
                StudentDay refDay = dayList.get(refIndex);
                int refStudentID = database.getStudentID(dayIndex, refDay);
                StudentFieldData refField = studentListData.getValueAt(refStudentID, dayIndex + 1);
                if (!refDay.isEmpty() && !refField.isStudentAllocated()) {
                    int refLectionLength = database.getStudent(refStudentID).getLectionLengthInFields();
                    int searchIndex = refIndex + 1;
                    while (searchIndex < dayList.size()) {
                        StudentDay searchDay = dayList.get(searchIndex);
                        int searchStudentID = database.getStudentID(dayIndex, searchDay);
                        StudentFieldData searchField = studentListData.getValueAt(searchStudentID, dayIndex + 1);
                        if (!searchField.isStudentAllocated() && searchDay.isWithin(refDay, refLectionLength)) {
                            searchDay = dayList.get(searchIndex);
                            searchStudentID = database.getStudentID(dayIndex, searchDay);
                            searchField = studentListData.getValueAt(searchStudentID, dayIndex + 1);
                            int searchLectionLength = database.getStudent(searchStudentID).getLectionLengthInFields();
                            boolean incompatible = searchDay.isIncompatibleTo(refDay, refLectionLength, searchLectionLength);
                            boolean unallocatable = incompatible && refField.isSingleDay() && searchField.isSingleDay();
                            if (!refField.isIncompatible()) {
                                refField.setIncompatible(incompatible);
                            }
                            if (!refField.isUnallocatable()) {
                                refField.setUnallocatable(unallocatable);
                            }
                            if (!searchField.isIncompatible()) {
                                searchField.setIncompatible(incompatible);
                            }
                            if (!searchField.isUnallocatable()) {
                                searchField.setUnallocatable(unallocatable);
                            }
                        }
                        searchIndex++;
                    }
                    refIndex++;
                } else {
                    refIndex++;
                }
            }
        }
    }

    public void setScheduleData(ScheduleData scheduleData) {
        this.scheduleData = scheduleData;
    }
}
