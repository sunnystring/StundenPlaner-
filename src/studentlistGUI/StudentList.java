/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlistGUI;

import studentData.StudentData;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import scheduleGUI.Schedule;
import scheduleGUI.TimeTable;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class StudentList extends JTable {

    private StudentData studentData;
    private StudentField studentField;  // Renderer und MouseListener

    public StudentList(StudentData studentData, Schedule schedule) { // studentData = TableModel

        this.studentData = studentData;

        setModel(studentData);
        setShowGrid(true);
        setGridColor(Colors.LIGHT_GRAY);
        // getColumnModel().setColumnSelectionAllowed(true); //  in alle Zellen kann geschrieben werden
        setFillsViewportHeight(true);

        setBackground(Colors.LIGHT_GRAY);
        setColumnSelectionAllowed(true);
        setRowSelectionAllowed(true);
        setCellSelectionEnabled(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        studentField = new StudentField(studentData);

        getTableHeader().setDefaultRenderer(new HeaderField(studentData));
        setDefaultRenderer(String.class, studentField);
        setRowHeight(25);  // ev. dynam.
        /* StudentList-Renderer registrieren*/
        addMouseMotionListener(studentField);
        addMouseListener(studentField);
        /* TimeTable-Renderer registrieren */
        for (TimeTable table : schedule.getDayColumnList()) {
            addMouseListener(table.getTimeField());
        }
    }

    public StudentData getStudentData() {
        return studentData;
    }

}
