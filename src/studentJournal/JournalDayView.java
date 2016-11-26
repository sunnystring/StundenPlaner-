/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentJournal;

import core.Database;
import core.Profile;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import scheduleData.DayColumnData;
import scheduleData.LectionData;
import scheduleData.ScheduleData;
import scheduleUI.DayField;
import utils.Colors;
import static utils.GUIConstants.*;

/**
 *
 * @author mathiaskielholz
 */
public class JournalDayView extends JDialog {

    private Database database;
    private ScheduleData scheduleData;
    private StyledDocument document;
    private JScrollPane centerField;
    private JPanel bottomField;
    private JButton closeButton;
    private JTextPane textPane;
    private String journal;
    private String nameString;

    public JournalDayView(Database database, ScheduleData scheduleData) {
        this.database = database;
        this.scheduleData = scheduleData;
        setMinimumSize(new Dimension(250, 600));
        createAndAddWidgets();
        addStylesToDocument();
        setResizable(false);
        pack();
    }

    private void addStylesToDocument() {
        document = textPane.getStyledDocument();
        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        document.addStyle("journal", defaultStyle);
        Style nameStyle = document.addStyle("name", defaultStyle);
        StyleConstants.setForeground(nameStyle, Colors.NAMEFIELD_SINGLE_COLOR);
    }

    private void createAndAddWidgets() {
        bottomField = new JPanel();
        bottomField.setLayout(new BoxLayout(bottomField, BoxLayout.LINE_AXIS));
        bottomField.setBorder(LIGHT_BORDER);
        textPane = new JTextPane();
        textPane.setEditable(false);
        centerField = new JScrollPane(textPane);
        centerField.setMinimumSize(textPane.getPreferredSize());
        centerField.setBorder(VERY_LIGHT_BORDER);
        closeButton = new JButton("Schliessen");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        bottomField.add(Box.createHorizontalGlue());
        bottomField.add(closeButton);
        add(BorderLayout.CENTER, centerField);
        add(BorderLayout.PAGE_END, bottomField);
    }

    public void showStudentJournals(DayField dayField) {
        textPane.setText("");
        setTitle(dayField.getText());
        setDialogLocation();
        DayColumnData dayColumn = scheduleData.getDayColumn(dayField.getDayIndex());
        for (LectionData lectionData : dayColumn.getLectionMap().values()) {
            int profileID = lectionData.getProfileID();
            Profile profile = database.getProfile(profileID);
            nameString = profile.getFirstName() + " " + profile.getName() + " " + profile.getThirdName() + "\n";
            journal = database.getJournalText(profileID) ;
            if (!journal.isEmpty()) {
                try {
                    document.insertString(document.getLength(), nameString, document.getStyle("name"));
                    document.insertString(document.getLength(), journal+ "\n\n", document.getStyle("journal"));
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setDialogLocation() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        location.setLocation(location.getX(), location.getY() + 20.0);
        setLocation(location);
    }

}
