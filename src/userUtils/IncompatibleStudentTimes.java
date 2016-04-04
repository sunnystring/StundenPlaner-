/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userUtils;

import core.Database;
import core.ScheduleTimes;
import core.Student;
import core.StudentDay;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
 * Findet die Schülerzeiten, zu denen nicht gleichzeitig eingeteilt werden kann,
 * die entsprechenden Felder werden rot markiert
 */
public class IncompatibleStudentTimes {

    private final Database database;
    private final StudentListData studentListData;
    private final ScheduleTimes scheduleTimes;
    private ScheduleData scheduleData;
    private final ArrayList<ArrayList<StudentDay>> sortedStudentDayLists;
    private final ArrayList<HashMap<StudentDay, Integer>> studentIDMaps;

    public IncompatibleStudentTimes(Database database, StudentListData studentListData) {
        this.studentListData = studentListData;
        this.database = database;
        scheduleTimes = database.getScheduleTimes();
        sortedStudentDayLists = new ArrayList<>();
        studentIDMaps = new ArrayList<>();
    }

    public void init(ScheduleData scheduleData) {
        this.scheduleData = scheduleData;
        for (int i = 0; i < scheduleTimes.getNumberOfValidDays(); i++) {
            studentIDMaps.add(new HashMap<>());
            sortedStudentDayLists.add(new ArrayList<>());
        }
    }

    public void update() {
        sortedStudentDayLists.clear();
        studentIDMaps.clear();
        for (int dayIndex = 0; dayIndex < scheduleTimes.getNumberOfValidDays(); dayIndex++) {
            HashMap<StudentDay, Integer> studentIDMap = new HashMap<>();
            ArrayList<StudentDay> sortedStudentDays = new ArrayList<>();
            for (int studentID = 0; studentID < database.getNumberOfStudents(); studentID++) {
                StudentDay studentDay = database.getStudent(studentID).getStudentDay(dayIndex);
                studentIDMap.put(studentDay, studentID);
                sortedStudentDays.add(studentDay);
            }
            studentIDMaps.add(studentIDMap);
            Collections.sort(sortedStudentDays);
            sortedStudentDayLists.add(sortedStudentDays);
        }
    }

    public void updateStudentAdd(Student student) {
        for (int dayIndex = 0; dayIndex < scheduleTimes.getNumberOfValidDays(); dayIndex++) {
            HashMap<StudentDay, Integer> studentIDMap = studentIDMaps.get(dayIndex);
            ArrayList<StudentDay> sortedStudentDays = sortedStudentDayLists.get(dayIndex);
            studentIDMap.put(student.getStudentDay(dayIndex), student.getID());
            sortedStudentDays.add(student.getStudentDay(dayIndex));
            Collections.sort(sortedStudentDays);
            resetStudentFieldsAt(dayIndex);
        }
    }

    public void updateStudentEdit(Student student) {
        for (int dayIndex = 0; dayIndex < scheduleTimes.getNumberOfValidDays(); dayIndex++) {
            Collections.sort(sortedStudentDayLists.get(dayIndex));
            resetStudentFieldsAt(dayIndex);
        }
    }

    public void updateStudentDelete(Student student) {
        update();
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
        }
    }

    public void findBlockedDays() {
        for (int dayIndex = 0; dayIndex < scheduleTimes.getNumberOfValidDays(); dayIndex++) {
            TreeMap<Time, LectionData> lectionMap = database.getLectionMapAt(dayIndex);
            if (!lectionMap.isEmpty()) {
                ArrayList<StudentDay> dayList = sortedStudentDayLists.get(dayIndex);
                DayColumnData dayColumn = scheduleData.getDayColumn(dayIndex);
                for (Map.Entry<Time, LectionData> entry : lectionMap.entrySet()) {
                    Time startSearchLection = entry.getKey();
                    Time endSearchLection = entry.getValue().getEnd();
                    for (int i = 0; i < dayList.size(); i++) {
                        StudentDay searchDay = dayList.get(i);
                        if (!searchDay.isEmpty()) {
                            int searchStudentID = studentIDMaps.get(dayIndex).get(searchDay);
                            StudentFieldData searchField = studentListData.getValueAt(searchStudentID, dayIndex + 1);
                            if (!searchField.isStudentAllocated()) {
                                int searchLectionLength = database.getStudent(searchStudentID).getLectionLength();
                                boolean withinValidBounds = searchDay.getEarliestStart().lessEqualsThan(endSearchLection)
                                        || searchDay.getLatestEnd().plusTimeOf(searchLectionLength).greaterEqualsThan(startSearchLection);
                                if (withinValidBounds) {
                                    boolean result = searchDay.isBlocked(dayColumn, searchLectionLength);
                                    searchField.setBlocked(result);
                                    searchField.setIncompatible(result);
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
            ArrayList<StudentDay> dayList = sortedStudentDayLists.get(dayIndex);
            while (refIndex < dayList.size() - 1) {
                StudentDay refDay = dayList.get(refIndex);
                int refStudentID = studentIDMaps.get(dayIndex).get(refDay);
                StudentFieldData refField = studentListData.getValueAt(refStudentID, dayIndex + 1);
                if (!refDay.isEmpty() && !refField.isStudentAllocated()) {
                    int refLectionLength = database.getStudent(refStudentID).getLectionLength();
                    int searchIndex = refIndex + 1;
                    StudentDay searchDay = dayList.get(searchIndex);
                    int searchStudentID = studentIDMaps.get(dayIndex).get(searchDay);
                    StudentFieldData searchField = studentListData.getValueAt(searchStudentID, dayIndex + 1);
                    while (searchIndex < dayList.size() && !searchField.isStudentAllocated() && searchDay.isWithin(refDay, refLectionLength)) {
                        searchDay = dayList.get(searchIndex);
                        searchStudentID = studentIDMaps.get(dayIndex).get(searchDay);
                        searchField = studentListData.getValueAt(searchStudentID, dayIndex + 1);
                        int searchLectionLength = database.getStudent(searchStudentID).getLectionLength();
                        boolean result = searchDay.isIncompatibleTo(refDay, refLectionLength, searchLectionLength);
                        if (!refField.isIncompatible()) {
                            refField.setIncompatible(result);
                        }
                        if (!searchField.isIncompatible()) {
                            searchField.setIncompatible(result);
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
}
