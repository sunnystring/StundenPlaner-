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
import util.Colors;

/**
 *
 * Renderer-Component für {@link StudentList}
 */
public class StudentField extends JLabel implements MouseMotionListener, TableCellRenderer {

    private StudentList studentList;
    private int selectedRow, tempRow;
    private int columnCount;
    public static final int NULL_ROW = -1;

    public StudentField(StudentList studentList) {
        this.studentList = studentList;
        StudentListData studentListData = (StudentListData) studentList.getModel();
        columnCount = studentListData.getColumnCount();
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
        setBackground(col == 0 ? Colors.NAME_FIELD : Colors.STUDENT_FIELD_BLUE);
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
            setBackground(col == 0 ? Colors.NAME_FIELD : Colors.STUDENT_FIELD_BLUE);
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
        // Row allocated
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
        if (selectedRow != tempRow) {
            paintStudentRow(selectedRow < tempRow);
            tempRow = selectedRow;
        }
    }

    private void paintStudentRow(boolean moveUp) {
        if (moveUp) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < columnCount; j++) {
                    studentList.repaint(studentList.getCellRect(selectedRow + i, j, false));
                }
            }
        } else {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < columnCount; j++) {
                    studentList.repaint(studentList.getCellRect(selectedRow - i, j, false));
                }
            }
        }
    }

    private void resetStudentRows() {
        selectedRow = NULL_ROW;
        tempRow = NULL_ROW;
    }

    @Override
    public void mouseDragged(MouseEvent me) {
    }
}
