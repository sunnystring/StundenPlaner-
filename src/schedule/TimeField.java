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
import scheduleData.ScheduleFieldData;
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
        resetTimeColumn();
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
    }

    private void resetTimeColumn() {
        selectedRow = -1;
        selectedCol = -1;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        ScheduleFieldData fieldData = (ScheduleFieldData) value;
        // Text ausgeben
        setText(fieldData.isMinute(row) ? fieldData.getMinute(row) : fieldData.getHour(row));
        // Foreground zeichen
        setForeground(fieldData.isTeacherTime() ? Color.BLACK : Color.LIGHT_GRAY);
        // Background zeichnen
        if (fieldData.getValidTime() == ScheduleFieldData.FAVORITE) {
            setBackground(Colors.FAVORITE);
        } else if (fieldData.getValidTime() == ScheduleFieldData.TIME_INTERVAL_1 || fieldData.getValidTime() == ScheduleFieldData.TIME_INTERVAL_2) {
            setBackground(Colors.LIGHT_GREEN);
        } else {
            setBackground(Colors.BACKGROUND);
        }
        if (!fieldData.isMinute(row) && fieldData.getValidTime() != ScheduleFieldData.FAVORITE) {
            if (fieldData.getValidTime() == ScheduleFieldData.TIME_INTERVAL_1 || fieldData.getValidTime() == ScheduleFieldData.TIME_INTERVAL_2) { // falls Einzellektion auf volle Stunde fällt 
                setBackground(Colors.LIGHT_GREEN);
            }
        }
        if (!fieldData.isMinute(col) && fieldData.getValidTime() != ScheduleFieldData.FAVORITE) {
            setBackground(Colors.TIMEFIELD_HOUR);
        }
        // Mouseover Schedule
        if (row == selectedRow && col == selectedCol) {
            setBackground(Color.GRAY);
            setForeground(Color.WHITE);
        }
        return this;
    }

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
            moveEnabled = studentList.isStudentSelected(); // Selection-State StudentList
            resetTimeColumn();
        } // Schedule
        else {
            moveEnabled = !scheduleData.isLectionAllocated();
            resetTimeColumn();
        }
    }
}
