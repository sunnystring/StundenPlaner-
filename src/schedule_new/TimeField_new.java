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
//    private ScheduleData_new scheduleData;
       private int selectedRow, selectedCol; // MouseEvent: Koordinaten TimeTable
//    private int tempRow, tempCol;
//    private int rowCount, columnCount;
    public TimeField_new(JTable timeTable) {

        super(timeTable);
        this.timeTable = timeTable;
//        scheduleData = (ScheduleData_new) timeTable.getModel();
        selectedRow = -1;
        selectedCol = -1;

//        tempRow = -1;
//        tempCol = -1;
//        rowCount = scheduleData.getRowCount();
//        columnCount = scheduleData.getColumnCount();
//
//        setHorizontalAlignment(SwingConstants.CENTER);
//        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
//        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        
  // System.out.println("timefield renderer:  selectedRow = " + selectedRow +"  selectedCol = "+selectedCol);
        
        
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
        System.out.println("mouse timefield:  selectedRow = " + selectedRow +"  selectedCol = "+selectedCol);
//        
//    
//        //  System.out.println("mouse timefield:" + m.getPoint());
//      //  System.out.println("mouse lectionfield:" + m.getPoint());
//
        Point p = m.getPoint();
        selectedCol = timeTable.columnAtPoint(p) - 1;
        selectedRow = timeTable.rowAtPoint(p);
//
        timeTable.repaint(timeTable.getCellRect(selectedRow, selectedCol, false));
//        if (selectedRow != tempRow) {
//            cleanDirtyColumn(selectedRow, selectedRow < tempRow);
//        }
//        if (selectedCol != tempCol) {
//            cleanDirtyRow(selectedCol, selectedCol < tempCol);
//        }
//        // Border unten, wird nicht erfasst durch mouseExited 
//        if (selectedRow < 0) {
//            selectedCol = -1;
//            selectedRow = -1;
//            for (int i = 0; i < columnCount; i++) {
//                timeTable.repaint(timeTable.getCellRect(rowCount - 1, i, false));
//            }
//        }
//        tempCol = selectedCol;
//        tempRow = selectedRow;
//
    }
//
//    public void cleanDirtyRow(int col, boolean moveLeft) {
//
//        if (moveLeft) {
//            timeTable.repaint(timeTable.getCellRect(selectedRow, col + 1, false));
//            timeTable.repaint(timeTable.getCellRect(selectedRow, col + 2, false));
//            timeTable.repaint(timeTable.getCellRect(selectedRow, col + 3, false));
//            timeTable.repaint(timeTable.getCellRect(selectedRow, col + 4, false));
//        } else {
//            timeTable.repaint(timeTable.getCellRect(selectedRow, col - 1, false));
//            timeTable.repaint(timeTable.getCellRect(selectedRow, col - 2, false));
//            timeTable.repaint(timeTable.getCellRect(selectedRow, col - 3, false));
//            timeTable.repaint(timeTable.getCellRect(selectedRow, col - 4, false));
//        }
//    }
//
//    public void cleanDirtyColumn(int row, boolean moveUp) {
//
//        if (moveUp) {
//            timeTable.repaint(timeTable.getCellRect(row + 1, selectedCol, false));
//            timeTable.repaint(timeTable.getCellRect(row + 2, selectedCol, false));
//            timeTable.repaint(timeTable.getCellRect(row + 3, selectedCol, false));
//            timeTable.repaint(timeTable.getCellRect(row + 4, selectedCol, false));
//            timeTable.repaint(timeTable.getCellRect(row + 5, selectedCol, false));
//            timeTable.repaint(timeTable.getCellRect(row + 6, selectedCol, false));
//            timeTable.repaint(timeTable.getCellRect(row + 7, selectedCol, false));
//            timeTable.repaint(timeTable.getCellRect(row + 8, selectedCol, false));
//        } else {
//            timeTable.repaint(timeTable.getCellRect(row - 1, selectedCol, false));
//            timeTable.repaint(timeTable.getCellRect(row - 2, selectedCol, false));
//            timeTable.repaint(timeTable.getCellRect(row - 3, selectedCol, false));
//            timeTable.repaint(timeTable.getCellRect(row - 4, selectedCol, false));
//            timeTable.repaint(timeTable.getCellRect(row - 5, selectedCol, false));
//            timeTable.repaint(timeTable.getCellRect(row - 6, selectedCol, false));
//            timeTable.repaint(timeTable.getCellRect(row - 7, selectedCol, false));
//            timeTable.repaint(timeTable.getCellRect(row - 8, selectedCol, false));
//        }
//    }
//
//    // damit MouseOver wieder aus TimeTable findet
//    @Override
//    public void mouseExited(MouseEvent m) {
//
//        // Border rechts
//        if (selectedCol > columnCount - 1) {
//            selectedCol = -1;
//            selectedRow = -1;
//            for (int i = 0; i < rowCount; i++) {
//                timeTable.repaint(timeTable.getCellRect(i, columnCount - 2, false));
//            }
//            // Border links und oben
//        } else {
//            selectedCol = -1;
//            selectedRow = -1;
//            timeTable.repaint();
//        }
//    }
}
