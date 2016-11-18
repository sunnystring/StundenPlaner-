/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userUtilsUI.lessonCompanion;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import static utils.GUIConstants.*;

/**
 *
 * @author mathiaskielholz
 */
public class DayView extends LessonCompanionDialog {

    private JTextPane dayViewTextPane;

    public DayView(String title) {
        setTitle(title);
        setMinimumSize(new Dimension(250, 600));
        createAndAddWidgets();
        setResizable(false);
        pack();
    }

    @Override
    public void createAndAddWidgets() {
        bottomField = new JPanel();
        bottomField.setLayout(new BoxLayout(bottomField, BoxLayout.LINE_AXIS));
        bottomField.setBorder(LIGHT_BORDER);
        dayViewTextPane = new JTextPane();
        centerField = new JScrollPane(dayViewTextPane);
        centerField.setMinimumSize(dayViewTextPane.getPreferredSize());
        centerField.setBorder(VERY_LIGHT_BORDER);
       // attendanceListButton = new JButton("E");
        // attendanceListButton.setForeground(Color.BLUE);
        // attendanceListButton.setFont(attendanceListButton.getFont().deriveFont(Font.BOLD, 25));
        // attendanceListButton.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        //  attendanceListButton.setToolTipText("Absenzen-Status: E=entschuldigt, U=unentschuldigt, L=Lehrerabsenz/Feiertage");
        cancelButton = new JButton("Schliessen");
       // cancelButton.setToolTipText("Journal-Eintrag löschen");
        //saveButton = new JButton("Speichern");
        // saveButton.setToolTipText("Absenzen-Status und Journal speichern");
        //  bottomField.add(attendanceListButton);
        bottomField.add(Box.createHorizontalGlue());
        bottomField.add(cancelButton);
        //   bottomField.add(saveButton);
        add(BorderLayout.CENTER, centerField);
        add(BorderLayout.PAGE_END, bottomField);
    }

}
