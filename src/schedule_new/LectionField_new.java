/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule_new;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import scheduleData_new.ScheduleData_new;
import util.Colors;

/**
 *
 * @author mathiaskielholz
 */
public class LectionField_new extends JLabel implements TableCellRenderer, MouseMotionListener, MouseListener {

    private JTable timeTable;
    private ScheduleData_new scheduleData;
    private int selectedRow, selectedCol; // MouseEvent: Koordinaten TimeTable
    private int rowCount, columnCount;
    private int tempRow, tempCol;
//

    public LectionField_new(JTable timeTable) {

        this.timeTable = timeTable;
        scheduleData = (ScheduleData_new) timeTable.getModel();
        selectedRow = -1;
        selectedCol = -1;
        tempRow = -1;
        tempCol = -1;
        rowCount = scheduleData.getRowCount();
        columnCount = scheduleData.getColumnCount();

        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        setBackground(Colors.BACKGROUND);
          System.out.println("lectionfield renderer:  selectedRow = " + selectedRow + "  selectedCol = " + selectedCol);
        // Mouseover Schedule
        if (row == selectedRow && col == selectedCol) {
            setBackground(Colors.LIGHT_GREEN);
            setForeground(Color.WHITE);
        }
        return this;
    }
//
//   
//

    /*  MouseMotionListener Implementation */
    @Override
    public void mouseMoved(MouseEvent m) {

           System.out.println("mouse lectionfield:  selectedRow = " + selectedRow + "  selectedCol = " + selectedCol);
        Point p = m.getPoint();
        selectedCol = timeTable.columnAtPoint(p);
        selectedRow = timeTable.rowAtPoint(p);

        timeTable.repaint(timeTable.getCellRect(selectedRow, selectedCol, false));
  
        // timeTable.repaint(timeTable.getCellRect(selectedRow, selectedColTimeField, false));
        if (selectedRow != tempRow) {
            cleanDirtyColumn(selectedRow, selectedRow < tempRow);
        }
        if (selectedCol != tempCol) {
            cleanDirtyRow(selectedCol, selectedCol < tempCol);
        }
        // Border unten, wird nicht erfasst durch mouseExited 
        if (selectedRow < 0) {
            selectedCol = -1;
            selectedRow = -1;
            for (int i = 0; i < columnCount; i++) {
                timeTable.repaint(timeTable.getCellRect(rowCount - 1, i, false));
            }
        }
        tempCol = selectedCol;
        tempRow = selectedRow;
    }

    public void cleanDirtyRow(int col, boolean moveLeft) {

        if (moveLeft) {
            timeTable.repaint(timeTable.getCellRect(selectedRow, col + 1, false));
            timeTable.repaint(timeTable.getCellRect(selectedRow, col + 2, false));
            timeTable.repaint(timeTable.getCellRect(selectedRow, col + 3, false));
            timeTable.repaint(timeTable.getCellRect(selectedRow, col + 4, false));
        } else {
            timeTable.repaint(timeTable.getCellRect(selectedRow, col - 1, false));
            timeTable.repaint(timeTable.getCellRect(selectedRow, col - 2, false));
            timeTable.repaint(timeTable.getCellRect(selectedRow, col - 3, false));
            timeTable.repaint(timeTable.getCellRect(selectedRow, col - 4, false));
        }
    }

    public void cleanDirtyColumn(int row, boolean moveUp) {

        if (moveUp) {
            timeTable.repaint(timeTable.getCellRect(row + 1, selectedCol, false));
            timeTable.repaint(timeTable.getCellRect(row + 2, selectedCol, false));
            timeTable.repaint(timeTable.getCellRect(row + 3, selectedCol, false));
            timeTable.repaint(timeTable.getCellRect(row + 4, selectedCol, false));
            timeTable.repaint(timeTable.getCellRect(row + 5, selectedCol, false));
            timeTable.repaint(timeTable.getCellRect(row + 6, selectedCol, false));
            timeTable.repaint(timeTable.getCellRect(row + 7, selectedCol, false));
            timeTable.repaint(timeTable.getCellRect(row + 8, selectedCol, false));
        } else {
            timeTable.repaint(timeTable.getCellRect(row - 1, selectedCol, false));
            timeTable.repaint(timeTable.getCellRect(row - 2, selectedCol, false));
            timeTable.repaint(timeTable.getCellRect(row - 3, selectedCol, false));
            timeTable.repaint(timeTable.getCellRect(row - 4, selectedCol, false));
            timeTable.repaint(timeTable.getCellRect(row - 5, selectedCol, false));
            timeTable.repaint(timeTable.getCellRect(row - 6, selectedCol, false));
            timeTable.repaint(timeTable.getCellRect(row - 7, selectedCol, false));
            timeTable.repaint(timeTable.getCellRect(row - 8, selectedCol, false));
        }
    }

    // damit MouseOver wieder aus TimeTable findet
    @Override
    public void mouseExited(MouseEvent m) {

        // Border rechts
        if (selectedCol > columnCount - 1) {
            selectedCol = -1;
            selectedRow = -1;
            for (int i = 0; i < rowCount; i++) {
                timeTable.repaint(timeTable.getCellRect(i, columnCount - 1, false));
            }
            // Border links und oben
        } else {
            selectedCol = -1;
            selectedRow = -1;
            timeTable.repaint();
        }
    }

    // unbenutzt
    @Override
    public void mouseDragged(MouseEvent me) {
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

}
