/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import core.Database;
import core.Student;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import mainframe.MainFrame;

/**
 *
 * @author Mathias
 */
public class StudentDataEntry extends JDialog {

    Database database;
    private Student student;

    public StudentDataEntry(MainFrame owner, Database database) {
        super(owner);
        this.database = database;
        setLocationRelativeTo(owner);
        setModal(true);
        setTitle("Schülerprofil");
        //  setPreferredSize(new Dimension(500, 282));
        setResizable(false);
        //     studentDataEntryMask.addListeners(this);
        // pack();

    }

    public Student getStudent() {
        return student;
    }
    

    public class CancelButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            StudentDataEntry.this.dispose();
        }
    }

    public class SaveButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {  // Student mit Daten befüllen
            student.setFirstName(firstName);
            student.setName(name);
            student.setLectionLength(Integer.parseInt(lectionType));
            database.addStudent(student);
            StudentDataEntry.this.dispose();
        }
    }

    public class DeleteButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

}
