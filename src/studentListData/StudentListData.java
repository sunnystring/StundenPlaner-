/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentListData;

import core.Database;
import core.DatabaseListener;
import core.Student;
import javax.swing.table.AbstractTableModel;
import scheduleData.ScheduleData;
import studentlist.StudentList;

/**
 *
 * @author Mathias
 */
public class StudentListData extends AbstractTableModel implements DatabaseListener {

    private Database database;
    private StudentList studentList;
    private int numberOfDays;
    private int numberOfStudents;
    private ScheduleData scheduleData;

    public StudentListData(Database database) {

        this.database = database;
        numberOfDays = 0;
        numberOfStudents = 0;
    }

    /* wir ausgeführt bei jedem geaddeten Student in Database*/
    @Override
    public void studentAdded(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
        fireTableDataChanged();
        studentList.showNumberOfStudents();
    }

    /*  Getter, Setter */
    public void setScheduleData(ScheduleData scheduleData) {  // in MainFrame aufgerufen
        this.scheduleData = scheduleData;  // ToDo alles von Database holen
        numberOfDays = scheduleData.getNumberOfDays();
        }

    public Student getStudent(int i) {
        return database.getStudentList().get(i);
    }

    public void setStudentList(StudentList studentList) {
        this.studentList = studentList;
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

        if (col == 0) {
            return "  Vorname Name  (" + String.valueOf(numberOfStudents) + ")"; //numberOfStudentString
        }
        if (col > 0) {
            return "  " + scheduleData.getDayColumn(col - 1).getDayName();
        } else {
            return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return String.class;
    }

    @Override
    public Object getValueAt(int row, int col) {

        Student student = database.getStudentList().get(row);
        if (col == 0) {
            return student.getFirstName() + " " + student.getName();
        } else {
            return "<html>" + student.getStudentDay(col - 1) + "<font color=blue>" + student.getStudentDay(col - 1).getFavoriteAsString() + "</font></html>";
        }
    }

}
