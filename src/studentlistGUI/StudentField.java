/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlistGUI;

import studentData.StudentData;
import dialogs.StudentDataEntry;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
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
public class StudentField extends JLabel implements TableCellRenderer, MouseMotionListener, MouseListener {

    private StudentData studentData;
    private int selectedRow, selectedCol;
    private Boolean fieldSelected, rowSelected;

    public StudentField(StudentData studentData) {

        this.studentData = studentData;
        fieldSelected = false;
        rowSelected = false;
        setHorizontalAlignment(SwingConstants.LEADING);
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
        setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object o, boolean isSelected, boolean hasFocus, int row, int col) {

        
        setText(studentData.getValueAt(row, col).toString());
        // Grundfarben
        if (col == 0) {
            setBackground(Colors.NAME_FIELD);

        } else {
            setBackground(Colors.STUDENT_FIELD_BLUE);

        }

        // Mouseover
        if (row == selectedRow) {
            setBackground(Colors.LIGHT_GREEN);
        } else {
            if (col == 0) {
                setBackground(Colors.NAME_FIELD);
            } else {
                setBackground(Colors.STUDENT_FIELD_BLUE);
            }
        }

        // MousePressed
        if (col > 0 && row == selectedRow && col == selectedCol) {
            if (fieldSelected) {
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

        JTable table = (JTable) m.getSource();
        Point point = m.getPoint();

        if (!rowSelected) {
            selectedRow = table.rowAtPoint(point);
            table.repaint();
        }
    }

    /*  MouseListener Implementation */
    @Override
    public void mousePressed(MouseEvent m) {

        if (m.getSource() instanceof StudentList) {

            JTable table = (JTable) m.getSource();
            // Zellen selektieren 
            if (table.columnAtPoint(m.getPoint()) > 0) {   // NameField nicht ansprechbar
                if (!rowSelected) {  // Falls noch keine Selektion gemacht
                    selectedRow = table.rowAtPoint(m.getPoint());  //  = StudentID
                    selectedCol = table.columnAtPoint(m.getPoint());
                    fieldSelected = true;
                    rowSelected = true;
                    table.repaint(table.getCellRect(selectedRow, selectedCol, false));
                    // 1. Selektion bzw. mehrere auf in gleicher Zelle
                } else if (selectedRow == table.rowAtPoint(m.getPoint()) && selectedCol == table.columnAtPoint(m.getPoint())) {
                    fieldSelected = !fieldSelected;
                    rowSelected = !rowSelected;
                    table.repaint(table.getCellRect(selectedRow, selectedCol, false));
                    // nachfolgende Selektionen
                } else if (selectedRow == table.rowAtPoint(m.getPoint()) && selectedCol != table.columnAtPoint(m.getPoint())) {
                    fieldSelected = false;
                    table.repaint(table.getCellRect(selectedRow, selectedCol, false)); // alte Zelle löschen
                    selectedRow = table.rowAtPoint(m.getPoint());  //  neue Koordinaten setzen
                    selectedCol = table.columnAtPoint(m.getPoint());
                    fieldSelected = true;
                    rowSelected = true;
                    table.repaint(table.getCellRect(selectedRow, selectedCol, false)); // neue Zelle zeichnen
                }
            } // StudentDataEntry aufrufen für Änderung Schülerdaten 
            else if (table.columnAtPoint(m.getPoint()) == 0 && !rowSelected && m.getClickCount() == 2) {
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
