/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author Mathias
 */
public class Student {

    private String firstName;
    private String name;
    private StudentTimes studentTimes;
    private int lectionLength;  // Anzahl Lection-/TimeFields
    private int studentID;

    public Student() {
        studentTimes = new StudentTimes();
    }

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

    public int getLectionLength() {
        return lectionLength;
    }

    public void setLectionLength(int lectionLengthInMinutes) {
        lectionLength = lectionLengthInMinutes / 5;
    }

    public StudentDay getStudentDay(int index) {
        return studentTimes.getStudentDay(index);
    }

}
