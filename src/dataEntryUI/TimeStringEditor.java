/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import javax.swing.DefaultCellEditor;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import utils.Dialogs;

/**
 *
 * Editor-Component von {@link SelectionTable} mit User-Input-Validation
 */
public class TimeStringEditor extends DefaultCellEditor {

    private JTextField textField;
    private InputVerifier iV = new InputVerifier() {
        private static final String INPUT_FORMAT = "([1][0-9]|[2][0-3])?([.][0-5][05]?)?";

        @Override
        public boolean verify(JComponent input) {
            JTextField field = (JTextField) input;
            return field.getText().trim().matches(INPUT_FORMAT);
        }

        @Override
        public boolean shouldYieldFocus(JComponent input) {
            boolean valid = verify(input);
            if (!valid) {
               Dialogs.showTimeInputFormatError();
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
