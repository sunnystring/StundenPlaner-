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

    private static final int DAYS = 6, COLUMNS = 3; // 6 = Mo-Sa <-> WEEKDAY_NAMES

    private static final ScheduleDay[] SCHEDULEDAY_LIST = new ScheduleDay[DAYS];  // fixe Liste aller Unterrichtstage für TableModel
    private static final String[] COLUMN_LABELS = {" ", "von", "bis"};
    private static final String[] WEEKDAY_NAMES = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"}; // fixes Mapping: Mo = 0, Di = 1 usw.

    private ArrayList<ScheduleDay> scheduleDayList; // dynamische Liste und neues Mapping mit den ausgewählten Unterrichtstagen zur Weiterverwendung nach aussen

    public ScheduleTimes() {

        for (int i = 0; i < DAYS; i++) {
            SCHEDULEDAY_LIST[i] = new ScheduleDay();
        }
        scheduleDayList = new ArrayList<>();
    }

    /* Getter, Setter */
    public ScheduleDay getScheduleDay(int i) {
        return scheduleDayList.get(i); // = ScheduleDay gemäss dynamischem Mapping 
    }

    public String getDayName(int i) {
        return scheduleDayList.get(i).getDayName(); // DayName gemäss dynamischem Mapping 
    }

    public int getNumberOfDays() {
        return scheduleDayList.size();
    }

    /* dynamische Liste mit gültigen ScheduleDays befüllen*/
    public void finalizeScheduleTimes() {

        for (int i = 0; i < DAYS; i++) {
            if (!SCHEDULEDAY_LIST[i].getValidStart().toString().trim().isEmpty()) {
                SCHEDULEDAY_LIST[i].setDayName(WEEKDAY_NAMES[i]);
                scheduleDayList.add(SCHEDULEDAY_LIST[i]); // hier entsteht neues Mapping: 1. Unterrichtstag = 0 usw.
            }
        }
    }

    /* Implementierung TableModel für ScheduleDataEntry */
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
                return WEEKDAY_NAMES[row]; // 1. Spalte
            case 1:
                return SCHEDULEDAY_LIST[row].getValidStart();
            case 2:
                return SCHEDULEDAY_LIST[row].getValidEnd();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object o, int row, int col) {  // col = 1,2

        String timeString = (String) o;
        SCHEDULEDAY_LIST[row].setScheduleTime(timeString, col);  // SCHEDULEDAY_LIST befüllen mit Eingaben aus der JTable
    }

    @Override
    public void addTableModelListener(TableModelListener tl) {
    }

    @Override
    public void removeTableModelListener(TableModelListener tl) {
    }

}
