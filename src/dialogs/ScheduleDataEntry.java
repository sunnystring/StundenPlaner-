/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import mainframe.MainFrame;

/**
 *
 * @author Mathias
 */
public class ScheduleDataEntry extends JDialog {

    private JPanel center, bottom;
    private JTextField numberOfStudents;
    private JTable selectionTable;
    private JButton cancel, save;

    public ScheduleDataEntry(MainFrame owner) {

        super(owner);

        setModal(true);
        setTitle("Stundenplan");
        setPreferredSize(new Dimension(300, 200));
        createWidgets();
        addWidgets();
        addListener();
        pack();

    }

    private void createWidgets() {

        center = new JPanel();
        center.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // ohne TableModel:
        selectionTable = new JTable(5, 3);
        selectionTable.setShowGrid(true);
        selectionTable.getColumnModel().setColumnSelectionAllowed(true); //  in alle Zellen kann geschrieben werden
        selectionTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        selectionTable.setRowHeight(20);
//        selectionTable.setValueAt(" ", 0, 0);
//        selectionTable.setValueAt("von", 0, 1);
//        selectionTable.setValueAt("bis", 0, 2);

        selectionTable.setValueAt("Montag", 0, 0);
        selectionTable.setValueAt("Dienstag", 1, 0);
        selectionTable.setValueAt("Mittwoch", 2, 0);
        selectionTable.setValueAt("Donnerstag", 3, 0);
        selectionTable.setValueAt("Freitag", 4, 0);

        numberOfStudents = new JTextField("Schülerzahl");
        numberOfStudents.setMaximumSize(numberOfStudents.getPreferredSize());
        cancel = new JButton("Abbrechen");
        save = new JButton("Speichern");

    }

    private void addWidgets() {

        center.add(BorderLayout.PAGE_START, selectionTable.getTableHeader());
        center.add(BorderLayout.PAGE_START, selectionTable);

        bottom.add(numberOfStudents);
        bottom.add(Box.createHorizontalGlue());
        bottom.add(cancel);
        bottom.add(save);

        add(BorderLayout.CENTER, center);
        add(BorderLayout.PAGE_END, bottom);

    }

    private void addListener() {

        cancel.addActionListener(new CancelListener());
        save.addActionListener(null);
    }

    private class CancelListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            ScheduleDataEntry.this.dispose();
        }
    }
}
