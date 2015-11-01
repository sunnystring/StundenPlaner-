/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlistGUI;

import studentListData.StudentListData;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import scheduleData_new.ScheduleData_new;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class StudentList extends JTable {

    private StudentListData studentData;
    private StudentField studentField;  // Renderer und MouseListener
    private HeaderField headerField;

    public StudentList(StudentListData studentData, ScheduleData_new scheduleData) { // studentData = TableModel, schedule = Listener-Objekt

        this.studentData = studentData;

        setModel(studentData);
        setShowGrid(true);
        setGridColor(Colors.BACKGROUND);
        // getColumnModel().setColumnSelectionAllowed(true); //  in alle Zellen kann geschrieben werden
        setFillsViewportHeight(true);

        setBackground(Colors.BACKGROUND);
        setColumnSelectionAllowed(true);
        setRowSelectionAllowed(true);
        setCellSelectionEnabled(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        headerField = new HeaderField();
        getTableHeader().setDefaultRenderer(headerField);

        studentField = new StudentField(studentData);
        setDefaultRenderer(String.class, studentField);
        setRowHeight(25);  // ev. dynam.

        /* Renderer sind auch Listener-Objekte */
        addMouseMotionListener(studentField);
        addMouseListener(studentField);

        /* Schedule reagiert auf StudentList-Events */
        addMouseListener(scheduleData);
    }

    public StudentListData getStudentData() {
        return studentData;
    }
}
