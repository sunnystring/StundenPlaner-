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
import javax.swing.BorderFactory;
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
    private int selectedRow, selectedCol, lectionEnd; // MouseEvent: Koordinaten TimeTable
    protected int rowCount, columnCount, lectionLenght = 8;
    private int tempRow, tempCol, lectionDiff;

    public LectionField_new(JTable timeTable) {

        this.timeTable = timeTable;
        scheduleData = (ScheduleData_new) timeTable.getModel();
        selectedRow = -1;
        selectedCol = -1;
        tempRow = -1;
        tempCol = -1;
        lectionDiff = -1;
        rowCount = scheduleData.getRowCount();
        columnCount = scheduleData.getColumnCount();

        setHorizontalAlignment(SwingConstants.LEADING);
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        FieldData_new fieldData = (FieldData_new) value;
        setBackground(Colors.BACKGROUND);
        setText("");

        // LectionPanel-Mouseover
        if (col == selectedCol && row >= selectedRow && row < lectionEnd) {
            setBackground(Colors.LIGHT_GREEN);
            setForeground(Color.WHITE);
            System.out.println("lectionDiff = " + lectionDiff);

            if (selectedRow == rowCount) {
                //.......
            }
            if (row == selectedRow) {
                setForeground(Color.GRAY);
                setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.WHITE));
                setFont(this.getFont().deriveFont(Font.PLAIN, 8));
                setText("  " + fieldData.getTime().toString());

            } else if (row == selectedRow + 1) {
                setFont(this.getFont().deriveFont(Font.BOLD, 10));
                setText("  Vorname");
            } else if (row == selectedRow + 2) {
                setText("  Name");
            }
            if (row == lectionEnd - 1) {
                setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.WHITE));
            }
        }

        // ??? 
        if (lectionDiff >= 0) {
            if (col == selectedCol + 2 && row >= 0 && row < lectionDiff) {
                setBackground(Colors.LIGHT_GREEN);
                setForeground(Color.WHITE);
                setFont(this.getFont().deriveFont(Font.BOLD, 10));
            }
//            if (lectionDiff == lectionLenght - 2 && row == 0) {
//                setText("  Name");
//            }
//            if (lectionDiff == lectionLenght - 1 ) {
//                setText("  Vorname");
//            }
        }
        return this;
    }

    protected void paintHorizontalPanel(boolean moveLeft) {

        if (moveLeft) {
            for (int i = 0; i < columnCount; i++) {
                timeTable.repaint(timeTable.getCellRect(selectedRow, selectedCol + i, false)); // alle Colums rechts von LectionPanel löschen
                for (int j = 0; j < lectionLenght; j++) {
                    timeTable.repaint(timeTable.getCellRect(selectedRow + j, selectedCol + i, false));
                }
            }
        } else {
            for (int i = 0; i < columnCount; i++) {
                timeTable.repaint(timeTable.getCellRect(selectedRow, selectedCol - i, false)); // alle Colums links von LectionPanel löschen
                for (int j = 0; j < lectionLenght; j++) {
                    timeTable.repaint(timeTable.getCellRect(selectedRow + j, selectedCol + i, false));
                }
            }
        }
    }

    protected void paintVerticalPanel(boolean moveUp) {

        if (moveUp) {
            for (int i = 0; i < rowCount; i++) {
                timeTable.repaint(timeTable.getCellRect(selectedRow + i, selectedCol, false)); // LectionPanel, darunter löschen
            }
        } else {
            for (int i = 0; i < rowCount; i++) {
                if (i < lectionLenght) {
                    timeTable.repaint(timeTable.getCellRect(selectedRow + i, selectedCol, false)); // LectionPanel 
                }
                timeTable.repaint(timeTable.getCellRect(selectedRow - i, selectedCol, false)); // darüber löschen
            }
        }
    }

    /*  MouseMotionListener Implementation */
    @Override
    public void mouseMoved(MouseEvent m) {

        // MouseEvent liefert in Lection- und TimeField die gleichen Koordinaten
        Point p = m.getPoint();
        if (timeTable.rowAtPoint(p) == -1) {  // damit Panel stehen bleibt wenn unten nicht mehr weiter einteilbar
            return;
        }
        selectedRow = timeTable.rowAtPoint(p);
        lectionEnd = selectedRow + lectionLenght;
        lectionDiff = lectionEnd - rowCount;
        // Columns zuweisen
        if (timeTable.columnAtPoint(p) % 2 != 0) {
            selectedCol = timeTable.columnAtPoint(p); // falls LectionColumn, diese zeichnen
        } else {
            selectedCol = timeTable.columnAtPoint(p) + 1;  // falls TimeColumn, die zugehörige LectionColumn rechts zeichnen
        }
        // LectionPanel zeichnen
        if (selectedRow != tempRow) {
            paintVerticalPanel(selectedRow < tempRow);
        }
        if (selectedCol != tempCol) {
            paintHorizontalPanel(selectedCol < tempCol);
        }
        // Spaltenende 
        if (lectionDiff >= 0) {
            if (selectedCol % 4 == 1) {  // 1. LectionColumn
                //  selectedCol = selectedCol + 2;  // Übertrag auf 2. LectionColumn
                lectionDiff = lectionEnd - rowCount;
                for (int i = 0; i < lectionDiff; i++) {
                    timeTable.repaint(timeTable.getCellRect(i, selectedCol + 2, false)); // Übertrag zeichnen in 2. LectionColumn, bei row = 0 beginnen

                    //   System.out.println("selectedRow=" + i);
                }
//                for (int i = 0; i < rowCount; i++) {
//                timeTable.repaint(timeTable.getCellRect(selectedRow + i, selectedCol, false)); // LectionPanel, darunter löschen
//            }

                // paintVerticalPanel(false);
            } else if (selectedCol % 4 == 3) { // 2. LectionColumn
                selectedRow = rowCount - lectionLenght;  // LectionField freezen
                // paintVerticalPanel(false);
            }
        }
        // Zwischenspeicher updaten
        tempCol = selectedCol;
        tempRow = selectedRow;
    }

    /*  MouseListener Implementation */
    @Override
    public void mouseClicked(MouseEvent me) {
    }

    // unbenutzt
    @Override
    public void mouseDragged(MouseEvent me) {
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

    @Override
    public void mouseExited(MouseEvent m) {

    }
}
