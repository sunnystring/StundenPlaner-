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
    private ArrayList<Student> studentDataList;
    private ArrayList<DatabaseListener> databaseListeners;
    private int numberOfDays;
    private int numberOfStudents; 

    public Database() {
        scheduleTimes = new ScheduleTimes();
        studentDataList = new ArrayList<>();
        databaseListeners = new ArrayList<>();
        numberOfDays = 0;
        numberOfStudents = 0;
    }

    public ScheduleTimes getScheduleTimes() {
        return scheduleTimes;
    }

    public void addStudent(Student student) {
        student.setStudentID(numberOfStudents);  
        student.getStudentTimes().setValidStudentDays(); 
        studentDataList.add(student);
        numberOfStudents = studentDataList.size(); 
        for (DatabaseListener l : databaseListeners) {
            l.studentAdded(numberOfStudents, student);
        }
    }

    public ArrayList<Student> getStudentList() {
        return studentDataList;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

     public void addDatabaseListener(DatabaseListener l) {
        databaseListeners.add(l);
    }
}
