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

    // fort
 //   private ArrayList<Time> timeColumn;

    private ArrayList<FieldData> fieldDataList;

    /* von Time zu int konvertierte Grössen */
    private int totalNumberOfFields; // globale Anzahl Time- bzw. Lectionfields (= Column-Höhe)
    private int fieldCountStart;  // Index lokaler Unterrichtsbeginn 
    private int fieldCountEnd;  // Index lokales Unterrichtsende 

    private ScheduleDay scheduleDay;

    public DayColumnData() {

      //  timeColumn = new ArrayList<>();
        fieldDataList = new ArrayList<>();
        
    }

    public void setTimeFrame(ScheduleDay scheduleDay, ScheduleTimeFrame timeFrame) {

        this.scheduleDay = scheduleDay;
        totalNumberOfFields = timeFrame.getTotalNumberOfFields();
        Time absoluteStart = timeFrame.getAbsoluteStart(); // untere globale Zeitgrenze Stundenplan

        /* von Time zu int konvertierte Grössen */
        fieldCountStart = scheduleDay.getValidStart().diff(absoluteStart); // validStart - absoluteStart = Anzahl 5-Min.-Felder
        fieldCountEnd = scheduleDay.getValidEnd().diff(absoluteStart); // validEnd - absoluteStart = Anzahl 5-Min.-Felder

        // timeColumn befüllen
        try {
            Time time = absoluteStart.clone();
            for (int i = 0; i < totalNumberOfFields; i++) {
                FieldData fieldData = new FieldData();
                fieldData.setTime(time.clone());
                fieldDataList.add(fieldData);
                //  timeColumn.add(time.clone());
                time.inc();
            }
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
    }

    public void setValidTimeMarks(StudentDay day) {

        for (int i = 0; i < totalNumberOfFields; i++) {

            FieldData fieldData = fieldDataList.get(i);

            if (fieldData.getTime().equals(day.getStartTime1())) {
                fieldData.setValidStart1(i);
            }
            if (fieldData.getTime().equals(day.getEndTime1())) {
                fieldData.setValidEnd1(i);
            }
            if (fieldData.getTime().equals(day.getStartTime2())) {
                fieldData.setValidStart2(i);
            }
            if (fieldData.getTime().equals(day.getEndTime2())) {
                fieldData.setValidEnd2(i);
            }
            if (fieldData.getTime().equals(day.getFavorite())) {
                fieldData.setFavorite(i);
            }
        }
    }

    public void resetValidTimeMarks() {

        for (int i = 0; i < totalNumberOfFields; i++) {
            fieldDataList.get(i).setValidStart1(totalNumberOfFields + 2);
            fieldDataList.get(i).setValidEnd1(-2);
            fieldDataList.get(i).setValidStart2(totalNumberOfFields + 2);
            fieldDataList.get(i).setValidEnd2(-2);
            fieldDataList.get(i).setFavorite(-2);
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

    public ArrayList<Time> getTimeColumn() {
        return timeColumn;
    }
// fort

    public String getMinute(int index) {
        return String.valueOf(timeColumn.get(index).getMinute());
    }
// fort

    public String getHour(int index) {
        return String.valueOf(timeColumn.get(index).getHour());
    }

    public ScheduleDay getScheduleDay() {
        return scheduleDay;
    }

    /* Schalter   */
// fort
    public Boolean isMinute(int index) {
        return timeColumn.get(index).getMinute() != 0;
    }

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
            return timeColumn.get(row);
        } else {
            return null;
        }
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return false;
    }

}
