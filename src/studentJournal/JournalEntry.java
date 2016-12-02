/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentJournal;

import core.Database;
import core.Profile;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import static utils.GUIConstants.*;

/**
 *
 * @author mathiaskielholz
 */
public class JournalEntry extends JDialog {

    private Database database;
    private Profile profile;
    private String text;
    private JScrollPane centerField;
    private JPanel bottomField;
    private JButton closeButton, eraseButton, saveButton;
    private JTextPane displayArea;
    private StyledDocument document;

    public JournalEntry(Database database) {
        this.database = database;
        setMinimumSize(new Dimension(250, 250));
        createAndAddWidgets();
        addButtonListeners();
        setResizable(false);
        pack();
    }

    private void createAndAddWidgets() {
        bottomField = new JPanel();
        bottomField.setLayout(new BoxLayout(bottomField, BoxLayout.LINE_AXIS));
        bottomField.setBorder(LIGHT_BORDER);
        displayArea = new JTextPane();
        document = displayArea.getStyledDocument();
        centerField = new JScrollPane(displayArea);
        centerField.setMinimumSize(displayArea.getPreferredSize());
        centerField.setBorder(BorderFactory.createEmptyBorder(1, 3, 0, 3));
        closeButton = new JButton("Schliessen");
        eraseButton = new JButton("Löschen");
        saveButton = new JButton("Speichern");
        bottomField.add(closeButton);
        bottomField.add(Box.createHorizontalGlue());
        bottomField.add(eraseButton);
        bottomField.add(saveButton);
        add(BorderLayout.CENTER, centerField);
        add(BorderLayout.PAGE_END, bottomField);
    }

    private void addButtonListeners() {
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        eraseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayArea.setText("");
                displayArea.requestFocusInWindow();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text = displayArea.getText();
                database.setJournalText(profile.getID(), text);
                dispose();
            }
        });
    }

    public void showTextOf(Profile profile) {
        displayArea.setText("");
        this.profile = profile;
        setTitle(profile.getFirstName() + " " + profile.getName() + " " + profile.getThirdName());
        setDialogLocation();
        text = database.getJournalText(profile.getID());
        try {
            document.insertString(0, text, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void setDialogLocation() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        location.setLocation(location.getX() + 20.0, location.getY() + 20.0);
        setLocation(location);
    }
}
