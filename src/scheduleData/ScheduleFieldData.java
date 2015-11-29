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
    private int validTimeMark;
    private boolean teacherTime;  // Unterrichtszeit
    private boolean lectionAllocated; // Lection gesetzt
    private boolean scheduleEnabled; // einteilbarer Bereich
    private boolean lastRow;
    private int lectionPanelArea;
    private int lectionContent;
    // Werte von validTimeMark
    public static final int TIME_INTERVAL_1 = 1, TIME_INTERVAL_2 = 2, FAVORITE = 3;
    // Werte von lectionPanelArea
    public static final int HEAD = 4, CENTER = 5, BOTTOM = 6;
    // Werte von lectionContent
    public static final int FIRST_NAME = 7, NAME = 8;
    public static final int NO_VALUE = -2;

    public ScheduleFieldData() {

        validTimeMark = NO_VALUE;
        lectionAllocated = false;
        scheduleEnabled = false;
        lastRow = false;
        lectionPanelArea = NO_VALUE;
        lectionContent = NO_VALUE;
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
    public void setValidTimeMark(int validTimeMark) {
        this.validTimeMark = validTimeMark;
    }

    public int getValidTimeMark() {
        return validTimeMark;
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

    public void setLectionPanelArea(int lectionPanelArea) {
        this.lectionPanelArea = lectionPanelArea;
    }

    public int getLectionPanelArea() {
        return lectionPanelArea;
    }

    public void setLastRow(boolean lastRow) {
        this.lastRow = lastRow;
    }

    public boolean isLastRow() {
        return lastRow;
    }

    public void setLectionContent(int lectionContent) {
        this.lectionContent = lectionContent;
    }

    public int getLectionContent() {
        return lectionContent;
    }
}
