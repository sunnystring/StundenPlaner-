/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceList;

/**
 *
 * @author mathiaskielholz
 */
public class AttendanceFieldData {

    private int absenceType;
    private int profileID;
    private String nameString;
    private boolean dayMarked;

    public AttendanceFieldData() {
        absenceType = AbsenceTypes.EMPTY_LESSON;
    }

    public void setNextAbsenceType() {
        if (absenceType < AbsenceTypes.EMPTY_LESSON) {
            absenceType++;
        } else {
            absenceType = 0;
        }
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

    public void setDayMarked(boolean dayMarked) {
        this.dayMarked = dayMarked;
    }

    public boolean isDayMarked() {
        return dayMarked;
    }

}
