/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import util.Time;

/**
 *
 * @author mathiaskielholz
 */
public class ScheduleTimes implements TableModel {

    private ScheduleDay[] scheduleDayList;

    private static final String[] COLUMN_LABELS = {" ", "von", "bis"};
    private static final String[] WEEKDAY_NAMES = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"};

    public ScheduleTimes() {

        scheduleDayList = new ScheduleDay[6];

        for (int i = 0; i < 6; i++) {
            scheduleDayList[i] = new ScheduleDay();
        }
    }

    public Time getTeacherTime(int row, int col) {
        switch (col) {
            case 1:
                return scheduleDayList[row].getValidStart();
            case 2:
                return scheduleDayList[row].getValidEnd();
            default:
                return null;
        }

    }

    public void printScheduleDayList() {

        for (ScheduleDay d : scheduleDayList) {
            System.out.println("start: " + d.getValidStart() + "  end: " + d.getValidEnd().toString());
        }
    }

    /* Implementierung TableModel für ScheduleDataEntry */
    @Override
    public int getRowCount() {

        return 6;
    }

    @Override
    public int getColumnCount() {

        return 3;
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

        if (col == 0) {
            return WEEKDAY_NAMES[row];
        } else {
            return null;
        }
    }

    @Override
    public void setValueAt(Object o, int row, int col) {

        String time = (String) o;
        scheduleDayList[row].setTime(time, col);
        System.out.println(time);

    }

    @Override
    public void addTableModelListener(TableModelListener tl) {
    }

    @Override
    public void removeTableModelListener(TableModelListener tl) {
    }

}
