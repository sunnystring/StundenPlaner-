/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleGUI;

import scheduleData.DayColumnData;
import javax.swing.JTable;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class TimeTable extends JTable {

    private TimeField timeField; // Renderer und MouseListener
    private LectionField lectionField;
    private DayColumnData dayColumnData;

    public TimeTable(DayColumnData dayColumnData) {

        this.dayColumnData = dayColumnData;
        
        setModel(dayColumnData);
        setFillsViewportHeight(true);
        setBackground(Colors.BACKGROUND);
        setRowSelectionAllowed(true);
        setCellSelectionEnabled(true);
        // setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowHeight(13);
        getColumnModel().getColumn(0).setMaxWidth(10);
        getColumnModel().getColumn(1).setMinWidth(50);
        getColumnModel().getColumn(2).setMaxWidth(10);
        getColumnModel().getColumn(3).setMinWidth(50);

    }

    public void addListeners(TimeField timeField, LectionField lectionField) {
        addMouseListener(timeField);
        addMouseMotionListener(timeField);
        addMouseListener(lectionField);
        addMouseMotionListener(lectionField);
    }

    public TimeField getTimeField() {
        return timeField;
    }

    public void createTimeFieldRenderer(Schedule schedule) {
        this.timeField = new TimeField(dayColumnData, schedule);
        getColumnModel().getColumn(0).setCellRenderer(timeField);
        getColumnModel().getColumn(2).setCellRenderer(timeField);
    }

    public LectionField getLectionField() {
        return lectionField;
    }

    public void createLectionFieldRenderer(Schedule schedule) {
        this.lectionField = new LectionField();
        getColumnModel().getColumn(1).setCellRenderer(lectionField);
        getColumnModel().getColumn(3).setCellRenderer(lectionField);
    }

}
