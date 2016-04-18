/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.ArrayList;
import java.util.TreeMap;
import scheduleData.LectionData;
import utils.Time;
import static core.ScheduleTimes.DAYS;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * Verwaltung aller relevanten Daten für die permanente Speicherung:
 * Schülerdatenbestand, Unterrichtstage/-zeiten, eingeteilte Lektionen
 */
public class Database {

    private final ScheduleTimes scheduleTimes;
    private final ArrayList<Student> studentDataList;
    private final ArrayList<DatabaseListener> databaseListeners;
    private final ArrayList<TreeMap<Time, LectionData>> lectionMaps;
    private final HashMap<Integer, LectionData> globalLectionMap;
    private final ArrayList<ArrayList<StudentDay>> sortedStudentDayLists;
    private final ArrayList<HashMap<StudentDay, Integer>> studentIDMaps;
    private int numberOfStudents;

    public Database() {
        scheduleTimes = new ScheduleTimes();
        studentDataList = new ArrayList<>();
        databaseListeners = new ArrayList<>();
        lectionMaps = new ArrayList<>();
        globalLectionMap = new HashMap<>();
        for (int i = 0; i < DAYS; i++) {
            lectionMaps.add(new TreeMap());
        }
        sortedStudentDayLists = new ArrayList<>();
        studentIDMaps = new ArrayList<>();
        numberOfStudents = 0;
    }

    public void addStudent(Student student) {
        student.setStudentID(numberOfStudents);
        studentDataList.add(student);
        numberOfStudents = studentDataList.size(); // nächster Student
        updateUserUtilsCollections();
        for (DatabaseListener l : databaseListeners) {
            l.studentAdded(numberOfStudents, student);
        }
    }

    public void editStudent(Student student) {
        updateUserUtilsCollections();
        for (DatabaseListener l : databaseListeners) {
            l.studentEdited(student);
        }
    }

    public void deleteStudent(Student student) {
        studentDataList.remove(student);
        numberOfStudents = studentDataList.size(); // = numberOfStudents--
        updateStudentIDs();
        updateUserUtilsCollections();
        for (DatabaseListener l : databaseListeners) {
            l.studentDeleted(numberOfStudents, student);
        }
    }

    private void updateStudentIDs() {
        for (int i = 0; i < numberOfStudents; i++) {
            studentDataList.get(i).setStudentID(i);
        }
    }

    public void update() {
        updateStudentDays();
        updateUserUtilsCollections();
    }

    private void updateStudentDays() {
        for (int i = 0; i < numberOfStudents; i++) {
            StudentTimes studentTimes = studentDataList.get(i).getStudentTimes();
            ArrayList<StudentDay> tempStudentDayList = new ArrayList<>();
            for (int j = 0; j < scheduleTimes.getNumberOfValidDays(); j++) {
                StudentDay studentDay;
                Integer oldDayIndex = scheduleTimes.getSharedDayIndexOf(j);
                if (oldDayIndex != null) {
                    studentDay = studentTimes.getValidStudentDay(oldDayIndex);
                } else {
                    studentDay = new StudentDay();
                    studentDay.setDayName(scheduleTimes.getValidScheduleDayAt(j).getDayName());
                }
                tempStudentDayList.add(studentDay);
            }
            studentTimes.setValidStudentDayList(tempStudentDayList);
        }
    }

    private void updateUserUtilsCollections() {
        sortedStudentDayLists.clear();
        studentIDMaps.clear();
        for (int dayIndex = 0; dayIndex < scheduleTimes.getNumberOfValidDays(); dayIndex++) {
            ArrayList<StudentDay> sortedStudentDays = new ArrayList<>();
            HashMap<StudentDay, Integer> studentIDMap = new HashMap<>();
            for (int studentID = 0; studentID < numberOfStudents; studentID++) {
                StudentDay studentDay = getStudent(studentID).getStudentDay(dayIndex);
                sortedStudentDays.add(studentDay);
                studentIDMap.put(studentDay, studentID);
            }
            Collections.sort(sortedStudentDays);
            sortedStudentDayLists.add(sortedStudentDays);
            studentIDMaps.add(studentIDMap);
        }
    }

    public ArrayList<StudentDay> getSortedStudentDayListAt(int dayIndex) {
        return sortedStudentDayLists.get(dayIndex);
    }

    public int getStudentID(int dayIndex, StudentDay studentDay) {
        return studentIDMaps.get(dayIndex).get(studentDay);
    }

    public void addDatabaseListener(DatabaseListener l) {
        databaseListeners.add(l);
    }

    public Student getStudent(int ID) {
        return studentDataList.get(ID);
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public ScheduleTimes getScheduleTimes() {
        return scheduleTimes;
    }

    public ArrayList<Student> getStudentDataList() {
        return studentDataList;
    }

    public TreeMap<Time, LectionData> getLectionMapAt(int validDayIndex) {
        return lectionMaps.get(scheduleTimes.getAbsoluteDayIndexOf(validDayIndex));
    }

    public TreeMap<Time, LectionData> getLectionMapAt(ScheduleDay scheduleDay) {
        return lectionMaps.get(scheduleTimes.getAbsoluteDayIndexOf(scheduleDay));
    }

    public HashMap<Integer, LectionData> getGlobalLectionMap() {
        return globalLectionMap;
    }

    public LectionData getLection(int studentID) {
        return globalLectionMap.get(studentID);
    }

    public String getDayNameAt(int dayIndex) {
        return getScheduleTimes().getValidScheduleDayAt(dayIndex).getDayName();
    }

    public int getNumberOfDays() {
        return scheduleTimes.getNumberOfValidDays();
    }
}
