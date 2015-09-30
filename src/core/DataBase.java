/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.ArrayList;

/**
 *
 * @author Mathias
 */
public class DataBase {

    private ArrayList<Student> studentDataList;
    private ScheduleTimes scheduleTimes;

    private DatabaseListener databaseListener; // = MainFrame

    private int numberOfDays; //  = Position in scheduleDayList(ScheduleTimes)
    private int numberOfStudents;  // = Position in studentDataList = Schülerzahl, wird für alle GUI Aktionen gebraucht

    public DataBase() {

        studentDataList = new ArrayList<>();
        scheduleTimes = new ScheduleTimes();
        numberOfDays = 0;
        numberOfStudents = 0;
    }

    /* Getter, Setter */
    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public int getStudentID(Student student) {
        return studentDataList.indexOf(student);
    }

    public ScheduleTimes getScheduleTimes() {
        return scheduleTimes;
    }

    public void addSchedule(ScheduleTimes scheduleTimes) {
        
        this.scheduleTimes = scheduleTimes;   // Schedule "adden"
        numberOfDays = scheduleTimes.getNumberOfDays(); // Database weiss jetzt die globale Zahl der Tage
        databaseListener.scheduleAdded(scheduleTimes);
    }

    public void addStudent(Student student) {
        
        student.setStudentID(numberOfStudents);  // 1. Student: ID = 0
        studentDataList.add(student);
        numberOfStudents = studentDataList.size(); // nächster Student ID = 1 usw. 
        databaseListener.studentAdded(student);
    }

    public void addDatabaseListener(DatabaseListener databaseListener) {
        this.databaseListener = databaseListener;
    }

}
