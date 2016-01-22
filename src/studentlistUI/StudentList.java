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
    private StudentListData studentListData;
    private ScheduleData scheduleData;

    public StudentList(StudentListData studentListData) {
        this.studentListData = studentListData;
        setShowGrid(true);
        setGridColor(Colors.BACKGROUND);
        setFillsViewportHeight(true);
        setBackground(Colors.BACKGROUND);
        headerField = new HeaderField();
        getTableHeader().setDefaultRenderer(headerField);
        getTableHeader().setVisible(false);
        setRowHeight(25);
    }

    public void setParameters(TimeTable timeTable) {
        setModel(studentListData);
        createDefaultColumnsFromModel();
        getTableHeader().setDefaultRenderer(headerField);
        getTableHeader().setVisible(true);
        scheduleData = (ScheduleData) timeTable.getModel();
        studentField = new StudentField(this, studentListData);
        setDefaultRenderer(String.class, studentField);
        addMouseListener(studentListData);
        addMouseMotionListener(studentField);
        addMouseListener(timeTable.getLectionField());
        addMouseListener(timeTable.getTimeField());
        addMouseListener(scheduleData);
    }
    
    public void updateParameters(){  // ToDo
    
        
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
