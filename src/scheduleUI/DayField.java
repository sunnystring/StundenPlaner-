/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import userUtilsUI.lessonCompanion.DayView;
import static utils.Colors.*;

/**
 *
 * Header in {@link Schedule}
 */
public class DayField extends JLabel implements MouseListener {

    private DayView dayView;

    public DayField(String text) {
        setText("  " + text);
        this.dayView = dayView;
        setBackground(DAYFIELD_COLOR);
        setForeground(Color.WHITE);
        setHorizontalAlignment(SwingConstants.LEADING);
        setFont(this.getFont().deriveFont(Font.BOLD + Font.PLAIN, 10));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, BACKGROUND_COLOR));
        setToolTipText("Klick: Journal und Unterrichtskontrolle dieses Tages anzeigen");
        setPreferredSize(new Dimension(0, 25));
        setOpaque(true);
    }

    public void setDayView(DayView dayView) {
        this.dayView = dayView;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (dayView != null) {
            dayView.dispose();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
