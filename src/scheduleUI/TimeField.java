/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import scheduleData.ScheduleData;
import scheduleData.ScheduleFieldData;
import studentListData.StudentFieldData;
import studentListData.StudentListData;
import studentlistUI.StudentList;
import utils.Colors;

/**
 *
 * Renderer-Component für Zeit-Spalten in {@link TimeTable}
 */
public class TimeField extends LectionField {

    private int movedRow, movedCol;
   
    public TimeField(TimeTable timeTable, ScheduleData scheduleData) {
        super(timeTable, scheduleData);
        resetTimeColumn();
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(this.getFont().deriveFont(Font.PLAIN, size1));
    }

    public void resetTimeColumn() {
        movedRow = NULL_VALUE;
        movedCol = NULL_VALUE;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        ScheduleFieldData fieldData = (ScheduleFieldData) value;
        // Schülerzeiten anzeigen
        boolean isValidTime = fieldData.getValidTimeMark() == ScheduleFieldData.TIME_INTERVAL_1 || fieldData.getValidTimeMark() == ScheduleFieldData.TIME_INTERVAL_2;
        setText(fieldData.isMinute(row) ? fieldData.getMinute(row) : fieldData.getHour(row));
        setForeground(fieldData.isTeacherTime() ? Color.BLACK : Color.LIGHT_GRAY);

        if (fieldData.getValidTimeMark() == ScheduleFieldData.FAVORITE) {
            setBackground(Colors.FAVORITE);
        } else if (isValidTime) {
            setBackground(Colors.LIGHT_GREEN);
        } else {
            setBackground(Colors.BACKGROUND);
        }
        if (!fieldData.isMinute(col) && fieldData.getValidTimeMark() != ScheduleFieldData.FAVORITE) {  // Hour markieren, wenn kein Favorit oder Einzellektion, 
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

    @Override
    public void mouseMoved(MouseEvent m) {
        Point p = m.getPoint();
        if (timeTable.rowAtPoint(p) == NULL_VALUE) {  // ausserhalb JTable movedRow = -1
            return;
        }
        movedRow = timeTable.rowAtPoint(p);
        if (timeTable.columnAtPoint(p) % 2 == 0) { // falls TimeColumn
            movedCol = timeTable.columnAtPoint(p);
        } else {
            movedCol = timeTable.columnAtPoint(p) - 1; // falls LectionColumn, TimeColumn links davon zeichnen
        }
        if (movedRow >= rowCount - lectionLenght && movedCol % 4 == 2) {  // Stundenplan-Ende  
            movedRow = rowCount - lectionLenght;
        }
        timeTable.repaint(timeTable.getCellRect(movedRow, movedCol, false));
    }

    /* Änderungen triggern beim Wechsel in MoveMode */
    @Override
    public void mousePressed(MouseEvent m) {
        Point p = m.getPoint();
        int selectedRow, selectedCol;
        if (m.getSource() instanceof StudentList) {
            StudentList studentList = (StudentList) m.getSource();
            selectedRow = studentList.rowAtPoint(p);
            selectedCol = studentList.columnAtPoint(p);
            StudentListData studentListData = (StudentListData) studentList.getModel();
            if (selectedRow >= 0 && selectedCol > 0) {
                StudentFieldData studentFieldData = studentList.getStudentFieldDataAt(selectedRow, selectedCol);
                if (studentFieldData.isFieldSelected()) {
                    resetTimeColumn();
                    lectionLenght = studentListData.getStudent(selectedRow).getLectionLength();
                } else if (studentFieldData.isStudentListReleased()) {
                    resetTimeColumn();
                }
            }
        }
        if (m.getSource() instanceof TimeTable) {
            selectedRow = timeTable.rowAtPoint(p);
            selectedCol = timeTable.columnAtPoint(p);
            if (selectedRow >= 0) {
                ScheduleFieldData scheduleFieldData = (ScheduleFieldData) scheduleData.getValueAt(selectedRow, selectedCol);
                if (selectedCol % 2 == 1 && scheduleFieldData.isMoveEnabled()) {
                    lectionLenght = scheduleFieldData.getStudent().getLectionLength();
                }
            }
        }
    }
}
