/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import core.ScheduleTimes;
import core.StudentTimes;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import util.Colors;

/**
 *
 * Sub-UI für die Zeiteinträge, das von {@link ScheduleEntryMask} und
 * {@link StudentEntryMask} benutzt wird
 */
public class SelectionTable extends JTable {

    ScheduleTimes scheduleTimes;

    public SelectionTable(ScheduleTimes scheduleTimes) {
        this.scheduleTimes = scheduleTimes;
        setModel(scheduleTimes);
        setRowHeight(25);
        setShowGrid(true);
        setSelectionBackground(Colors.DARK_GREEN);
        putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        setDefaultEditor(String.class, new TimeStringEditor());
    }

    public void setParameters() {
        setColumnParameters();
        setDefaultRenderer(String.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object o, boolean isSelected, boolean hasFocus, int row, int col) {
                setText(scheduleTimes.getValueAt(row, col).toString());
                if (row % 2 == 0) {
                    setBackground(Colors.BACKGROUND);
                } else {
                    setBackground(Colors.LIGHT_GRAY);
                }
                if (isSelected && col > 0) {
                    setBackground(Colors.DARK_GREEN);
                }
                return this;
            }
        });
    }

    public void setParameters(StudentTimes studentTimes) {
        setModel(studentTimes);  // Model von ScheduleEntryMask überschreiben
        setColumnParameters();
        setDefaultRenderer(String.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object o, boolean isSelected, boolean hasFocus, int row, int col) {
                setText(studentTimes.getValueAt(row, col).toString());
                if (scheduleTimes.isValidScheduleDay(row)) {
                    setBackground(Colors.BACKGROUND);
                } else {
                    setBackground(Colors.LIGHT_GRAY);
                }
                if (isSelected && col > 0 && scheduleTimes.isValidScheduleDay(row)) {
                    setBackground(Colors.DARK_GREEN);
                }
                return this;
            }
        });
    }

    private void setColumnParameters() {
        getColumnModel().setColumnSelectionAllowed(true);
        for (int i = 0; i < getModel().getColumnCount(); i++) {
            getColumnModel().getColumn(i).setPreferredWidth(85);
        }
        setPreferredScrollableViewportSize(this.getPreferredSize());
    }
}
