/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleData;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import studentListData.StudentDay;

import util.Time;

/**
 *
 * @author mathiaskielholz
 */
public class DayColumnData extends AbstractTableModel {

    private ArrayList<FieldData> fieldDataList;

    /* von Time zu int konvertierte Grössen */
    private int totalNumberOfFields; // globale Anzahl Time- bzw. Lectionfields (= Column-Höhe)
    private int fieldCountStart;  // Index lokaler Unterrichtsbeginn 
    private int fieldCountEnd;  // Index lokales Unterrichtsende 

    private ScheduleDay scheduleDay;

    public DayColumnData() {

        fieldDataList = new ArrayList<>();
    }

    public void setTimeFrame(ScheduleDay scheduleDay, ScheduleTimeFrame timeFrame) {

        this.scheduleDay = scheduleDay;
        totalNumberOfFields = timeFrame.getTotalNumberOfFields();
        Time absoluteStart = timeFrame.getAbsoluteStart(); // untere globale Zeitgrenze Stundenplan

        /* von Time zu int konvertierte Grössen */
        fieldCountStart = scheduleDay.getValidStart().diff(absoluteStart); // Anzahl 5-Min.-Felder
        fieldCountEnd = scheduleDay.getValidEnd().diff(absoluteStart);

        // FieldDataList init.
        try {
            Time time = absoluteStart.clone();
            for (int i = 0; i < totalNumberOfFields; i++) {
                FieldData fieldData = new FieldData();
                fieldData.setTime(time.clone());
                fieldDataList.add(fieldData);
                time.inc();
            }
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
    }

    public void setValidTimeMarks(StudentDay day) {

        FieldData fieldData;
        for (int i = 0; i < totalNumberOfFields; i++) {
            fieldData = fieldDataList.get(i);
            if (fieldData.getTime().greaterEqualsThan(day.getStartTime1()) && fieldData.getTime().smallerEqualsThan(day.getEndTime1())) {
                fieldData.setValidTime(FieldData.TIME_INTERVAL_1);
            }
            if (fieldData.getTime().greaterEqualsThan(day.getStartTime2()) && fieldData.getTime().smallerEqualsThan(day.getEndTime2())) {
                fieldData.setValidTime(FieldData.TIME_INTERVAL_2);
            }
            if (fieldData.getTime().equals(day.getFavorite())) {
                fieldData.setValidTime(FieldData.FAVORITE);
            }
        }
    }

    public void resetValidTimeMarks() {
        for (int i = 0; i < totalNumberOfFields; i++) {
            fieldDataList.get(i).setValidTime(0);
        }
    }

    /* Getter, Setter */
    public int getFieldCountStart() {
        return fieldCountStart;
    }

    public int getFieldCountEnd() {
        return fieldCountEnd;
    }

    public int getTotalNumberOfFields() {
        return totalNumberOfFields;
    }

    public String getDayName() {
        return scheduleDay.getDayName();
    }

    public ScheduleDay getScheduleDay() {
        return scheduleDay;
    }

    /* Schalter   */
    public Boolean isValidTime(int index) {     // vom Lehrer vorgegebene Unterrichtszeit
        return index >= fieldCountStart && index <= fieldCountEnd;
    }

    /*  TableModel Implementierung*/
    @Override
    public int getRowCount() {
        return totalNumberOfFields / 2;
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int row, int col) {  // damit Renderer "Values" in Zellen schreiben kann

        if (col == 0 || col == 2) {
            if (col == 2) {
                row = row + totalNumberOfFields / 2;
            }
            return fieldDataList.get(row);
        } else {
            return null;
        }
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return false;
    }

}
