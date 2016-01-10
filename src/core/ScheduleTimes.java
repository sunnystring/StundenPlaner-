/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import dataEntryUI.ScheduleEntryMask;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * Gesamtheit aller Unterrichtstage (vom Lehrer vorgegeben), TableModel für
 * {@link ScheduleEntryMask}
 */
public class ScheduleTimes extends AbstractTableModel {

    private static final int DAYS = 6, COLUMNS = 3;
    private ScheduleDay[] daySelectionList;
    private static final String[] COLUMN_LABELS = {" ", "von", "bis"};
    private static final String[] WEEKDAY_NAMES = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"};
    private ArrayList<ScheduleDay> validScheduleDayList;

    public ScheduleTimes() {
        daySelectionList = new ScheduleDay[DAYS];
        for (int i = 0; i < DAYS; i++) {
            daySelectionList[i] = new ScheduleDay();
        }
        validScheduleDayList = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return DAYS;
    }

    @Override
    public int getColumnCount() {
        return COLUMNS;
    }

    @Override
    public String getColumnName(int col) {
        return COLUMN_LABELS[col];
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col > 0;
    }

    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0:
                return WEEKDAY_NAMES[row];
            case 1:
                return daySelectionList[row].getValidStart();
            case 2:
                return daySelectionList[row].getValidEnd();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object o, int row, int col) {
        String timeString = (String) o;
        daySelectionList[row].setTimeSlot(timeString, col);
    }

    public void setValidScheduleDays() {
        for (int i = 0; i < DAYS; i++) {
            if (isValidScheduleDay(i)) {
                daySelectionList[i].setDayName(WEEKDAY_NAMES[i]);
                validScheduleDayList.add(daySelectionList[i]); // Mapping: 1. Unterrichtstag = 0 usw.
            }
        }
    }

    public boolean areTimeEntriesValid() {
        boolean allSlotsValid = true;
        for (int i = 0; i < daySelectionList.length; i++) {
            if (daySelectionList[i].hasInvalidTimeSlots()) {
                allSlotsValid = false;
                daySelectionList[i].cleanInvalidTimeSlots();
            }
        }
        if (!allSlotsValid) {
            JOptionPane.showMessageDialog(null, "Ungültige Zeiteingabe:\n"
                    + "Unterrichtsschluss muss später\n"
                    + "sein als Unterrichtsbeginn!");
        }
        fireTableDataChanged();
        return allSlotsValid;
    }

    public boolean isValidScheduleDay(int i) {
        return !daySelectionList[i].getValidStart().toString().trim().isEmpty();
    }

    public ScheduleDay getValidScheduleDay(int i) {
        return validScheduleDayList.get(i);
    }

    public String getDayName(int i) {
        return validScheduleDayList.get(i).getDayName();
    }

    public int getNumberOfDays() {
        return validScheduleDayList.size();
    }

}
