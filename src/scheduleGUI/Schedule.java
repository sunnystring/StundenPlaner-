/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleGUI;

import scheduleData.ScheduleData;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class Schedule extends JPanel {

    private ScheduleData scheduleData;
    private ArrayList<TimeTable> dayColumnList;  // gleiches Mapping wie DayColumnDataList
    private int tempScheduleDayID, tempStudentID; // globale Zwischenspeicher für DayColumn-Management
            
            //selectedRow, selectedCol;  

    public Schedule(ScheduleData scheduleData) {

        this.scheduleData = scheduleData;
        dayColumnList = new ArrayList<>();
        tempScheduleDayID = 0;
        tempStudentID = 0;
   //     tempSelectedRow = -2;


        setBackground(Colors.BACKGROUND);
    }

    // DayColumn-Widget erzeugen und anzeigen, Listener adden 
    public void createDayColumns() {

        setLayout(new GridLayout(1, scheduleData.getNumberOfDays()));

        for (int i = 0; i < scheduleData.getNumberOfDays(); i++) {

            JPanel dayColumn = new JPanel(new BorderLayout());
            JLabel header = new DayField(scheduleData.getDayColumnData(i).getDayName());
            TimeTable timeTable = new TimeTable(scheduleData.getDayColumnData(i)); // Referenz auf entpr. DayColumn = TableModel
            timeTable.createTimeFieldRenderer(this); // Renderer und Listener mit Referenz auf "Verwaltungszentrale" (= Schedule)
            timeTable.createLectionFieldRenderer(this);
            dayColumnList.add(timeTable);  // Nur die timeTables müssen später ansprechbar sein, nicht die ganze DayColumn
            dayColumn.add(header);
            dayColumn.add(BorderLayout.NORTH, header);
            dayColumn.add(BorderLayout.CENTER, timeTable);
            add(dayColumn); // zeichnen
        }
        // alle Listener an alle TimeTables hängen (Listener = Renderer = Time-/LectionFields)
        for (int i = 0; i < scheduleData.getNumberOfDays(); i++) {
            TimeField timeField = dayColumnList.get(i).getTimeField();
            LectionField lectionField = dayColumnList.get(i).getLectionField();
            for (int j = 0; j < scheduleData.getNumberOfDays(); j++) {
                dayColumnList.get(j).addListeners(timeField, lectionField);
            }
        }
    }

    /*  Getter, Setter   */
    public ArrayList<TimeTable> getDayColumnList() {
        return dayColumnList;
    }

    public TimeTable getTimeTable(int i) {
        return dayColumnList.get(i);

    }

//    public int getTempSelectedRow() {
//        return tempSelectedRow;
//    }
//
//    public void setTempSelectedRow(int selectedRow) {
//        this.tempSelectedRow = selectedRow;
//    }

//    public int getTempSelectedCol() {
//        return selectedCol;
//    }
//
//    public void setTempSelectedCol(int selectedCol) {
//        this.selectedCol = selectedCol;
//    }

    public int getTempScheduleDayID() {
        return tempScheduleDayID;
    }

    public void setTempScheduleDayID(int tempScheduleDayID) {
        this.tempScheduleDayID = tempScheduleDayID;
    }

    public int getTempStudentID() {
        return tempStudentID;
    }

    public void setTempStudentID(int tempStudentID) {
        this.tempStudentID = tempStudentID;
    }
    
}
