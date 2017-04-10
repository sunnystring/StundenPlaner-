/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceListData;

import attendanceListUI.AttendanceField;

/**
 *
 * Model von {@link AttendanceField}
 */
public class AttendanceFieldData {

    private int absenceType;
    private String absenceTypeString;
    private int profileID;
    private String nameString;
    private boolean dayMarked;
    private boolean lectionReplaced;

    public AttendanceFieldData() {
        absenceType = AbsenceTypes.EMPTY_LESSON;
        absenceTypeString = "";
        lectionReplaced = false;
    }

    public void setNextAbsenceType() {
        if (absenceType < AbsenceTypes.EMPTY_LESSON) {
            absenceType++;
        } else {
            absenceType = 0;
        }
            setAbsenceTypeCharacter();
            lectionReplaced = false;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public int getProfileID() {
        return profileID;
    }

    public void setNameString(String nameString) {
        this.nameString = nameString;
    }

    public String getNameString() {
        return nameString;
    }

    public void setAbsenceType(int absenceType) {
        this.absenceType = absenceType;
    }

    public int getAbsenceType() {
        return absenceType;
    }

    public void setAbsenceTypeString(String absenceTypeString) {
        this.absenceTypeString = absenceTypeString;
    }

    public String getAbsenceTypeString() {
        return absenceTypeString;
    }

    public void setAbsenceTypeCharacter() {
        absenceTypeString = AbsenceTypes.getCharacterOf(absenceType);
    }

    public void setDayMarked(boolean dayMarked) {
        this.dayMarked = dayMarked;
    }

    public boolean isDayMarked() {
        return dayMarked;
    }

    public boolean isReplaceableAbsenceType() {
        return absenceType == AbsenceTypes.TEACHER
                || absenceType == AbsenceTypes.OFFICIAL
                || absenceType == AbsenceTypes.STUDENT_REPORTED;
    }

    public boolean isLectionReplaced() {
        return lectionReplaced;
    }

    public void setLectionReplaced(boolean replacedLection) {
        this.lectionReplaced = replacedLection;
    }
}
