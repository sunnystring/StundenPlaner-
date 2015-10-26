/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleGUI;

import scheduleData.ScheduleData;
import studentData.StudentData;
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

    private JPanel dayColumn;
    private JLabel header;
    private TimeTable timeTable;
    private ScheduleData scheduleData;
    private StudentData studentData;
    private ArrayList<TimeTable> dayColumnList;

    public Schedule(ScheduleData scheduleData, StudentData studentData) {

        this.scheduleData = scheduleData;
        this.studentData = studentData;
        dayColumnList = new ArrayList<>();
        setBackground(Colors.BACKGROUND);
    }

    public void createDayColumns() {

        setLayout(new GridLayout(1, scheduleData.getNumberOfDays()));

        for (int i = 0; i < scheduleData.getNumberOfDays(); i++) {

            dayColumn = new JPanel(new BorderLayout());
            header = new DayField(scheduleData.getDayColumnData(i).getDayName());
            timeTable = new TimeTable(scheduleData.getDayColumnData(i));
          //  scheduleData.getDayColumnData(i).setTimeTable(timeTable); // DayColumnData(= TableModel) braucht Referenz auf "seine" TimeTable
            dayColumnList.add(timeTable);   // TimeTables = DayColumns speichern
            dayColumn.add(header);
            dayColumn.add(BorderLayout.NORTH, header);
            dayColumn.add(BorderLayout.CENTER, timeTable);
            add(dayColumn); // zeichnen
        }
    }

    /*  Getter, Setter   */
    public ArrayList<TimeTable> getDayColumnList() {
        return dayColumnList;
    }
}
