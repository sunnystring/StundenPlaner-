/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dialogs;

import core.ScheduleTimes;
import core2.ScheduleData;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import mainframe.MainFrame;

/**
 *
 * @author Mathias
 */
public class ScheduleDataEntry extends JDialog {

    private ScheduleTimes scheduleTimes; // alle Unterrichtstage mit den entspr. Zeiten
    private MainFrame mainFrame;
    private ScheduleData scheduleData;

    private JScrollPane center;
    private JPanel bottom;
    private JTable selectionTable;
    private JButton cancel, save;

    public ScheduleDataEntry(ScheduleData scheduleData, MainFrame mainFrame) {

        super(mainFrame); // mainFrame = owner
        this.scheduleData = scheduleData;
        this.mainFrame = mainFrame;
        scheduleTimes  = scheduleData.getScheduleTimes();

        setLocationRelativeTo(mainFrame);
        setModal(true);
        setTitle("Stundenplan erstellen");
        setPreferredSize(new Dimension(300, 244));
        setResizable(false);
        createWidgets();
        addWidgets();
        addListener();
        pack();

    }

    private void createWidgets() {

        selectionTable = new JTable(scheduleTimes);
        selectionTable.setFillsViewportHeight(true);
        selectionTable.setShowGrid(true);
        selectionTable.setRowHeight(25);
        selectionTable.getColumnModel().setColumnSelectionAllowed(true); //  in alle Zellen kann geschrieben werden

        center = new JScrollPane(selectionTable, JScrollPane.VERTICAL_SCROLLBAR_NEVER, // Scrollbar nur wegen autom. JTable-Header
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

    private class CancelButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ScheduleDataEntry.this.dispose();
        }
    }

    private class SaveButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            mainFrame.createSchedule(scheduleTimes);
            mainFrame.prepareStudentList();
            ScheduleDataEntry.this.dispose();

        }
    }

}
