/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;
import scheduleData.ScheduleData;
import studentListData.StudentListData;
import userUtil.ScheduleZoom;
import util.Colors;

/**
 *
 * View des ganzen Stundenplans, bestehend aus {@link TimeTable} und
 * {@link DayField}
 */
public class Schedule extends JPanel {

    private final ScheduleData scheduleData;
    private JPanel header;
    private final TimeTable timeTable;
    private final ScheduleZoom scheduleZoom;

    public Schedule(ScheduleData scheduleData, StudentListData studentListData) {
        this.scheduleData = scheduleData;
        header = new JPanel();
        header.setLayout(new GridLayout(1, 4));
        header.setBackground(Colors.BACKGROUND);
        timeTable = new TimeTable(scheduleData, studentListData); // studentListData = Referenz für MouseListener
        scheduleZoom = new ScheduleZoom();
        setLayout(new BorderLayout());
        setBackground(Colors.BACKGROUND);
        add(BorderLayout.NORTH, header);
        add(BorderLayout.CENTER, timeTable);
    }

    public void createHeader() {
        for (int i = 0; i < scheduleData.getNumberOfValidDays(); i++) {
            header.add(new DayField(scheduleData.getDayColumn(i).getDayName()));
        }
    }

    public void fireNextScheduleSize() {
        scheduleZoom.setNextSize();
        timeTable.setRowHeight(scheduleZoom.getRowHeight());
        timeTable.getLectionField().setFontSize1(scheduleZoom.getFontSize1());
        timeTable.getLectionField().setFontSize2(scheduleZoom.getFontSize2());
        scheduleData.fireTableDataChanged();
    }

    public void removeHeader() {
        header.removeAll();
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }
}
