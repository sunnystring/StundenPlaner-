/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleData;

import core.Database;
import core.Student;
import scheduleUI.TimeTable;
import utils.Time;

/**
 *
 * Kleinste Einheit des Stundenplans und Model einer Zelle von {@link TimeTable}
 * mit allen relevanten Student- und Time-Daten
 */
public class ScheduleFieldData {

    public static final int TIME_INTERVAL_1 = 1, TIME_INTERVAL_2 = 2, FAVORITE = 3;
    public static final int HEAD = 4, CENTER = 5, SECOND_LAST_ROW = 6, LAST_ROW = 7;
    public static final int FIRST_NAME = 7, NAME = 8;
    public static final int NO_VALUE = -1;
    private Database database;
    private int tempStudentID;
    private Time fieldTime;
    private int validTimeMark;
    private int allocatedTimeMark;
    private boolean teacherTime;
    private boolean lectionAllocated;
    private boolean moveEnabled;
    private int lectionPanelAreaMark;
    private int nameMark;

    public ScheduleFieldData(Database database) {
        this.database = database;
        lectionAllocated = false;
        moveEnabled = false;
        resetTimeMarks();
        resetPanelAreaMarks();
    }

    public void resetPanelAreaMarks() {
        nameMark = NO_VALUE;
        lectionPanelAreaMark = NO_VALUE;
    }

    public void resetTimeMarks() {
        validTimeMark = NO_VALUE;
        allocatedTimeMark = NO_VALUE;
    }

    public void decrementLocalStudentID() {
        tempStudentID--;
    }

    public void setTempStudentID(int tempStudentID) {
        this.tempStudentID = tempStudentID;
    }

    public int getTempStudentID() {
        return tempStudentID;
    }

    public Student getStudent() {
        return database.getStudent(tempStudentID);
    }

    public void setFieldTime(Time fieldTime) {
        this.fieldTime = fieldTime;
    }

    public Time getFieldTime() {
        return fieldTime;
    }

    public String getMinute(int index) {
        return String.valueOf(fieldTime.getMinute());
    }

    public String getHour(int index) {
        return String.valueOf(fieldTime.getHour());
    }

    public void setValidTimeMark(int validTimeMark) {
        this.validTimeMark = validTimeMark;
    }

    public int getValidTimeMark() {
        return validTimeMark;
    }

    public boolean isValidTime() {
        return getValidTimeMark() != NO_VALUE;
    }

    public void setAllocatedTimeMark(int allocatedTimeMark) {
        this.allocatedTimeMark = allocatedTimeMark;
    }

    public int getAllocatedTimeMark() {
        return allocatedTimeMark;
    }

    public boolean isMinute(int index) {
        return fieldTime.getMinute() != 0;
    }

    public boolean isTeacherTime() {
        return teacherTime;
    }

    public void setTeacherTimeEnabled(boolean teacherTime) {
        this.teacherTime = teacherTime;
    }

    public boolean isMoveEnabled() {
        return moveEnabled;
    }

    public void setMoveEnabled(boolean moveEnabled) {
        this.moveEnabled = moveEnabled;
    }

    public boolean isLectionAllocated() {
        return lectionAllocated;
    }

    public void setLectionAllocated(boolean lectionAllocated) {
        this.lectionAllocated = lectionAllocated;
    }

    public void setLectionPanelAreaMark(int lectionPanelAreaMark) {
        this.lectionPanelAreaMark = lectionPanelAreaMark;
    }

    public int getLectionPanelAreaMark() {
        return lectionPanelAreaMark;
    }

    public boolean isHead() {
        return lectionPanelAreaMark == HEAD;
    }

    public void setNameMark(int nameMark) {
        this.nameMark = nameMark;
    }

    public int getNameMark() {
        return nameMark;
    }
}
