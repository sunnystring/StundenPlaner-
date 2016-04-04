/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentListData;

import core.Database;
import core.Student;
import java.awt.Color;
import studentlistUI.StudentList;
import utils.Colors;

/**
 *
 * Model einer Zelle von {@link StudentList}
 */
public class StudentFieldData {

    private final Database database;
    private Color fieldColor;
    private int studentID;
    private String nameString;
    private String validTimeString;
    private boolean fieldSelected;
    private boolean studentListReleased; // globaler und lokaler Row-Schalter: falls lokal, studentListReleased != studentAllocated
    private boolean studentAllocated;
    private boolean incompatible;
    private boolean blocked;
    private int selectedRowIndex;

    public StudentFieldData(Database database) {
        this.database = database;
        reset();
    }

    public void reset() {
        fieldColor = Colors.BLUE_DEFAULT;
        fieldSelected = false;
        studentListReleased = true;
        selectedRowIndex = -1;
        studentAllocated = false;
        incompatible = false;
        blocked = false;
    }

    public void setFieldColor(Color fieldColor) {
        this.fieldColor = fieldColor;
    }

    public Color getFieldColor() {
        return fieldColor;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public Student getStudent() {
        return database.getStudent(studentID);
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

    public boolean isStudentListReleased() {
        return studentListReleased;
    }

    public void setStudentListReleased(boolean studentListReleased) {
        this.studentListReleased = studentListReleased;
    }

    public void setAllocationState(boolean studentAllocated) {
        this.studentAllocated = studentAllocated;
    }

    public boolean isStudentAllocated() {
        return studentAllocated;
    }

    public void setIncompatible(boolean incompatible) {
        this.incompatible = incompatible;
    }

    public boolean isIncompatible() {
        return incompatible;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void setSelectedRowIndex(int selectedRowIndex) {
        this.selectedRowIndex = selectedRowIndex;
    }

    public int getSelectedRowIndex() {
        return selectedRowIndex;
    }

}
