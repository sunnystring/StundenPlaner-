/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import core.Database;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import core.ScheduleTimes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import mainframe.MainFrame;

/**
 *
 * UI, das von {@link ScheduleEntryDialog} benutzt wird
 */
public class ScheduleEntryMask extends JPanel {

    private Database database;
    private MainFrame mainFrame;
    private ScheduleTimes scheduleTimes;
    private ScheduleEntryDialog scheduleEntryDialog;
    private JScrollPane center;
    private JPanel bottom;
    private SelectionTable selectionTable;
    private JButton cancel, save;

    public ScheduleEntryMask(Database database) {
        this.database = database;
        this.scheduleTimes = database.getScheduleTimes();
        setLayout(new BorderLayout());
        createWidgets();
        addWidgets();
        addListeners();
    }

    private void createWidgets() {
        selectionTable = new SelectionTable(scheduleTimes);
        selectionTable.setParameters();
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

    public void addListeners() {
        save.addActionListener(new SaveButtonListener());
        cancel.addActionListener(new CancelButtonListener());
    }

    public class CancelButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            scheduleEntryDialog.dispose();
        }
    }

    public class SaveButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!scheduleTimes.areTimeEntriesValid()) {
                return;
            }
            scheduleTimes.setValidScheduleDays();
            database.setNumberOfDays(scheduleTimes.getNumberOfDays());
            mainFrame.setScheduleData();
            mainFrame.createSchedule();
            mainFrame.setStudentListData();
            mainFrame.createStudentList();
            mainFrame.setStudentListToStudentListData(); // für Anzeige von numberOfStudents in HeaderField
            mainFrame.setStudentButtonsEnabled(true);
            scheduleEntryDialog.dispose();
        }
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void setScheduleEntryDialog(ScheduleEntryDialog scheduleEntryDialog) {
        this.scheduleEntryDialog = scheduleEntryDialog;
    }
}
