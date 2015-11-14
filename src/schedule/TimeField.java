/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import scheduleData.FieldData;
import studentlist.StudentList;
import util.Colors;

/**
 *
 * @author mathiaskielholz
 */
public class TimeField extends LectionField {

    private int selectedRow, selectedCol; // MouseEvent: Koordinaten TimeTable

    public TimeField(Schedule schedule) {

        super(schedule);
        selectedRow = -1;
        selectedCol = -1;
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        FieldData fieldData = (FieldData) value;
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
            }
        }
        if (!fieldData.isMinute(col) && fieldData.getValidTime() != FieldData.FAVORITE) {
            setBackground(Colors.TIMEFIELD_HOUR);
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

        if (moveEnabled) {
            // MouseEvent liefert in Lection- und TimeField die gleichen Koordinaten
            Point p = m.getPoint();
            if (timeTable.rowAtPoint(p) == -1) {  // damit TimeField stehen bleibt wenn unten nicht mehr weiter einteilbar
                return;
            }
            selectedRow = timeTable.rowAtPoint(p);
            // Columns zuweisen
            if (timeTable.columnAtPoint(p) % 2 == 0) { // falls TimeColumn, diese zeichnen
                selectedCol = timeTable.columnAtPoint(p);
            } else {
                selectedCol = timeTable.columnAtPoint(p) - 1; // falls LectionColumn, die zugehörige TimeColumn links zeichnen
            }
            // TimeField zeichnen
            timeTable.repaint(timeTable.getCellRect(selectedRow, selectedCol, false));
            // Spaltenende 
            if (selectedRow + lectionLenght > rowCount) {
                if (selectedCol % 4 == 2) { // 2. TimeColumn
                    selectedRow = rowCount - lectionLenght; // TimeField freezen
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent m) {
        // StudentList 
        if (m.getSource() instanceof StudentList) {
            StudentList studentList = (StudentList) m.getSource();
            moveEnabled = studentList.isStudentSelected();
            // reset TimeColumn
            selectedRow = -1;
            selectedCol = -1;
        } // Schedule
        else {
            
            System.out.println("timetable in timeField");
        }
    }
}
