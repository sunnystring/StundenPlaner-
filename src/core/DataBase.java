/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.ArrayList;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author Mathias
 */
public class DataBase implements TableModel {

    /* ---------------globale Daten------------------------ */
    private static final ArrayList<DatabaseListener> databaseListener = new ArrayList<>();

    private static int dayIndex;  // counter für Tage = Position in dayDataList  = Day-ID
    private static int numberOfDays; // vorgegebene Maximalzahl,  ToDo: dynamisch,
    private static int studentIndex;  // counter für Student = Position in studentDataList = Student-ID
    private static int numberOfStudents;  // vorgegebene Maximalzahl,  ToDo: dynamisch, 

    public DataBase() {

        dayIndex = 0;
        numberOfDays = 3;
        studentIndex = 0;
        numberOfStudents = 36;

    }

    /* ---------------Schedule------------------------*/
    private static ArrayList<ScheduleDay> dayDataList = new ArrayList<>();  // enthält die ScheduleDay-Rohdaten

    /* ----------------Studentlist------------------------*/
    private static ArrayList<Student> studentDataList = new ArrayList<>();  // enthält die Student-Rohdaten

    /* globale Getter, Setter  */
    public static int getNumberOfStudents() {
        return numberOfStudents;
    }

    public static int getNumberOfDays() {
        return numberOfDays;
    }

    public static int getStudentIndex() {
        return studentIndex;
    }

    public static ScheduleDay getDayDataList(int index) {
        return dayDataList.get(index);
    }

    /*  Listener */
    public static void addDatabaseListener(DatabaseListener l) {
        databaseListener.add(l);
    }

    public static void removeDatabaseListener(DatabaseListener l) {
        databaseListener.remove(l);
    }

    /*-------------------für Dateneingabe Rohfassung-------------------------*/
    public static void addDay(ScheduleDay day) {

        day.setDayIndex(dayIndex);
        dayDataList.add(day);   // ScheduleDay-Daten in dayDataList speichern
        dayIndex++;  // zählt die Anzahl eingebener Tage (= vom Tag unabhängiger Tag-Index)

        for (DatabaseListener l : databaseListener) {  // l = Referenz auf Schedule,
            l.dayAdded(day);                  // füllt Daten in DayColumns und zeichnet diese      
        }
    }

    public static void addStudent(Student student) {

        student.setStudentIndex(studentIndex);
        studentDataList.add(student);// Student-Daten in studentDataList speichern
        studentIndex++;

        for (DatabaseListener l : databaseListener) { // l = Referenz auf StudentList
            l.studentAdded(student);        // füllt Daten in Student-Rows und zeichnet diese      

        }
    }

    /* Implementierung TableModel für StudentDataEntry */
    @Override
    public int getRowCount() {
        return 7;
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public String getColumnName(int col) {

        switch (col) {
            case 0:
                return " ";
            case 1:
                return "von";
            case 2:
                return "bis";
            case 3:
                return "von";
            case 4:
                return "bis";
            case 5:
                return "Wunschzeit";
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int col) {

        switch (col) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            case 4:
                return String.class;
            case 5:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {

        return row > 0 && col > 0;
    }

    @Override
    public Object getValueAt(int row, int col) {

        if (col == 0) {
            switch (row) {

                case 1:
                    return "Montag";
                case 2:
                    return "Dienstag";
                case 3:
                    return "Mittwoch";
                case 4:
                    return "Donnerstag";
                case 5:
                    return "Freitag";
                case 6:
                    return "Samstag";
                default:
                    return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setValueAt(Object o, int row, int col) {

        String time;
        if (row > 0 && col > 0) {
            time = o.toString();
            System.out.println(time);
        }
    }

    @Override
    public void addTableModelListener(TableModelListener tl) {
    }

    @Override
    public void removeTableModelListener(TableModelListener tl) {
    }

}
