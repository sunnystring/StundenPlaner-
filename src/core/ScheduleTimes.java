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
import java.util.HashMap;
import javax.swing.table.AbstractTableModel;

/**
 *
 * Gesamtheit aller Unterrichtstage (vom Lehrer vorgegeben), TableModel für
 * {@link ScheduleInputMask}
 */
public class ScheduleTimes extends AbstractTableModel {

    public static final int COLUMNS = 3;
    private static final String[] COLUMN_LABELS = {" ", "von", "bis"};
    private static final String[] WEEKDAY_NAMES = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"};
    public static final int DAYS = WEEKDAY_NAMES.length;
    private final ScheduleDay[] daySelectionList;
    private final ArrayList<ScheduleDay> validScheduleDayList;
    private final HashMap<Integer, Integer> sharedDayIndicesMap;

    public ScheduleTimes() {
        daySelectionList = new ScheduleDay[DAYS];
        for (int i = 0; i < DAYS; i++) {
            daySelectionList[i] = new ScheduleDay();
        }
        validScheduleDayList = new ArrayList<>();
        sharedDayIndicesMap = new HashMap<>();
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

    public boolean checkTimeSlots() {
        boolean allSlotsEmpty = true;
        boolean allSlotsValid = true;
        for (int i = 0; i < DAYS; i++) {
            if (!daySelectionList[i].isEmptyDay()) {
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

    public void validateDayEntry() {
        boolean hasDaysToBeErased = false;
        String erasedDayString = " ";
        for (int j = 0; j < getNumberOfValidDays(); j++) {  // bestehende Tage
            if (isExistingDayToBeErasedAt(j)) {
                hasDaysToBeErased = true;
                erasedDayString += validScheduleDayList.get(j).getDayName() + " ";
            }
        }
        if (hasDaysToBeErased) {
            throw new DayEraseException(erasedDayString);
        }
    }

    public boolean isExistingDayToBeErasedAt(int j) {
        boolean toBeErased = false;
        for (int i = 0; i < DAYS; i++) {  // alle Tage
            if (dayMatchesAt(i, j)) {
                if (!validScheduleDayList.get(j).isEmptyDay() && daySelectionList[i].isEmptyDay()) {
                    toBeErased = true;
                }
            }
        }
        return toBeErased;
    }

    private boolean dayMatchesAt(int i, int j) {
        return daySelectionList[i].getDayName().equals(validScheduleDayList.get(j).getDayName());

    }

    public void setValidScheduleDays() {
        for (int i = 0; i < DAYS; i++) {
            if (isValidDay(i)) {
                daySelectionList[i].setDayName(WEEKDAY_NAMES[i]);
                validScheduleDayList.add(daySelectionList[i].clone()); // Mapping: 1. Unterrichtstag = 0 usw.
            }
        }
    }

    public void updateValidDays() {
        sharedDayIndicesMap.clear();
        createSharedDayIndices();
        validScheduleDayList.clear();
        setValidScheduleDays();
    }

    private void createSharedDayIndices() {  // K = Index neuer Tag, V = Index bestehender Tag
        int newDayIndex = 0;
        int oldDayIndex;
        for (int i = 0; i < DAYS; i++) {
            if (isValidDay(i)) {
                for (int j = 0; j < getNumberOfValidDays(); j++) {
                    oldDayIndex = j;
                    if (dayMatchesAt(i, oldDayIndex)) {
                        sharedDayIndicesMap.put(newDayIndex, oldDayIndex);
                    }
                }
                newDayIndex++;
            }
        }
    }

    public void returnToExistingSelection() {
        for (int i = 0; i < DAYS; i++) {
            daySelectionList[i].cleanTimeSlots();
            for (int j = 0; j < getNumberOfValidDays(); j++) {
                if (dayMatchesAt(i, j)) {
                    daySelectionList[i] = validScheduleDayList.get(j).clone();
                }
            }
        }
        fireTableDataChanged();
    }

    public boolean isEmpty() {
        return validScheduleDayList.isEmpty();
    }

    public boolean isValidDay(int i) {
        return !daySelectionList[i].isEmptyDay();
    }

    public int getDayIndexOf(ScheduleDay scheduleDay) {
        int index = -1;
        for (int i = 0; i < DAYS; i++) {
            if (WEEKDAY_NAMES[i].equals(scheduleDay.getDayName())) {
                index = i;
            }
        }
        return index;
    }

    public Integer getSharedDayIndexOf(int newDayIndex) {
        return sharedDayIndicesMap.get(newDayIndex);
    }

    public int getNumberOfValidDays() {
        return validScheduleDayList.size();
    }

    public ScheduleDay getValidScheduleDay(int i) {
        return validScheduleDayList.get(i);
    }

    public ScheduleDay getSelectedScheduleDay(int i) {
        return daySelectionList[i];
    }
}
