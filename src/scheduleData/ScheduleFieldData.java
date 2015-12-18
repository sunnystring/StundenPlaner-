/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleData;

import core.Student;
import scheduleUI.TimeTable;
import util.Time;

/**
 *
 * Model einer Zelle von {@link TimeTable} 
 */
public class ScheduleFieldData {

    public static final int TIME_INTERVAL_1 = 1, TIME_INTERVAL_2 = 2, FAVORITE = 3;
    public static final int HEAD = 4, CENTER = 5, SECOND_LAST_ROW = 6, LAST_ROW = 7;
    public static final int FIRST_NAME = 7, NAME = 8;
    public static final int NO_VALUE = -1;
    private Student student;
    private Time time;
    private int validTimeMark;
    private int allocatedTimeMark;
    private boolean teacherTime;
    private boolean lectionAllocated;
    private boolean moveEnabled;
    private int lectionPanelAreaMark;
    private int nameMark;

    public ScheduleFieldData() {
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
        return time.getMinute() != 0;
    }

    public boolean isTeacherTime() {
        return teacherTime;
    }

    public void setTeacherTime(boolean teacherTime) {
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
