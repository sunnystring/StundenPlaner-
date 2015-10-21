/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule2;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Mathias
 */
public class ScheduleRenderer extends JTable implements TableCellRenderer{

    public ScheduleRenderer() {
        
         super(4,40);
         
        setShowGrid(true);
        getColumnModel().setColumnSelectionAllowed(true); //  in alle Zellen kann geschrieben werden
       // setFillsViewportHeight(true);
    }
    
        
    
   

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int row, int col) {
       
        return this;
    }
    
}
