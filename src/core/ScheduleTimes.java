/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import dataEntryUI.ScheduleInputMask;
import exceptions.IllegalDayEntryException;
import exceptions.IllegalTimeSlotException;
import exceptions.NoEntryException;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * Gesamtheit aller Unterrichtstage (vom Lehrer vorgegeben), TableModel für
 * {@link ScheduleInputMask}
 */
public class ScheduleTimes extends AbstractTableModel {

    private static final int DAYS = 6, COLUMNS = 3;
    private ScheduleDay[] daySelectionList;
    private static final String[] COLUMN_LABELS = {" ", "von", "bis"};
    private static final String[] WEEKDAY_NAMES = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"};
    private ArrayList<ScheduleDay> validScheduleDayList;
    private String illegalDayEntries;

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

    public boolean verifyInput() {
        boolean allSlotsEmpty = true;
        boolean allSlotsValid = true;
        for (int i = 0; i < daySelectionList.length; i++) {
            if (!daySelectionList[i].isEmpty()) {
                allSlotsEmpty = false;
            }
            if (daySelectionList[i].hasInvalidTimeSlots()) {
                allSlotsValid = false;
            }
        }
        if (allSlotsEmpty) {
            throw new NoEntryException();
        }
        if (!allSlotsValid) {
            throw new IllegalTimeSlotException();
        }
        return allSlotsValid;
    }

    // ein bestehender Unterrichtstag darf nicht ohne Bestätigung gelöscht werden
    public void verifyDayEntries() {
        boolean allDaysLegal = true;
        illegalDayEntries = " ";
        for (int i = 0; i < validScheduleDayList.size(); i++) {
            for (int j = 0; j < daySelectionList.length; j++) {
                if (validScheduleDayList.get(i).getDayName() == daySelectionList[j].getDayName()) {
                    if (!validScheduleDayList.get(i).isEmpty() && daySelectionList[j].isEmpty()) {
                        allDaysLegal = false;
                        illegalDayEntries += validScheduleDayList.get(i).getDayName() + " ";
                    }
                }
            }
        }
        if (!allDaysLegal) {
            throw new IllegalDayEntryException();
        }
    }

    public void setValidScheduleDays() {
        for (int i = 0; i < DAYS; i++) {
            if (isValidScheduleDay(i)) {
                daySelectionList[i].setDayName(WEEKDAY_NAMES[i]);
                validScheduleDayList.add(daySelectionList[i].clone()); // Mapping: 1. Unterrichtstag = 0 usw.
            }
        }
    }

    public void updateValidScheduleDays() {
        validScheduleDayList.clear();
        setValidScheduleDays();
    }

    public void restoreSelectionTable() {
        for (int i = 0; i < validScheduleDayList.size(); i++) {
            for (int j = 0; j < daySelectionList.length; j++) {
                if (validScheduleDayList.get(i).getDayName() == daySelectionList[j].getDayName()) {
                    daySelectionList[j] = validScheduleDayList.get(i).clone();
                }
            }
        }
        fireTableDataChanged();
    }

    // noch unbenutzt
    public void cleanScheduleDays() {
        for (ScheduleDay d : daySelectionList) {
            d.cleanTimeSlots();
        }
        validScheduleDayList.clear();
    }

    public boolean isEmpty() {
        return validScheduleDayList.isEmpty();
    }

    public boolean isValidScheduleDay(int i) {
        return !daySelectionList[i].isEmpty();
    }

    public ScheduleDay getValidScheduleDay(int i) {
        return validScheduleDayList.get(i);
    }

    public int getNumberOfDays() {
        return validScheduleDayList.size();
    }

    public String getIllegalDayEntries() {
        return illegalDayEntries;
    }

}
