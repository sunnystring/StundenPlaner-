/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentJournal;

import core.Database;
import core.Profile;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import mainframe.MainFrame;
import utils.Colors;
import utils.Dialogs;
import utils.GUIConstants;

/**
 *
 * Gesamtsicht aller Journale eines Schülers, die über {@link JournalEntry} gespeichert wurden
 */
public class JournalArchiveView extends JournalDayView {

    private Database database;
    private Profile profile;
    private JPanel topField;
    private JLabel nameField;
    private JButton deleteArchiveButton;

    public JournalArchiveView(MainFrame mainFrame) {
        super(mainFrame);
        database = mainFrame.getDatabase();
        setTitle("Journal-Archiv");
        createAndAddWidgets();
        setModal(true);
    }

    private void createAndAddWidgets() {
        topField = new JPanel();
        topField.setLayout(new BoxLayout(topField, BoxLayout.LINE_AXIS));
        topField.setBorder(GUIConstants.LIGHT_BORDER);
        nameField = new JLabel();
        nameField.setForeground(Colors.NAMEFIELD_SINGLE_COLOR);
        nameField.setFont(nameField.getFont().deriveFont(Font.PLAIN, 14));
        topField.add(Box.createHorizontalGlue());
        topField.add(nameField);
        topField.add(Box.createHorizontalGlue());
        deleteArchiveButton = new JButton("Archiv löschen");
        deleteArchiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Dialogs.showAffirmDeleteJournalArchiveMessage(textPane, profile.getFirstName()
                        + " " + profile.getName()) == JOptionPane.YES_OPTION) {
                    database.getJournalArchiveOf(profile.getID()).clear();
                    dispose();
                }
            }
        });
        bottomField.add(deleteArchiveButton);
        add(BorderLayout.PAGE_START, topField);
    }

    public void showArchiveOf(Profile profile) {
        this.profile = profile;
        addStylesToDocument();
        nameField.setText(profile.getFirstName() + " " + profile.getName() + " " + profile.getThirdName());
        ArrayList<JournalData> archive = database.getJournalArchiveOf(profile.getID());
        for (int i = archive.size() - 1; i >= 0; i--) {
            String date = archive.get(i).getDate();
            String text = archive.get(i).getText();
            if (!text.isEmpty()) {
                try {
                    document.insertString(document.getLength(), date + "\n", document.getStyle("date"));
                    document.insertString(document.getLength(), text + "\n\n", document.getStyle("journalArchive"));
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addStylesToDocument() {
        document = textPane.getStyledDocument();
        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        document.addStyle("journalArchive", defaultStyle);
        StyleConstants.setFontSize(defaultStyle, 14);
        Style dateStyle = document.addStyle("date", defaultStyle);
        StyleConstants.setForeground(dateStyle, Colors.DARK_GREEN);
        StyleConstants.setFontSize(dateStyle, 14);
        StyleConstants.setBold(dateStyle, true);
    }
}
