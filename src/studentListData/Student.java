/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentListData;

import studentListData.StudentDay;
import studentListData.StudentTimes;

/**
 *
 * @author Mathias
 */
/* Schülerdaten */
public class Student {

    private String firstName;
    private String name;
    private StudentTimes studentTimes; // implements TableModel
    private int lectionType;  // Achtung: konvertierte Grösse = Anzahl Lection- bzw. TimeFields (= 5 Min.)
    private int studentID; 

    public Student() {
        
        studentTimes  = new StudentTimes();
    }

    /* Getter, Setter */
    public StudentTimes getStudentTimes() {
        return studentTimes;
    }

    public void setStudentTimes(StudentTimes studentTimes) {
        this.studentTimes = studentTimes;
    }

    public void setStudentID(int index) {
        studentID = index;
    }

    public int getStudentID() {
        return studentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLectionType() {
        return lectionType;
    }

    public void setLectionType(int lectionLength) {  
        lectionType = lectionLength / 5;  // nicht Minuten, sondern Anzahl Fields à 5 Min.
    }

    public StudentDay getStudentDay(int index) {
        return studentTimes.getStudentDay(index);
    }

}
