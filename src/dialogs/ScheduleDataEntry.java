/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dialogs;

import core.DataBase;
import core.TeacherTimes;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author Mathias
 */
public class ScheduleDataEntry extends JDialog {

    private TeacherTimes scheduleTimes; // alle Unterrichtstage mit den entspr. Zeiten
    private DataBase database;

    private JScrollPane center;
    private JPanel bottom;
    private JTable selectionTable;
    private JButton cancel, save;

    private static JFrame owner;

    public ScheduleDataEntry(DataBase database) {

        super(owner);
        this.database = database;

        scheduleTimes = new TeacherTimes(); // "Null"- Initialisierung für TableModel

        setLocationRelativeTo(owner);
        setModal(true);
        setTitle("Stundenplan");
        setPreferredSize(new Dimension(300, 220));
        createWidgets();
        addWidgets();
        addListener();
        pack();

    }

    private void createWidgets() {

        selectionTable = new JTable(scheduleTimes);
        selectionTable.setShowGrid(true);
        selectionTable.getColumnModel().setColumnSelectionAllowed(true); //  in alle Zellen kann geschrieben werden
  
        center = new JScrollPane(selectionTable, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        cancel = new JButton("Abbrechen");
        save = new JButton("Speichern");

    }

    private void addWidgets() {

        bottom.add(Box.createHorizontalGlue());
        bottom.add(cancel);
        bottom.add(save);

        add(BorderLayout.CENTER, center);
        add(BorderLayout.PAGE_END, bottom);
    }

    private void addListener() {

        cancel.addActionListener(new CancelButtonListener());
        save.addActionListener(new SaveButtonListener());
        
    }

    public static void setOwner(JFrame mainframe) {
        owner = mainframe;
    }

    private class CancelButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ScheduleDataEntry.this.dispose();
        }
    }

    private class SaveButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            scheduleTimes.finalizeTeacherTimes();
            database.addSchedule(scheduleTimes);
            ScheduleDataEntry.this.dispose();

        }
    }

}
