/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import javax.swing.table.TableModel;
import util.Time;

/**
 *
 * @author Mathias
 */
/* Schülerdaten */
public class Student {

    private String firstName;
    private String name;
    private StudentTimes times; // implements TableModel
    private int lectionType;  // Anzahl Lection- bzw. TimeFields (= 5 Min.)

    private int studentListIndex;  // Position in der Schülerdaten-Liste = Student-ID -> unused

    private StudentDay[] dayList;  //statische Demoversion: 1. Tag = 0, 2. Tag = 1 usw.  

    public Student() {

        times = new StudentTimes();

    }

    /* statische Demoversion */
    public Student(String firstName, String name, StudentDay day0, StudentDay day1, StudentDay day2, int lectionLength) {

        this.firstName = firstName;
        this.name = name;
        this.lectionType = lectionLength / 5;

        dayList = new StudentDay[DataBase.getNumberOfDays()];

        dayList[0] = day0;  // 1. Unterrichtstag
        dayList[0].setLectionLength(lectionLength);  // Lektionsdauer wird später gebraucht
        dayList[1] = day1;  // usw.
        dayList[1].setLectionLength(lectionLength);
        dayList[2] = day2;
        dayList[2].setLectionLength(lectionLength);

        /*--------------------------------------------------------------*/
    }

    public TableModel getStudentTimeModel() {
        return times;
    }

    public Time getStudentTime(int row, int col) {
        return times.getTimeSlot(row, col);
    }

    // ---------------------------------------------
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
        return dayList[index];
    }

    public int getLectionType() {
        return lectionType;
    }

    public void setLectionType(int lectionType) {
        this.lectionType = lectionType;
    }

    /* -----------------Rohfassung: Referenz auf studentDayList */
    public StudentDay[] getStudentDayList() {
        return dayList;
    }
}
