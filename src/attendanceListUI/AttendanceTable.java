/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceListUI;

import attendanceListData.AttendanceListData;
import javax.swing.JTable;
import mainframe.MainFrame;
import static utils.Colors.BACKGROUND_COLOR;

/**
 *
 * @author mathiaskielholz
 */
public class AttendanceTable extends JTable {

    private AttendanceField attendanceField;
    private HeaderField headerField;

    public AttendanceTable(AttendanceListData attendanceListData, MainFrame mainFrame) {
        setModel(attendanceListData);
        headerField = new HeaderField(this);
        getTableHeader().setDefaultRenderer(headerField);
        addMouseMotionListener(headerField);
        attendanceField = new AttendanceField(this);
        addMouseListener(attendanceListData);
        addMouseMotionListener(attendanceField);
        setFillsViewportHeight(true);
        setShowGrid(true);
        setGridColor(BACKGROUND_COLOR);
        setBackground(BACKGROUND_COLOR);
        setRowHeight(25);
    }

    public void update() {
        headerField.init();
        attendanceField.init();
        createDefaultColumnsFromModel();
        for (int i = 0; i < getColumnCount(); i++) {
            if (i == 0) {
                getColumnModel().getColumn(i).setMinWidth(100);
                getColumnModel().getColumn(i).setCellRenderer(attendanceField);
            } else {
                getColumnModel().getColumn(i).setPreferredWidth(25);
                getColumnModel().getColumn(i).setCellRenderer(attendanceField);
            }
        }
    }
}
