/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in thed editor.
 */
package core;

import java.util.ArrayList;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author mathiaskielholz
 */
public class StudentTimes implements TableModel {

    private static final int DAYS = 6, COLUMNS = 6;
    private ScheduleTimes scheduleTimes;
    private static final StudentDay[] STUDENTDAY_LIST = new StudentDay[DAYS];
    private static final String[] COLUMN_LABELS = {" ", "von", "bis*", "von", "bis*", "Wunschzeit*"};
    private static final String[] WEEKDAY_NAMES = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"};
    private ArrayList<StudentDay> studentDayList;

    public StudentTimes() {
        for (int i = 0; i < DAYS; i++) {
            STUDENTDAY_LIST[i] = new StudentDay(COLUMNS - 1); // ohne 1. Spalte
        }
        studentDayList = new ArrayList<>();
    }

    public StudentDay getStudentDay(int i) {
        return studentDayList.get(i);
    }

    public void setScheduleTimes(ScheduleTimes scheduleTimes) {
        this.scheduleTimes = scheduleTimes;
    }

    public void setValidStudentDays() {
        for (int day = 0; day < DAYS; day++) {
            STUDENTDAY_LIST[day].setSingleLections();
            if (scheduleTimes.isValidScheduleDay(day)) {
                studentDayList.add(STUDENTDAY_LIST[day]); // Mapping: 1. StudentDay = 0 usw.
            }
        }
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
        return col > 0 && scheduleTimes.isValidScheduleDay(row);
    }

    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0:
                return WEEKDAY_NAMES[row];
            case 1:
                return STUDENTDAY_LIST[row].getStartTime1();
            case 2:
                return STUDENTDAY_LIST[row].getEndTime1();
            case 3:
                return STUDENTDAY_LIST[row].getStartTime2();
            case 4:
                return STUDENTDAY_LIST[row].getEndTime2();
            case 5:
                return STUDENTDAY_LIST[row].getFavorite();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object o, int row, int col) {
        String timeString = (String) o;
        STUDENTDAY_LIST[row].setTimeSlot(timeString, col);
    }

    @Override
    public void addTableModelListener(TableModelListener tl) {
    }

    @Override
    public void removeTableModelListener(TableModelListener tl) {
    }

}
