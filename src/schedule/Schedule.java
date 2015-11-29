/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;
import scheduleData.ScheduleData;
import studentListData.StudentListData;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class Schedule extends JPanel {

    private ScheduleData scheduleData;
    private JPanel header;
    private TimeTable timeTable;
 
    public Schedule(ScheduleData scheduleData, StudentListData studentListData) {

        this.scheduleData = scheduleData;
        setLayout(new BorderLayout());

        header = new JPanel();
        header.setLayout(new GridLayout(1, 4));
        header.setBackground(Colors.BACKGROUND);
        for (int i = 0; i < scheduleData.getNumberOfDays(); i++) {
            header.add(new DayField(scheduleData.getDayColumn(i).getDayName()));
        }
        
        timeTable = new TimeTable(scheduleData, studentListData);
        
        setBackground(Colors.BACKGROUND);
        add(BorderLayout.NORTH, header);
        add(BorderLayout.CENTER, timeTable);
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }
}
