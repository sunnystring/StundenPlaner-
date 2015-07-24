/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stundenplan;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author Mathias
 */
public class DayField extends JLabel {

    public DayField() {

   //     setVerticalAlignment(this.TOP);
        setHorizontalAlignment(SwingConstants.LEADING);
        setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 3));
        setFont(this.getFont().deriveFont(Font.BOLD + Font.PLAIN, 10));
        setForeground(Color.WHITE);
        setOpaque(true);

    }

}
