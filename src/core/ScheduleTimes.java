/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.ArrayList;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author mathiaskielholz
 */
public class ScheduleTimes implements TableModel {

    private static final int DAYS = 6, COLUMNS = 3;
    private static final ScheduleDay[] SCHEDULEDAY_LIST = new ScheduleDay[DAYS];
    private static final String[] COLUMN_LABELS = {" ", "von", "bis"};
    private static final String[] WEEKDAY_NAMES = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"};
    private ArrayList<ScheduleDay> scheduleDayList;

    public ScheduleTimes() {
        for (int day = 0; day < DAYS; day++) {
            SCHEDULEDAY_LIST[day] = new ScheduleDay();
        }
        scheduleDayList = new ArrayList<>();
    }

    public ScheduleDay getScheduleDay(int i) {
        return scheduleDayList.get(i);
    }

    public String getDayName(int i) {
        return scheduleDayList.get(i).getDayName();
    }

    public int getNumberOfDays() {
        return scheduleDayList.size();
    }

    public void setValidScheduleDays() {
        for (int day = 0; day < DAYS; day++) {
            if (isValidScheduleDay(day)) {
                SCHEDULEDAY_LIST[day].setDayName(WEEKDAY_NAMES[day]);
                scheduleDayList.add(SCHEDULEDAY_LIST[day]); // Mapping: 1. Unterrichtstag = 0 usw.
            }
        }
    }

    public boolean isValidScheduleDay(int day) {
        return !SCHEDULEDAY_LIST[day].getValidStart().toString().trim().isEmpty();
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
                return SCHEDULEDAY_LIST[row].getValidStart();
            case 2:
                return SCHEDULEDAY_LIST[row].getValidEnd();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object o, int row, int col) {
        String timeString = (String) o;
        SCHEDULEDAY_LIST[row].setTimeSlot(timeString, col);
    }

    @Override
    public void addTableModelListener(TableModelListener tl) {
    }

    @Override
    public void removeTableModelListener(TableModelListener tl) {
    }

}
