/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleUI;

import core.Database;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPanel;
import scheduleData.ScheduleData;
import studentListData.StudentListData;
import userUtilsUI.ScheduleZoom;
import studentJournal.JournalDayView;
import static utils.Colors.*;

/**
 *
 * View des ganzen Stundenplans, bestehend aus {@link TimeTable} und
 * {@link DayField}
 */
public class Schedule extends JPanel {

    private Database database;
    private ScheduleData scheduleData;
    private JPanel header;
    private final TimeTable timeTable;
    private final ScheduleZoom scheduleZoom;
    private ArrayList<DayField> headerFieldList;
    private MouseAdapter headerListener;
    private JournalDayView journalDayView;

    public Schedule(Database database, ScheduleData scheduleData, StudentListData studentListData) {
        this.database = database;
        this.scheduleData = scheduleData;
        header = new JPanel();
        header.setLayout(new GridLayout(1, 4));
        header.setBackground(BACKGROUND_COLOR);
        timeTable = new TimeTable(scheduleData, studentListData); // studentListData = Referenz für MouseListener
        scheduleZoom = new ScheduleZoom();
        headerFieldList = new ArrayList<>();
        journalDayView = new JournalDayView(database, scheduleData);
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        add(BorderLayout.NORTH, header);
        add(BorderLayout.CENTER, timeTable);
    }

    public void createHeader() {
        for (int i = 0; i < scheduleData.getNumberOfValidDays(); i++) {
            DayField dayField = new DayField(scheduleData.getDayNameAt(i));
            dayField.setDayIndex(i);
            addHeaderListener(dayField);
            addDayViewRefsToHeader();
            headerFieldList.add(dayField);
            header.add(dayField);
        }
    }

    public void updateHeader() {
        removeHeader();
        for (int i = 0; i < scheduleData.getNumberOfValidDays(); i++) {
            DayField dayField = new DayField(scheduleData.getDayNameAt(i));
            dayField.setDayIndex(i);
            dayField.removeMouseListener(headerListener);
            addHeaderListener(dayField);
            addDayViewRefsToHeader();
            headerFieldList.add(dayField);
            header.add(dayField);
            scheduleData.getBreakWatcher().check(i);
        }
    }

    private void addHeaderListener(DayField dayField) {
        headerListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                journalDayView.dispose();
                journalDayView.showStudentJournals((DayField) e.getSource());
                journalDayView.setVisible(true);
            }
        };
        dayField.addMouseListener(headerListener);
    }

    private void addDayViewRefsToHeader() {
        for (DayField d : headerFieldList) {
            d.setDayView(journalDayView);
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

    public ArrayList<DayField> getHeaderFieldList() {
        return headerFieldList;
    }

}
