/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleUI;

import core.ProfileTypes;
import static java.awt.Color.*;
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
import userUtilsUI.ScheduleZoom;
import static utils.Colors.*;
import static studentListData.StudentListData.NULL_VALUE;
import static scheduleData.ScheduleFieldConstants.*;

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
    protected float size1, size2;

    public LectionField(TimeTable timeTable, ScheduleData scheduleData) {
        this.timeTable = timeTable;
        this.scheduleData = scheduleData;
        size1 = ScheduleZoom.DEFAULT_SIZE_1;
        size2 = ScheduleZoom.DEFAULT_SIZE_2;
        setFont(this.getFont().deriveFont(Font.PLAIN, size1));
        initScheduleDimension();
        resetLectionColumn();
        setHorizontalAlignment(SwingConstants.LEADING);
        setOpaque(true);
    }

    public final void resetLectionColumn() {
        movedRow = 0;
        movedCol = 0;
        tempRow = NULL_VALUE;
        tempCol = 0;
        lectionLenght = 0;
        lectionEnd = 0;
        lectionDiff = 0;
    }

    public final void initScheduleDimension() {
        rowCount = scheduleData.getRowCount();
        columnCount = scheduleData.getColumnCount();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        ScheduleFieldData fieldData = (ScheduleFieldData) value;
        ScheduleFieldData fieldDataAtMovedCoordinates = scheduleData.getValueAt(movedRow, movedCol);
        setBackground(BACKGROUND_COLOR);
        setText("");
        // Allocated Mode
        if (fieldData.isLectionAllocated()) {
            if (fieldData.getAllocatedTimeMark() == UNVALID) {
                setBackground(UNVALID_COLOR);
            } else {
                setBackground(fieldData.getLectionProfileType() == ProfileTypes.GROUP ? GROUP_LECTION_GREEN : DARK_GREEN);
            }
            if (fieldData.isLectionGapFiller()) {
                setBackground(LIGHT_GREEN);
            }
            setForeground(fieldData.getAllocatedTimeMark() == FAVORITE ? FAVORITE_COLOR : BLACK);
            setBorder(BorderFactory.createMatteBorder(0, 1, 0, 2, WHITE));
            setFont(this.getFont().deriveFont(Font.BOLD, size1));
            if (fieldData.isHead()) {
                setForeground(GRAY);
                setFont(this.getFont().deriveFont(Font.BOLD, size1));
                setText(" " + fieldData.getFieldTime().toString());
            } else if (fieldData.getNameMark() == FIRST_NAME) {
                setText(" " + fieldData.getProfile().getFirstName());
            } else if (fieldData.getNameMark() == NAME) {
                setText(" " + fieldData.getProfile().getName());
            } else if (fieldData.getNameMark() == THIRD_NAME) {
                setText(" " + fieldData.getProfile().getThirdName());
            }
            if (fieldData.getLectionPanelAreaMark() == LAST_ROW) {
                setBorder(BorderFactory.createMatteBorder(0, 1, 1, 2, WHITE));
            }
        } // Move Mode
        else if (fieldDataAtMovedCoordinates.isMoveEnabled() && !fieldDataAtMovedCoordinates.isLectionAllocated() && movedRow >= 0) { // ausserhalb JTable movedRow = -1
            if (col == movedCol && row >= movedRow && row < lectionEnd) {
                setBackground(fieldDataAtMovedCoordinates.isValidTime() ? LIGHT_GREEN : UNVALID_COLOR);
                setForeground(WHITE);
                setBorder(BorderFactory.createMatteBorder(0, 1, 0, 2, WHITE));
                setFont(this.getFont().deriveFont(Font.BOLD, size1));
                if (row == movedRow) {
                    setForeground(GRAY);
                    setFont(this.getFont().deriveFont(Font.BOLD, size1));
                    setText(" " + fieldData.getFieldTime().toString());
                } else if (row == movedRow + 1) {
                    setText(" " + fieldData.getProfile().getFirstName());
                } else if (row == movedRow + 2) {
                    setText(" " + fieldData.getProfile().getName());
                } else if (row == movedRow + 3) {
                    setText(" " + fieldData.getProfile().getThirdName());
                }
                if (row == lectionEnd - 1) {
                    setBorder(BorderFactory.createMatteBorder(0, 1, 1, 2, WHITE));
                }
            }
            // Übertrag auf 2. LectionColumn (Head bleibt in 1. Column stehen)
            if (lectionDiff > 0) {
                if (col == movedCol + 2 && row >= 0 && row < lectionDiff) {
                    setBackground(fieldDataAtMovedCoordinates.isValidTime() ? LIGHT_GREEN : UNVALID_COLOR);
                    setForeground(WHITE);
                    setBorder(BorderFactory.createMatteBorder(0, 1, 0, 2, WHITE));
                    setFont(this.getFont().deriveFont(Font.BOLD, size1));
                    if (lectionDiff == lectionLenght - 3) { // noch 3 Fields in 1. Column
                        if (row == 0) {
                            setText(" " + fieldData.getProfile().getThirdName());
                        }
                    } else if (lectionDiff == lectionLenght - 2) { // noch 2 Fields in 1. Column
                        if (row == 0) {
                            setText(" " + fieldData.getProfile().getName());
                        }
                        if (row == 1) {
                            setText(" " + fieldData.getProfile().getThirdName());
                        }
                    } else if (lectionDiff == lectionLenght - 1) { // nur noch Head in 1. Column
                        if (row == 0) {
                            setText(" " + fieldData.getProfile().getFirstName());
                        }
                        if (row == 1) {
                            setText(" " + fieldData.getProfile().getName());
                        }
                        if (row == 2) {
                            setText(" " + fieldData.getProfile().getThirdName());
                        }
                    }
                    if (row == lectionDiff - 1) {
                        setBorder(BorderFactory.createMatteBorder(0, 1, 1, 2, WHITE));
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
                StudentFieldData studentFieldData = studentList.getStudentFieldDataAtView(selectedRow, selectedCol);
                if (studentFieldData.isFieldSelected()) { // StudentDay selektiert 
                    resetLectionColumn();
                    lectionLenght = studentFieldData.getProfile().getLectionLengthInFields();
                } else if (studentListData.isStudentListReleased()) { // Student-Selection rückgängig gemacht, aber noch in SelectionState
                    resetLectionColumn();
                }
            }
        }
        if (m.getSource() instanceof TimeTable) {
            selectedRow = timeTable.rowAtPoint(p);
            selectedCol = timeTable.columnAtPoint(p);
            if (selectedRow >= 0) {
                ScheduleFieldData scheduleFieldData = scheduleData.getValueAt(selectedRow, selectedCol);
               
                if (selectedCol % 2 == 1 && scheduleFieldData.isMoveEnabled()) {
                    lectionLenght = scheduleFieldData.getProfile().getLectionLengthInFields();
                    lectionEnd = selectedRow + lectionLenght;
                    lectionDiff = lectionEnd - rowCount;
                }
                 if (scheduleFieldData.getLectionPanelAreaMark() == NAME_ROW) {
                    movedRow = movedRow - 1;
                    lectionEnd = lectionEnd-1;
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

    public void setFontSize1(float size1) {
        this.size1 = size1;
    }

    public void setFontSize2(float size2) {
        this.size2 = size2;
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
