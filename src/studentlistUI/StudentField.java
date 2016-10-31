/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlistUI;

import static core.ProfileTypes.*;
import static java.awt.Color.*;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import studentListData.StudentFieldData;
import studentListData.StudentListData;
import static utils.Colors.*;
import static studentListData.StudentListData.NULL_VALUE;

/**
 *
 * Renderer-Component für {@link StudentList}
 */
public class StudentField extends JLabel implements MouseMotionListener, TableCellRenderer {

    private final StudentList studentList;
    private final StudentListData studentListData;
    private int movedRow, tempRow;
    private int columnCount;
    private static final int PAINTED_ROWS = 10;

    public StudentField(StudentList studentList, StudentListData studentListData) {
        this.studentList = studentList;
        this.studentListData = studentListData;
        setColumnCount(studentListData.getColumnCount());
        resetRowIndices();
        setHorizontalAlignment(SwingConstants.LEADING);
        setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        StudentFieldData studentFieldData = (StudentFieldData) value;
        setText(col == 0 ? studentFieldData.getNameString() : studentFieldData.getValidTimeString());
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
        setForeground(BLACK);
        // Namensfeld
        if (col == 0) {
            setNameFieldBackground(studentFieldData.getLectionProfileType());
        } else {
            setBackground(studentFieldData.getLectionProfileType() == KGU_MEMBER ? NAMEFIELD_KGU_MEMBER_COLOR : studentFieldData.getFieldColor());
        }
        // nicht einteilbar
        if (studentFieldData.isUnallocatable()) {
            setBackground(RED_DEFAULT);
        }
        // Mouseover
        if (studentListData.isStudentListReleased() && row == movedRow) {
            if (col == 0) {
                setBackground(NAMEFIELD_SELECTED_COLOR);
                setFont(this.getFont().deriveFont(Font.BOLD, 10));
                setForeground(WHITE);
            } else {
                setMovedBackground(studentFieldData.getLectionProfileType());
            }
        } else if (col == 0) {
            setNameFieldBackground(studentFieldData.getLectionProfileType());
        }
        // Row selected
        if (row == studentFieldData.selectedRowIndex()) {
            if (col == 0) {
                setBackground(NAMEFIELD_SELECTED_COLOR);
                setFont(this.getFont().deriveFont(Font.BOLD, 10));
                setForeground(WHITE);
            } else {
                setBackground(studentFieldData.isFieldSelected() ? DARK_GREEN : LIGHT_GREEN);
            }
        }
        // LectionGapFiller aktiviert
        if (studentFieldData.isLectionGapFiller() && studentFieldData.getLectionProfileType() != KGU_MEMBER) {
            setBackground(LIGHT_GREEN);
        }
        // eingeteilt
        if (studentFieldData.isProfileAllocated()) {
            setFont(this.getFont().deriveFont(Font.PLAIN, 10));
            setBackground(LIGHT_GRAY_COLOR);
            setForeground(GRAY);
        }
        return this;
    }

    private void setMovedBackground(int profileType) {
        setBackground(profileType == KGU_MEMBER ? NAMEFIELD_KGU_MEMBER_COLOR : LIGHT_GREEN);
    }

    private void setNameFieldBackground(int profileType) {
        switch (profileType) {
            case GROUP:
                setBackground(NAMEFIELD_GROUP_COLOR);
                break;
            case SINGLE_LECTION:
                setBackground(NAMEFIELD_SINGLE_COLOR);
                break;
            case KGU_MEMBER:
                setBackground(NAMEFIELD_KGU_MEMBER_COLOR);
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseMoved(MouseEvent m) {
        Point point = m.getPoint();
        movedRow = studentList.rowAtPoint(point);
        if (movedRow >= 0) {
            if (movedRow != tempRow) {
                paintStudentRow(movedRow < tempRow);
                tempRow = movedRow;
            }
        } else { // ausserhalb Table (unten)
            studentList.repaint();
        }
    }

    private void paintStudentRow(boolean moveUp) {
        if (moveUp) {
            for (int i = 0; i < PAINTED_ROWS; i++) {
                for (int j = 0; j < columnCount; j++) {
                    studentList.repaint(studentList.getCellRect(movedRow + i, j, false));
                }
            }
        } else {
            for (int i = 0; i < PAINTED_ROWS; i++) {
                for (int j = 0; j < columnCount; j++) {
                    studentList.repaint(studentList.getCellRect(movedRow - i, j, false));
                }
            }
        }
    }

    public final void resetRowIndices() {
        movedRow = NULL_VALUE;
        tempRow = NULL_VALUE;
    }

    public final void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }
}
