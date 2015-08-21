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
/* Schülerdaten */
public class Student {

    private String firstName;
    private String name;

    private StudentDay[] studentList;  //1. Tag = 0, 2. Tag = 1 usw.  

    private int lectionType;  // Anzahl Lection- bzw. TimeFields (= 5 Min.)

    private int studentListIndex;  // Position in der Schülerdaten-Liste = Student-ID

    public Student(String firstName, String name, StudentDay day0, StudentDay day1, StudentDay day2, int lectionLength) {

        this.firstName = firstName;
        this.name = name;

        lectionType = lectionLength / 5;

        /* -----------Rohfassung -> später dynamisch ----------------------------*/
        studentList = new StudentDay[DataBase.getNumberOfDays()];

        studentList[0] = day0;  // 1. Unterrichtstag
        studentList[0].setLectionLength(lectionLength);  // Lektionsdauer wird später gebraucht
        studentList[1] = day1;  // usw.
        studentList[1].setLectionLength(lectionLength);
        studentList[2] = day2;
        studentList[2].setLectionLength(lectionLength);

        /*--------------------------------------------------------------*/
    }

    public void setStudentIndex(int index) {
        studentListIndex = index;
    }

    public int getStudentIndex() {
        return studentListIndex;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getName() {
        return name;
    }

    public StudentDay getStudentDay(int index) {
        return studentList[index];
    }

    public int getLectionType() {
        return lectionType;
    }

    public void setLectionType(int lectionType) {
        this.lectionType = lectionType;
    }

    /* -----------------Rohfassung: Referenz auf studentDayList */
    public StudentDay[] getStudentDayList() {
        return studentList;
           }
}
