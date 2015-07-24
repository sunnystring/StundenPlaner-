/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schuelerliste;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 *
 * @author Mathias
 */
public class VariableField extends JLabel {

    private boolean selected;

    public VariableField(String text, Color color) {

        selected = false;
        setText(text);
        setBorder(BorderFactory.createEmptyBorder(5, 3, 5, 3));
        setBackground(color);
        setForeground(Color.WHITE);
        setOpaque(true);

    }

    public boolean isSelected() {
        return selected;
    }

    public void switchSelectionState() {
        if (selected) {
            selected = false;
        } else {
            selected = true;
        }
    }
}
