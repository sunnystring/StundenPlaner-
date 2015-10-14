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
public class DayColumnModel extends DefaultTableModel {

    private ArrayList<Time> timeColumn1;
    private ArrayList<Time> timeColumn2;

    private String dayName;
    private Time absoluteStart;  // untere globale Zeitgrenze Stundenplan

    /* von Time zu int konvertierte Grössen */
    private int totalNumberOfFields; // globale maximale Anzahl Time- bzw. Lectionfields (= Column-Höhe)
    private int fieldCountStart;  // lokaler Unterrichtsbeginn (Zähler, nicht Zeit)
    private int fieldCountEnd;  // lokales Unterrichtsende (Zähler, nicht Zeit)

    public DayColumnModel() {

        timeColumn1 = new ArrayList<>();
        timeColumn2 = new ArrayList<>();
    }

    public void setTimeFrame(ScheduleDay scheduleDay, ScheduleTimeFrame timeFrame) {

        dayName = scheduleDay.getDayName();

        //   this.scheduleDay = scheduleDay;
        totalNumberOfFields = timeFrame.getTotalNumberOfFields();
        absoluteStart = timeFrame.getAbsoluteStart();

        /* von Time zu int konvertierte Grössen */
        fieldCountStart = scheduleDay.getValidStart().diff(absoluteStart); // validStart - absoluteStart = Anzahl 5-Min.-Felder
        fieldCountEnd = scheduleDay.getValidEnd().diff(absoluteStart); // validEnd - absoluteStart = Anzahl 5-Min.-Felder

        // timeColumns befüllen
        try {
            Time time = absoluteStart.clone();
            for (int i = 0; i < totalNumberOfFields / 2; i++) {
                timeColumn1.add(time.clone());
                time.inc();
            }
            for (int i = totalNumberOfFields / 2; i < totalNumberOfFields; i++) {
                timeColumn2.add(time.clone());
                time.inc();
            }

        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
    }

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
        return dayName;
    }

    public Time getColumn1Time(int i) {
        return timeColumn1.get(i);
    }

    public Time getColumn2Time(int i) {
        return timeColumn2.get(i);
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
    public Object getValueAt(int row, int col) {
        return null;
    }

}
