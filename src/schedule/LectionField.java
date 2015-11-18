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
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputListener;
import javax.swing.table.TableCellRenderer;
import scheduleData.ScheduleFieldData;
import scheduleData.ScheduleData;
import studentlist.StudentList;
import util.Colors;

/**
 *
 * @author mathiaskielholz
 */
public class LectionField extends JLabel implements TableCellRenderer, MouseInputListener {

    protected JTable timeTable;
    protected ScheduleData scheduleData;
    protected boolean moveEnabled; // Einteilungsmodus (= moveEnabled) bzw. Lection gesetzt
    private int selectedRow, selectedCol, lectionEnd; // MouseEvent: Koordinaten TimeTable
    protected int rowCount, columnCount, lectionLenght = 8;
    private int tempRow, tempCol, lectionDiff;

    public LectionField(Schedule schedule) {

        timeTable = schedule.getTimeTable();
        scheduleData = (ScheduleData) timeTable.getModel();
        rowCount = scheduleData.getRowCount();
        columnCount = scheduleData.getColumnCount();
        moveEnabled = false;
        resetLectionColumn();

        setHorizontalAlignment(SwingConstants.LEADING);
        setOpaque(true);
    }

    private void resetLectionColumn() {

        selectedRow = -1;
        selectedCol = -1;
        tempRow = -1;
        tempCol = -1;
        lectionDiff = -1;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        ScheduleFieldData fieldData = (ScheduleFieldData) value;
        setBackground(Colors.BACKGROUND);
        setText("");
        // LectionPanel-Mouseover
        if (col == selectedCol && row >= selectedRow && row < lectionEnd) {
            setBackground(Colors.LIGHT_GREEN);
            setForeground(Color.WHITE);

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
        // Mouseover Übertrag auf 2. LectionColumn
        if (lectionDiff >= 0) {
            if (col == selectedCol + 2 && row >= 0 && row < lectionDiff) {
                setBackground(Colors.LIGHT_GREEN);
                setForeground(Color.WHITE);
                setFont(this.getFont().deriveFont(Font.BOLD, 10));
                if (lectionDiff == lectionLenght - 2) {
                    if (row == 0) {
                        setText("  Name");
                    }
                }
                if (lectionDiff == lectionLenght - 1) {
                    if (row == 0) {
                        setText("  Name");
                    }
                    if (row == 1) {
                        setText("  Vorname");
                    }
                }
            }
        }
        return this;
    }

    /* dirty region painten */
    protected void paintHorizontalPanel(boolean moveLeft) {

        if (moveLeft) {
            for (int i = 0; i < columnCount; i++) {
                timeTable.repaint(timeTable.getCellRect(selectedRow, selectedCol + i, false)); // alle Colums rechts von LectionPanel löschen
                for (int j = 0; j < lectionLenght; j++) {
                    timeTable.repaint(timeTable.getCellRect(selectedRow + j, selectedCol + i, false));
                }
            }
            if (selectedCol % 4 == 3) { // falls Übertrag, Panelfläche oben in 2. LectionColum löschen
                lectionDiff = -1;
                for (int i = 0; i < lectionLenght; i++) {
                    timeTable.repaint(timeTable.getCellRect(i, selectedCol + 4, false));  // falls Übertrag, oben löschen
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
            if (selectedCol % 4 == 1) {  // falls Übertrag in 1. LectionColumn, 2. LectionColumn oben löschen
                for (int i = 0; i < lectionLenght; i++) {
                    timeTable.repaint(timeTable.getCellRect(i, selectedCol + 2, false));
                }
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

    @Override
    public void mouseMoved(MouseEvent m) {

        if (moveEnabled) {
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
                    for (int i = 0; i < lectionDiff; i++) {
                        timeTable.repaint(timeTable.getCellRect(i, selectedCol + 2, false)); // Übertrag zeichnen 2. LectionColumn
                    }
                } else if (selectedCol % 4 == 3) { // 2. LectionColumn
                    selectedRow = rowCount - lectionLenght;  // LectionField freezen
                }
            }
            // Zwischenspeicher updaten
            tempCol = selectedCol;
            tempRow = selectedRow;
        }
    }

    @Override
    public void mousePressed(MouseEvent m) {
        // StudentList 
        if (m.getSource() instanceof StudentList) {
            StudentList studentList = (StudentList) m.getSource();
            moveEnabled = studentList.isStudentSelected(); // Selection-State StudentList
            resetLectionColumn();
        } // Schedule
        else {
            moveEnabled = !scheduleData.isLectionAllocated();
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
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

}
