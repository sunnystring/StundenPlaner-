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

    private JPanel dayColumn;
    private JLabel header;
    private TimeTable timeTable;
    private ScheduleData scheduleData;
    private ArrayList<TimeTable> dayColumnList;

    public Schedule(ScheduleData scheduleData) {

        this.scheduleData = scheduleData;
        dayColumnList = new ArrayList<>();
        setBackground(Colors.BACKGROUND);
    }

    public void createDayColumns() {

        setLayout(new GridLayout(1, scheduleData.getNumberOfDays()));

        for (int i = 0; i < scheduleData.getNumberOfDays(); i++) {

            dayColumn = new JPanel(new BorderLayout());
            header = new DayField(scheduleData.getDayColumnData(i).getDayName());
            timeTable = new TimeTable(scheduleData.getDayColumnData(i)); // Referenz auf entpr. DayColumn = TableModel
            dayColumnList.add(timeTable);   // TimeTables = DayColumns in Liste speichern
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
