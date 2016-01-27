/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import dataEntryUI.ScheduleInputMask;
import exceptions.IllegalDayEraseException;
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
    private final ScheduleDay[] daySelectionList;
    private static final String[] COLUMN_LABELS = {" ", "von", "bis"};
    private static final String[] WEEKDAY_NAMES = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"};
    private final ArrayList<ScheduleDay> validScheduleDayList;
    private ArrayList<ScheduleDay> temporaryScheduleDayList;
    private String erasedDayString;
    //  public static final int NO_DAY_CHANGED = 0, DAYS_ERASED = 1, DAYS_ADDED = 2, DAYS_ERASED_AND_ADDED = 3;

    public ScheduleTimes() {
        daySelectionList = new ScheduleDay[DAYS];
        for (int i = 0; i < DAYS; i++) {
            daySelectionList[i] = new ScheduleDay();
        }
        validScheduleDayList = new ArrayList<>();
        temporaryScheduleDayList = new ArrayList<>();
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

//    public int findExistingDaysToBeErased() {
//        int state = NO_DAY_CHANGED;
//        boolean erased = false;
//        boolean added = false;
//        boolean noExistingDay;
//        erasedDayString = " ";
//        int k = 0;
//        for (int i = k; i < daySelectionList.length; i++) {  // Auswahl-Tage
//            noExistingDay = true;
//            for (int j = 0; j < validScheduleDayList.size(); j++) {  // bestehende Tage
//                if (daySelectionList[i].getDayName().equals(validScheduleDayList.get(j).getDayName())) { // falls schon Tag besteht
//                    if (!validScheduleDayList.get(j).isEmpty() && daySelectionList[i].isEmpty()) {
//                        erased = true;
//                        state = DAYS_ERASED;
//                        erasedDayString += validScheduleDayList.get(j).getDayName() + " ";
//                    }
//                    noExistingDay = false;
//                    k++;
//                    break; // sobald Tag gefunden, nächstfolgender Tag (= k) checken
//                }
//            }
//            if (noExistingDay && !daySelectionList[i].isEmpty()) {  // falls Tag noch nicht besteht
//                state = DAYS_ADDED;
//                added = true;
//            }
//        }
//        if (!erased && !added) {
//            state = NO_DAY_CHANGED;
//        }
//        if (erased && added) {
//            state = DAYS_ERASED_AND_ADDED;
//        }
//        return state;
//    }
//    public boolean hasExistingDaysToBeErased() {
//        boolean erased = false;
//        erasedDayString = " ";
//        for (int i = 0; i < daySelectionList.length; i++) {  // alle Tage
//            for (int j = 0; j < validScheduleDayList.size(); j++) {  // bestehende Tage
//                if (daySelectionList[i].getDayName().equals(validScheduleDayList.get(j).getDayName())) {
//                    if (!validScheduleDayList.get(j).isEmpty() && daySelectionList[i].isEmpty()) {
//                        erased = true;
//                        erasedDayString += validScheduleDayList.get(j).getDayName() + " ";
//                    }
//                }
//            }
//        }
//        return erased;
//    }
    public void findExistingDaysToBeErased() {
        boolean erased = false;
        erasedDayString = " ";
        for (int i = 0; i < daySelectionList.length; i++) {  // alle Tage
            for (int j = 0; j < validScheduleDayList.size(); j++) {  // bestehende Tage
                if (daySelectionList[i].getDayName().equals(validScheduleDayList.get(j).getDayName())) {
                    if (!validScheduleDayList.get(j).isEmpty() && daySelectionList[i].isEmpty()) {
                        erased = true;
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
        if (erased) {
            throw new IllegalDayEraseException();
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

    public void setSelectedScheduleDays() {
        for (int i = 0; i < DAYS; i++) {
            if (isValidScheduleDay(i)) {
                daySelectionList[i].setDayName(WEEKDAY_NAMES[i]);
                temporaryScheduleDayList.add(daySelectionList[i].clone());
            }
        }
    }

//    public void clear() {
//        validScheduleDayList.clear();
//   //     setValidScheduleDays();
//    }
    public void returnToExistingSelection() {
        for (int i = 0; i < validScheduleDayList.size(); i++) {
            for (int j = 0; j < daySelectionList.length; j++) {
                if (validScheduleDayList.get(i).getDayName() == daySelectionList[j].getDayName()) {
                    daySelectionList[j] = validScheduleDayList.get(i).clone();
                }
            }
        }
        fireTableDataChanged();
    }

    public boolean isEmpty() {
        return validScheduleDayList.isEmpty();
    }

    public boolean isValidScheduleDay(int i) {
        return !daySelectionList[i].isEmpty();
    }

    public int getNumberOfValidDays() {
        return validScheduleDayList.size();
    }

    public ScheduleDay getValidScheduleDay(int i) {
        return validScheduleDayList.get(i);
    }

    public int getNumberOfSelectedDays() {
        return temporaryScheduleDayList.size();
    }

    public ScheduleDay getSelectedScheduleDay(int i) {
        return temporaryScheduleDayList.get(i);
    }

    public void clearTemporaryScheduleDays() {
        temporaryScheduleDayList.clear();
    }

    public String getErasedDay() {
        return erasedDayString;
    }

}
