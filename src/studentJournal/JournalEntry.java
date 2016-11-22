/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentJournal;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import static utils.GUIConstants.*;

/**
 *
 * @author mathiaskielholz
 */
public class JournalEntry extends JDialog {

    protected JScrollPane centerField;
    protected JPanel bottomField;
    protected JButton cancelButton, saveButton;
    private JTextArea journalText;

    public JournalEntry() {
        setMinimumSize(new Dimension(250, 250));
        createAndAddWidgets();
        setResizable(false);
        pack();
    }

    public void setDialogLocation() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        setLocation(location);
    }

    public void createAndAddWidgets() {
        bottomField = new JPanel();
        bottomField.setLayout(new BoxLayout(bottomField, BoxLayout.LINE_AXIS));
        bottomField.setBorder(LIGHT_BORDER);
        journalText = new JTextArea();
        journalText.requestFocusInWindow();
        centerField = new JScrollPane(journalText);
        centerField.setMinimumSize(journalText.getPreferredSize());
        centerField.setBorder(BorderFactory.createEmptyBorder(1, 3, 0, 3));
        cancelButton = new JButton("Löschen");
        saveButton = new JButton("Speichern");
        bottomField.add(Box.createHorizontalGlue());
        bottomField.add(cancelButton);
        bottomField.add(saveButton);
        add(BorderLayout.CENTER, centerField);
        add(BorderLayout.PAGE_END, bottomField);
    }

}
