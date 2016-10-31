/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentListData;

import core.Database;
import core.Profile;
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
    private int profileID;
    private int lectionProfileType;
    private int dayIndex;
    private String nameString;
    private String validTimeString;
    private boolean fieldSelected;
    private boolean profileAllocated;
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
        profileAllocated = false;
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

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public int getProfileID() {
        return profileID;
    }

    public void setLectionProfileType(int lectionProfileType) {
        this.lectionProfileType = lectionProfileType;
    }

    public int getLectionProfileType() {
        return lectionProfileType;
    }

    public Profile getProfile() {
        return database.getProfile(profileID);
    }

    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public StudentDay getStudentDay() {
        return getProfile().getStudentDay(dayIndex);
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

    public boolean isProfileAllocated() {
        return profileAllocated;
    }

    public void setProfileAllocated(boolean profileAllocated) {
        this.profileAllocated = profileAllocated;
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
