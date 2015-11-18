/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlist;

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
import scheduleData.ScheduleData;
import studentListData.StudentListData;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class StudentField extends JLabel implements MouseInputListener, TableCellRenderer {

    private StudentList studentList;
    //   StudentListData studentListData;
    private ScheduleData scheduleData;
    private int selectedRow, selectedCol;
    private Boolean rowSelected;
    private int columnCount, tempRow;

    public StudentField(StudentList studentList, ScheduleData scheduleData) {

        this.studentList = studentList;
        this.scheduleData = scheduleData;
        StudentListData studentListData = (StudentListData) studentList.getModel();
        columnCount = studentListData.getColumnCount();
        rowSelected = false;
        resetStudentRows();

        setHorizontalAlignment(SwingConstants.LEADING);
        setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
        setOpaque(true);
    }

    private void resetStudentRows() {
        selectedCol = -1;
        selectedRow = -1;
        tempRow = -1;
    }

    public boolean isRowSelected() {
        return rowSelected;
    }

    public void setRowSelected(Boolean studentSelected) {
        this.rowSelected = studentSelected;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        String msg = (String) value;
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
        setForeground(Color.BLACK);
        setText(msg);

        // Grundfarben
        if (col == 0) {
            setBackground(Colors.NAME_FIELD);

        } else {
            setBackground(Colors.STUDENT_FIELD_BLUE);
        }
        // Mouseover
        if (row == selectedRow) {
            if (col == 0) {  // Name-Column
                setBackground(Colors.NAME_FIELD_SELECTED);
                setFont(this.getFont().deriveFont(Font.BOLD, 10));
                setForeground(Color.WHITE);
            } else {  // StudentDay-Columns
                setBackground(Colors.LIGHT_GREEN);
            }
        } else {
            if (col == 0) {
                setBackground(Colors.NAME_FIELD); // ToDo:konditionalop.
            } else {
                setBackground(Colors.STUDENT_FIELD_BLUE);
            }
        }
        // MousePressed (Rows sperren/entsperren)
        if (col > 0 && row == selectedRow && col == selectedCol) {
            if (rowSelected) {   // ToDo:konditionalop.
                setBackground(Colors.DARK_GREEN);
            } else {
                setBackground(Colors.LIGHT_GREEN);
            }
        }
//        if (lectionAllocated && row == allocatedRow) {  // nicht lösbar so
//            setBackground(Colors.LIGHT_GRAY);
//            setForeground(Color.GRAY);
//        }
        return this;
    }

    /*  MouseMotionListener Implementation */
    @Override
    public void mouseMoved(MouseEvent m) {

        Point point = m.getPoint();
        selectedRow = studentList.rowAtPoint(point);
        if (!rowSelected && selectedRow != tempRow) {
            paintRow(selectedRow < tempRow);
            tempRow = selectedRow;
        }
    }

    private void paintRow(boolean moveUp) {

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

    /* StudentDay-Selection-Handling */
    @Override
    public void mousePressed(MouseEvent m
    ) {
        // StudentList 
        if (m.getSource() instanceof StudentList) {
            Point p = m.getPoint();
            // Zellen selektieren 
            if (studentList.columnAtPoint(p) > 0) {   // NameField nicht ansprechbar
                if (!rowSelected) {  // Falls noch keine Selektion gemacht
                    selectedRow = studentList.rowAtPoint(p);  //  = StudentID
                    selectedCol = studentList.columnAtPoint(p);
                    rowSelected = true;
                    studentList.repaint(studentList.getCellRect(selectedRow, selectedCol, false));
                    // 1. Selektion bzw. mehrere in gleicher Zelle
                } else if (selectedRow == studentList.rowAtPoint(p) && selectedCol == studentList.columnAtPoint(p)) {
                    rowSelected = !rowSelected;
                    studentList.repaint(studentList.getCellRect(selectedRow, selectedCol, false));
                    // nachfolgende Selektionen
                } else if (selectedRow == studentList.rowAtPoint(p) && selectedCol != studentList.columnAtPoint(p)) {
                    studentList.repaint(studentList.getCellRect(selectedRow, selectedCol, false)); // alte Zelle löschen
                    selectedRow = studentList.rowAtPoint(p);  //  neue Koordinaten setzen
                    selectedCol = studentList.columnAtPoint(p);
                    rowSelected = true;
                    studentList.repaint(studentList.getCellRect(selectedRow, selectedCol, false)); // neue Zelle zeichnen
                }
            } // 1. Spalte: Doppelklick auf NameField 
            else if (studentList.columnAtPoint(p) == 0 && !rowSelected && m.getClickCount() == 2) {
                // ToDo.....
                // falsch, nicht new, sondern  Maske, die auf alte Daten zugreift, Student darf nicht in Entry erzeugt werden
                //    StudentDataEntry mask = new StudentDataEntry(studentListData, null);
                //   mask.setVisible(true);
            }
        } // Schedule
        else {
            rowSelected = !scheduleData.isLectionAllocated(); // Rows entsperren
            resetStudentRows();
            studentList.repaint(); // ToDo mit repaint(rectangle)

        }
    }

    // ----unbenutzt-------------
    @Override
    public void mouseClicked(MouseEvent m
    ) {
    }

    @Override
    public void mouseReleased(MouseEvent me
    ) {
    }

    @Override
    public void mouseEntered(MouseEvent me
    ) {
    }

    @Override
    public void mouseExited(MouseEvent me
    ) {
    }

    @Override
    public void mouseDragged(MouseEvent me
    ) {
    }
}
