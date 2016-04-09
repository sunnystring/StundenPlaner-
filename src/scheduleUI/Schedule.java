/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import scheduleData.ScheduleData;
import studentListData.StudentListData;
import userUtilsUI.ScheduleZoom;
import utils.Colors;

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
    private ArrayList<DayField> headerFieldList;

    public Schedule(ScheduleData scheduleData, StudentListData studentListData) {
        this.scheduleData = scheduleData;
        header = new JPanel();
        header.setLayout(new GridLayout(1, 4));
        header.setBackground(Colors.BACKGROUND);
        timeTable = new TimeTable(scheduleData, studentListData); // studentListData = Referenz für MouseListener
        scheduleZoom = new ScheduleZoom();
        headerFieldList = new ArrayList<>();
        setLayout(new BorderLayout());
        setBackground(Colors.BACKGROUND);
        add(BorderLayout.NORTH, header);
        add(BorderLayout.CENTER, timeTable);
    }

    public void createHeader() {
        for (int i = 0; i < scheduleData.getNumberOfValidDays(); i++) {
            DayField dayField = new DayField(scheduleData.getDayNameAt(i));
            headerFieldList.add(dayField);
            header.add(dayField);
        }
    }

    public void updateHeader() {
        removeHeader();
        for (int i = 0; i < scheduleData.getNumberOfValidDays(); i++) {
            DayField dayField = new DayField(scheduleData.getDayNameAt(i));
            headerFieldList.add(dayField);
            header.add(dayField);
            scheduleData.getBreakWatcher().check(i);
        }
    }

    public void fireNextScheduleSize() {
        scheduleZoom.setNextSize();
        timeTable.getLectionField().setFontSize1(scheduleZoom.getFontSize1());
        timeTable.getLectionField().setFontSize2(scheduleZoom.getFontSize2());
        timeTable.setRowHeight(scheduleZoom.getRowHeight());
        scheduleData.fireTableDataChanged();
    }

    public void removeHeader() {
        headerFieldList.clear();
        header.removeAll();
    }

    public void showBreakRequired(int dayIndex, Color col, String msg) {
        DayField dayField = headerFieldList.get(dayIndex);
        dayField.setBackground(col);
        dayField.setText("  " + msg);
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }
}
