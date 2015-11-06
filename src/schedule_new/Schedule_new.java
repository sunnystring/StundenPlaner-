/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule_new;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.JTable;
import scheduleData_new.DayColumnData_new;
import scheduleData_new.ScheduleData_new;
import studentListData.StudentDay;
import studentlistGUI.StudentList;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class Schedule_new extends JPanel {

    private ScheduleData_new scheduleData;
    private JPanel header;
    private JTable timeTable;
    private TimeField_new timeField;  // Renderer und Listener-Objekt
    private LectionField_new lectionField; // Renderer und Listener-Objekt

    public Schedule_new(ScheduleData_new scheduleData) {

        this.scheduleData = scheduleData;

        setLayout(new BorderLayout());

        header = new JPanel();
        header.setLayout(new GridLayout(1, 4));
        header.setBackground(Colors.BACKGROUND);

        for (int i = 0; i < scheduleData.getNumberOfDays(); i++) {
            header.add(new DayField_new(scheduleData.getDayColumn(i).getDayName()));
        }
        createTimeTable();
        setBackground(Colors.BACKGROUND);

        add(BorderLayout.NORTH, header);
        add(BorderLayout.CENTER, timeTable);
    }

    private void createTimeTable() {

        timeTable = new JTable(scheduleData); // scheduleData = TableModel
        timeTable.setFillsViewportHeight(true);
        timeTable.setBackground(Colors.BACKGROUND);
        timeTable.setRowSelectionAllowed(true);
        timeTable.setCellSelectionEnabled(true);
        timeTable.setRowHeight(14);

        timeField = new TimeField_new(timeTable);
        lectionField = new LectionField_new(timeTable);

        for (int i = 0; i < scheduleData.getColumnCount(); i++) {
            if (i % 2 == 0) {
                timeTable.getColumnModel().getColumn(i).setMaxWidth(10);
                timeTable.getColumnModel().getColumn(i).setCellRenderer(timeField);
            } else {
                timeTable.getColumnModel().getColumn(i).setPreferredWidth(50);
                timeTable.getColumnModel().getColumn(i).setCellRenderer(lectionField);
            }
        }
        timeTable.addMouseListener(scheduleData); // Click in timeTable ändert TableModel (scheduleData)
        timeTable.addMouseMotionListener(timeField);  // Anzeige: bewirkt keine Änderung im TableModel
       // timeTable.addMouseListener(timeField);
        timeTable.addMouseMotionListener(lectionField);
        timeTable.addMouseListener(lectionField);

    }

    public TimeField_new getTimeField() {
        return timeField;
    }

    public LectionField_new getLectionField() {
        return lectionField;
    }

}
