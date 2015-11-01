/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule_new;

import studentListData.StudentDay;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import scheduleData.FieldData;
import scheduleData_new.DayColumnData_new;
import scheduleData_new.FieldData_new;
import scheduleData_new.ScheduleData_new;
import studentlistGUI.StudentList;
import util.Colors;

/**
 *
 * @author mathiaskielholz
 */
public class TimeField_new extends JLabel implements TableCellRenderer, MouseMotionListener {

    private JTable timeTable;
    private ScheduleData_new scheduleData;
    private int selectedRow, selectedCol; // MouseEvent: Koordinaten TimeTable
    private int tempRow, tempCol;

    public TimeField_new(JTable timeTable) {

        this.timeTable = timeTable;
        scheduleData = (ScheduleData_new) timeTable.getModel();
        selectedRow = -1;
        tempRow = -1;
        tempCol = 0;

        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        FieldData_new fieldData = (FieldData_new) value;
        //   System.out.println("col="+col+"   row="+row+"   teacherTime="+fieldData.isTeacherTime()+"   colID="+fieldData.getColumnID()+"hasFoscus="+hasFocus+"isSelected="+isSelected);
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
        // Mouseover 
        if (row == selectedRow && col == selectedCol) {
            setBackground(Color.GRAY);
            setForeground(Color.WHITE);
        }
        return this;
    }

    /*  MouseMotionListener Implementation */
    @Override
    public void mouseMoved(MouseEvent m) {

        // Schedule    
        Point p = m.getPoint();
        selectedCol = timeTable.columnAtPoint(p) - 1;
        selectedRow = timeTable.rowAtPoint(p);

        if (selectedRow > tempRow) {  // abwärts
            for (int i = 0; i < scheduleData.getRowCount(); i++) {
                timeTable.repaint(timeTable.getCellRect(selectedRow - i, selectedCol, false));
            }

        } else {   // aufwärts
            for (int i = 0; i < scheduleData.getRowCount(); i++) {
                timeTable.repaint(timeTable.getCellRect(selectedRow + i, selectedCol, false));
            }
        }
        if (selectedCol > tempCol) {   // nach rechts
            for (int i = 0; i < scheduleData.getColumnCount() * 4; i++) {
                timeTable.repaint(timeTable.getCellRect(selectedRow, selectedCol - i, false));
            }
//            if (tempCol == 0) {
//                for (int i = 0; i < scheduleData.getRowCount(); i++) {
//                    timeTable.repaint(timeTable.getCellRect(i, 0, false));
//                }
//            }
        } else {  // nach links
            for (int i = 0; i < scheduleData.getColumnCount() * 4; i++) {
                timeTable.repaint(timeTable.getCellRect(selectedRow, selectedCol + i, false));
            }
        }
        // Randzonen
//        if (tempCol == 0 && selectedCol == 0) { // links
//            for (int i = 0; i < scheduleData.getRowCount(); i++) {
//                timeTable.repaint(timeTable.getCellRect(i, 0, false));
//            }
//        }
        tempCol = selectedCol;
        tempRow = selectedRow;
    }

    @Override
    public void mouseDragged(MouseEvent me) {
    }
}
