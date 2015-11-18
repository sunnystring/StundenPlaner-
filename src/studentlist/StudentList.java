/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlist;

import studentListData.StudentListData;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import schedule.Schedule;
import scheduleData.ScheduleData;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class StudentList extends JTable {

    private StudentField studentField;  // Renderer und MouseListener
    private HeaderField headerField;  // Renderer

    public StudentList(StudentListData studentListData, Schedule schedule) {

        setModel(studentListData);
        ScheduleData scheduleData = (ScheduleData) schedule.getTimeTable().getModel();
        setShowGrid(true);
        setGridColor(Colors.BACKGROUND);
        setFillsViewportHeight(true);
        setBackground(Colors.BACKGROUND);

        headerField = new HeaderField();
        getTableHeader().setDefaultRenderer(headerField);

        studentField = new StudentField(this, scheduleData);
        setDefaultRenderer(String.class, studentField);
        setRowHeight(25);  // ev. dynam.

        // StudentList: Renderer als Listener registrieren 
        addMouseMotionListener(studentField);
        addMouseListener(studentField);
        // Schedule
        addMouseListener(scheduleData); // Klick in StudentList ändert Schedule-TableModel (scheduleData)
        addMouseListener(schedule.getLectionField());
        addMouseListener(schedule.getTimeField());
    }

    /* Anzeige numberOfStudents in 1. HeaderField*/
    public void showNumberOfStudents() {
        JTableHeader header = getTableHeader();
        header.getColumnModel().getColumn(0).setHeaderValue(getColumnName(0));
        header.repaint();
    }

    public StudentField getStudentField() {
        return studentField;
    }

    public boolean isStudentSelected() {  // Selection-State 
        return studentField.isRowSelected();
    }

}
