/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlistUI;

import studentListData.StudentListData;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import scheduleUI.TimeTable;
import scheduleData.ScheduleData;
import util.Colors;

/**
 *
 * View der ganzen Schülerliste
 */
public class StudentList extends JTable {

    private StudentField studentField;
    private HeaderField headerField;

    public StudentList(StudentListData studentListData, TimeTable timeTable) {
        setModel(studentListData);
        ScheduleData scheduleData = (ScheduleData) timeTable.getModel();
        setShowGrid(true);
        setGridColor(Colors.BACKGROUND);
        setFillsViewportHeight(true);
        setBackground(Colors.BACKGROUND);
        headerField = new HeaderField();
        getTableHeader().setDefaultRenderer(headerField);
        studentField = new StudentField(this);
        setDefaultRenderer(String.class, studentField);
        setRowHeight(25);
        
        addMouseListener(studentListData);
        addMouseMotionListener(studentField);
        addMouseListener(timeTable.getLectionField());
        addMouseListener(timeTable.getTimeField());
        addMouseListener(scheduleData);
    }

    public void showNumberOfStudents() {
        JTableHeader header = getTableHeader();
        header.getColumnModel().getColumn(0).setHeaderValue(getColumnName(0));
        header.repaint();
    }

    public StudentField getStudentField() {
        return studentField;
    }
}
