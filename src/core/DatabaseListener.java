/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import studentListData.StudentListData;
import studentlistUI.StudentList;

/**
 *
 * Passt {@link  StudentList} an bei Änderungen am Schülerdatenbestand
 */
public interface DatabaseListener {

    public void studentAdded(int numberOfStudents, Student student);

    public void studentEdited(Student student);

    public void studentDeleted(int numberOfStudents, int studentID);
}
