/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlist;

import studentListData.StudentListData;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import scheduleData.ScheduleData;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class StudentList extends JTable {

    private StudentField studentField;  // Renderer und MouseListener
    private HeaderField headerField;

    public StudentList(StudentListData studentData, ScheduleData scheduleData) { // studentData = TableModel, schedule = Listener-Objekt

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
        addMouseListener(scheduleData); // Click in StudentList ändert Schedule-TableModel (scheduleData)
    }
}
