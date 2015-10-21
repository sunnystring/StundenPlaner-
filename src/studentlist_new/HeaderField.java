/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlist_new;

import core2.StudentData;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
public class HeaderField extends JLabel implements TableCellRenderer {

    private StudentData studentData;

    public HeaderField(StudentData studentData) {

        this.studentData = studentData;

        setBackground(Colors.DAY_FIELD);
        setForeground(Color.WHITE);
        setHorizontalAlignment(SwingConstants.LEADING);
        setFont(this.getFont().deriveFont(Font.BOLD + Font.PLAIN, 10));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Colors.LIGHT_GRAY));
        setPreferredSize(new Dimension(0, 25));
        setOpaque(true);

    }

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int row, int col) {

        if (col == 0) {
            // setText("<html>" + "Vorname Name " + "<font color=yellow>" + "(" + String.valueOf(studentData.getNumberOfStudents()) + ")" + "</font></html>");
            setText("  Vorname Name  (" + String.valueOf(studentData.getNumberOfStudents()) + ")");
            setBackground(Colors.NAME_FIELD_SELECTED);

        } else {
            setText("  " + studentData.getColumnName(col));
            setBackground(Colors.DAY_FIELD);
        }
        return this;
    }
}
