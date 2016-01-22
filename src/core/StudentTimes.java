/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in thed editor.
 */
package core;

import dataEntryUI.StudentInputMask;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * Gesamtheit aller Unterrichtstage mit den verfügbaren Schülerzeiten,
 * TableModel für {@link StudentInputMask}
 */
public class StudentTimes extends AbstractTableModel {

    private static final int DAYS = 6, COLUMNS = 6;
    private ScheduleTimes scheduleTimes;
    private StudentDay[] daySelectionList;
    private static final String[] COLUMN_LABELS = {" ", "von", "bis*", "von", "bis*", "Wunschzeit*"};
    private static final String[] WEEKDAY_NAMES = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"};
    private ArrayList<StudentDay> validStudentDayList;

    public StudentTimes() {
        daySelectionList = new StudentDay[DAYS];
        for (int i = 0; i < DAYS; i++) {
            daySelectionList[i] = new StudentDay(COLUMNS - 1); // ohne 1. Spalte
        }
        validStudentDayList = new ArrayList<>();
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
        return col > 0 && scheduleTimes.isValidScheduleDay(row);
    }

    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0:
                return WEEKDAY_NAMES[row];
            case 1:
                return daySelectionList[row].getStartTime1();
            case 2:
                return daySelectionList[row].getEndTime1();
            case 3:
                return daySelectionList[row].getStartTime2();
            case 4:
                return daySelectionList[row].getEndTime2();
            case 5:
                return daySelectionList[row].getFavorite();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object o, int row, int col) {
        String timeString = (String) o;
        daySelectionList[row].setTimeSlot(timeString, col);
    }

    public boolean checkAndCorrectTimeEntries() {
        boolean allSlotsValid = true;
        for (int i = 0; i < daySelectionList.length; i++) {
            if (daySelectionList[i].hasInvalidTimeSlots()) {
                allSlotsValid = false;
                daySelectionList[i].correctInvalidTimeSlots();
            }
        }
        fireTableDataChanged();
        if (!allSlotsValid) {
            throw new IllegalStateException();
        }
        return allSlotsValid;
    }

    public void setValidStudentDays() {
        for (int i = 0; i < DAYS; i++) {
            daySelectionList[i].setSingleLections(); // falls solche gesetzt: endTime = startTime
            if (scheduleTimes.isValidScheduleDay(i)) {
                validStudentDayList.add(daySelectionList[i]); // Mapping: 1. StudentDay = 0 usw.
            }
        }
    }

    public void updateValidStudentDays() {
        resetStudentDays();
        setValidStudentDays();
    }

    private void resetStudentDays() {
        validStudentDayList.clear();
    }

    public StudentDay getValidStudentDay(int i) {
        return validStudentDayList.get(i);
    }

    public void setScheduleTimes(ScheduleTimes scheduleTimes) {
        this.scheduleTimes = scheduleTimes;
    }
}
