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
import util.Colors;

/**
 *
 * Stundenplan-UI in {@link Schedule}
 */
public class TimeTable extends JTable {

    private TimeField timeField;
    private LectionField lectionField;
    private ScheduleData scheduleData;
    private StudentListData studentListData;
    public static final int DEFAULT_HEIGHT = 14;
    public static final int HEIGHT_DIFF = 4;
    public static final int UPPER_HEIGHT = DEFAULT_HEIGHT + HEIGHT_DIFF;
    public static final int LOWER_HEIGHT = DEFAULT_HEIGHT - HEIGHT_DIFF;
    private int rowHeight;

    public TimeTable(ScheduleData scheduleData, StudentListData studentListData) {
        this.scheduleData = scheduleData;
        this.studentListData = studentListData;
        setModel(scheduleData);
        lectionField = new LectionField(this, scheduleData); // darf erst hier erzeugt werden, weil scheduleData noch nicht definiert
        timeField = new TimeField(this, scheduleData);
        rowHeight = DEFAULT_HEIGHT;
        addMouseListener(lectionField);
        addMouseListener(timeField);
        addMouseMotionListener(lectionField);
        addMouseMotionListener(timeField);
        addMouseListener(studentListData);
        addMouseListener(scheduleData);
        setFillsViewportHeight(true);
        setBackground(Colors.BACKGROUND);
        setRowHeight(rowHeight);
    }

    public void updateParameters() {
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

    public void fireNextScheduleSizeOption() {
        lectionField.decrementFontSizes(decrementRowHeight());
        setRowHeight(rowHeight);
        scheduleData.fireTableDataChanged();
    }

    private boolean decrementRowHeight() {
        boolean stillDecrementing = rowHeight > LOWER_HEIGHT;
        if (stillDecrementing) {
            rowHeight--;
        } else {
            rowHeight = UPPER_HEIGHT;
        }
        return stillDecrementing;
    }

    public ScheduleFieldData getScheduleFieldAt(int row, int col) {
        return (ScheduleFieldData) getValueAt(row, col);
    }

    public LectionField getLectionField() {
        return lectionField;
    }

    public TimeField getTimeField() {
        return timeField;
    }
}
