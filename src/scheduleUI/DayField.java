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
import static utils.Colors.*;

/**
 *
 * Header in {@link Schedule}
 */
public class DayField extends JLabel {

    public DayField(String text) {
        setText("  " + text);
        setBackground(DAYFIELD_COLOR);
        setForeground(Color.WHITE);
        setHorizontalAlignment(SwingConstants.LEADING);
        setFont(this.getFont().deriveFont(Font.BOLD + Font.PLAIN, 10));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, BACKGROUND_COLOR));
        setToolTipText("Klick: Journal und Unterrichtskontrolle dieses Tages anzeigen");
        setPreferredSize(new Dimension(0, 25));
        setOpaque(true);
    }

}
