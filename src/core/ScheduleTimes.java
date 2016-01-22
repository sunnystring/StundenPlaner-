/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import dataEntryUI.ScheduleInputMask;
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
    private String erasedDayString;
    public static final int NO_DAY_CHANGED = 0, DAYS_ERASED = 1, DAYS_ADDED = 2, DAYS_ERASED_AND_ADDED = 3;

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

    public int getDayEntryState() {
        int state = NO_DAY_CHANGED;
        boolean erased = false;
        boolean added = false;
        boolean noExistingDay;
        erasedDayString = " ";
        int k = 0;
        for (int i = k; i < daySelectionList.length; i++) {  // Auswahl-Tage
            noExistingDay = true;
            for (int j = 0; j < validScheduleDayList.size(); j++) {  // bestehende Tage
                if (daySelectionList[i].getDayName().equals(validScheduleDayList.get(j).getDayName())) { // falls schon Tag besteht
                    if (!validScheduleDayList.get(j).isEmpty() && daySelectionList[i].isEmpty()) {
                        erased = true;
                        state = DAYS_ERASED;
                        erasedDayString += validScheduleDayList.get(j).getDayName() + " ";
                    }
                    noExistingDay = false;
                    k++;
                    break; // sobald Tag gefunden, nächstfolgender Tag (= k) checken
                }
            }
            if (noExistingDay && !daySelectionList[i].isEmpty()) {  // falls Tag noch nicht besteht
                state = DAYS_ADDED;
                added = true;
            }
        }
        if (!erased && !added) {
            state = NO_DAY_CHANGED;
        }
        if (erased && added) {
            state = DAYS_ERASED_AND_ADDED;
        }
        return state;
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

    public String getErasedDay() {
        return erasedDayString;
    }

}
