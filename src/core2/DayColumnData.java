/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core2;

import core.ScheduleDay;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import schedule.ScheduleTimeFrame;
import util.Time;

/**
 *
 * @author mathiaskielholz
 */
public class DayColumnData extends DefaultTableModel {

    private ArrayList<Time> timeColumn;

    /* von Time zu int konvertierte Grössen */
    private int totalNumberOfFields; // globale Anzahl Time- bzw. Lectionfields (= Column-Höhe)
    private int fieldCountStart;  // Index lokaler Unterrichtsbeginn 
    private int fieldCountEnd;  // Index lokales Unterrichtsende 

    private ScheduleDay scheduleDay;
  
    // Ein TableModel ist aus Flexibilitätsgründen nicht fest an eine JTable gekoppelt, es kann gleichzeitig für 
    // verschiedene JTables eingesetzt werden, deshalb kann es auch keine eindeutige Zuordnung TableModel-> JTable geben, 
    //die Zuordnung muss hier gesetzt werden:
   // private TimeTable timeTable;

    public DayColumnData() {

        timeColumn = new ArrayList<>();
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
                timeColumn.add(time.clone());
                time.inc();
            }
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
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

    public String getMinute(int index) {
        return String.valueOf(timeColumn.get(index).getMinute());
    }

    public String getHour(int index) {
        return String.valueOf(timeColumn.get(index).getHour());
    }

    public ScheduleDay getScheduleDay() {
        return scheduleDay;
    }

//    public void setTimeTable(TimeTable timeTable) {
//        this.timeTable = timeTable;
//    }
//
//    public TimeTable getTimeTable() {
//        return timeTable;
//    }


    /* Schalter   */
    public Boolean isMinute(int index) {
        return timeColumn.get(index).getMinute() != 0;
    }

    public Boolean isValidTime(int index) {
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
        return null;
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return false;
    }

}
