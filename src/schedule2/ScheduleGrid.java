/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule2;

import java.awt.Dimension;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author mathiaskielholz
 */
public class ScheduleGrid extends JTable {

    public ScheduleGrid() {

      //  super(1, 3);
        setShowGrid(true);
        getColumnModel().setColumnSelectionAllowed(true); //  in alle Zellen kann geschrieben werden
        setFillsViewportHeight(true);
        setDefaultRenderer(Object.class, new ScheduleRenderer());

        setModel(new AbstractTableModel() {

            @Override
            public int getRowCount() {
                return 1;
            }

            @Override
            public int getColumnCount() {
                return 3;
            }

            @Override
            public Object getValueAt(int i, int i1) {
                return null;
            }
        });

    }

}
