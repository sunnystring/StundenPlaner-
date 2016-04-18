/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleData;

import java.util.ArrayList;
import utils.Time;

/**
 *
 * Einheit einer eingeteilten Lektion, bestehend aus n = Lektionsdauer/5 Zellen
 * (= {@link ScheduleFieldData})
 */
public class LectionData {

    private ArrayList<ScheduleFieldData> lection;
    private int studentID;

    public LectionData() {
        lection = new ArrayList<>();
    }

    public void add(ScheduleFieldData field) {
        lection.add(field);
        studentID = field.getStudentID();
    }

    public ArrayList<ScheduleFieldData> getFieldList() {
        return lection;
    }

    public Time getEnd() {
        return lection.get(lection.size() - 1).getFieldTime();
    }

    public int getLength() {
        return lection.size();
    }

    public void updateStudentID(int deletedStudentID) {
        for (ScheduleFieldData f : lection) {
            if (f.getStudentID() > deletedStudentID) {
                f.decrementStudentID();
            }
        }
    }

    public void updateTeacherTime(boolean teacherTime) {
        for (ScheduleFieldData f : lection) {
            f.setTeacherTimeEnabled(teacherTime);
        }
    }

    public void setGapFillerMarkEnabled(boolean state) {
        for (ScheduleFieldData f : lection) {
            f.setLectionGapFiller(state);
        }
    }

    public int getStudentID() {
        return studentID;
    }
}
