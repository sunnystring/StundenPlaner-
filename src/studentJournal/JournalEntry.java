/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentJournal;

import attendanceListData.AttendanceListData;
import core.Database;
import core.Profile;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import mainframe.MainFrame;
import utils.Colors;
import static utils.GUIConstants.*;

/**
 *
 * @author mathiaskielholz
 */
public class JournalEntry extends JDialog {

    private Database database;
    private AttendanceListData attendanceListData;
    private Profile profile;
    private String text;
    private JLabel dateField1, nameField, dateField2;
    private JScrollPane centerField;
    private JPanel topField, bottomField;
    private JButton closeButton, eraseButton, saveButton;
    private JTextPane displayArea;
    private StyledDocument document;

    public JournalEntry(MainFrame mainFrame) {
        super(mainFrame);
        database = mainFrame.getDatabase();
        attendanceListData = mainFrame.getAttendanceListData();
        createAndAddWidgets();
        addButtonListeners();
        addStylesToDocument();
        setResizable(false);
    }

    private void createAndAddWidgets() {
        bottomField = new JPanel();
        bottomField.setLayout(new BoxLayout(bottomField, BoxLayout.LINE_AXIS));
        bottomField.setBorder(LIGHT_BORDER);
        displayArea = new JTextPane();
        document = displayArea.getStyledDocument();
        centerField = new JScrollPane(displayArea);
        centerField.setBorder(BorderFactory.createEmptyBorder(1, 3, 0, 3));
        closeButton = new JButton("Schliessen");
        eraseButton = new JButton("Löschen");
        saveButton = new JButton("Speichern");
        bottomField.add(closeButton);
        bottomField.add(Box.createHorizontalGlue());
        bottomField.add(eraseButton);
        bottomField.add(saveButton);
        createTopField();
        add(BorderLayout.PAGE_START, topField);
        add(BorderLayout.CENTER, centerField);
        add(BorderLayout.PAGE_END, bottomField);
    }

    private void createTopField() {
        topField = new JPanel();
        topField.setLayout(new BoxLayout(topField, BoxLayout.LINE_AXIS));
        topField.setBorder(LIGHT_BORDER);
        dateField1 = new JLabel();
        dateField1.setForeground(Colors.DARK_GREEN);
        dateField1.setFont(dateField1.getFont().deriveFont(Font.PLAIN, 14));
        dateField1.setToolTipText("Woche der Erstellung");
        nameField = new JLabel();
        nameField.setForeground(Colors.NAMEFIELD_SINGLE_COLOR);
        nameField.setFont(nameField.getFont().deriveFont(Font.PLAIN, 14));
        dateField2 = new JLabel();
        dateField2.setForeground(Colors.VERY_DARK_GREEN);
        dateField2.setFont(dateField2.getFont().deriveFont(Font.PLAIN, 14));
        dateField2.setToolTipText("Aktuelle Woche");
        topField.add(Box.createHorizontalGlue());
        topField.add(nameField);
        topField.add(Box.createHorizontalGlue());
        setMinimumSize(new Dimension(topField.getPreferredSize().width, 250));
    }

    private void addStylesToDocument() {
        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        document.addStyle("journal", defaultStyle);
        StyleConstants.setFontSize(defaultStyle, 14);
    }

    private void addDateFields() {
        topField.removeAll();
        topField.add(dateField1);
        topField.add(Box.createHorizontalGlue());
        topField.add(nameField);
        topField.add(Box.createHorizontalGlue());
        topField.add(dateField2);
    }

    private void removeDateFields() {
        topField.removeAll();
        topField.add(Box.createHorizontalGlue());
        topField.add(nameField);
        topField.add(Box.createHorizontalGlue());
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
                database.setCurrentJournalText(profile.getID(), text);
                dispose();
            }
        });
    }

    public void showTextOf(Profile profile) {
        displayArea.setText("");
        this.profile = profile;
        setTitle("Journal");
        if (attendanceListData.isJournalArchiveEnabled()) {
            addDateFields();
            dateField1.setText("12.12.16");
            dateField2.setText(database.getWeekNameAt(attendanceListData.getCurrentWeekIndex()));
        } else {
            removeDateFields();
        }
        nameField.setText("  " + profile.getFirstName() + " " + profile.getName() + " " + profile.getThirdName() + " ");
        setDialogLocation();
        text = database.getCurrentJournalText(profile.getID());
        try {
            document.insertString(0, text, document.getStyle("journal"));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        displayArea.setPreferredSize(new Dimension(250, 150));
        setMinimumSize(new Dimension(topField.getPreferredSize().width, 250));
        pack();
    }

    private void setDialogLocation() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        location.setLocation(location.getX() + 30.0, location.getY() + 20.0);
        setLocation(location);
    }
}
