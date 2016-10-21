/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleData;

import core.Database;
import java.util.ArrayList;
import utils.Time;

/**
 *
 * Einheit einer eingeteilten Lektion, bestehend aus n = Lektionsdauer/5 Zellen
 * (= {@link ScheduleFieldData})
 */
public class LectionData {

    private ArrayList<ScheduleFieldData> lection;
    private int profileID;
    private int dayIndex;

    public LectionData(int dayIndex) {
        this.dayIndex = dayIndex;
        lection = new ArrayList<>();
    }

    public void add(ScheduleFieldData field) {
        lection.add(field);
        profileID = field.getProfileID();
    }

    public ArrayList<ScheduleFieldData> getFieldList() {
        return lection;
    }

    public Time start() {
        return lection.get(0).getFieldTime();
    }

    public Time end() {
        return lection.get(lection.size() - 1).getFieldTime();
    }

    public void updateStudentID(int deletedStudentID) {
        if (profileID > deletedStudentID) {
            profileID--;
        }
        for (ScheduleFieldData f : lection) {
            if (f.getProfileID() > deletedStudentID) {
                f.decrementProfileID();
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

    public void setDatabaseReference(Database database) {
        for (ScheduleFieldData field : lection) {
            field.setDatabase(database);
        }
    }

    public int getLength() {
        return lection.size();
    }

    public int getProfileID() {
        return profileID;
    }

    public int getDayIndex() {
        return dayIndex;
    }
}
