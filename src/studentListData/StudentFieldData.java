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

    private String nameString;
    private String validTimeString;
    private boolean fieldSelected;
    private boolean studentListEnabled;
    private boolean studentAllocated;
    private int selectedRowIndex;

    public StudentFieldData() {
        fieldSelected = false;
        studentListEnabled = true;
        selectedRowIndex = -1;
        studentAllocated = false;
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
