/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.ArrayList;

/**
 *
 * @author mathiaskielholz
 */
public class Profile {

    private String firstName;
    private String name;
    private StudentTimes studentTimes;
    private int lectionLengthInMinutes;
    private int profileID;
    private boolean allocated;
    private int profileType;
    private String profileName;
    private ArrayList<Integer> KGUMemberIDs;

    public Profile() {
        studentTimes = new StudentTimes();
        KGUMemberIDs = new ArrayList<>();
        allocated = false;
    }

    public void setStudentTimes(StudentTimes studentTimes) {
        this.studentTimes = studentTimes;
    }

    public StudentTimes getStudentTimes() {
        return studentTimes;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public int getProfileID() {
        return profileID;
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

    public boolean getDaySelectionStateAt(int dayIndex) {
        return !studentTimes.getValidStudentDay(dayIndex).isEmpty() && studentTimes.getNumberOfSelectedDays() == 1;
    }

    public void setKGUMemberID(int KGUMemberID) {
        KGUMemberIDs.add(KGUMemberID);
    }

    public ArrayList<Integer> getKGUMemberIDs() {
        return KGUMemberIDs;
    }

}
