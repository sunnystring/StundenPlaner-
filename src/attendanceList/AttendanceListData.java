/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceList;

import core.Database;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author mathiaskielholz
 */
public class AttendanceListData extends AbstractTableModel {

    private Database database;

    public AttendanceListData(Database database) {
        this.database = database;
    }

    @Override
    public int getRowCount() {
        return database.getNumberOfStudents();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return "E";
    }

}
