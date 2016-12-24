/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.ArrayList;
import static core.ProfileTypes.*;

/**
 *
 * Datenbestand eines Schülers bzw einer Gruppe
 */
public class Profile {

    private String firstName;
    private String name;
    private String thirdName;
    private StudentTimes studentTimes;
    private int lectionLengthInMinutes;
    private int ID;
    private boolean allocated;
    private int profileType;
    private String profileName;
    private ArrayList<Integer> KGUMemberIDs;

    public Profile() {
        studentTimes = new StudentTimes();
        KGUMemberIDs = new ArrayList<>();
        firstName = "";
        name = "";
        thirdName = "";
        allocated = false;
    }

    public void setStudentTimes(StudentTimes studentTimes) {
        this.studentTimes = studentTimes;
    }

    public StudentTimes getStudentTimes() {
        return studentTimes;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setProfileType(int profileType) {
        this.profileType = profileType;
    }

    public int getProfileType() {
        return profileType;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileName() {
        return profileName;
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

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public int getLectionLengthInFields() {
        return lectionLengthInMinutes / 5;
    }

    public int getLectionLengthInMinutes() {
        return lectionLengthInMinutes;
    }

    public void setLectionLengthInMinutes(int lectionLengthInMinutes) {
        this.lectionLengthInMinutes = lectionLengthInMinutes;
    }

    public StudentDay getStudentDay(int dayIndex) {
        return studentTimes.getValidStudentDay(dayIndex);
    }

    public void setAllocated(boolean lectionAllocated) {
        this.allocated = lectionAllocated;
    }

    public boolean isAllocated() {
        return allocated;
    }

    public boolean isStudent() {
        return profileType != REGULAR_GROUP && profileType != SDG;
    }

    public boolean isSelectableMemberGroup() {
        return profileName.equals(KGU_NAME) || profileName.equals(SDG_NAME);
    }

    public boolean isRegularGroup() {
        return profileType == REGULAR_GROUP && !isSelectableMemberGroup();
    }

    public boolean getDaySelectionStateAt(int dayIndex) {
        return !studentTimes.getValidStudentDay(dayIndex).isEmpty() && studentTimes.getNumberOfSelectedDays() == 1;
    }

    public void addKGUMemberID(int KGUMemberID) {
        KGUMemberIDs.add(KGUMemberID);
    }

    public ArrayList<Integer> getMemberIDs() {
        return KGUMemberIDs;
    }

}
