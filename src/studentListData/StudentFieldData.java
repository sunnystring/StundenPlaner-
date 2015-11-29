/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentListData;

/**
 *
 * @author Mathias
 */
public class StudentFieldData {

    // angezeigte Daten
    private String nameString;
    private String validTimeString;
    // GUI-Management
    private boolean fieldSelected; // Zustand eines StudentDay-Fields
    private boolean studentListEnabled; // entspricht globalem StudentList-Schalter
    private boolean studentAllocated; // markiert, wenn Schüler eingeteilt ist
    private int selectedRowIndex;  // speichert die temporär selektierte Row

    public StudentFieldData() {

        fieldSelected = false;
        studentListEnabled = true;
        selectedRowIndex = -1;
        studentAllocated = false;
    }

    // angezeigte Daten
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

    // GUI-Management
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

    public boolean isStudentListEnabled() {
        return studentListEnabled;
    }

    public void setStudentListEnabled(boolean studentListEnabled) {
        this.studentListEnabled = studentListEnabled;
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
