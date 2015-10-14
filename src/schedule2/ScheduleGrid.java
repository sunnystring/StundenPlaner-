/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule2;

import java.awt.Dimension;
import javax.swing.JTable;

/**
 *
 * @author mathiaskielholz
 */
public class ScheduleGrid extends JTable {

    public ScheduleGrid() {

        super(1, 3);
        setShowGrid(true);
        getColumnModel().setColumnSelectionAllowed(true); //  in alle Zellen kann geschrieben werden
        setFillsViewportHeight(true);
        setPreferredSize(new Dimension(600, 0));
        setCellEditor(new ScheduleEditor());
       
    }

}
