/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule2;

import schedule_new.TimeTable;
import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author mathiaskielholz
 */
public class ScheduleEditor extends AbstractCellEditor implements TableCellEditor {

    private TimeTable timeTable;
    
    public ScheduleEditor() {
        
       // timeTable  = new TimeTable();
    }
    
    
    

    @Override
    public Object getCellEditorValue() {
        return timeTable;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return timeTable;
    }

}
