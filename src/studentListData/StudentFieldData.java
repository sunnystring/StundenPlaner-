/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentListData;

import core.Database;
import core.Student;
import core.StudentDay;
import java.awt.Color;
import studentlistUI.StudentList;
import utils.Colors;
import static studentListData.StudentListData.NULL_VALUE;

/**
 *
 * Model einer Zelle von {@link StudentList}
 */
public class StudentFieldData {

    private final Database database;
    private Color fieldColor;
    private int studentID;
    private int dayIndex;
    private String nameString;
    private String validTimeString;
    private boolean fieldSelected;
    private boolean studentAllocated;
    private boolean incompatible;
    private boolean blocked;
    private boolean unallocatable;
    private boolean singleDay;
    private boolean lectionGapFiller;
    private int selectedRowIndex;

    public StudentFieldData(Database database) {
        this.database = database;
        reset();
    }

    public void reset() {
        fieldColor = Colors.BLUE_DEFAULT;
        fieldSelected = false;
        selectedRowIndex = NULL_VALUE;
        dayIndex = dayIndex = NULL_VALUE;
        studentAllocated = false;
        incompatible = false;
        blocked = false;
        unallocatable = false;
        lectionGapFiller = false;
        singleDay = true;
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

    public int getStudentID() {
        return studentID;
    }

    public Student getStudent() {
        return database.getStudent(studentID);
    }

    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public StudentDay getStudentDay() {
        return getStudent().getStudentDay(dayIndex);
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

    public void setStudentAllocated(boolean studentAllocated) {
        this.studentAllocated = studentAllocated;
    }

    public void setSelectedRowIndex(int selectedRowIndex) {
        this.selectedRowIndex = selectedRowIndex;
    }

    public int selectedRowIndex() {
        return selectedRowIndex;
    }

    public boolean isIncompatible() {
        return incompatible;
    }

    public void setIncompatible(boolean incompatible) {
        this.incompatible = incompatible;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isUnallocatable() {
        return unallocatable;
    }

    public void setUnallocatable(boolean unAllocatable) {
        this.unallocatable = unAllocatable;
    }

    public boolean isSingleDay() {
        return singleDay;
    }

    public void setSingleDay(boolean singleDay) {
        this.singleDay = singleDay;
    }

    public boolean isLectionGapFiller() {
        return lectionGapFiller;
    }

    public void setLectionGapFiller(boolean lectionGapFillerMark) {
        this.lectionGapFiller = lectionGapFillerMark;
    }
}
