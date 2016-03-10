/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in thed editor.
 */
package core;

import dataEntryUI.StudentInputMask;
import exceptions.IllegalTimeSlotException;
import exceptions.ScheduleOutOfBoundException;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import scheduleData.ScheduleTimeFrame;

/**
 *
 * Gesamtheit aller Unterrichtstage mit den verfügbaren Schülerzeiten,
 * TableModel für {@link StudentInputMask}
 */
public class StudentTimes extends AbstractTableModel {

    public static final int COLUMNS = 6;
    private static final String[] COLUMN_LABELS = {" ", "von", "bis*", "von", "bis*", "Wunschzeit*"};
    private final String[] WEEKDAY_NAMES = ScheduleTimes.WEEKDAY_NAMES;
    private final int DAYS = ScheduleTimes.DAYS;
    private ScheduleTimes scheduleTimes;
    private final StudentDay[] daySelectionList;
    private ArrayList<StudentDay> validStudentDayList;

    public StudentTimes() {
        daySelectionList = new StudentDay[DAYS];
        for (int i = 0; i < DAYS; i++) {
            daySelectionList[i] = new StudentDay();
            daySelectionList[i].setDayName(WEEKDAY_NAMES[i]);
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
        return col > 0 && scheduleTimes.isValidDay(row);
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

    public void checkAndCorrectTimeEntries() {
        boolean allSlotsValid = true;
        for (int i = 0; i < daySelectionList.length; i++) {
            if (daySelectionList[i].hasInvalidTimeSlots()) {
                allSlotsValid = false;
                daySelectionList[i].correctInvalidTimeSlots();
            }
        }
        fireTableDataChanged();
        if (!allSlotsValid) {
            throw new IllegalTimeSlotException();
        }
    }

    public void checkScheduleBounds(ScheduleTimes scheduleTimes, ScheduleTimeFrame scheduleTimeFrame, int lectionLengthInFields) {
        boolean daysOutOfBound = false;
        String dayName = " ";
        for (int i = 0; i < daySelectionList.length; i++) {
            StudentDay studentDay = daySelectionList[i];
            if (!studentDay.isEmpty()) {
                boolean dayOutOfBound = false;
                if (studentDay.isOutOfTimeFrame(scheduleTimeFrame, lectionLengthInFields)) {
                    dayOutOfBound = true;
                }
                if (studentDay.isOutOfValidEnd(scheduleTimes)) {
                    dayOutOfBound = true;
                }
                if (dayOutOfBound) {
                    dayName += studentDay.getDayName() + " ";
                    daysOutOfBound = true;
                }
            }
        }
        if (daysOutOfBound) {
            throw new ScheduleOutOfBoundException(dayName);
        }
    }

    public void setValidStudentDays() {
        for (int i = 0; i < DAYS; i++) {
            daySelectionList[i].setSingleLections(); // falls solche gesetzt: endTime = startTime
            if (scheduleTimes.isValidDay(i)) {
                daySelectionList[i].setDayName(WEEKDAY_NAMES[i]);
                validStudentDayList.add(daySelectionList[i]); // Mapping: 1. StudentDay = 0 usw.
            }
        }
    }

    public void updateValidStudentDays() {
        validStudentDayList.clear();
        setValidStudentDays();
    }

    public StudentDay getValidStudentDay(int i) {
        return validStudentDayList.get(i);
    }

    public void setScheduleTimes(ScheduleTimes scheduleTimes) {
        this.scheduleTimes = scheduleTimes;
    }

    public void setValidStudentDayList(ArrayList<StudentDay> validStudentDayList) {
        this.validStudentDayList = validStudentDayList;
    }
}
