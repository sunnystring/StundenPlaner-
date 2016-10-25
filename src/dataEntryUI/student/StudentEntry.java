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
 * Eingabe eines neuen Schülerprofils
 */
public class StudentEntry extends DataEntryAndEdit {

    private Profile student;

    public StudentEntry(MainFrame mainFrame, Profile student) {
        super(mainFrame, "Schülerprofil erstellen");
        this.student = student;
        setUpMask();
        pack();
    }

    @Override
    public void setUpMask() {
        studentInputMask.setProfile(student); // setProfile
        studentInputMask.setupEntryUI(this);
        studentInputMask.clearUpperEntryFields();
        add(studentInputMask);
    }
}
