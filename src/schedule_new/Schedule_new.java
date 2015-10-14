/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule_new;

import core2.ScheduleData;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class Schedule_new extends JPanel {

    private JPanel dayColumn;
    private JLabel header;
    private TimeTable timeTable;
    private ScheduleData scheduleData;

    public Schedule_new(ScheduleData scheduleData) {

        this.scheduleData = scheduleData;
        setLayout(new GridLayout(1, 3));
        setBackground(Colors.LIGHT_GRAY);
    }

    public void createDayColumns() {

        for (int i = 0; i < scheduleData.getNumberOfDays(); i++) {

            dayColumn = new JPanel(new BorderLayout(1, 1));
            header = new DayField_new(scheduleData.getDayColumnModel(i).getDayName());
            timeTable = new TimeTable(scheduleData.getDayColumnModel(i));
            dayColumn.add(header);
            dayColumn.add(BorderLayout.NORTH, header);
            dayColumn.add(BorderLayout.CENTER, timeTable);
            add(dayColumn); // zeichnen

        }

    }

}
