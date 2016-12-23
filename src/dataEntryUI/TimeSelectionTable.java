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
import javax.swing.text.JTextComponent;
import utils.Colors;

/**
 *
 * Sub-UI für die Zeiteinträge, das von {@link ScheduleInputMask} und
 * {@link StudentInputMask} benutzt wird
 */
public class TimeSelectionTable extends JTable {

    private ScheduleTimes scheduleTimes;
    private StudentTimes studentTimes;
    private boolean KGUselected;

    public TimeSelectionTable(ScheduleTimes scheduleTimes) {
        this.scheduleTimes = scheduleTimes;
        KGUselected = false;
        setModel(scheduleTimes);
        setRowHeight(32);
        setShowGrid(true);
        putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        setDefaultEditor(String.class, new TimeStringEditor());
        changeSelection(0, 1, false, false); // Fokus auf 1. editierbare Zelle 
    }

    public void setupScheduleInputMask() {
        setColumnParameters();
        setDefaultRenderer(String.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object o, boolean isSelected, boolean hasFocus, int row, int col) {
                setText(scheduleTimes.getValueAt(row, col).toString());
                if (row % 2 == 0) {
                    setBackground(Colors.BACKGROUND_COLOR);
                } else {
                    setBackground(Colors.LIGHT_GRAY_COLOR);
                }
                if (isSelected && col > 0) {
                    setBackground(getSelectionBackground());
                }
                return this;
            }
        });
    }

    public void setupStudentInputMask(StudentTimes studentTimes) {
        this.studentTimes = studentTimes;
        setModel(studentTimes);  // Model von ScheduleEntryMask überschreiben
        setColumnParameters();
        setDefaultRenderer(String.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object o, boolean isSelected, boolean hasFocus, int row, int col) {
                boolean isValidTimeslot2 =!(KGUselected && (col == 3 || col == 4));
                setText(studentTimes.getValueAt(row, col).toString());
                if (scheduleTimes.isValidDay(row) && isValidTimeslot2) {
                    setBackground(Colors.BACKGROUND_COLOR);
                } else {
                    setBackground(Colors.LIGHT_GRAY_COLOR);
                }
                if (isSelected && col > 0 && isValidTimeslot2) {
                    setBackground(getSelectionBackground());
                }
                return this;
            }
        });
    }

    public void adjustTimeSlotsToKGUEntry(boolean state) {
        KGUselected = state;
        studentTimes.setKGUselected(state);
        studentTimes.fireTableDataChanged();
    }

    private void setColumnParameters() {
        getColumnModel().setColumnSelectionAllowed(true);
        for (int i = 0; i < getModel().getColumnCount(); i++) {
            getColumnModel().getColumn(i).setPreferredWidth(85);
        }
        setPreferredScrollableViewportSize(this.getPreferredSize());
    }

    @Override  //  Place cell in edit mode when it 'gains focus'
    public void changeSelection(int row, int column, boolean toggle, boolean extend) {
        super.changeSelection(row, column, toggle, extend);
        if (editCellAt(row, column)) {
            Component editor = getEditorComponent();
            editor.requestFocusInWindow();
            ((JTextComponent) editor).selectAll();
        }
    }

}
