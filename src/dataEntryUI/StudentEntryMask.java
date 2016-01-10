/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import core.Database;
import core.ScheduleTimes;
import core.Student;
import core.StudentTimes;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 *
 * UI, das von {@link StudentEntryDialog} und {@link StudentEditDialog} benutzt
 * wird
 */
public class StudentEntryMask extends JPanel {

    private Database database;
    private Student student;
    private ScheduleTimes scheduleTimes;
    private StudentTimes studentTimes;
    private String firstName, name;
    private String lectionType = "30";
    private JPanel top, bottom;
    private JScrollPane center;
    private JLabel firstnameLabel, nameLabel, lectiontypeLabel, footnote;
    private JTextField firstnameField, nameField, lectiontypeField;
    private SelectionTable selectionTable;
    private JButton cancelButton, saveButton, deleteButton;
    private ActionListener cancelButtonListener, saveButtonListener, deleteButtonListener, changeButtonListener;

    public StudentEntryMask(Database database) {
        this.database = database;
        scheduleTimes = database.getScheduleTimes();
        setLayout(new BorderLayout());
        createWidgets();
        addWidgets();
        addTextFieldListeners();
    }

    private void createWidgets() {
        selectionTable = new SelectionTable(scheduleTimes);
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
        cancelButton = new JButton("abbrechen");
        saveButton = new JButton();
        deleteButton = new JButton();
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
        add(BorderLayout.PAGE_START, top);
        add(BorderLayout.CENTER, center);
        add(BorderLayout.PAGE_END, bottom);
    }

    public void addEntryButtons() {
        bottom.add(cancelButton);
        saveButton.setText("Profil speichern");
        bottom.add(saveButton);
    }

    public void addChangeAndDeleteButtons() {
        bottom.add(cancelButton);
        deleteButton.setText("Profil löschen");
        bottom.add(deleteButton);
        saveButton.setText("Profil ändern");
        bottom.add(saveButton);
    }

    private void addTextFieldListeners() {
        firstnameField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent ce) {
                JTextField f = (JTextField) ce.getSource();
                firstName = f.getText();
            }
        });
        firstnameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                firstnameField.selectAll();
            }
        });
        nameField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent ce) {
                JTextField f = (JTextField) ce.getSource();
                name = f.getText();
            }
        });
        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                nameField.selectAll();
            }
        });
        lectiontypeField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent ce) {
                JTextField f = (JTextField) ce.getSource();
                lectionType = (f.getText().trim().isEmpty()) ? lectiontypeField.getText() : f.getText();
            }
        });
        lectiontypeField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                lectiontypeField.selectAll();
            }
        });
        lectiontypeField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JTextField field = (JTextField) input;
                return field.getText().trim().matches("[34][0]");
            }

            @Override
            public boolean shouldYieldFocus(JComponent input) {
                boolean valid = verify(input);
                if (!valid) {
                    JOptionPane.showMessageDialog(null, "Lektionslänge oder Zeitformat ungültig:\n"
                            + "Nur 30 oder 40 Minuten möglich!\n"
                            );
                }
                return valid;
            }
        });
    }

    public void addCancelButtonListener(StudentEntryDialog studentEntryDialog) {
        cancelButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                studentEntryDialog.dispose();
                removeButtonListeners();
                removeButtons();
            }
        };
        cancelButton.addActionListener(cancelButtonListener);
    }

    public void addCancelButtonListener(StudentEditDialog studentEditDialog) {
        cancelButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                studentEditDialog.dispose();
                removeButtonListeners();
                removeButtons();
            }
        };
        cancelButton.addActionListener(cancelButtonListener);
    }

    public void addSaveButtonListener(StudentEntryDialog studentEntryDialog) {
        saveButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!studentTimes.areTimeEntriesValid()) {
                    return;
                }
                studentTimes.setValidStudentDays();
                transferEntryDataToStudent();
                database.addStudent(student);
                studentEntryDialog.dispose();
                removeButtonListeners();
                removeButtons();
            }
        };
        saveButton.addActionListener(saveButtonListener);
    }

    public void addChangeButtonListener(StudentEditDialog studentEditDialog) {
        changeButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!studentTimes.areTimeEntriesValid()) {
                    return;
                }
                studentTimes.updateValidStudentDays();
                transferEntryDataToStudent();
                database.editStudent(student);
                studentEditDialog.dispose();
                removeButtonListeners();
                removeButtons();
            }
        };
        saveButton.addActionListener(changeButtonListener);
    }

    public void addDeleteButtonListener(StudentEditDialog studentEditDialog) {
        deleteButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.deleteStudent(student);
                studentEditDialog.dispose();
                removeButtonListeners();
                removeButtons();
            }
        };
        deleteButton.addActionListener(deleteButtonListener);
    }

    public void removeButtons() {
        bottom.remove(cancelButton);
        bottom.remove(saveButton);
        bottom.remove(deleteButton);
    }

    public void removeButtonListeners() {
        cancelButton.removeActionListener(cancelButtonListener);
        saveButton.removeActionListener(saveButtonListener);
        deleteButton.removeActionListener(deleteButtonListener);
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setUpSelectionTable() {
        studentTimes = student.getStudentTimes();
        studentTimes.setScheduleTimes(scheduleTimes);
        selectionTable.setParameters(studentTimes);
    }

    private void transferEntryDataToStudent() {
        student.setFirstName(firstName);
        student.setName(name);
        student.setLectionLengthInMinutes(Integer.parseInt(lectionType));
        student.setStudentTimes(studentTimes);
    }

    public void clearTextFields() {
        firstnameField.setText("");
        nameField.setText("");
        lectiontypeField.setText("30");
    }

    public void updateTextFields() {
        firstnameField.setText(student.getFirstName());
        nameField.setText(student.getName());
        lectiontypeField.setText(String.valueOf(student.getLectionLengthInMinutes()));
    }
}