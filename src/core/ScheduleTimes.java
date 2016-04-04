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
import exceptions.OutOfBoundException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.AbstractTableModel;
import scheduleData.ScheduleTimeFrame;

/**
 *
 * Gesamtheit aller Unterrichtstage (vom Lehrer vorgegeben), TableModel für
 * {@link ScheduleInputMask}
 */
public class ScheduleTimes extends AbstractTableModel {

    public static final int COLUMNS = 3;
    private static final String[] COLUMN_LABELS = {" ", "von", "bis"};
    public static final String[] WEEKDAY_NAMES = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"};
    public static final int DAYS = WEEKDAY_NAMES.length;
    private final ScheduleDay[] daySelectionList;
    private final ArrayList<ScheduleDay> validScheduleDayList;
    private final HashMap<Integer, Integer> sharedDayIndicesMap;

    public ScheduleTimes() {
        daySelectionList = new ScheduleDay[DAYS];
        for (int i = 0; i < DAYS; i++) {
            daySelectionList[i] = new ScheduleDay();
            daySelectionList[i].setDayName(WEEKDAY_NAMES[i]);
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
                if (!validScheduleDayList.get(j).isEmpty() && daySelectionList[i].isEmpty()) {
                    toBeErased = true;
                }
            }
        }
        return toBeErased;
    }

    private boolean dayMatchesAt(int i, int j) {
        return daySelectionList[i].getDayName().equals(validScheduleDayList.get(j).getDayName());
    }

    public void checkStudentListBounds(Database database) {
        String daysAndStudents = "";
        boolean hasDaysOutOfBounds = false;
        ScheduleTimeFrame tempTimeFrame = createTemporaryTimeFrame();
        for (int i = 0; i < daySelectionList.length; i++) {
            ScheduleDay scheduleDay = daySelectionList[i];
            boolean thisDayOutOfBounds = false;
            if (!scheduleDay.isEmpty()) {
                String studentNames = "";
                for (Student student : database.getStudentDataList()) {
                    StudentTimes studentTimes = student.getStudentTimes();
                    StudentDay studentDay = studentTimes.getMatchingStudentDayOf(scheduleDay);
                    if (studentDay != null && !studentDay.isEmpty()) {
                        boolean outOfValidEnd = studentDay.outOfValidEndOf(scheduleDay);
                        boolean outOfTimeFrame = studentDay.outOfTimeFrame(tempTimeFrame, student.getLectionLength());
                        if (outOfTimeFrame || outOfValidEnd) {
                            studentNames += student.getFirstName() + " " + student.getName() + "\n";
                            thisDayOutOfBounds = true;
                        }
                    }
                }
                if (thisDayOutOfBounds) {
                    hasDaysOutOfBounds = true;
                    daysAndStudents += scheduleDay.getDayName() + ":\n" + studentNames + "\n";
                }
            }
        }
        if (hasDaysOutOfBounds) {
            throw new OutOfBoundException(daysAndStudents);
        }
    }

    public ScheduleTimeFrame createTemporaryTimeFrame() {
        ScheduleTimeFrame tempFrame = new ScheduleTimeFrame();
        for (int i = 0; i < getRowCount(); i++) {
            if (isValidDay(i)) {
                tempFrame.setBounds(getSelectedScheduleDay(i));
            }
        }
        return tempFrame;
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
        return !daySelectionList[i].isEmpty();
    }

    public ScheduleDay getMatchingScheduleDayOf(StudentDay studentDay) {
        ScheduleDay matchingDay = null;
        for (ScheduleDay scheduleDay : validScheduleDayList) {
            if (scheduleDay.matches(studentDay.getDayName())) {
                matchingDay = scheduleDay;
            }
        }
        return matchingDay;
    }

    public int getStaticDayIndexOf(int validDayIndex) {
        int index = -1;
        for (int i = 0; i < DAYS; i++) {
            if (WEEKDAY_NAMES[i].equals(validScheduleDayList.get(validDayIndex).getDayName())) {
                index = i;
                break;
            }
        }
        return index;
    }

    public int getStaticDayIndexOf(ScheduleDay scheduleDay) {
        int index = -1;
        for (int i = 0; i < DAYS; i++) {
            if (WEEKDAY_NAMES[i].equals(scheduleDay.getDayName())) {
                index = i;
                break;
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

    public ScheduleDay getValidScheduleDayAt(int i) {
        return validScheduleDayList.get(i);
    }

    public ScheduleDay getSelectedScheduleDay(int i) {
        return daySelectionList[i];
    }
}
