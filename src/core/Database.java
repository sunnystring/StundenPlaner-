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
public class Database {

    private ScheduleTimes scheduleTimes;
    private ArrayList<Student> studentList;
    private ArrayList<DatabaseListener> databaseListeners;
    private int numberOfDays;
    private int numberOfStudents;

    public Database() {

        scheduleTimes = new ScheduleTimes();
        studentList = new ArrayList<>();
        databaseListeners = new ArrayList<>();
        numberOfDays = 0;
        numberOfStudents = 0;
    }

    public ScheduleTimes getScheduleTimes() {
        return scheduleTimes;
    }

    public void addStudent(Student student) {

        student.setStudentID(numberOfStudents);  // 1. Student: ID = 0
        student.getStudentTimes().setStudentDays(); // StudentDayList mit gültigen Zeiteinträgen erstellen
        studentList.add(student);
        numberOfStudents = studentList.size(); // numberOfStudents++ für nächste StudentID
        for (DatabaseListener l : databaseListeners) {
            l.studentAdded(numberOfStudents);
        }
    }

    public ArrayList<Student> getStudentList() {
        return studentList;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public int getNumberOfStudents() {
        numberOfStudents = studentList.size();
        return numberOfStudents;
    }
    
     public void addDatabaseListener(DatabaseListener l) {
        databaseListeners.add(l);
    }
}
