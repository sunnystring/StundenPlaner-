/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule;

import mainframe.WidgetInteraction;
import core.ScheduleTimes;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import studentlist.StudentList;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class Schedule extends JComponent {

    private WidgetInteraction wi;  // Schalter für GUI Management

    private ScheduleTimeFrame timeFrame;  // globale Unterricht-Zeitgrenzen des Stundenplans
    private ArrayList<DayColumn> dayColumnList;  // enthält alle DayColumns des Schedules

    public Schedule(WidgetInteraction wi) {

        this.wi = wi;
        timeFrame = new ScheduleTimeFrame();
        dayColumnList = new ArrayList<>();

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBackground(Colors.BACKGROUND);
    }

    /*  Referenz auf eine DayColumn  */
    public DayColumn getDayColumn(int index) {
        return dayColumnList.get(index);
    }
    /*  Referenz auf DayColumnList */

    public ArrayList<DayColumn> getDayColumnList() {
        return dayColumnList;
    }

    /* alle DayColumns erzeugen und initialisieren */
    public void initSchedule(ScheduleTimes scheduleTimes) {

        for (int i = 0; i < scheduleTimes.getNumberOfDays(); i++) {
            dayColumnList.add(new DayColumn(wi)); // alle DayColumns erzeugen
            timeFrame.initTimeFrame(scheduleTimes.getScheduleDay(i)); // absolutStart und absolutEnd bestimmen
        }
        for (int i = 0; i < scheduleTimes.getNumberOfDays(); i++) {
            dayColumnList.get(i).setTimeFrame(scheduleTimes.getScheduleDay(i), timeFrame); // ScheduleDay, TotalNumberOfFields, FieldCounts einsetzen
        }
    }

    public void drawSchedule() {

        for (DayColumn dayColumn : dayColumnList) {
            dayColumn.drawDayColumn();
            add(dayColumn);  // Daycolumns in Schedule zeichnen
        }
    }

    /*  Listener an alle LectionFields aller DayColumns adden */
    public void addListener(StudentList studentList) {

        for (DayColumn dayColumn : dayColumnList) {

            LectionField[] lectionFieldList;
            lectionFieldList = dayColumn.getLectionFieldList();

            for (int i = 0; i < lectionFieldList.length; i++) {
                lectionFieldList[i].addMouseListener(dayColumn); // in DayColumn und TimeField implementiert
                lectionFieldList[i].addMouseListener(studentList);  // Referenz auf StudentList 
                /*  innerer Loop: jedes LectionField bekommt Referenzen auf alle DayColumns, damit die ValidTimes aller Tage farbig angezeigt werden können*/
                for (DayColumn dayCol : dayColumnList) {
                    lectionFieldList[i].addValidTimeListener(dayCol);
                }
            }
        }
    }

}
