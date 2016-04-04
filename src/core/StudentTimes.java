/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in thed editor.
 */
package core;

import dataEntryUI.StudentInputMask;
import exceptions.IllegalTimeSlotException;
import exceptions.OutOfBoundException;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import scheduleData.ScheduleTimeFrame;
import static core.ScheduleTimes.*;

/**
 *
 * Gesamtheit aller Unterrichtstage mit den verfügbaren Schülerzeiten,
 * TableModel für {@link StudentInputMask}
 */
public class StudentTimes extends AbstractTableModel {

    public static final int COLUMNS = 6;
    private static final String[] COLUMN_LABELS = {" ", "von", "bis*", "von", "bis*", "Wunschzeit*"};
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
                return daySelectionList[row].start1();
            case 2:
                return daySelectionList[row].end1();
            case 3:
                return daySelectionList[row].start2();
            case 4:
                return daySelectionList[row].end2();
            case 5:
                return daySelectionList[row].favorite();
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
        for (int i = 0; i < DAYS; i++) {
            if (daySelectionList[i].validateTimeSlots()) {
                allSlotsValid = false;
                daySelectionList[i].correctInvalidTimeSlots();
            }
        }
        fireTableDataChanged();
        if (!allSlotsValid) {
            throw new IllegalTimeSlotException();
        }
    }

    public void initAndCheckScheduleBounds(ScheduleTimes scheduleTimes, ScheduleTimeFrame scheduleTimeFrame, int lectionLengthInFields) {
        String dayNames = " ";
        boolean daysOutOfBounds = false;
        for (int i = 0; i < DAYS; i++) {
            StudentDay studentDay = daySelectionList[i];
            studentDay.setSelectionState(); // emptyDay, falls keine Zeiteinträge
            studentDay.setSingleLections();
            studentDay.setLowestAndHighestBounds();
            if (!studentDay.isEmpty()) {
                boolean outOfValidEnd = studentDay.outOfValidEndOf(scheduleTimes.getMatchingScheduleDayOf(studentDay));
                boolean outOfTimeFrame = studentDay.outOfTimeFrame(scheduleTimeFrame, lectionLengthInFields);
                if (outOfValidEnd || outOfTimeFrame) {
                    dayNames += studentDay.getDayName() + " ";
                    daysOutOfBounds = true;
                }
            }
        }
        if (daysOutOfBounds) {
            throw new OutOfBoundException(dayNames);
        }
    }

    public void setValidStudentDays() {
        for (int i = 0; i < DAYS; i++) {
            if (scheduleTimes.isValidDay(i)) {
                daySelectionList[i].setDayName(WEEKDAY_NAMES[i]);
                daySelectionList[i].setValidTimes(); // für IncompatibleStudentTimes
                validStudentDayList.add(daySelectionList[i]); // Mapping: 1. StudentDay = 0 usw.
            }
        }
    }

    public void updateValidStudentDays() {
        validStudentDayList.clear();
        setValidStudentDays();
    }

    public StudentDay getMatchingStudentDayOf(ScheduleDay scheduleDay) {
        StudentDay matchingDay = null;
        for (StudentDay studentDay : validStudentDayList) {
            if (studentDay.matches(scheduleDay.getDayName())) {
                matchingDay = studentDay;
            }
        }
        return matchingDay;
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
