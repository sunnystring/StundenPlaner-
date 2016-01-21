/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleUI;

import javax.swing.JTable;
import scheduleData.ScheduleData;
import studentListData.StudentListData;
import util.Colors;

/**
 *
 * Stundenplan-UI in {@link Schedule}
 */
public class TimeTable extends JTable {

    private TimeField timeField;
    private LectionField lectionField;
    private ScheduleData scheduleData;
    StudentListData studentListData;

    public TimeTable(ScheduleData scheduleData, StudentListData studentListData) {
        this.scheduleData = scheduleData;
        this.studentListData = studentListData;
        setFillsViewportHeight(true);
        setBackground(Colors.BACKGROUND);
        setRowHeight(14);
    }

    public void setParameters() {
        setModel(scheduleData);
        createDefaultColumnsFromModel();
        lectionField = new LectionField(this, scheduleData);
        timeField = new TimeField(this, scheduleData);
        for (int i = 0; i < scheduleData.getColumnCount(); i++) {
            if (i % 2 == 0) {
                getColumnModel().getColumn(i).setMaxWidth(10);
                getColumnModel().getColumn(i).setCellRenderer(timeField);
            } else {
                getColumnModel().getColumn(i).setPreferredWidth(50);
                getColumnModel().getColumn(i).setCellRenderer(lectionField);
            }
        }
        addMouseListener(lectionField);
        addMouseListener(timeField);
        addMouseMotionListener(lectionField);
        addMouseMotionListener(timeField);
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
