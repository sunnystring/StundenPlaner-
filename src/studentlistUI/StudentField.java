/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlistUI;

import java.awt.Color;
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
import utils.Colors;

/**
 *
 * Renderer-Component für {@link StudentList}
 */
public class StudentField extends JLabel implements MouseMotionListener, TableCellRenderer {

    private StudentList studentList;
    private int selectedRow, tempRow;
    private int columnCount;
    public static final int NULL_ROW = -1;
    private static final int PAINTED_ROWS = 10;

    public StudentField(StudentList studentList, StudentListData studentListData) {
        this.studentList = studentList;
        setColumnCount(studentListData.getColumnCount());
        resetStudentRows();
        setHorizontalAlignment(SwingConstants.LEADING);
        setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        StudentFieldData studentFieldData = (StudentFieldData) value;
        setText(col == 0 ? studentFieldData.getNameString() : studentFieldData.getValidTimeString());
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
        setForeground(Color.BLACK);
        setBackground(col == 0 ? Colors.NAME_FIELD : studentFieldData.getFieldColor());
        // Mouseover
        if (studentFieldData.isStudentListReleased() && row == selectedRow) {
            if (col == 0) {
                setBackground(Colors.NAME_FIELD_SELECTED);
                setFont(this.getFont().deriveFont(Font.BOLD, 10));
                setForeground(Color.WHITE);
            } else {
                setBackground(Colors.LIGHT_GREEN);
            }
        } else {
            setBackground(col == 0 ? Colors.NAME_FIELD : studentFieldData.getFieldColor());
        }
        // Row selected
        if (row == studentFieldData.getSelectedRowIndex()) {
            if (col == 0) {
                if (col == 0) {
                    setBackground(Colors.NAME_FIELD_SELECTED);
                    setFont(this.getFont().deriveFont(Font.BOLD, 10));
                    setForeground(Color.WHITE);
                }
            } else {
                setBackground(studentFieldData.isFieldSelected() ? Colors.DARK_GREEN : Colors.LIGHT_GREEN);
            }
        }
        if (studentFieldData.isUnallocatable()) {
            setBackground(Colors.UNVALID);
        }
        if (studentFieldData.isStudentAllocated()) {
            setBackground(Colors.LIGHT_GRAY);
            setForeground(Color.GRAY);
        }
        return this;
    }

    @Override
    public void mouseMoved(MouseEvent m) {
        Point point = m.getPoint();
        selectedRow = studentList.rowAtPoint(point);
        if (selectedRow >= 0) {
            if (selectedRow != tempRow) {
                paintStudentRow(selectedRow < tempRow);
                tempRow = selectedRow;
            }
        } else { // ausserhalb Table (unten)
            studentList.repaint();
        }
    }

    private void paintStudentRow(boolean moveUp) {
        if (moveUp) {
            for (int i = 0; i < PAINTED_ROWS; i++) {
                for (int j = 0; j < columnCount; j++) {
                    studentList.repaint(studentList.getCellRect(selectedRow + i, j, false));
                }
            }
        } else {
            for (int i = 0; i < PAINTED_ROWS; i++) {
                for (int j = 0; j < columnCount; j++) {
                    studentList.repaint(studentList.getCellRect(selectedRow - i, j, false));
                }
            }
        }
    }

    public final void resetStudentRows() {
        selectedRow = NULL_ROW;
        tempRow = NULL_ROW;
    }

    public final void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    @Override
    public void mouseDragged(MouseEvent me) {
    }
}
