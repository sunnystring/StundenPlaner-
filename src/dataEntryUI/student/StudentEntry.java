/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.student;

import core.Student;
import dataEntryUI.DataEntryAndEdit;
import mainframe.MainFrame;

/**
 *
 * Eingabe eines neuen Schülerprofils
 */
public class StudentEntry extends DataEntryAndEdit {

    private Student student;

    public StudentEntry(MainFrame mainFrame, Student student) {
        super(mainFrame, "Schülerprofil erstellen");
        this.student = student;
        setUpMask();
        pack();
    }

    @Override
    public void setUpMask() {
        studentInputMask.setStudent(student);
        studentInputMask.setupEntryUI(this);
        add(studentInputMask);
    }
}
