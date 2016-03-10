/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import core.Student;
import javax.swing.JDialog;
import mainframe.MainFrame;
import studentlistUI.StudentList;

/**
 *
 * Eingabe eines neuen Schülerprofils
 */
public class StudentEntry extends DataEntryAndEdit {

    private Student student;

    public StudentEntry(MainFrame mainFrame, Student student) {
        super(mainFrame, "Neues Schülerprofil erstellen");
        this.student = student;
        setUpMask();
        pack();
    }

    @Override
    public void setUpMask() {
        studentInputMask.addEntryButtons();
        studentInputMask.addCancelButtonListener(this);
        studentInputMask.addSaveButtonListener(this);
        studentInputMask.setStudent(student);
        studentInputMask.setUpSelectionTable();
        add(studentInputMask);
    }
}
