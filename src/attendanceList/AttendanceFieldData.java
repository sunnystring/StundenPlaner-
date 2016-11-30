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
    private int studentID;
    private int weekID;

    public void setAbsenceType(int absenceType) {
        this.absenceType = absenceType;
    }

    public int getAbsenceType() {
        return absenceType;
    }

}
