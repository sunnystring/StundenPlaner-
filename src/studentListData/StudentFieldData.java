/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentListData;

import core.Student;
import studentlistUI.StudentList;

/**
 *
 * Model einer Zelle von {@link StudentList}
 */
public class StudentFieldData {

    private Student student;
    private String nameString;
    private String validTimeString;
    private boolean fieldSelected;
    private boolean studentListReleased;
    private boolean studentAllocated;
    private int selectedRowIndex;

    public StudentFieldData() {
        fieldSelected = false;
        studentListReleased = true;
        selectedRowIndex = -1;
        studentAllocated = false;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getNameString() {
        return nameString;
    }

    public void setNameString(String nameString) {
        this.nameString = nameString;
    }

    public void setValidTimeString(String validTimeString) {
        this.validTimeString = validTimeString;
    }

    public String getValidTimeString() {
        return validTimeString;
    }

    public boolean isFieldSelected() {
        return fieldSelected;
    }

    public void setFieldSelected(boolean fieldSelected) {
        this.fieldSelected = fieldSelected;
    }

    public void switchSelectionState() {
        fieldSelected = !fieldSelected;
    }

    public boolean isStudentAllocated() {
        return studentAllocated;
    }

    public boolean isStudentListReleased() {
        return studentListReleased;
    }

    public void setStudentListReleased(boolean studentListReleased) {
        this.studentListReleased = studentListReleased;
    }

    public void setStudentAllocated(boolean studentAllocated) {
        this.studentAllocated = studentAllocated;
    }

    public void setSelectedRowIndex(int selectedRowIndex) {
        this.selectedRowIndex = selectedRowIndex;
    }

    public int getSelectedRowIndex() {
        return selectedRowIndex;
    }
}
