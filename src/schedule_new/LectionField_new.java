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
import scheduleData_new.FieldData_new;
import scheduleData_new.ScheduleData_new;
import util.Colors;

/**
 *
 * @author mathiaskielholz
 */
public class LectionField_new extends JLabel implements TableCellRenderer, MouseMotionListener, MouseListener {

    private JTable timeTable;
    private ScheduleData_new scheduleData;
    private int selectedRow, selectedCol, selectedRowEnd; // MouseEvent: Koordinaten TimeTable
    protected int rowCount, columnCount;
    private int tempRow, tempCol;
    protected static final int LECTION_ID = 8;

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
        FieldData_new fieldData = (FieldData_new) value;

        setBackground(Colors.BACKGROUND);
        // Mouseover Schedule
        if (col == selectedCol && row >= selectedRow && row < selectedRowEnd) {
            setBackground(Colors.LIGHT_GREEN);
            setForeground(Color.WHITE);
        }
        return this;
    }

    protected void paintHorizontalPanel(boolean moveLeft) {

        if (moveLeft) {
            for (int i = 0; i < columnCount; i++) {
                timeTable.repaint(timeTable.getCellRect(selectedRow, selectedCol + i, false)); // alle Colums rechts von selectedCol
                for (int j = 0; j < LECTION_ID; j++) {
                    timeTable.repaint(timeTable.getCellRect(selectedRow + j, selectedCol + i, false)); // darunter Rows (= Länge lectionType)
                }
            }
        } else {
            for (int i = 0; i < columnCount; i++) {
                timeTable.repaint(timeTable.getCellRect(selectedRow, selectedCol - i, false)); // alle Colums links von selectedCol
                for (int j = 0; j < LECTION_ID; j++) {
                    timeTable.repaint(timeTable.getCellRect(selectedRow + j, selectedCol + i, false)); // darunter Rows
                }
            }
        }
    }

    protected void paintVerticalPanel(boolean moveUp) {

        if (moveUp) {
            for (int i = 0; i < rowCount; i++) {
                timeTable.repaint(timeTable.getCellRect(selectedRow + i, selectedCol, false)); // alle Rows unter selectedCol
            }
        } else {
            for (int i = 0; i < rowCount; i++) {

                if (i < LECTION_ID) {
                    timeTable.repaint(timeTable.getCellRect(selectedRow + i, selectedCol, false)); // LectionPanel 
                }
                timeTable.repaint(timeTable.getCellRect(selectedRow - i, selectedCol, false)); // alle Rows über selectedCol, darunter das LectionPanel
            }
        }
    }

    /*  MouseMotionListener Implementation */
    @Override
    public void mouseMoved(MouseEvent m) {
        // MouseEvent liefert in Lection- und TimeField die gleichen Koordinaten
        Point p = m.getPoint();
        if (timeTable.columnAtPoint(p) % 2 != 0) {
            selectedCol = timeTable.columnAtPoint(p); // falls LectionColumn, diese zeichnen
        } else {
            selectedCol = timeTable.columnAtPoint(p) + 1;  // falls TimeColumn, die zugehörige LectionColumn rechts zeichnen
        }
        selectedRow = timeTable.rowAtPoint(p);
        selectedRowEnd = selectedRow + LECTION_ID;

        if (selectedRow != tempRow) {
            paintVerticalPanel(selectedRow < tempRow);
        }
        if (selectedCol != tempCol) {
            paintHorizontalPanel(selectedCol < tempCol);
        }
        tempCol = selectedCol;
        tempRow = selectedRow;
    }

    /*  MouseListener Implementation */
    @Override
    public void mouseExited(MouseEvent m) {

        if(selectedCol == columnCount-1 && selectedRow != 0){
        selectedRow = -1;
        selectedCol = -1;
        timeTable.repaint();}
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
    public void mouseEntered(MouseEvent m) {
    }
}
