/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceList;

import core.Database;
import core.Profile;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.swing.table.AbstractTableModel;
import scheduleData.LectionData;
import utils.Time;

/**
 *
 * @author mathiaskielholz
 */
public class AttendanceListData extends AbstractTableModel implements MouseListener {

    private Database database;
    private ArrayList<ArrayList<AttendanceFieldData>> fieldDataMatrix;
    private TreeMap<String, Integer> weekIndices;
    private int numberOfWeeks;
    private int numberOfValidProfiles;

    public AttendanceListData(Database database) {
        fieldDataMatrix = new ArrayList<>();
        weekIndices = new TreeMap<>();
        this.database = database;
        numberOfWeeks = 0;
    }

    public void update() {
        fieldDataMatrix.clear();
        numberOfValidProfiles = 0;
        numberOfWeeks = database.getNumberOfWeeks();
        initWeekIndices();
        for (int i = 0; i < database.getNumberOfValidDays(); i++) {
            TreeMap<Time, LectionData> lectionMap = database.getLectionMapAt(i);
            for (LectionData lectionData : lectionMap.values()) {
                Profile profile = database.getProfile(lectionData.getProfileID());
                if (profile.isStudent() || profile.isRegularGroup()) {
                    addRow(profile, i);
                    numberOfValidProfiles++;
                } else if (profile.isSelectableMemberGroup()) {
                    for (Integer memberID : profile.getMemberIDs()) {
                        addRow(database.getProfile(memberID), i);
                        numberOfValidProfiles++;
                    }
                }
            }
        }
    }

    private void initWeekIndices() {
        weekIndices.clear();
        for (int i = 0; i < numberOfWeeks; i++) {
            weekIndices.put(database.getWeekNames().get(i), i);
        }
    }

    private void addRow(Profile profile, int dayIndex) {
        ArrayList<AttendanceFieldData> fieldList = new ArrayList<>();
        AttendanceFieldData field = new AttendanceFieldData();
        field.setNameString(profile.getFirstName() + " " + profile.getName());
        fieldList.add(field);
        for (Integer absenceType : database.getAbsenceListAt(profile.getID())) {
            field = new AttendanceFieldData();
            field.setAbsenceType(absenceType);
            field.setProfileID(profile.getID());
            field.setDayMarked(dayIndex % 2 == 1);
            fieldList.add(field);
        }
        fieldDataMatrix.add(fieldList);
    }

    public void saveAbsenceEntries() {
        for (int i = 0; i < getRowCount(); i++) {
            ArrayList<AttendanceFieldData> fieldRow = fieldDataMatrix.get(i);
            for (int j = 1; j < getColumnCount(); j++) {
                AttendanceFieldData field = fieldRow.get(j);
                database.getAbsenceListAt(field.getProfileID()).set(j - 1, field.getAbsenceType());
            }
        }
    }

    @Override
    public int getRowCount() {
        return numberOfValidProfiles;
    }

    @Override
    public int getColumnCount() {
        return numberOfWeeks + 1;
    }

    @Override
    public String getColumnName(int col) {
        if (col == 0) {
            return "  Vorname Name";
        } else {
            return database.getWeekNameAt(col - 1);
        }
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return AttendanceFieldData.class;
    }

    @Override
    public AttendanceFieldData getValueAt(int row, int col) {
        return fieldDataMatrix.get(row).get(col);
    }

    public int getNumberOfValidProfiles() {
        return numberOfValidProfiles;
    }

    public int getWeekIndex(String weekName) {
        return weekIndices.get(weekName);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        AttendanceTable attendanceTable = (AttendanceTable) e.getSource();
        Point p = e.getPoint();
        int selectedRow, selectedCol;
        selectedRow = attendanceTable.rowAtPoint(p);
        selectedCol = attendanceTable.columnAtPoint(p);
        AttendanceFieldData field = (AttendanceFieldData) attendanceTable.getValueAt(selectedRow, selectedCol);
        field.setNextAbsenceType();
        fireTableCellUpdated(selectedRow, selectedCol);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
