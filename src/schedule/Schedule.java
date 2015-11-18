/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import scheduleData.ScheduleData;
import studentlist.StudentList;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class Schedule extends JPanel {

    private ScheduleData scheduleData;
    private JPanel header;
    private JTable timeTable;
    private TimeField timeField;  // Renderer und Listener-Objekt
    private LectionField lectionField; // Renderer und Listener-Objekt

    public Schedule(ScheduleData scheduleData) {

        this.scheduleData = scheduleData;
        setLayout(new BorderLayout());

        header = new JPanel();
        header.setLayout(new GridLayout(1, 4));
        header.setBackground(Colors.BACKGROUND);

        for (int i = 0; i < scheduleData.getNumberOfDays(); i++) {
            header.add(new DayField(scheduleData.getDayColumn(i).getDayName()));
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
        timeTable.setRowHeight(14);

        timeField = new TimeField(this);
        lectionField = new LectionField(this);

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
        timeTable.addMouseMotionListener(lectionField);
        timeTable.addMouseListener(timeField);
        timeTable.addMouseListener(lectionField);
    }

    public void addStudentField(StudentList studentList) {
        timeTable.addMouseListener(studentList.getStudentField()); // Klick in timeTable ändert StudentList-Ansicht (nicht das TableModel)
    }

    public JTable getTimeTable() {
        return timeTable;
    }

    public LectionField getLectionField() {
        return lectionField;
    }

    public TimeField getTimeField() {
        return timeField;
    }
}
