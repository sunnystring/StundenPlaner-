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
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputListener;
import javax.swing.table.TableCellRenderer;
import scheduleData.ScheduleFieldData;
import scheduleData.ScheduleData;
import studentListData.StudentFieldData;
import studentListData.StudentListData;
import studentlist.StudentList;
import util.Colors;

/**
 *
 * @author mathiaskielholz
 */
public class LectionField extends JLabel implements TableCellRenderer, MouseInputListener {

    protected JTable timeTable;
    protected ScheduleData scheduleData;
    private int movedRow, movedCol, lectionEnd; // MouseEvent: Koordinaten TimeTable
    protected int rowCount, columnCount;
    protected int lectionLenght;
    private int tempRow, tempCol, lectionDiff;  // Hilfsgrössen für Panel-Move

    public LectionField(TimeTable timeTable) {

        this.timeTable = timeTable;
        scheduleData = (ScheduleData) timeTable.getModel();
        rowCount = scheduleData.getRowCount();
        columnCount = scheduleData.getColumnCount();
        resetLectionColumn();

        setHorizontalAlignment(SwingConstants.LEADING);
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        ScheduleFieldData fieldData = (ScheduleFieldData) value;
        setBackground(Colors.BACKGROUND);
        setText("");

        // Mouseover
        if (fieldData.isScheduleEnabled() && !fieldData.isLectionAllocated()) {
            if (col == movedCol && row >= movedRow && row < lectionEnd) {
                setBackground(Colors.LIGHT_GREEN);
                setForeground(Color.WHITE);
                if (row == movedRow) {
                    setForeground(Color.GRAY);
                    setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.WHITE));
                    setFont(this.getFont().deriveFont(Font.PLAIN, 8));
                    setText("  " + fieldData.getTime().toString());

                } else if (row == movedRow + 1) {
                    setFont(this.getFont().deriveFont(Font.BOLD, 10));
                    setText(" " + fieldData.getStudent().getFirstName());
                } else if (row == movedRow + 2) {
                    setText(" " + fieldData.getStudent().getName());
                }
                if (row == lectionEnd - 1) {
                    setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.WHITE));
                }
            }
            // Übertrag auf 2. LectionColumn
            if (lectionDiff >= 0) {
                if (col == movedCol + 2 && row >= 0 && row < lectionDiff) {
                    setBackground(Colors.LIGHT_GREEN);
                    setForeground(Color.WHITE);
                    setFont(this.getFont().deriveFont(Font.BOLD, 10));
                    if (lectionDiff == lectionLenght - 2) {
                        if (row == 0) {
                            setText(" " + fieldData.getStudent().getName());
                        }
                    }
                    if (lectionDiff == lectionLenght - 1) {
                        if (row == 0) {
                            setText(" " + fieldData.getStudent().getName());
                        }
                        if (row == 1) {
                            setText(" " + fieldData.getStudent().getFirstName());
                        }
                    }
                }
            }
        } // LectionPanel gesetzt
        else if (fieldData.isLectionAllocated()) {
            setBackground(Colors.DARK_GREEN);
            setForeground(Color.BLACK);
            setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.WHITE));
            setFont(this.getFont().deriveFont(Font.BOLD, 10));
            // HEAD = eingetielte Zeit
            if (fieldData.getLectionPanelArea() == ScheduleFieldData.HEAD) {
                setForeground(Color.GRAY);
                setFont(this.getFont().deriveFont(Font.PLAIN, 8));
                setText("  " + fieldData.getTime().toString());
            } // FIRSTNAME
            else if (fieldData.getLectionContent() == ScheduleFieldData.FIRST_NAME) {
                // setFont(this.getFont().deriveFont(Font.BOLD, 10));
                setText(" " + fieldData.getStudent().getFirstName());
            } // NAME
            else if (fieldData.getLectionContent() == ScheduleFieldData.NAME) {
                setText(" " + fieldData.getStudent().getName());
            }
            // LAST_ROW
            if (fieldData.isLastRow()) {
                setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.WHITE));
            }
        }

        return this;
    }

    @Override
    public void mouseMoved(MouseEvent m) {

        // MouseEvent liefert in Lection- und TimeField die gleichen Koordinaten
        Point p = m.getPoint();
        if (timeTable.rowAtPoint(p) == -1) {  // damit Panel stehen bleibt wenn unten nicht mehr weiter einteilbar
            return;
        }
        movedRow = timeTable.rowAtPoint(p);
        lectionEnd = movedRow + lectionLenght;
        lectionDiff = lectionEnd - rowCount;
        // Columns zuweisen
        if (timeTable.columnAtPoint(p) % 2 != 0) {
            movedCol = timeTable.columnAtPoint(p); // falls LectionColumn, diese zeichnen
        } else {
            movedCol = timeTable.columnAtPoint(p) + 1;  // falls TimeColumn, die zugehörige LectionColumn rechts zeichnen
        }
        // LectionPanel zeichnen
        if (movedRow != tempRow) {
            paintVerticalPanel(movedRow < tempRow);
        }
        if (movedCol != tempCol) {
            paintHorizontalPanel(movedCol < tempCol);
        }
        // Spaltenende 
        if (lectionDiff >= 0) {
            if (movedCol % 4 == 1) {  // 1. LectionColumn
                for (int i = 0; i < lectionDiff; i++) {
                    timeTable.repaint(timeTable.getCellRect(i, movedCol + 2, false)); // Übertrag zeichnen 2. LectionColumn
                }
            } else if (movedCol % 4 == 3) { // 2. LectionColumn
                movedRow = rowCount - lectionLenght;  // LectionField freezen
            }
        }
        // Zwischenspeicher updaten
        tempCol = movedCol;
        tempRow = movedRow;
    }

    /* dirty region painten */
    protected void paintHorizontalPanel(boolean moveLeft) {

        if (moveLeft) {
            for (int i = 0; i < columnCount; i++) {
                timeTable.repaint(timeTable.getCellRect(movedRow, movedCol + i, false)); // alle Colums rechts von LectionPanel löschen
                for (int j = 0; j < lectionLenght; j++) {
                    timeTable.repaint(timeTable.getCellRect(movedRow + j, movedCol + i, false));
                }
            }
            if (movedCol % 4 == 3) { // falls Übertrag, Panelfläche oben in 2. LectionColum löschen
                lectionDiff = -1;
                for (int i = 0; i < lectionLenght; i++) {
                    timeTable.repaint(timeTable.getCellRect(i, movedCol + 4, false));  // falls Übertrag, oben löschen
                }
            }
        } else {
            for (int i = 0; i < columnCount; i++) {
                timeTable.repaint(timeTable.getCellRect(movedRow, movedCol - i, false)); // alle Colums links von LectionPanel löschen
                for (int j = 0; j < lectionLenght; j++) {
                    timeTable.repaint(timeTable.getCellRect(movedRow + j, movedCol + i, false));
                }
            }
        }
    }

    protected void paintVerticalPanel(boolean moveUp) {

        if (moveUp) {
            for (int i = 0; i < rowCount; i++) {
                timeTable.repaint(timeTable.getCellRect(movedRow + i, movedCol, false)); // LectionPanel, darunter löschen
            }
            if (movedCol % 4 == 1) {  // falls Übertrag in 1. LectionColumn, 2. LectionColumn oben löschen
                for (int i = 0; i < lectionLenght; i++) {
                    timeTable.repaint(timeTable.getCellRect(i, movedCol + 2, false));
                }
            }
        } else {
            for (int i = 0; i < rowCount; i++) {
                if (i < lectionLenght) {
                    timeTable.repaint(timeTable.getCellRect(movedRow + i, movedCol, false)); // LectionPanel 
                }
                timeTable.repaint(timeTable.getCellRect(movedRow - i, movedCol, false)); // darüber löschen
            }
        }
    }

    public void resetLectionColumn() {
        movedRow = -1;
        movedCol = -1;
        tempRow = -1;
        tempCol = -1;
        lectionDiff = -1;
    }

    /* MouseEvents triggern Änderungen in der View des MoveMode, die nicht über das TableModel gemacht werden*/
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
                    resetLectionColumn();
                    lectionLenght = studentListData.getStudent(selectedRow).getLectionType();
                }// falls Student-Selection rückgängig gemacht
                else if (studentFieldData.isStudentListEnabled()) {
                    resetLectionColumn();
                }
            }
        }
        // Schedule (TimeTable)
        if (m.getSource() instanceof TimeTable) {
            TimeTable timeTable = (TimeTable) m.getSource();
            ScheduleData scheduleData = (ScheduleData) timeTable.getModel();
            int selectedRow = timeTable.rowAtPoint(p);
            int selectedCol = timeTable.columnAtPoint(p);
            ScheduleFieldData scheduleFieldData = (ScheduleFieldData) scheduleData.getValueAt(selectedRow, selectedCol);
            // Events nur von LectionColumn, Lection muss entsperrt sein
            if (selectedCol % 2 == 1 && scheduleFieldData.isScheduleEnabled()) {
                lectionLenght = scheduleFieldData.getStudent().getLectionType();
            }
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
