/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * Schülerprofil
 */
public class Student {

    private String firstName;
    private String name;
    private StudentTimes studentTimes;
    private int lectionLengthInMinutes;
    private int studentID;

    public Student() {
        studentTimes = new StudentTimes();
    }

    public void setStudentTimes(StudentTimes studentTimes) {
        this.studentTimes = studentTimes;
    }

    public StudentTimes getStudentTimes() {
        return studentTimes;
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
        return lectionLengthInMinutes / 5;
    }

    public int getLectionLengthInMinutes() {
        return lectionLengthInMinutes;
    }

    public void setLectionLengthInMinutes(int lectionLengthInMinutes) {
        this.lectionLengthInMinutes = lectionLengthInMinutes;
    }

    public StudentDay getStudentDay(int index) {
        return studentTimes.getValidStudentDay(index);
    }

}
