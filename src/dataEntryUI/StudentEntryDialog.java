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
 * Eingabe eines Schülerprofils über {@link StudentEntryMask}
 */
public class StudentEntryDialog extends JDialog {

    public StudentEntryDialog(MainFrame mainFrame, Student student) {
        super(mainFrame);
        setModal(true);
        setTitle("Schülerprofil erstellen");
        setLocationRelativeTo(mainFrame);
        setResizable(false);
        StudentEntryMask studentEntryMask = mainFrame.getStudentEntryMask();
        studentEntryMask.addEntryButtons();
        studentEntryMask.addCancelButtonListener(this);
        studentEntryMask.addSaveButtonListener(this);
        studentEntryMask.setStudent(student);
        studentEntryMask.setAndInitStudentTimes();
        studentEntryMask.setSelectionTableParameters();
        add(studentEntryMask);
        pack();
    }
}
