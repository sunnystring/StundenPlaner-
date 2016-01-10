/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.DefaultCellEditor;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * Überprüft User-Zeiteinträge in der {@link SelectionTable}
 */
public class TimeStringEditor extends DefaultCellEditor {

    private JTextField textField;
    private InputVerifier iV = new InputVerifier() {
        private static final String INPUT_FORMAT = "([1][0-9]|[2][0-3])?([.][0-5][05])?";

        @Override
        public boolean verify(JComponent input) {
            JTextField field = (JTextField) input;
            return field.getText().trim().matches(INPUT_FORMAT);
        }

        @Override
        public boolean shouldYieldFocus(JComponent input) {
            boolean valid = verify(input);
            if (!valid) {
                JOptionPane.showMessageDialog(null, "Ungültiges Zeitformat:\n"
                        + "Zeit zwischen 10.00 und 23.55 eingeben\n"
                        + "oder Feld löschen!");
            }
            return valid;
        }
    };

    public TimeStringEditor() {
        super(new JTextField());
        textField = (JTextField) getComponent();
        textField.setInputVerifier(iV);
    }

    @Override
    public boolean stopCellEditing() {
        if (!iV.shouldYieldFocus(textField)) { // für Aufruf von Error-Message
            return false;
        }
        return super.stopCellEditing();
    }
}
