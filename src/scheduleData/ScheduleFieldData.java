/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleData;

import core.Database;
import core.Profile;
import scheduleUI.TimeTable;
import static scheduleData.ScheduleFieldConstants.*;
import utils.Time;

/**
 *
 * Kleinste Einheit des Stundenplans und Model einer Zelle von {@link TimeTable}
 * mit allen relevanten Student- und Time-Daten
 */
public class ScheduleFieldData {

    private transient Database database;
    private int profileID;
    private int lectionProfileType;
    private Time fieldTime;
    private int validTimeMark;
    private int allocatedTimeMark;
    private boolean teacherTime;
    private boolean lectionAllocated;
    private boolean moveEnabled;
    private boolean lectionGapFiller;
    private int lectionPanelAreaMark;
    private int nameMark;

    public ScheduleFieldData(Database database) {
        this.database = database;
        lectionAllocated = false;
        moveEnabled = false;
        lectionGapFiller = false;
        resetTimeMarks();
        resetPanelAreaMarks();
    }

    public void resetPanelAreaMarks() {
        nameMark = UNVALID_VALUE;
        lectionPanelAreaMark = UNVALID_VALUE;
    }

    public void resetTimeMarks() {
        validTimeMark = UNVALID_VALUE;
        allocatedTimeMark = UNVALID_VALUE;
    }

    public void decrementProfileID() {
        profileID--;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public int getProfileID() {
        return profileID;
    }

    public Profile getProfile() {
        return database.getProfile(profileID);
    }

    public void setLectionProfileType(int lectionProfileType) {
        this.lectionProfileType = lectionProfileType;
    }

    public int getLectionProfileType() {
        return lectionProfileType;
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
        return getValidTimeMark() != UNVALID_VALUE;
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

    public void setLectionGapFiller(boolean lectionGapFiller) {
        this.lectionGapFiller = lectionGapFiller;
    }

    public boolean isLectionGapFiller() {
        return lectionGapFiller;
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

    public void setDatabase(Database database) {
        this.database = database;
    }
}
