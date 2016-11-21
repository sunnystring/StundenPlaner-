/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userUtilsUI.lessonCompanion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import userUtilsUI.lessonCompanion.LessonCompanionDialog;
import static utils.GUIConstants.*;

/**
 *
 * @author mathiaskielholz
 */
public class JournalEntry extends LessonCompanionDialog {

    private JTextPane journalTextPane;

    public JournalEntry(String title) {
        setTitle(title);
        Point location = MouseInfo.getPointerInfo().getLocation();
        setLocation(location);
        setMinimumSize(new Dimension(250, 250));
        createAndAddWidgets();
        setResizable(false);
        pack();
      //  journalTextPane.requestFocusInWindow();
    }

    @Override
    public void createAndAddWidgets() {
//        topField = new JPanel();
//        topField.setLayout(new BoxLayout(topField, BoxLayout.LINE_AXIS));
//        topField.setBorder(VERY_LIGHT_BORDER);
        bottomField = new JPanel();
        bottomField.setLayout(new BoxLayout(bottomField, BoxLayout.LINE_AXIS));
        bottomField.setBorder(LIGHT_BORDER);
        journalTextPane = new JournalText();
        journalTextPane.requestFocusInWindow();
        centerField = new JScrollPane(journalTextPane);
        centerField.setMinimumSize(journalTextPane.getPreferredSize());
        centerField.setBorder(BorderFactory.createEmptyBorder(1, 3, 0, 3));
//        attendanceListButton = new JButton("E");
//        attendanceListButton.setForeground(Color.BLUE);
//        attendanceListButton.setFont(attendanceListButton.getFont().deriveFont(Font.BOLD, 20));
//        attendanceListButton.setBorder(BorderFactory.createEmptyBorder(1, 8, 1, 8));
//        attendanceListButton.setToolTipText("Absenzen-Status speichern: E=entschuldigt, U=unentschuldigt, L=Lehrerabsenz, F=Feiertage/Konvente usw.");
        cancelButton = new JButton("Löschen");
       // cancelButton.setToolTipText("Bestehender Journal-Eintrag löschen");
        saveButton = new JButton("Speichern");
      //  saveButton.setToolTipText("Neuer Journal-Eintrag speichern");
//        topField.add(attendanceListButton);
//        topField.add(Box.createHorizontalGlue());
        bottomField.add(Box.createHorizontalGlue());
        bottomField.add(cancelButton);
        bottomField.add(saveButton);
      //  add(BorderLayout.PAGE_START, topField);
        add(BorderLayout.CENTER, centerField);
        add(BorderLayout.PAGE_END, bottomField);
    }

}
