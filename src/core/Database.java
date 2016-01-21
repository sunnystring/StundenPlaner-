/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import controllers.DatabaseListener;
import java.util.ArrayList;

/**
 * 
 * Verwaltung aller relevanten Daten für die permanente Speicherung: 
 * Schülerdatenbestand, Unterrichtstage und -zeiten, 
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

    public void addStudent(Student student) {
        student.setStudentID(numberOfStudents);
        studentDataList.add(student);
        numberOfStudents = studentDataList.size();
        for (DatabaseListener l : databaseListeners) {
            l.studentAdded(numberOfStudents, student);
        }
    }

    public void editStudent(Student student) {
        for (DatabaseListener l : databaseListeners) {
            l.studentEdited(student);
        }
    }

    public void deleteStudent(Student student) {
        studentDataList.remove(student);
        numberOfStudents = studentDataList.size();
        for (DatabaseListener l : databaseListeners) {
            l.studentDeleted(numberOfStudents, student.getStudentID());
        }
        updateStudentIDs();
    }

    private void updateStudentIDs() {
        for (int i = 0; i < studentDataList.size(); i++) {
            studentDataList.get(i).setStudentID(i);
        }
    }

    public ScheduleTimes getScheduleTimes() {
        return scheduleTimes;
    }

    public ArrayList<Student> getStudentDataList() {
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
