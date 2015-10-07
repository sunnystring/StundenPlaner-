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

    private static final int DAYS = 6, COLUMNS = 6; // DAYS = 6 = Mo-Sa

    private static final StudentDay[] STUDENTDAY_LIST = new StudentDay[DAYS]; // fixe interne Liste aller Unterrichtstage für TableModel
    private static final String[] COLUMN_LABELS = {" ", "von", "bis*", "von", "bis*", "Wunschzeit*"};
    private static final String[] WEEKDAY_NAMES = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"};

    private ArrayList<StudentDay> studentDayList; // dynamische Liste und neues Mapping mit den ausgewählten Unterrichtstagen zur Weiterverwendung nach aussen

    public StudentTimes() {

        for (int i = 0; i < DAYS; i++) {
            STUDENTDAY_LIST[i] = new StudentDay(COLUMNS - 1);
        }
        studentDayList = new ArrayList<>();
    }

    public StudentDay getStudentDay(int i) {
        return studentDayList.get(i);  // = StudentDay gemäss dynamischem Mapping 
    }

    /* dynamische Liste mit gültigen ScheduleDays befüllen*/
    public void finalizeStudentTimes() {

        for (int i = 0; i < DAYS; i++) {
               studentDayList.add(STUDENTDAY_LIST[i]); // hier entsteht neues Mapping: 1. Unterrichtstag = 0 usw.
        }
    }

    /* Implementierung TableModel für StudentDataEntry */
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
    public void setValueAt(Object o, int row, int col) {  // col = 1,2,..5

        String timeString = (String) o;
        STUDENTDAY_LIST[row].setStudentTime(timeString, col); // timeString an die richtigen Koordinaten der JTable setzen
    }

    @Override
    public void addTableModelListener(TableModelListener tl) {
    }

    @Override
    public void removeTableModelListener(TableModelListener tl) {
    }

}
