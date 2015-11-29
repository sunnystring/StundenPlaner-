/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlist;

import studentListData.StudentListData;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import schedule.TimeTable;
import scheduleData.ScheduleData;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class StudentList extends JTable {

    private StudentField studentField;  // Renderer und MouseListener
    private HeaderField headerField;  // Renderer

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
        setRowHeight(25);  // ev. dynam.

        // StudentList: Renderer als Listener registrieren 
        addMouseListener(studentListData); // muss vor scheduleData geaddet werden, damit dort richtiger Event übergeben wird!
        addMouseMotionListener(studentField);
        // Schedule
        addMouseListener(scheduleData); // Klick in StudentList ändert Schedule-TableModel
        addMouseListener(timeTable.getLectionField()); 
        addMouseListener(timeTable.getTimeField());  

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
  }
