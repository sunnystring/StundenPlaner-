/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlistGUI;

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

    public HeaderField() {

        setForeground(Colors.BACKGROUND);
        setHorizontalAlignment(SwingConstants.LEADING);
        setFont(this.getFont().deriveFont(Font.BOLD + Font.PLAIN, 10));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Colors.LIGHT_GRAY));
        setPreferredSize(new Dimension(0, 25));
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object o, boolean bln, boolean bln1, int row, int col) {

        String msg = (String) o;
        setText(msg);

        if (col == 0) {
            setBackground(Colors.NAME_FIELD_SELECTED);
        } else {
            setBackground(Colors.DAY_FIELD);
        }
        return this;
    }
}
