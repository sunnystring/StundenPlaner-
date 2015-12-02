/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule;

import javax.swing.JTable;
import scheduleData.ScheduleData;
import studentListData.StudentListData;
import studentlist.StudentList;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class TimeTable extends JTable {

    private TimeField timeField;  // Renderer und Listener-Objekt
    private LectionField lectionField; // Renderer und Listener-Objekt

    public TimeTable(ScheduleData scheduleData, StudentListData studentListData) {

        setModel(scheduleData);
        setFillsViewportHeight(true);
        setBackground(Colors.BACKGROUND);
        setRowHeight(15);

        timeField = new TimeField(this);
        lectionField = new LectionField(this);

        for (int i = 0; i < scheduleData.getColumnCount(); i++) {
            if (i % 2 == 0) {
                getColumnModel().getColumn(i).setMaxWidth(10);
                getColumnModel().getColumn(i).setCellRenderer(timeField);
            } else {
                getColumnModel().getColumn(i).setPreferredWidth(50);
                getColumnModel().getColumn(i).setCellRenderer(lectionField);
            }
        }

        // Schedule: Renderer als Listener registrieren 
        addMouseListener(lectionField);  // triggert reset und übergibt lectionLength in LectionField
        addMouseListener(timeField); // triggert reset und übergibt lectionLength in TimeField
        addMouseMotionListener(lectionField); // Anzeige: bewirkt keine Änderung im TableModel
        addMouseMotionListener(timeField);  // Anzeige: bewirkt keine Änderung im TableModel
        addMouseListener(studentListData);
        addMouseListener(scheduleData);

    }

    public LectionField getLectionField() {
        return lectionField;
    }

    public TimeField getTimeField() {
        return timeField;
    }
}
