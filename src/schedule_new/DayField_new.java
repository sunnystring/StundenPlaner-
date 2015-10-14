/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule_new;

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
public class DayField_new extends JLabel implements TableCellRenderer {

    public DayField_new() {
        this("");

    }

    public DayField_new(String text) {

        super("  " + text);
        setBackground(Colors.DAY_FIELD);
        setForeground(Color.WHITE);
        setHorizontalAlignment(SwingConstants.LEADING);
        setFont(this.getFont().deriveFont(Font.BOLD + Font.PLAIN, 10));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Colors.LIGHT_GRAY));
        setPreferredSize(new Dimension(0, 25));
        setOpaque(true);

    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }

}
