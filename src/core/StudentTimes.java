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

    private static final int DAYS = 6, COLUMNS = 6; // DAYS = 6 = Mo-Sa,
    private ScheduleTimes scheduleTimes;

    private static final StudentDay[] STUDENTDAY_LIST = new StudentDay[DAYS]; // fixe interne Liste aller Unterrichtstage für TableModel
    private static final String[] COLUMN_LABELS = {" ", "von", "bis*", "von", "bis*", "Wunschzeit*"};
    private static final String[] WEEKDAY_NAMES = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"};

    private ArrayList<StudentDay> studentDayList; // dynamische Liste umit neuem Mapping mit den ausgewählten Unterrichtstagen zur Weiterverwendung nach aussen

    public StudentTimes() {

        for (int i = 0; i < DAYS; i++) {
            STUDENTDAY_LIST[i] = new StudentDay(COLUMNS - 1); // COLUMNS - 1 = TimeSlots in StudentDay
        }
        studentDayList = new ArrayList<>();
    }

    public StudentDay getStudentDay(int i) {
        return studentDayList.get(i);  // = StudentDay gemäss dynamischem Mapping in ScheduleTimes
    }

    public void setScheduleTimes(ScheduleTimes scheduleTimes) {
        this.scheduleTimes = scheduleTimes;
    }

    // studentDayList mit leeren StudentDays bekommt gerade die richtige Size 
    public void createList() {
        for (int i = 0; i < DAYS; i++) {
            STUDENTDAY_LIST[i].setSingleLections(); // falls nur validStart eingegeben, validEnd = validStart
            if (scheduleTimes.isValidScheduleDay(i)) {
                studentDayList.add(STUDENTDAY_LIST[i]); // neues Mapping: 1. Tag = 0 usw.
            }
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
        // empty Slots in SCHEDULEDAY_LIST dürfen in der entspr. STUDENTDAY_LIST nicht editierbar sein
        return col > 0 && scheduleTimes.isValidScheduleDay(row);
    }

    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0:
                return WEEKDAY_NAMES[row]; // 1. Spalte
            case 1:
                return STUDENTDAY_LIST[row].getStartTime1(); // Noch unbenutzt
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
        STUDENTDAY_LIST[row].setStudentTime(timeString, col - 1); // col = 0 = WEEKDAY_NAMES
    }

    @Override
    public void addTableModelListener(TableModelListener tl) {
    }

    @Override
    public void removeTableModelListener(TableModelListener tl) {
    }

}
