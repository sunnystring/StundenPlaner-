/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import dataEntryUI.ProfileNames;

/**
 *
 * @author mathiaskielholz
 */
public class Profile {

    protected String firstName;
    protected String name;
    protected StudentTimes studentTimes;
    protected int lectionLengthInMinutes;
    protected int profileID;
    protected boolean allocated;
    protected String profileName;
  //  protected boolean singleLectionMode;

    public Profile() {
        studentTimes = new StudentTimes();
        allocated = false;
        profileName = ProfileNames.SINGLE;
      //  singleLectionMode = true;
    }

    public void setStudentTimes(StudentTimes studentTimes) {
        this.studentTimes = studentTimes;
    }

    public StudentTimes getStudentTimes() {
        return studentTimes;
    }

    public void setID(int profileID) {
        this.profileID = profileID;
    }

    public int getID() {
        return profileID;
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

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileName() {
        return profileName;
    }

//    public void setProfileMode(boolean profileMode) {
//        this.singleLectionMode = profileMode;
//    }
//
//    public boolean isSingleLectionMode() {
//        return singleLectionMode;
//    }

}
