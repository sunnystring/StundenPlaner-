/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import core.Student;
import javax.swing.JDialog;
import mainframe.MainFrame;

/**
 *
 * Bearbeiten oder Löschen eines Schülerprofils
 */
public class StudentEditDialog extends JDialog {

    public StudentEditDialog(MainFrame mainFrame, Student student) {
        super(mainFrame);
        setModal(true);
        setTitle("Schülerprofil ändern oder löschen");
        setLocationRelativeTo(mainFrame);
        setResizable(false);
        StudentEntryMask studentEntryMask = mainFrame.getStudentEntryMask();
        studentEntryMask.addChangeAndDeleteButtons();
        studentEntryMask.addCancelButtonListener(this);
        studentEntryMask.addChangeButtonListener(this);
        studentEntryMask.addDeleteButtonListener(this);
        studentEntryMask.setStudent(student);
        studentEntryMask.setUpSelectionTable();
        studentEntryMask.updateTextFields();
        add(studentEntryMask);
        pack();
    }

}
