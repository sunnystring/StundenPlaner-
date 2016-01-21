/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import core.ScheduleTimes;
import core.Student;
import core.StudentTimes;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableCellRenderer;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class StudentDataEntryMask extends JPanel {

   // private Database dataBase;
    ScheduleTimes scheduleTimes;
 //   private Student student;
 //   private StudentTimes studentTimes;
 //   private String firstName, name;
    private String lectionType;
    private JPanel top, bottom;
    private JScrollPane center;
    private JLabel firstnameLabel, nameLabel, lectiontypeLabel, footnote;
    private JTextField firstnameField, nameField, lectiontypeField;
    private JTable selectionTable;
    private JButton cancelButton, saveButton, deleteButton;

    public StudentDataEntryMask(ScheduleTimes scheduleTimes) {
      //  super(owner);
        this.scheduleTimes = scheduleTimes;
    //    student = new Student();
        studentTimes = student.getStudentTimes();
        studentTimes.setScheduleTimes(scheduleTimes);
        lectionType = "30"; 
//        setLocationRelativeTo(owner);
//        setModal(true);
//        setTitle("Schülerprofil");
//        setPreferredSize(new Dimension(500, 282));
//        setResizable(false);
        createWidgets();
        addWidgets();
    //    addListeners();
//        pack();

    }

    private void createWidgets() {
        selectionTable = new JTable(studentTimes);
        selectionTable.setShowGrid(true);
        selectionTable.getColumnModel().setColumnSelectionAllowed(true); //  in alle Zellen kann geschrieben werden
        selectionTable.setFillsViewportHeight(true);
        selectionTable.setRowHeight(25);
        selectionTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object o, boolean isSelected, boolean hasFocus, int row, int col) {
                setText(table.getModel().getValueAt(row, col).toString());
                if (scheduleTimes.isValidScheduleDay(row)) {
                    setBackground(Colors.BACKGROUND);
                } else {
                    setBackground(Colors.LIGHT_GRAY);
                }
                if (isSelected && col > 0) {
                    setBackground(Colors.DARK_GREEN);
                }
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

    public void addListeners(StudentDataEntry studentDataEntry) {
        
        firstnameField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent ce) {
                JTextField f = (JTextField) ce.getSource();
                firstName = f.getText();  
            }
        });
        nameField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent ce) {
                JTextField f = (JTextField) ce.getSource();
                name = f.getText();  
            }
        });
        lectiontypeField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent ce) {
                JTextField f = (JTextField) ce.getSource();  
                lectionType = (f.getText().trim().isEmpty()) ? lectiontypeField.getText() : f.getText();
            }
        });
        cancelButton.addActionListener(studentDataEntry.new CancelButtonListener());
        saveButton.addActionListener(studentDataEntry.new SaveButtonListener());
        deleteButton.addActionListener(studentDataEntry.new DeleteButtonListener());
    }

//    private class CancelButtonListener implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//
//            StudentDataEntryMask.this.dispose();
//        }
//    }
//
//    private class SaveButtonListener implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {  // Student mit Daten befüllen
//
//            student.setFirstName(firstName);
//            student.setName(name);
//            student.setLectionLength(Integer.parseInt(lectionType));
//            dataBase.addStudent(student);
//            StudentDataEntryMask.this.dispose();
//        }
//    }
//
//    private class DeleteButtonListener implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//
//        }
//    }
}
