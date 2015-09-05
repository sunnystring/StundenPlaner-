/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in thed editor.
 */
package core;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import util.Time;

/**
 *
 * @author mathiaskielholz
 */
public class StudentTimes implements TableModel {

    private StudentDay[] studentDayList;

    private static final String[] COLUMN_LABELS = {" ", "von", "bis*", "von", "bis*", "Wunschzeit*"};
    private static final String[] WEEKDAY_NAMES = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"};

    public StudentTimes() {

        studentDayList = new StudentDay[6];

        for (int i = 0; i < 6; i++) {
            studentDayList[i] = new StudentDay();
        }

    }

    public StudentDay getStudentDay(int index) {
        return studentDayList[index];
    }

    public Time getTimeSlot(int row, int col) {
        return studentDayList[row].getTime(col);
    }


    /* Implementierung TableModel für StudentDataEntry */
    @Override
    public int getRowCount() {

        return 6;
    }

    @Override
    public int getColumnCount() {

        return 6;
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
        studentDayList[row].setTime(time, col - 1);

    }

    @Override
    public void addTableModelListener(TableModelListener tl) {
    }

    @Override
    public void removeTableModelListener(TableModelListener tl) {
    }

}
