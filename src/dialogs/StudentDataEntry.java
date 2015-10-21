/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dialogs;

import core.Student;
import core.StudentTimes;
import core2.StudentData;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableCellRenderer;
import mainframe.MainFrame;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class StudentDataEntry extends JDialog {
    
    private String firstName, name, lectionType = "30";
    
    private StudentData studentData;
    private Student student;
    private StudentTimes studentTimes;
    
    private JPanel top, bottom;
    private JScrollPane center;
    private JLabel firstnameLabel, nameLabel, lectiontypeLabel, footnote;
    private JTextField firstnameField, nameField, lectiontypeField;
    private JTable selectionTable;
    private JButton cancelButton, saveButton, deleteButton;
    
    public StudentDataEntry(StudentData studentData, MainFrame owner) {
        
        super(owner);
        this.studentData = studentData;  // Referenz gebraucht für addStudent()

        student = new Student();
        studentTimes = student.getStudentTimes();  // StudentTimes ist TableModel für SelectionTable
        studentTimes.setScheduleTimes(studentData.getScheduleTimes());  // studentTimes braucht scheduleTimes

        setLocationRelativeTo(owner);
        setModal(true);
        setTitle("Schülerprofil");
        setPreferredSize(new Dimension(500, 282));
        setResizable(false);
        createWidgets();
        addWidgets();
        addListener();
        pack();
        
    }
    
    private void createWidgets() {
        
        selectionTable = new JTable(studentTimes);
        selectionTable.setShowGrid(true);
        selectionTable.getColumnModel().setColumnSelectionAllowed(true); //  in alle Zellen kann geschrieben werden
        selectionTable.setFillsViewportHeight(true);
        selectionTable.setRowHeight(25);
        selectionTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object o, boolean bln, boolean bln1, int row, int col) {
                
                if (studentData.getScheduleTimes().isValidScheduleDay(row)) {
                    setBackground(Colors.BACKGROUND);
                } else {
                    setBackground(Colors.LIGHT_GRAY);
                }
                setText(table.getModel().getValueAt(row, col).toString());
                return this;
            }
        });
        
        top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
        top.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        center = new JScrollPane(selectionTable, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        firstnameLabel = new JLabel("Vorname:");
        nameLabel = new JLabel("  Name:");
        lectiontypeLabel = new JLabel("  Lektionsdauer:");
        footnote = new JLabel("* Beginn der Lektion");
        
        footnote.setFont(footnote.getFont().deriveFont(Font.PLAIN, 9));
        
        firstnameField = new JTextField(" ");
        nameField = new JTextField(" ");
        lectiontypeField = new JTextField(lectionType);
        lectiontypeField.setMaximumSize(lectiontypeField.getPreferredSize());
        
        cancelButton = new JButton("Abbrechen");
        saveButton = new JButton("Speichern");
        deleteButton = new JButton("Profil löschen");
        
    }
    
    private void addWidgets() {
        
        top.add(firstnameLabel);
        top.add(firstnameField);
        top.add(Box.createHorizontalGlue());
        top.add(nameLabel);
        top.add(nameField);
        top.add(Box.createHorizontalGlue());
        top.add(lectiontypeLabel);
        top.add(lectiontypeField);
        
        bottom.add(footnote);
        bottom.add(Box.createHorizontalGlue());
        bottom.add(cancelButton);
        bottom.add(deleteButton);
        bottom.add(saveButton);
        
        add(BorderLayout.PAGE_START, top);
        add(BorderLayout.CENTER, center);
        add(BorderLayout.PAGE_END, bottom);
    }
    
    private void addListener() {
        
        firstnameField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent ce) {
                JTextField f = (JTextField) ce.getSource();
                firstName = f.getText();  // ToDo IllegalArg. Except.
            }
        });
        nameField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent ce) {
                JTextField f = (JTextField) ce.getSource();
                name = f.getText();  // ToDo IllegalArg. Except.
            }
        });
        lectiontypeField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent ce) {
                
                JTextField f = (JTextField) ce.getSource();  // ToDo IllegalArg. Except.
                lectionType = (f.getText().trim().isEmpty()) ? lectiontypeField.getText() : f.getText();
            }
        });
        cancelButton.addActionListener(new CancelButtonListener());
        saveButton.addActionListener(new SaveButtonListener());
        deleteButton.addActionListener(new DeleteButtonListener());
        
    }
    
    private class CancelButtonListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
            StudentDataEntry.this.dispose();
        }
    }
    
    private class SaveButtonListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {  // Student mit Daten befüllen

            student.setFirstName(firstName);
            student.setName(name);
            student.setLectionType(Integer.parseInt(lectionType));
            studentData.addStudent(student);
            StudentDataEntry.this.dispose();
        }
    }
    
    private class DeleteButtonListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
        }
    }
}
