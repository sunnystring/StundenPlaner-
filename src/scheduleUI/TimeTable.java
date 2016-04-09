/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleUI;

import javax.swing.JTable;
import scheduleData.ScheduleData;
import scheduleData.ScheduleFieldData;
import studentListData.StudentListData;
import userUtilsUI.ScheduleZoom;
import utils.Colors;

/**
 *
 * Stundenplan-UI in {@link Schedule}
 */
public class TimeTable extends JTable {

    private final TimeField timeField;
    private final LectionField lectionField;
    private final ScheduleData scheduleData;

    public TimeTable(ScheduleData scheduleData, StudentListData studentListData) {
        this.scheduleData = scheduleData;
        setModel(scheduleData);
        lectionField = new LectionField(this, scheduleData); // darf erst hier erzeugt werden, weil scheduleData noch nicht definiert
        timeField = new TimeField(this, scheduleData);
        addMouseListener(lectionField);
        addMouseListener(timeField);
        addMouseMotionListener(lectionField);
        addMouseMotionListener(timeField);
        addMouseListener(studentListData);
        addMouseListener(scheduleData);
        setFillsViewportHeight(true);
        setBackground(Colors.BACKGROUND);
        setRowHeight(ScheduleZoom.DEFAULT_HEIGHT);
    }

    public void update() {
        createDefaultColumnsFromModel();
        timeField.initScheduleDimension();
        timeField.resetTimeColumn();
        lectionField.initScheduleDimension();
        lectionField.resetLectionColumn();
        for (int i = 0; i < scheduleData.getColumnCount(); i++) {
            if (i % 2 == 0) {
                getColumnModel().getColumn(i).setMaxWidth(10);
                getColumnModel().getColumn(i).setCellRenderer(timeField);
            } else {
                getColumnModel().getColumn(i).setPreferredWidth(50);
                getColumnModel().getColumn(i).setCellRenderer(lectionField);
            }
        }
    }

    public ScheduleFieldData getScheduleFieldDataAt(int row, int col) {
        return (ScheduleFieldData) getValueAt(row, col);
    }

    public LectionField getLectionField() {
        return lectionField;
    }

    public TimeField getTimeField() {
        return timeField;
    }
}
