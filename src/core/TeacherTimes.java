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
public class TeacherTimes implements TableModel {

    private static final int DAYS = 6, COLUMNS = 3; // 6 = Mo-Sa <-> WEEKDAY_NAMES

    private static final TeacherDay[] TEACHERDAY_LIST = new TeacherDay[DAYS];  // fixe Liste aller Unterrichtstage für TableModel
    private static final String[] COLUMN_LABELS = {" ", "von", "bis"};
    private static final String[] WEEKDAY_NAMES = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"}; // fixes Mapping: Mo = 0, Di = 1 usw.

    private ArrayList<TeacherDay> teacherDayList; // dynamische Liste und neues Mapping mit den ausgewählten Unterrichtstagen zur Weiterverwendung nach aussen

    public TeacherTimes() {

        for (int i = 0; i < DAYS; i++) {
            TEACHERDAY_LIST[i] = new TeacherDay();
        }
        teacherDayList = new ArrayList<>();
    }

    /* Getter, Setter */
    public TeacherDay getTeacherDay(int i) {
        return teacherDayList.get(i); // = TeacherDay gemäss dynamischem Mapping 
    }

    public String getDayName(int i) {
        return teacherDayList.get(i).getDayName(); // DayName gemäss dynamischem Mapping 
    }

    public int getNumberOfDays() {
        return teacherDayList.size();
    }

    /* dynamische Liste mit gültigen ScheduleDays befüllen*/
    public void finalizeTeacherTimes() {

        for (int i = 0; i < DAYS; i++) {
            if (!TEACHERDAY_LIST[i].getValidStart().toString().trim().isEmpty()) {
                TEACHERDAY_LIST[i].setDayName(WEEKDAY_NAMES[i]);
                teacherDayList.add(TEACHERDAY_LIST[i]); // hier entsteht neues Mapping: 1. Unterrichtstag = 0 usw.
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
                return TEACHERDAY_LIST[row].getValidStart();
            case 2:
                return TEACHERDAY_LIST[row].getValidEnd();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object o, int row, int col) {  // col = 1,2

        String timeString = (String) o;
        TEACHERDAY_LIST[row].setTeacherTime(timeString, col);  // SCHEDULEDAY_LIST befüllen mit Eingaben aus der JTable
    }

    @Override
    public void addTableModelListener(TableModelListener tl) {
    }

    @Override
    public void removeTableModelListener(TableModelListener tl) {
    }

}
