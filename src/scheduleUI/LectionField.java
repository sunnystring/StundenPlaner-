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
import studentlistUI.StudentList;
import util.Colors;

/**
 *
 * Renderer-Component für Lection-Spalten in {@link TimeTable}
 */
public class LectionField extends JLabel implements TableCellRenderer, MouseInputListener {

    protected JTable timeTable;
    protected ScheduleData scheduleData;
    protected int rowCount, columnCount;
    protected int lectionLenght;
    private int movedRow, movedCol;
    private int tempRow, tempCol;
    protected int lectionEnd, lectionDiff;
    protected static final int NULL_VALUE = -1;
    private static final float DEFAULT_SIZE_1 = (float) 10, DEFAULT_SIZE_2 = (float) 8;
    private static final float INC1 = (float) 0.3, INC2 = (float) 0.2;
    private static final float UPPER_SIZE_1 = DEFAULT_SIZE_1 + TimeTable.HEIGHT_DIFF * INC1;
    private static final float UPPER_SIZE_2 = DEFAULT_SIZE_2 + TimeTable.HEIGHT_DIFF * INC2;
    protected float size1, size2;

    public LectionField(TimeTable timeTable, ScheduleData scheduleData) {
        this.timeTable = timeTable;
        this.scheduleData = scheduleData;
        size1 = DEFAULT_SIZE_1;
        size2 = DEFAULT_SIZE_2;
        setFont(this.getFont().deriveFont(Font.PLAIN, size1));
        initScheduleDimension();
        resetLectionColumn();
        setHorizontalAlignment(SwingConstants.LEADING);
        setOpaque(true);
    }

    public void resetLectionColumn() {
        movedRow = 0;
        movedCol = 0;
        tempRow = NULL_VALUE;
        tempCol = 0;
        lectionLenght = 0;
        lectionEnd = 0;
        lectionDiff = 0;
    }

    public void initScheduleDimension() {
        rowCount = scheduleData.getRowCount();
        columnCount = scheduleData.getColumnCount();
    }

    public void decrementFontSizes(boolean stillDecrementing) {
        if (stillDecrementing) {
            size1 -= INC1;
            size2 -= INC2;
        } else {
            size1 = UPPER_SIZE_1;
            size2 = UPPER_SIZE_2;
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        ScheduleFieldData fieldData = (ScheduleFieldData) value;
        ScheduleFieldData fieldDataAtMovedCoordinates = (ScheduleFieldData) scheduleData.getValueAt(movedRow, movedCol);
        setBackground(Colors.BACKGROUND);
        setText("");
        // Lection setzen
        if (fieldData.isLectionAllocated()) {
            setBackground(fieldData.getAllocatedTimeMark() != ScheduleFieldData.NO_VALUE ? Colors.DARK_GREEN : Colors.LECTION_FIELD_OUT_OF_BOUNDS);
            setForeground(fieldData.getAllocatedTimeMark() == ScheduleFieldData.FAVORITE ? Colors.FAVORITE : Color.BLACK);
            setBorder(BorderFactory.createMatteBorder(0, 1, 0, 2, Color.WHITE));
            setFont(this.getFont().deriveFont(Font.BOLD, size1));
            if (fieldData.isHead()) {
                setForeground(Color.GRAY);
                setFont(this.getFont().deriveFont(Font.PLAIN, size2));
                setText(" " + fieldData.getFieldTime().toString());
            } else if (fieldData.getNameMark() == ScheduleFieldData.FIRST_NAME) {
                setText(" " + fieldData.getStudent().getFirstName());
            } else if (fieldData.getNameMark() == ScheduleFieldData.NAME) {
                setText(" " + fieldData.getStudent().getName());
            }
            if (fieldData.getLectionPanelAreaMark() == ScheduleFieldData.LAST_ROW) {
                setBorder(BorderFactory.createMatteBorder(0, 1, 1, 2, Color.WHITE));
            }
        } // Lection moven
        else if (fieldDataAtMovedCoordinates.isMoveEnabled() && !fieldDataAtMovedCoordinates.isLectionAllocated() && movedRow >= 0) { // ausserhalb JTable movedRow = -1
            if (col == movedCol && row >= movedRow && row < lectionEnd) {
                setBackground(fieldDataAtMovedCoordinates.isValidTime() ? Colors.LIGHT_GREEN : Colors.LECTION_FIELD_OUT_OF_BOUNDS);
                setForeground(Color.WHITE);
                setBorder(BorderFactory.createMatteBorder(0, 1, 0, 2, Color.WHITE));
                setFont(this.getFont().deriveFont(Font.BOLD, size1));
                if (row == movedRow) {
                    setForeground(Color.GRAY);
                    setFont(this.getFont().deriveFont(Font.PLAIN, size2));
                    setText(" " + fieldData.getFieldTime().toString());
                } else if (row == movedRow + 1) {
                    setText(" " + fieldData.getStudent().getFirstName());
                } else if (row == movedRow + 2) {
                    setText(" " + fieldData.getStudent().getName());
                }
                if (row == lectionEnd - 1) {
                    setBorder(BorderFactory.createMatteBorder(0, 1, 1, 2, Color.WHITE));
                }
            }
            // Übertrag auf 2. LectionColumn (Head bleibt in 1. Column stehen)
            if (lectionDiff > 0) {
                if (col == movedCol + 2 && row >= 0 && row < lectionDiff) {
                    setBackground(fieldDataAtMovedCoordinates.isValidTime() ? Colors.LIGHT_GREEN : Colors.LECTION_FIELD_OUT_OF_BOUNDS);
                    setForeground(Color.WHITE);
                    setBorder(BorderFactory.createMatteBorder(0, 1, 0, 2, Color.WHITE));
                    setFont(this.getFont().deriveFont(Font.BOLD, size1));
                    if (lectionDiff == lectionLenght - 2 && row == 0) { // noch 2 Fields in 1. Column
                        setText(" " + fieldData.getStudent().getName());
                    } else if (lectionDiff == lectionLenght - 1) { // nur noch Head in 1. Column
                        if (row == 0) {
                            setText(" " + fieldData.getStudent().getName());
                        }
                        if (row == 1) {
                            setText(" " + fieldData.getStudent().getFirstName());
                        }
                    }
                    if (row == lectionDiff - 1) {
                        setBorder(BorderFactory.createMatteBorder(0, 1, 1, 2, Color.WHITE));
                    }
                }
            }
        }
        return this;
    }

    @Override
    public void mouseMoved(MouseEvent m) {
        Point p = m.getPoint();
        if (timeTable.rowAtPoint(p) == NULL_VALUE) {
            return;
        }
        movedRow = timeTable.rowAtPoint(p);
        lectionEnd = movedRow + lectionLenght;
        lectionDiff = lectionEnd - rowCount;
        if (timeTable.columnAtPoint(p) % 2 != 0) {
            movedCol = timeTable.columnAtPoint(p);
        } else {
            movedCol = timeTable.columnAtPoint(p) + 1;  // falls TimeColumn, LectionColumn rechts davon zeichnen
        }
        if (movedRow != tempRow) {
            paintLectionVertically(movedRow < tempRow);
        }
        if (movedCol != tempCol) {
            paintLectionHorizontally(movedCol < tempCol);
        }
        if (lectionDiff >= 0) { // Übertrag 2. LectionColumn
            if (movedCol % 4 == 1) {
                for (int i = 0; i < lectionDiff; i++) {
                    timeTable.repaint(timeTable.getCellRect(i, movedCol + 2, false));
                }
            } // Stundenplan-Ende
            else if (movedCol % 4 == 3) {
                movedRow = rowCount - lectionLenght;
            }
        }
        tempCol = movedCol;
        tempRow = movedRow;
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
                StudentFieldData studentFieldData = studentList.getStudentFieldAt(selectedRow, selectedCol);
                if (studentFieldData.isFieldSelected()) { // StudentDay selektiert 
                    resetLectionColumn();
                    lectionLenght = studentListData.getStudent(selectedRow).getLectionLength();
                } else if (studentFieldData.isStudentListReleased()) { // Student-Selection rückgängig gemacht, aber noch in SelectionState
                    resetLectionColumn();
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
                    lectionEnd = selectedRow + lectionLenght;
                    lectionDiff = lectionEnd - rowCount;
                }
            }
        }
    }

    protected void paintLectionHorizontally(boolean moveLeft) {
        if (moveLeft) {
            for (int i = 0; i < columnCount; i++) {
                timeTable.repaint(timeTable.getCellRect(movedRow, movedCol + i, false)); // dirty region rechts von Lection 
                for (int j = 0; j < lectionLenght; j++) {
                    timeTable.repaint(timeTable.getCellRect(movedRow + j, movedCol + i, false));
                }
            }
            if (movedCol % 4 == 3) { // Übertrag: 2. LectionColum oben 
                lectionDiff = NULL_VALUE;
                for (int i = 0; i < lectionLenght; i++) {
                    timeTable.repaint(timeTable.getCellRect(i, movedCol + 4, false));
                }
            }
        } else {
            for (int i = 0; i < columnCount; i++) {
                timeTable.repaint(timeTable.getCellRect(movedRow, movedCol - i, false)); // dirty region links von Lection 
                for (int j = 0; j < lectionLenght; j++) {
                    timeTable.repaint(timeTable.getCellRect(movedRow + j, movedCol + i, false));
                }
            }
        }
    }

    protected void paintLectionVertically(boolean moveUp) {
        if (moveUp) {
            for (int i = 0; i < rowCount; i++) {
                timeTable.repaint(timeTable.getCellRect(movedRow + i, movedCol, false)); // dirty region unterhalb von Lection 
            }
            if (movedCol % 4 == 1) {  // Übertrag: 2. LectionColumn oben 
                for (int i = 0; i < lectionLenght; i++) {
                    timeTable.repaint(timeTable.getCellRect(i, movedCol + 2, false));
                }
            }
        } else {
            for (int i = 0; i < rowCount; i++) {
                if (i < lectionLenght) {
                    timeTable.repaint(timeTable.getCellRect(movedRow + i, movedCol, false));
                }
                timeTable.repaint(timeTable.getCellRect(movedRow - i, movedCol, false)); // dirty region oberhalb von Lection
            }
        }
    }

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
