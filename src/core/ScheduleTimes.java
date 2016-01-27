/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import dataEntryUI.ScheduleInputMask;
import exceptions.DayEraseException;
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

    // public static final int DAYS = 6;
    public static final int COLUMNS = 3;
    private static final String[] COLUMN_LABELS = {" ", "von", "bis"};
    private static final String[] WEEKDAY_NAMES = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"};
    public static final int DAYS = WEEKDAY_NAMES.length;
    private final ScheduleDay[] daySelectionList;

    private final ArrayList<ScheduleDay> validScheduleDayList;

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

    public void validateScheduleDayEntry() {
        boolean hasDaysToBeErased = false;
        String erasedDayString = " ";
        for (int i = 0; i < validScheduleDayList.size(); i++) {  // bestehende Tage
            if (isExistingDayToBeErased(i)) {
                hasDaysToBeErased = true;
                erasedDayString += validScheduleDayList.get(i).getDayName() + " ";
            }
        }
        if (hasDaysToBeErased) {
            throw new DayEraseException(erasedDayString);
        }
    }

    public boolean isExistingDayToBeErased(int i) {
        boolean toBeErased = false;
        for (int j = 0; j < daySelectionList.length; j++) {  // alle Tage
            if (daySelectionList[j].getDayName().equals(validScheduleDayList.get(i).getDayName())) {
                if (!validScheduleDayList.get(i).isEmpty() && daySelectionList[j].isEmpty()) {
                    toBeErased = true;
                }
            }
        }
        return toBeErased;
    }

    public void setValidScheduleDays() {
        for (int i = 0; i < DAYS; i++) {
            if (isValidDay(i)) {
                daySelectionList[i].setDayName(WEEKDAY_NAMES[i]);
                validScheduleDayList.add(daySelectionList[i].clone()); // Mapping: 1. Unterrichtstag = 0 usw.
            }
        }
    }

    public void updateValidScheduleDays() {
        validScheduleDayList.clear();
        setValidScheduleDays();

    }

    public void returnToExistingSelection() {
        for (int i = 0; i < validScheduleDayList.size(); i++) {
            for (int j = 0; j < daySelectionList.length; j++) {
                if (validScheduleDayList.get(i).getDayName().equals(daySelectionList[j].getDayName())) {
                    daySelectionList[j] = validScheduleDayList.get(i).clone();
                }
            }
        }
        fireTableDataChanged();
    }

    public boolean isEmpty() {
        return validScheduleDayList.isEmpty();
    }

    public boolean isValidDay(int i) {
        return !daySelectionList[i].isEmpty();
    }

    public int getNumberOfValidDays() {
        return validScheduleDayList.size();
    }

    public ScheduleDay getValidScheduleDay(int i) {
        return validScheduleDayList.get(i);
    }

    public int getNumberOfSelectedDays() {
        return DAYS;
    }

    public ScheduleDay getSelectedScheduleDay(int i) {
        return daySelectionList[i];
    }

}
