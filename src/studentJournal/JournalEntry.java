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
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import mainframe.MainFrame;
import utils.Colors;
import utils.DateUtil;
import static utils.GUIConstants.*;

/**
 *
 * Eingabe-Dialogfenster für den laufenden Journaltext, dieser wird bei der
 * Speicherung auch ins Archiv übernommen, am selben Tag gemachte Änderungen
 * werden angepasst, ab dem folgenden Tag archiviert
 */
public class JournalEntry extends JDialog {

    private MainFrame mainFrame;
    private Database database;
    private Profile profile;
    private String text;
    private JLabel nameField;
    private JScrollPane centerField;
    private JPanel topField, bottomField;
    private JButton closeButton, eraseButton, saveButton;
    private JTextPane textPane;
    private StyledDocument document;
    private double currentX;

    public JournalEntry(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        database = mainFrame.getDatabase();
        text = "";
        createAndAddWidgets();
        addButtonListeners();
        setupArchiveView();
        setResizable(false);
    }

    private void createAndAddWidgets() {
        topField = new JPanel();
        topField.setLayout(new BoxLayout(topField, BoxLayout.LINE_AXIS));
        topField.setBorder(LIGHT_BORDER);
        nameField = new JLabel();
        nameField.setForeground(Colors.NAMEFIELD_SINGLE_COLOR);
        nameField.setFont(nameField.getFont().deriveFont(Font.PLAIN, 14));
        topField.add(Box.createHorizontalGlue());
        topField.add(nameField);
        topField.add(Box.createHorizontalGlue());
        setMinimumSize(new Dimension(topField.getPreferredSize().width, 250));
        bottomField = new JPanel();
        bottomField.setLayout(new BoxLayout(bottomField, BoxLayout.LINE_AXIS));
        bottomField.setBorder(LIGHT_BORDER);
        textPane = new JTextPane();
        textPane.setFont(textPane.getFont().deriveFont(Font.PLAIN, 14));
        centerField = new JScrollPane(textPane);
        centerField.setBorder(BorderFactory.createEmptyBorder(1, 3, 0, 3));
        closeButton = new JButton("Schliessen");
        eraseButton = new JButton("Löschen");
        saveButton = new JButton("Speichern");
        bottomField.add(closeButton);
        bottomField.add(Box.createHorizontalGlue());
        bottomField.add(eraseButton);
        bottomField.add(saveButton);
        add(BorderLayout.PAGE_START, topField);
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
                textPane.setText("");
                textPane.requestFocusInWindow();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text = textPane.getText();
                JournalData currentJournal = database.getCurrentJournalOf(profile.getID());
                if (database.getJournalArchiveOf(profile.getID()).size() > 0) {
                    if (DateUtil.isBefore(currentJournal.getDate(), DateUtil.getTodayString())) {
                        updateJournalAndAddToArchive(currentJournal);
                    } else {
                        updateTodayJournalAndArchive(currentJournal);
                    }
                } else {
                    updateJournalAndAddToArchive(currentJournal);
                }
                mainFrame.saveCurrentEntriesToFile();
                dispose();
            }
        });
    }

    private void updateJournalAndAddToArchive(JournalData currentJournal) {
        currentJournal.setText(text);
        currentJournal.setDate(DateUtil.getTodayString());
        if (!text.isEmpty()) {
            database.addJournalToArchive(profile.getID(), new JournalData(text, DateUtil.getTodayString()));
        }
    }

    private void updateTodayJournalAndArchive(JournalData currentJournal) {
        currentJournal.setText(text);
        database.updateCurrentJournalInArchive(profile.getID(), currentJournal);
    }

    private void setupArchiveView() {
        textPane.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JournalArchiveView archiveView = new JournalArchiveView(mainFrame);
                    archiveView.setLocation(getArchiveViewLocation());
                    archiveView.showArchiveOf(profile);
                    archiveView.setVisible(true);

                }
            }
        });
    }

    public void showTextOf(Profile profile) {
        addStylesToDocument();
        textPane.setText("");
        this.profile = profile;
        JournalData journal = database.getCurrentJournalOf(profile.getID());
        setTitle(journal.getDate());
        nameField.setText(profile.getFirstName() + " " + profile.getName() + " " + profile.getThirdName());
        text = journal.getText();
        try {
            document.insertString(0, text, document.getStyle("journalEntry"));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        textPane.setPreferredSize(new Dimension(250, 150));
        setMinimumSize(new Dimension(topField.getPreferredSize().width, 250));
        pack();
        setLocation(getEntryDialogLocation());
    }

    private void addStylesToDocument() {
        document = textPane.getStyledDocument();
        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        document.addStyle("journalEntry", defaultStyle);
        StyleConstants.setFontSize(defaultStyle, 14);
    }

    private Point getEntryDialogLocation() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        location.setLocation(location.getX() + 30.0, location.getY() + 20.0);
        currentX = location.getX();
        return location;
    }

    private Point getArchiveViewLocation() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        location.setLocation(currentX, 20);
        return location;
    }

}
