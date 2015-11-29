/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleData;

import core.Student;
import util.Time;

/**
 *
 * @author Mathias
 */
public class ScheduleFieldData {

    private Student student;
    private Time time; // TimeColumn
    // GUI-Management
    private int validTime, allocatedTime;
    private boolean teacherTime;  // Unterrichtszeit
    private boolean lectionAllocated; // Lection gesetzt
    private boolean scheduleEnabled; // einteilbarer Bereich
    private int lectionPanelAreaMark;
    private int lectionContent;

    // Werte von validTimeMark und allocatedTime
    public static final int TIME_INTERVAL_1 = 1, TIME_INTERVAL_2 = 2, FAVORITE = 3;
    // Werte von lectionPanelAreaMark
    public static final int HEAD = 4, CENTER = 5, SECOND_LAST_ROW = 6, LAST_ROW = 7;
    // Werte von lectionContent
    public static final int FIRST_NAME = 7, NAME = 8;
    public static final int NULL_VALUE = -1;

    public ScheduleFieldData() {

        validTime = NULL_VALUE;
        lectionAllocated = false;
        scheduleEnabled = false;
        resetPanelAreaMarks();
    }

    public void resetPanelAreaMarks() {
        lectionContent = NULL_VALUE;
        lectionPanelAreaMark = NULL_VALUE;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Time getTime() {
        return time;
    }

    public String getMinute(int index) {
        return String.valueOf(time.getMinute());
    }

    public String getHour(int index) {
        return String.valueOf(time.getHour());
    }

    // GUI-Management 
    public void setValidTime(int validTime) {
        this.validTime = validTime;
    }

    public int getValidTime() {
        return validTime;
    }

    public boolean isValidTime() {
        return getValidTime() != NULL_VALUE;
    }

    public void setAllocatedTime(int allocatedTime) {
        this.allocatedTime = allocatedTime;
    }

    public int getAllocatedTime() {
        return allocatedTime;
    }

    public boolean isMinute(int index) {
        return time.getMinute() != 0;
    }

    public boolean isTeacherTime() {
        return teacherTime;
    }

    public void setTeacherTime(boolean teacherTime) {
        this.teacherTime = teacherTime;
    }

    public boolean isScheduleEnabled() {
        return scheduleEnabled;
    }

    public void setScheduleEnabled(boolean scheduleEnabled) {
        this.scheduleEnabled = scheduleEnabled;
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

    public void setLectionContent(int lectionContent) {
        this.lectionContent = lectionContent;
    }

    public int getLectionContent() {
        return lectionContent;
    }
}
