/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core2;

import core.Student;
import java.util.ArrayList;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author Mathias
 */
public class StudentData implements TableModel {

    private ArrayList<Student> studentDataList;
    private ArrayList<TableModelListener> tableModelListener;

    private int numberOfStudents = 0;

    public void addStudent(Student student) {

        student.setStudentID(numberOfStudents);  // 1. Student: ID = 0
        studentDataList.add(student);
        numberOfStudents = studentDataList.size(); // nächster Student ID = 1 usw. 
        // ToDo Listener...fireEvent
    }

    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public String getColumnName(int i) {
        return null;
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return null;
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return false;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        return null;
    }

    @Override
    public void setValueAt(Object o, int i, int i1) {
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        tableModelListener.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        tableModelListener.remove(l);
    }

}
