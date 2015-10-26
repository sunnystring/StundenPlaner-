/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core2;

import core.ScheduleTimes;
import core.Student;
import java.util.ArrayList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author Mathias
 */
public class StudentData implements TableModel {

    private ArrayList<Student> studentDataList;
    private ArrayList<TableModelListener> tableModelListener;
    private int numberOfDays;
    private int numberOfStudents;
    private ScheduleData scheduleData;
    private ScheduleTimes scheduleTimes;

    public StudentData() {

        studentDataList = new ArrayList<>();
        tableModelListener = new ArrayList<>();
        numberOfDays = 0;
        numberOfStudents = 0;
    }

    public void addStudent(Student student) {

        student.setStudentID(numberOfStudents);  // 1. Student: ID = 0
        student.getStudentTimes().createList(); // StudentDayList mit nur gültigen Zeiteinträgen erstellen
        studentDataList.add(student);

        for (TableModelListener l : tableModelListener) {
            l.tableChanged(new TableModelEvent(this));  // nur aktuell eingefügte Row updaten -> performanter ??
        }
        numberOfStudents = studentDataList.size(); // nächster Student ID = 1 usw. 
    }

    /*  Getter, Setter */
    public void setScheduleData(ScheduleData scheduleData) {  // in MainFrame aufgerufen

        this.scheduleData = scheduleData;
        numberOfDays = scheduleData.getNumberOfDays();
        scheduleTimes = scheduleData.getScheduleTimes();

    }

    public ScheduleTimes getScheduleTimes() {
        return scheduleTimes;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public Student getStudent(int i) {
        return studentDataList.get(i);
    }


    /* TableModel */
    @Override
    public int getRowCount() {
        return numberOfStudents;
    }

    @Override
    public int getColumnCount() {
        return numberOfDays + 1;
    }

    @Override
    public String getColumnName(int col) {

        if (col > 0) {
            return scheduleData.getDayColumnData(col - 1).getDayName();
        } else {
            return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return false;
    }

    @Override
    public Object getValueAt(int row, int col) {

        Student student = studentDataList.get(row);
        if (col == 0) {
            return student.getFirstName() + " " + student.getName();
        } else {
            return "<html>" + student.getStudentDay(col - 1) + "<font color=blue>" + student.getStudentDay(col - 1).getFavoriteAsString() + "</font></html>";
        }
    }

    @Override
    public void setValueAt(Object o, int row, int col) {
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
