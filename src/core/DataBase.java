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

    /* TableModel Implementation */
    private static final String[] COLUMN_LABELS = {" ", "von", "bis*", "von", "bis*", "Wunschzeit*"};
    private static final String[] WEEKDAY_NAMES = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"};

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

        return 6;
    }

    @Override
    public int getColumnCount() {

        return 6;
    }

    @Override
    public String getColumnName(int col) {

        return COLUMN_LABELS[col];
    }

    @Override
    public Class<?> getColumnClass(int col) {

        return String.class;
    }

    @Override
    public boolean isCellEditable(int row, int col) {

        return col > 0;
    }

    @Override
    public Object getValueAt(int row, int col) {

        if (col == 0) {
            return WEEKDAY_NAMES[row];
        } else {
            return null;
        }
    }

    @Override
    public void setValueAt(Object o, int row, int col) {

        System.out.println(o.toString());
    }

    @Override
    public void addTableModelListener(TableModelListener tl) {
    }

    @Override
    public void removeTableModelListener(TableModelListener tl) {
    }

}
