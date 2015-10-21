/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule_new;

import core.StudentDay;
import core2.DayColumnData;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class TimeTable extends JTable {

    private TimeField_new timeField; // Renderer und MouseListener
    private LectionField_new lectionField;
    
    
    
    public TimeTable(DayColumnData dayColumnData) {

        setModel(dayColumnData);
        // setShowGrid(true);
        setFillsViewportHeight(true);
        setBackground(Colors.BACKGROUND);
        //   setSelectionBackground(Colors.LIGHT_GREEN);
      //  getColumnModel().setColumnSelectionAllowed(true);
        setRowSelectionAllowed(true);
        setCellSelectionEnabled(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

     
        timeField = new TimeField_new(dayColumnData);
        lectionField  = new LectionField_new();
        
        getColumnModel().getColumn(0).setCellRenderer(timeField);
        getColumnModel().getColumn(1).setCellRenderer(lectionField);
        getColumnModel().getColumn(2).setCellRenderer(timeField);
        getColumnModel().getColumn(3).setCellRenderer(lectionField);

        getColumnModel().getColumn(0).setMaxWidth(10);
        getColumnModel().getColumn(1).setMinWidth(50);
        getColumnModel().getColumn(2).setMaxWidth(10);
        getColumnModel().getColumn(3).setMinWidth(50);
        
        addMouseListener(timeField);
        addMouseListener(lectionField);
    }

    public TimeField_new getTimeField() {
        return timeField;
    }

    public LectionField_new getLectionField() {
        return lectionField;
    }
    

}
