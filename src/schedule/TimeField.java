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
import studentListData.StudentFieldData;
import studentListData.StudentListData;
import studentlist.StudentList;
import util.Colors;

/**
 *
 * @author mathiaskielholz
 */
public class TimeField extends LectionField {

    private int movedRow, movedCol; // MouseEvent: Koordinaten TimeTable

    public TimeField(TimeTable timeTable) {

        super(timeTable);
        resetTimeColumn();
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
    }

    private void resetTimeColumn() {
        movedRow = -1;
        movedCol = -1;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        ScheduleFieldData fieldData = (ScheduleFieldData) value;

        // ValidTimes zeichnen
        boolean isValidTime = fieldData.getValidTimeMark() == ScheduleFieldData.TIME_INTERVAL_1 || fieldData.getValidTimeMark() == ScheduleFieldData.TIME_INTERVAL_2;
        // Text 
        setText(fieldData.isMinute(row) ? fieldData.getMinute(row) : fieldData.getHour(row));
        setForeground(fieldData.isTeacherTime() ? Color.BLACK : Color.LIGHT_GRAY);
        // Background Optionen
        if (fieldData.getValidTimeMark() == ScheduleFieldData.FAVORITE) {
            setBackground(Colors.FAVORITE);
        } else if (isValidTime) {
            setBackground(Colors.LIGHT_GREEN);
        } else {
            setBackground(Colors.BACKGROUND);
        }
        // ausser bei Favorit und Einzellektion, Hour immer markieren
        if (!fieldData.isMinute(col) && fieldData.getValidTimeMark() != ScheduleFieldData.FAVORITE) {
            if (!fieldData.isMinute(row) && isValidTime) {
                setBackground(Colors.LIGHT_GREEN);
            } else {
                setBackground(Colors.TIMEFIELD_HOUR);
            }
        }
        // Mouseover 
        if (fieldData.isMoveEnabled() && !fieldData.isLectionAllocated()) {
            if (row == movedRow && col == movedCol) {
                setBackground(Color.GRAY);
                setForeground(Color.WHITE);
            }
        }
        return this;
    }

    /* MouseEvents triggern Änderungen in der View des MoveMode, die nicht über das TableModel gemacht werden*/
    @Override
    public void mouseMoved(MouseEvent m) {

        // MouseEvent liefert in Lection- und TimeField die gleichen Koordinaten
        Point p = m.getPoint();
        if (timeTable.rowAtPoint(p) == -1) {  // damit TimeField stehen bleibt wenn ausserhalb Table
            return;
        }
        movedRow = timeTable.rowAtPoint(p);
        // Columns zuweisen
        if (timeTable.columnAtPoint(p) % 2 == 0) { // falls 1. TimeColumn
            movedCol = timeTable.columnAtPoint(p);
        } else {
            movedCol = timeTable.columnAtPoint(p) - 1; // falls LectionColumn, die zugehörige TimeColumn links zeichnen
        }
        // TimeField zeichnen
        timeTable.repaint(timeTable.getCellRect(movedRow, movedCol, false));
        // Spaltenende 2. TimeColumn
        if (movedRow + lectionLenght > rowCount) {
            if (movedCol % 4 == 2) {
                movedRow = rowCount - lectionLenght; // TimeField freezen
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent m) {

        Point p = m.getPoint();
        // StudentList 
        if (m.getSource() instanceof StudentList) {
            StudentList studentList = (StudentList) m.getSource();
            StudentListData studentListData = (StudentListData) studentList.getModel();
            int selectedRow = studentList.rowAtPoint(p);
            int selectedCol = studentList.columnAtPoint(p);
            // Events nur von selectedRow, ausserhalb JTable row =-1
            if (selectedRow >= 0 && selectedCol > 0) {
                StudentFieldData studentFieldData = (StudentFieldData) studentListData.getValueAt(selectedRow, selectedCol);
                // lectionlength übergeben
                if (studentFieldData.isFieldSelected()) {
                    resetTimeColumn();
                    lectionLenght = studentListData.getStudent(selectedRow).getLectionType();
                } // falls Student-Selection rückgängig gemacht
                else if (studentFieldData.isStudentListEnabled()) {
                    resetTimeColumn();
                }
            }
        }
        // Schedule (TimeTable)
        if (m.getSource() instanceof TimeTable) {
            int selectedRow = timeTable.rowAtPoint(p);
            int selectedCol = timeTable.columnAtPoint(p);
            if (selectedRow >= 0) { //  ausserhalb JTable: selectedRow = -1
                ScheduleFieldData scheduleFieldData = (ScheduleFieldData) scheduleData.getValueAt(selectedRow, selectedCol);
                // Events nur von LectionColumn, Lection muss entsperrt sein
                if (selectedCol % 2 == 1 && scheduleFieldData.isMoveEnabled()) {
                    // falls in Move-State gewechselt, TimeColumn updaten 
                    lectionLenght = scheduleFieldData.getStudent().getLectionType();
                }
            }
        }
    }
}
