/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceList;

import core.Database;
import core.Profile;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.swing.table.AbstractTableModel;
import scheduleData.LectionData;
import utils.Time;

/**
 *
 * @author mathiaskielholz
 */
public class AttendanceListData extends AbstractTableModel {

    private Database database;
    private ArrayList<ArrayList<Integer>> fieldDataMatrix;
    private ArrayList<String> names;
    private int numberOfWeeks;

    public AttendanceListData(Database database) {
        fieldDataMatrix = new ArrayList<>();
        names = new ArrayList<>();
        this.database = database;
        numberOfWeeks = 0;
    }

    public void update() {
        for (int i = 0; i < database.getNumberOfDays(); i++) {
            TreeMap<Time, LectionData> lectionMap = database.getLectionMaps().get(i);
            for (LectionData lectionData : lectionMap.values()) {
                Profile profile = database.getProfile(lectionData.getProfileID());
                if (profile.isStudent()) {
                    addFields(profile);
                } else if (profile.isSelectableMemberGroup()) {
                    for (Integer memberID : profile.getMemberIDs()) {
                        addFields(database.getProfile(memberID));
                    }
                }
            }
        }
        numberOfWeeks = database.getNumberOfWeeks();
    }

    private void addFields(Profile student) {
        names.add(student.getFirstName() + " " + student.getName());
        fieldDataMatrix.add(database.getAttendanceFieldsAt(student.getID()));
    }

    @Override
    public int getRowCount() {
        return database.getNumberOfStudents();
    }

    @Override
    public int getColumnCount() {
        return numberOfWeeks + 1;
    }

    @Override
    public String getColumnName(int col) {
        if (col == 0) {
            return "Vorname Name";
        } else {
            return "12.12.";
        }
    }

    @Override
    public Class<?> getColumnClass(int col) {
        if (col == 0) {
            return String.class;
        } else {
            return AttendanceFieldData.class;
        }
    }

    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return names.get(row);
        } else {
            return fieldDataMatrix.get(row).get(col);
        }
    }
}
