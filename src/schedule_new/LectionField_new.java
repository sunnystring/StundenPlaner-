/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule_new;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import util.Colors;

/**
 *
 * @author mathiaskielholz
 */
public class LectionField_new extends JLabel implements TableCellRenderer {

    public LectionField_new() {
        
        
        setHorizontalAlignment(SwingConstants.LEADING);
        setFont(this.getFont().deriveFont(Font.BOLD, 10));
        setText(" ");
        setBackground(Colors.BACKGROUND);
        setOpaque(true);
    }
    
    

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }

}
