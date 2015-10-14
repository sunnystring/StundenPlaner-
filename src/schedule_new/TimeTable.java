/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule_new;

import core2.DayColumnModel;
import java.awt.Dimension;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class TimeTable extends JTable {

    public TimeTable(DayColumnModel model) {

        super(model);
        setShowGrid(true);
        setFillsViewportHeight(true);
        setBackground(Colors.BACKGROUND);
      
        
        //   setSelectionBackground(Colors.LIGHT_GREEN);

        getColumnModel().setColumnSelectionAllowed(true);

        setRowSelectionAllowed(true);
        setCellSelectionEnabled(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        getColumnModel().getColumn(0).setCellRenderer(new TimeField_new(model));
        getColumnModel().getColumn(1).setCellRenderer(new LectionField_new());
        getColumnModel().getColumn(2).setCellRenderer(new TimeField_new(model));
        getColumnModel().getColumn(3).setCellRenderer(new LectionField_new());

        getColumnModel().getColumn(0).setPreferredWidth(5);  // ToDo: Width dynamisch kalkulieren
        getColumnModel().getColumn(1).setPreferredWidth(100);
        getColumnModel().getColumn(2).setPreferredWidth(5);
        getColumnModel().getColumn(3).setPreferredWidth(100);

    }

}
