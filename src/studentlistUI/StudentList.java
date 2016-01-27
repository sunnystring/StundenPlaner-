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
import scheduleUI.LectionField;
import scheduleUI.TimeField;
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
    private TimeTable timeTable;
    private LectionField lectionField;
    private TimeField timeField;
    
    public StudentList(StudentListData studentListData, TimeTable timeTable) {
        this.studentListData = studentListData;
        this.timeTable = timeTable;
        
        setModel(studentListData);
        //    createDefaultColumnsFromModel();
        headerField = new HeaderField();
        getTableHeader().setDefaultRenderer(headerField);
        getTableHeader().setVisible(false);
        getTableHeader().setDefaultRenderer(headerField);
        
        scheduleData = (ScheduleData) timeTable.getModel();
        studentField = new StudentField(this, studentListData);
        setDefaultRenderer(String.class, studentField);
        lectionField = timeTable.getLectionField();
        timeField = timeTable.getTimeField();
        addMouseListener(studentListData);
        addMouseMotionListener(studentField);
        addMouseListener(lectionField);
        addMouseListener(timeField);
        addMouseListener(scheduleData);
        
        setShowGrid(true);
        setGridColor(Colors.BACKGROUND);
        setFillsViewportHeight(true);
        setBackground(Colors.BACKGROUND);
//        headerField = new HeaderField();
//        getTableHeader().setDefaultRenderer(headerField);
//        getTableHeader().setVisible(false);
        setRowHeight(25);
    }
    
    public void updateParameters() {
        //  setModel(studentListData);
        createDefaultColumnsFromModel();
//        getTableHeader().setDefaultRenderer(headerField);
      //  getTableHeader().setVisible(true);
        studentField.setColumnCount(studentListData.getColumnCount());
        studentField.resetStudentRows();
//        scheduleData = (ScheduleData) timeTable.getModel();
//        studentField = new StudentField(this, studentListData);
//        setDefaultRenderer(String.class, studentField);
//        lectionField = timeTable.getLectionField();
//        timeField = timeTable.getTimeField();
//        addMouseListener(studentListData);
        //       addMouseMotionListener(studentField);
//        addMouseListener(lectionField);
//        addMouseListener(timeField);
//        addMouseListener(scheduleData);
    }
    
//    public void updateParameters(){  // ToDo
//    
//        
//    } 

//    public void updateParameters() {
//       // removeMouseListeners();
//        updateParameters();
//    }
    private void removeMouseListeners() {
        removeMouseListener(studentListData);
        removeMouseMotionListener(studentField);
        removeMouseListener(lectionField);
        removeMouseListener(timeField);
        removeMouseListener(scheduleData);
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
