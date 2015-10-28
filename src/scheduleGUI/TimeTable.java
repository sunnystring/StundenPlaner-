/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleGUI;

import scheduleData.DayColumnData;
import javax.swing.JTable;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class TimeTable extends JTable {

    private TimeField timeField; // Renderer und MouseListener
    private LectionField lectionField;
    
    public TimeTable(DayColumnData dayColumnData) {
        
        setModel(dayColumnData);
        setFillsViewportHeight(true);
        setBackground(Colors.BACKGROUND);
        setRowSelectionAllowed(true);
        setCellSelectionEnabled(true);
       // setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        timeField = new TimeField(dayColumnData);
        lectionField  = new LectionField();
        
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

    public TimeField getTimeField() {
        return timeField;
    }

    public LectionField getLectionField() {
        return lectionField;
    }
    

}
