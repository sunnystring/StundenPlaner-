/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule2;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author Mathias
 */
public class TimeTable extends JTable {

    public TimeTable() {

        super(50, 4);
        setShowGrid(true);
        getColumnModel().setColumnSelectionAllowed(true); //  in alle Zellen kann geschrieben werden
        setFillsViewportHeight(true);

        getColumnModel().getColumn(0).setPreferredWidth(10);
        getColumnModel().getColumn(1).setPreferredWidth(190);
        getColumnModel().getColumn(2).setPreferredWidth(10);
        getColumnModel().getColumn(3).setPreferredWidth(190);

    }

}
