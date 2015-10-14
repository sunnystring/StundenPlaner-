/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule_new;

import core2.DayColumnModel;
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
 * @author mathiaskielholz
 */
public class TimeField_new extends JLabel implements TableCellRenderer {

    private DayColumnModel model;

    public TimeField_new(DayColumnModel model) {

        this.model = model;

        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
        setOpaque(true);

    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

       
        if (col == 0) {

            if (model.getColumn1Time(row).getMinute() != 0) {
                setText(String.valueOf(model.getColumn1Time(row).getMinute()));
                setBackground(Colors.LIGHT_GRAY);

            } else {
                setText(String.valueOf(model.getColumn1Time(row).getHour()));
                setBackground(Colors.TIMEFIELD_HOUR);
            }

        }
        if (col == 2) {

            if (model.getColumn2Time(row).getMinute() != 0) {
                setText(String.valueOf(model.getColumn2Time(row).getMinute()));
                setBackground(Colors.LIGHT_GRAY);

            } else {
                setText(String.valueOf(model.getColumn2Time(row).getHour()));
                setBackground(Colors.TIMEFIELD_HOUR);
            }
        }
        return this;
    }
    
 
}
