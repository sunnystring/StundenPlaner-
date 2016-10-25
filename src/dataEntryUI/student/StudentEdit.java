/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.student;

import core.Profile;
import dataEntryUI.DataEntryAndEdit;
import mainframe.MainFrame;

/**
 *
 * Bearbeiten oder Löschen eines Schülerprofils
 */
public class StudentEdit extends DataEntryAndEdit {

    private Profile student;

    public StudentEdit(MainFrame mainFrame, Profile student) {
        super(mainFrame, "Schülerprofil ändern oder löschen");
        this.student = student;
        setUpMask();
        pack();
    }

    @Override
    public void setUpMask() {
        studentInputMask.setProfile(student);
        studentInputMask.setupEditUI(this);
        studentInputMask.updateUpperEntryFields();
        studentInputMask.restoreLectiontypeSelectionBox();
        add(studentInputMask);
    }
}
