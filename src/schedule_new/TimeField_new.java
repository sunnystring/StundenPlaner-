/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule_new;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import scheduleData.FieldData;
import scheduleData_new.FieldData_new;
import util.Colors;

/**
 *
 * @author mathiaskielholz
 */
public class TimeField_new extends LectionField_new {

    private JTable timeTable;
    private int selectedRow, selectedCol; // MouseEvent: Koordinaten TimeTable

    public TimeField_new(JTable timeTable) {

        super(timeTable);
        this.timeTable = timeTable;
        selectedRow = -1;
        selectedCol = -1;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        FieldData_new fieldData = (FieldData_new) value;
        // Text ausgeben
        if (fieldData.isMinute(row)) {
            setText(fieldData.getMinute(row));
        } else {
            setText(fieldData.getHour(row));
        }
        // Foreground zeichen
        if (fieldData.isTeacherTime()) {
            setForeground(Color.BLACK);
        } else {
            setForeground(Color.LIGHT_GRAY);
        }
        // Background zeichnen
        if (fieldData.getValidTime() == FieldData.FAVORITE) {
            setBackground(Colors.FAVORITE);
        } else if (fieldData.getValidTime() == FieldData.TIME_INTERVAL_1 || fieldData.getValidTime() == FieldData.TIME_INTERVAL_2) {
            setBackground(Colors.LIGHT_GREEN);
        } else {
            setBackground(Colors.BACKGROUND);
        }
        if (!fieldData.isMinute(row) && fieldData.getValidTime() != FieldData.FAVORITE) {
            if (fieldData.getValidTime() == FieldData.TIME_INTERVAL_1 || fieldData.getValidTime() == FieldData.TIME_INTERVAL_2) { // falls Einzellektion auf volle Stunde fällt 
                setBackground(Colors.LIGHT_GREEN);
            } else {
                setBackground(Colors.TIMEFIELD_HOUR);
            }
        }
        // Mouseover Schedule
        if (row == selectedRow && col == selectedCol) {
            setBackground(Color.GRAY);
            setForeground(Color.WHITE);
        }
        return this;
    }

    /*  MouseMotionListener Implementation */
    @Override
    public void mouseMoved(MouseEvent m) {
        // MouseEvent liefert in Lection- und TimeField die gleichen Koordinaten
        Point p = m.getPoint();
        if (timeTable.columnAtPoint(p) % 2 == 0) { // falls TimeColumn, diese zeichnen
            selectedCol = timeTable.columnAtPoint(p);
        } else {
            selectedCol = timeTable.columnAtPoint(p) - 1; // falls LectionColumn, die zugehörige TimeColumn links zeichnen
        }
        selectedRow = timeTable.rowAtPoint(p);
        timeTable.repaint(timeTable.getCellRect(selectedRow, selectedCol, false));

    }

    @Override
    public void mouseExited(MouseEvent m) {

        Point p = m.getPoint();
        if (timeTable.rowAtPoint(p) == 0) {  // falls LectionColumn, diese zeichnen
            if (timeTable.columnAtPoint(p) % 2 == 0) { // falls TimeColumn, diese zeichnen
                selectedCol = timeTable.columnAtPoint(p);
            } else {
                selectedCol = timeTable.columnAtPoint(p) - 1; // falls LectionColumn, die zugehörige TimeColumn links zeichnen
            }
            selectedRow = 0;
            for (int i = 0; i < rowCount; i++) {
                timeTable.repaint(timeTable.getCellRect(selectedRow + i, selectedCol, false)); // alle Rows unter selectedCol
            }
        }
    }
}
