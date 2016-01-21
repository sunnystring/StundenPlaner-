/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class DayField extends JLabel {

    public DayField(String text) {
        setText("  " + text);
        setBackground(Colors.DAY_FIELD);
        setForeground(Color.WHITE);
        setHorizontalAlignment(SwingConstants.LEADING);
        setFont(this.getFont().deriveFont(Font.BOLD + Font.PLAIN, 10));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Colors.BACKGROUND));
        setPreferredSize(new Dimension(0, 25));
        setOpaque(true);

    }

}
