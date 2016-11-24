/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in thed editor.
 */
package core;

import dataEntryUI.student.StudentInputMask;
import exceptions.IllegalTimeSlotException;
import exceptions.OutOfBoundException;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import scheduleData.ScheduleTimeFrame;
import static core.ScheduleTimes.*;

import utils.Time;

/**
 *
 * Gesamtheit aller Unterrichtstage mit den verfügbaren Schülerzeiten,
 * TableModel für {@link StudentInputMask}
 */
public class StudentTimes extends AbstractTableModel {

    public static final int COLUMNS = StudentDay.SLOTS + 1;
    private static final String[] COLUMN_LABELS = {" ", "von", "bis*", "von", "bis*", "Wunschzeit*"};
    private ScheduleTimes scheduleTimes;
    private final StudentDay[] daySelectionList;
    private ArrayList<StudentDay> validStudentDayList;
    private int numberOfSelectedDays;
    private boolean KGUselected;

    public StudentTimes() {
        KGUselected = false;
        daySelectionList = new StudentDay[DAYS];
        for (int i = 0; i < DAYS; i++) {
            daySelectionList[i] = new StudentDay();
            daySelectionList[i].setDayName(WEEKDAY_NAMES[i]);
        }
        validStudentDayList = new ArrayList<>();
        numberOfSelectedDays = 0;
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
        return col > 0 && scheduleTimes.isValidDay(row) && !(KGUselected && (col == 3 || col == 4));
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

    public void initAndCheckScheduleBounds(ScheduleTimeFrame timeFrame, int lectionLength) {
        String errorLog = "";
        boolean daysOutOfBounds = false;
        for (int i = 0; i < DAYS; i++) {
            StudentDay studentDay = daySelectionList[i];
            ScheduleDay scheduleDay = scheduleTimes.getMatchingScheduleDayOf(studentDay);
            studentDay.setSelectionState(); // emptyDay, falls keine Zeiteinträge
            studentDay.setSingleSlots();
            studentDay.setTimeBounds();
            boolean outOfTimeFrame = studentDay.outOfTimeFrame(timeFrame, lectionLength);
            boolean outOfScheduleDayBounds = studentDay.outOfValidBoundsOf(scheduleDay);
            if (!studentDay.isEmpty()) {
                if (outOfTimeFrame || outOfScheduleDayBounds) {
                    daysOutOfBounds = true;
                }
                Time endTimeFrame = timeFrame.getAbsoluteEnd().minusLengthOf(lectionLength + 1);
                Time endScheduleDay = scheduleDay.getValidEnd();
                if (outOfTimeFrame) {
                    if (endScheduleDay.lessEqualsThan(endTimeFrame)) {
                        endTimeFrame = endScheduleDay;
                    }
                    errorLog += studentDay.getDayName() + ":\n" + scheduleDay.getValidStart()
                            + " bis " + endTimeFrame + "\n";
                } else if (outOfScheduleDayBounds) {
                    if (endTimeFrame.lessThan(endScheduleDay)) {
                        endScheduleDay = endTimeFrame;
                    }
                    errorLog += studentDay.getDayName() + ":\n" + scheduleDay.getValidStart()
                            + " bis " + endScheduleDay + "\n";
                    daysOutOfBounds = true;
                }
            }
        }
        if (daysOutOfBounds) {
            throw new OutOfBoundException(errorLog);
        }
    }

    public void updateValidStudentDays() {
        validStudentDayList.clear();
        setValidStudentDays();
    }

    public void setValidStudentDays() {
        numberOfSelectedDays = 0;
        for (int i = 0; i < DAYS; i++) {
            if (scheduleTimes.isValidDay(i)) {
                StudentDay studentDay = daySelectionList[i];
                studentDay.setDayName(WEEKDAY_NAMES[i]);
                studentDay.setValidTimes(); // für IncompatibleStudentTimes
                if (!studentDay.isEmpty()) {
                    numberOfSelectedDays++;
                }
                validStudentDayList.add(daySelectionList[i]); // Mapping: 1. StudentDay = 0 usw.
            }
        }
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

    public void cleanDaySelectionList(ArrayList<Integer> unvalidDaysAbsoluteIndizes) {
        for (Integer unvalidDayIndex : unvalidDaysAbsoluteIndizes) {
            daySelectionList[unvalidDayIndex].resetAllTimes();
        }
    }

    public StudentDay getValidStudentDay(int i) {
        return validStudentDayList.get(i);
    }

    public StudentDay getDaySelectionListAt(int i) {
        return daySelectionList[i];
    }

    public ArrayList<StudentDay> getValidStudentDayList() {
        return validStudentDayList;
    }

    public void setValidStudentDayList(ArrayList<StudentDay> validStudentDayList) {
        this.validStudentDayList = validStudentDayList;
    }

    public int getNumberOfSelectedDays() {
        return numberOfSelectedDays;
    }

    public void setScheduleTimes(ScheduleTimes scheduleTimes) {
        this.scheduleTimes = scheduleTimes;
    }

    public boolean isKGUselected() {
        return KGUselected;
    }

    public void setKGUselected(boolean KGUselected) {
        this.KGUselected = KGUselected;
    }

}
