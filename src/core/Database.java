/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import attendanceListData.AbsenceTypes;
import java.util.ArrayList;
import java.util.TreeMap;
import scheduleData.LectionData;
import utils.Time;
import static core.ScheduleTimes.DAYS;
import io.FileIO;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import static core.ProfileTypes.*;

/**
 *
 * Verwaltung aller relevanten Daten für die permanente Speicherung:
 * Schülerdatenbestand, Unterrichtstage/-zeiten, eingeteilte Lektionen
 */
public class Database {

    private ScheduleTimes scheduleTimes;
    private ArrayList<Profile> studentDataList;
    private ArrayList<DatabaseListener> databaseListeners;
    private ArrayList<TreeMap<Time, LectionData>> lectionMaps;
    private HashMap<Integer, LectionData> lectionIDMap;
    private ArrayList<ArrayList<StudentDay>> sortedStudentDayLists;
    private ArrayList<HashMap<StudentDay, Integer>> studentIDMaps;
    private ArrayList<String> currentStudentJournals;
    private ArrayList<ArrayList<Integer>> absenceLists;
    private ArrayList<String> weekNames;
    private int numberOfProfiles;
    private int numberOfStudents;

    public Database() {
        scheduleTimes = new ScheduleTimes();
        studentDataList = new ArrayList<>();
        databaseListeners = new ArrayList<>();
        lectionMaps = new ArrayList<>();
        initLectionMaps();
        lectionIDMap = new HashMap<>();
        sortedStudentDayLists = new ArrayList<>();
        studentIDMaps = new ArrayList<>();
        currentStudentJournals = new ArrayList<>();
        absenceLists = new ArrayList<>();
        weekNames = new ArrayList<>();
        numberOfProfiles = 0;
        numberOfStudents = 0;
    }

    private void initLectionMaps() {
        for (int i = 0; i < DAYS; i++) {
            lectionMaps.add(new TreeMap<>());
        }
    }

    public void addProfile(Profile profile) {
        profile.setID(numberOfProfiles);
        studentDataList.add(profile);
        currentStudentJournals.add(numberOfProfiles, "");
        addAbsenceRow();
        numberOfProfiles = studentDataList.size();
        if (profile.isStudent()) {
            numberOfStudents++;
        }
        updateUserUtilsCollections();
        for (DatabaseListener l : databaseListeners) {
            l.profileAdded(numberOfProfiles, profile);
        }
    }

    public void editProfile(Profile profile) {
        updateUserUtilsCollections();
        for (DatabaseListener l : databaseListeners) {
            l.profileEdited(profile);
        }
    }

    public void deleteProfile(Profile profile) {
        currentStudentJournals.remove(profile.getID());
        removeAbsenceRow(profile);
        updateProfileIDs(profile.getID());
        studentDataList.remove(profile);
        numberOfProfiles = studentDataList.size();
        if (profile.isStudent()) {
            numberOfStudents--;
        }
        updateUserUtilsCollections();
        for (DatabaseListener l : databaseListeners) {
            l.profileDeleted(numberOfProfiles, profile);
        }
    }

    private void addAbsenceRow() {
        ArrayList<Integer> absenceList = new ArrayList<>();
        for (int i = 0; i < getNumberOfWeeks(); i++) {
            absenceList.add(AbsenceTypes.EMPTY_LESSON);
        }
        absenceLists.add(absenceList);
    }

    private void removeAbsenceRow(Profile profile) {
        absenceLists.remove(profile.getID());
    }

    private void updateProfileIDs(int deletedProfileID) {
        for (int i = 0; i < numberOfProfiles; i++) {
            if (i > deletedProfileID) {
                Profile profile = studentDataList.get(i);
                profile.setID(i - 1);
                if (profile.getProfileType() == REGULAR_GROUP || profile.getProfileType() == SDG) {
                    ArrayList<Integer> kguMemberIDs = profile.getMemberIDs();
                    if (kguMemberIDs.size() > 0) {
                        for (int j = 0; j < kguMemberIDs.size(); j++) {
                            int memberID = kguMemberIDs.get(j);
                            if (memberID > deletedProfileID) {
                                kguMemberIDs.set(j, memberID - 1);
                            }
                        }
                    }
                }
            }
        }
    }

    public void updateAfterFileEntry(ScheduleTimes scheduleTimes, FileIO fileIO) {
        this.scheduleTimes = scheduleTimes;
        setStudentDataList(fileIO.getStudentDataList());
        setLectionMaps(fileIO.getLectionMaps());
        setCurrentStudentJournals(fileIO.getStudentJournals());
        setAbsenceLists(fileIO.getAttendanceFieldLists());
        setWeekNames(fileIO.getWeekNames());
        updateLections();
        updateNumberOfSingleStudents();
        setScheduleTimesRefToStudentTimes();
        updateUserUtilsCollections();
    }

    private void setScheduleTimesRefToStudentTimes() {
        for (Profile profile : studentDataList) {
            profile.getStudentTimes().setScheduleTimes(scheduleTimes);
        }
    }

    private void updateLections() {
        for (TreeMap<Time, LectionData> map : lectionMaps) {
            for (Map.Entry<Time, LectionData> entry : map.entrySet()) {
                LectionData lection = entry.getValue();
                lection.setDatabaseReference(this);// Workaround wegen lectionData -> scheduleFieldData -> transient database
                lectionIDMap.put(lection.getProfileID(), lection);
            }
        }
    }

    private void updateNumberOfSingleStudents() {
        numberOfProfiles = studentDataList.size();
        numberOfStudents = 0;
        for (Profile profile : studentDataList) {
            if (profile.isStudent()) {
                numberOfStudents++;
            }
        }
    }

    public void adjustStudentDaysToScheduleChange() {
        for (int i = 0; i < numberOfProfiles; i++) {
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
            studentTimes.cleanDaySelectionList(scheduleTimes.getUnvalidDaysAsAbsoluteIndizes());
        }
    }

    public void updateUserUtilsCollections() {
        sortedStudentDayLists.clear();
        studentIDMaps.clear();
        for (int dayIndex = 0; dayIndex < scheduleTimes.getNumberOfValidDays(); dayIndex++) {
            ArrayList<StudentDay> sortedStudentDays = new ArrayList<>();
            HashMap<StudentDay, Integer> studentIDMap = new HashMap<>();
            for (int studentID = 0; studentID < numberOfProfiles; studentID++) {
                StudentDay studentDay = getProfile(studentID).getStudentDay(dayIndex);
                sortedStudentDays.add(studentDay);
                studentIDMap.put(studentDay, studentID);
            }
            Collections.sort(sortedStudentDays);
            sortedStudentDayLists.add(sortedStudentDays);
            studentIDMaps.add(studentIDMap);
        }
    }

    public void addWeek(String weekName) {
        weekNames.add(weekName);
        for (ArrayList<Integer> absenceList : absenceLists) {
            absenceList.add(AbsenceTypes.EMPTY_LESSON);
        }
    }

    public void removeWeek(int weekIndex) {
        weekNames.remove(weekIndex);
        for (ArrayList<Integer> absenceList : absenceLists) {
            absenceList.remove(weekIndex);
        }
    }

    public void renameWeek(int weekIndex, String weekName) {
        weekNames.set(weekIndex, weekName);
    }

    public int getNumberOfProfiles() {
        return numberOfProfiles;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public String getNumberOfStudentsAsString() {
        return String.valueOf(numberOfStudents);
    }

    public int getNumberOfWeeks() {
        return weekNames.size();
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

    public Profile getProfile(int ID) {
        return studentDataList.get(ID);
    }

    public ScheduleTimes getScheduleTimes() {
        return scheduleTimes;
    }

    public void setStudentDataList(ArrayList<Profile> studentDataList) {
        this.studentDataList = studentDataList;
    }

    public ArrayList<Profile> getStudentDataList() {
        return studentDataList;
    }

    public TreeMap<Time, LectionData> getLectionMapAt(int validDayIndex) {
        return lectionMaps.get(scheduleTimes.getAbsoluteDayIndexOf(validDayIndex));
    }

    public TreeMap<Time, LectionData> getLectionMapAt(ScheduleDay scheduleDay) {
        return lectionMaps.get(scheduleTimes.getAbsoluteDayIndexOf(scheduleDay));
    }

    public LectionData getLectionByID(int studentID) {
        return lectionIDMap.get(studentID);
    }

    public String getDayNameAt(int dayIndex) {
        return getScheduleTimes().getValidScheduleDayAt(dayIndex).getDayName();
    }

    public int getNumberOfValidDays() {
        return scheduleTimes.getNumberOfValidDays();
    }

    public void setLectionMaps(ArrayList<TreeMap<Time, LectionData>> lectionMaps) {
        this.lectionMaps = lectionMaps;
    }

    public ArrayList<TreeMap<Time, LectionData>> getLectionMaps() {
        return lectionMaps;
    }

    public HashMap<Integer, LectionData> getLectionIDMap() {
        return lectionIDMap;
    }

    public String getCurrentJournalText(int id) {
        return currentStudentJournals.get(id);
    }

    public void setCurrentJournalText(int id, String text) {
        currentStudentJournals.set(id, text);
    }

    public ArrayList<String> getCurrentStudentJournals() {
        return currentStudentJournals;
    }

    public void setCurrentStudentJournals(ArrayList<String> currentStudentJournals) {
        this.currentStudentJournals = currentStudentJournals;
    }

    public ArrayList<Integer> getAbsenceRowAt(int profileID) {
        return absenceLists.get(profileID);
    }

    public void setAbsenceLists(ArrayList<ArrayList<Integer>> absenceLists) {
        this.absenceLists = absenceLists;
    }

    public ArrayList<ArrayList<Integer>> getAbsenceLists() {
        return absenceLists;
    }

    public String getWeekNameAt(int weekIndex) {
        return weekNames.get(weekIndex);
    }

    public ArrayList<String> getWeekNames() {
        return weekNames;
    }

    public void setWeekNames(ArrayList<String> weekNames) {
        this.weekNames = weekNames;
    }
}
