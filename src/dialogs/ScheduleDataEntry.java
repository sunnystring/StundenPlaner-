/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dialogs;

import core.ScheduleTimes;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import mainframe.MainFrame;

/**
 *
 * @author Mathias
 */
public class ScheduleDataEntry extends JDialog {

    private ScheduleTimes schedule; // = TableModel

    private JScrollPane center;
    private JPanel bottom;
    private JLabel numberOfStudentsLabel;
    private JTextField numberOfStudents;
    private JTable selectionTable;
    private JButton cancel, save;
    
        private static JFrame owner;


    public ScheduleDataEntry() {

        super(owner);
        schedule = new ScheduleTimes();

        setLocationRelativeTo(null);
        setModal(true);
        setTitle("Stundenplan");
        setPreferredSize(new Dimension(300, 220));
        createWidgets();
        addWidgets();
        addListener();
        pack();

    }

    private void createWidgets() {

        selectionTable = new JTable(schedule);
        selectionTable.setShowGrid(true);
        selectionTable.getColumnModel().setColumnSelectionAllowed(true); //  in alle Zellen kann geschrieben werden

        center = new JScrollPane(selectionTable, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        numberOfStudentsLabel = new JLabel(" Schülerzahl:");
        numberOfStudents = new JTextField("20");
        numberOfStudents.setMaximumSize(numberOfStudents.getPreferredSize());
        cancel = new JButton("Abbrechen");
        save = new JButton("Speichern");

    }

    private void addWidgets() {

        bottom.add(numberOfStudentsLabel);
        bottom.add(numberOfStudents);
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
            System.out.println(schedule.getTeacherTime(0, 1));

        }

    }

}
