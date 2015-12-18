/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import core.ScheduleTimes;
import dataEntryUI.ScheduleDataEntry.CancelButtonListener;
import dataEntryUI.ScheduleDataEntry.SaveButtonListener;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class ScheduleDataEntryMask extends JPanel {

    private ScheduleTimes scheduleTimes;
    private JScrollPane center;
    private JPanel bottom;
    private JTable selectionTable;
    private JButton cancel, save;

    public ScheduleDataEntryMask(ScheduleTimes scheduleTimes) {
        this.scheduleTimes = scheduleTimes;
        setLayout(new BorderLayout());
        createWidgets();
        addWidgets();
    }

    private void createWidgets() {
        selectionTable = new JTable(scheduleTimes);
        selectionTable.setRowHeight(25);
        selectionTable.setPreferredScrollableViewportSize(selectionTable.getPreferredSize());
        selectionTable.setShowGrid(true);
        selectionTable.getColumnModel().setColumnSelectionAllowed(true);
        selectionTable.setSelectionBackground(Colors.DARK_GREEN);
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

    public void addListeners(ScheduleDataEntry scheduleDataEntry) {
        save.addActionListener(scheduleDataEntry.new SaveButtonListener());
        cancel.addActionListener(scheduleDataEntry.new CancelButtonListener());
    }
}
