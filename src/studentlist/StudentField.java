/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlist;

import studentListData.StudentListData;
import dialogs.StudentDataEntry;
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
import javax.swing.table.TableCellRenderer;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class StudentField extends JLabel implements MouseMotionListener, MouseListener, TableCellRenderer {

    private StudentListData studentData;
    private int selectedRow, selectedCol;
    private Boolean rowSelected;

    public StudentField(StudentListData studentData) {

        this.studentData = studentData;
        rowSelected = false;
        setHorizontalAlignment(SwingConstants.LEADING);
        setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
        setOpaque(true);
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
            } else {  // StudentDay-Column
                setBackground(Colors.LIGHT_GREEN);
            }
        } else {
            if (col == 0) {
                setBackground(Colors.NAME_FIELD);
            } else {
                setBackground(Colors.STUDENT_FIELD_BLUE);
            }
        }
        // MousePressed
        if (col > 0 && row == selectedRow && col == selectedCol) {
            if (rowSelected) {
                setBackground(Colors.DARK_GREEN);
            } else {
                setBackground(Colors.LIGHT_GREEN);
            }
        }
        return this;
    }

    /*  MouseMotionListener Implementation */
    @Override
    public void mouseMoved(MouseEvent m) {

        if (m.getSource() instanceof StudentList) {

            StudentList studentList = (StudentList) m.getSource();
            Point point = m.getPoint();

            if (!rowSelected) {
                selectedRow = studentList.rowAtPoint(point);
                studentList.repaint();
            }
        }
    }

    /*  MouseListener Implementation */
    @Override
    public void mousePressed(MouseEvent m) {

        if (m.getSource() instanceof StudentList) {

            StudentList studentList = (StudentList) m.getSource();
            Point p = m.getPoint();
            // Zellen selektieren 
            if (studentList.columnAtPoint(p) > 0) {   // NameField nicht ansprechbar
                if (!rowSelected) {  // Falls noch keine Selektion gemacht
                    selectedRow = studentList.rowAtPoint(p);  //  = StudentID
                    selectedCol = studentList.columnAtPoint(p);
                    rowSelected = true;
                    studentList.repaint(studentList.getCellRect(selectedRow, selectedCol, false));
                    // 1. Selektion bzw. mehrere auf in gleicher Zelle
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
            } // StudentDataEntry aufrufen für Änderung Schülerdaten 
            else if (studentList.columnAtPoint(p) == 0 && !rowSelected && m.getClickCount() == 2) {

                // falsch, nicht new, sondern  Maske, die auf alte Daten zugreift, Student darf nicht in Entry erzeugt werden
                StudentDataEntry mask = new StudentDataEntry(studentData, null);
                mask.setVisible(true);
            }
        }
    }

    // ----unbenutzt-------------
    @Override
    public void mouseClicked(MouseEvent m) {
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

    @Override
    public void mouseDragged(MouseEvent me) {
    }
}
