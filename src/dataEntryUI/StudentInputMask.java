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
import exceptions.IllegalTimeSlotException;
import exceptions.OutOfBoundException;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import mainframe.MainFrame;
import scheduleData.ScheduleTimeFrame;
import utils.Dialogs;

public class StudentInputMask extends JPanel {

    private Database database;
    private Student student;
    private ScheduleTimes scheduleTimes;
    private StudentTimes studentTimes;
    private String firstName, name;
    protected String lectionLength;
    protected String[] lectionTypes;
    protected JPanel top;
    private JPanel bottom;
    private JScrollPane center;
    protected JLabel firstnameLabel, nameLabel, lectiontypeLabel;
    private JLabel footnote;
    private JTextField firstnameField, nameField;
    protected JComboBox lectiontypeSelectionBox;
    protected ActionListener lectiontypeSelectionListener;
    private TimeSelectionTable timeSelectionTable;
    protected JButton cancelButton, saveButton, deleteButton;
    protected ActionListener cancelButtonListener, saveButtonListener, deleteButtonListener;

    public StudentInputMask(Database database) {
        this.database = database;
        scheduleTimes = database.getScheduleTimes();
        lectionLength = "30";
        lectionTypes = new String[]{"30", "40", "50", "KGU"};
        setLayout(new BorderLayout());
        createWidgets();
        addWidgets();
        addTextFieldListeners();
        createLectiontypeSelectionListener();
        addLectiontypeSelectionListener();
    }

    private void createWidgets() {
        timeSelectionTable = new TimeSelectionTable(scheduleTimes);
        top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
        top.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        center = new JScrollPane(timeSelectionTable, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        firstnameLabel = new JLabel("Vorname:");
        nameLabel = new JLabel("  Name:");
        lectiontypeLabel = new JLabel("  Lektionstyp:");
        footnote = new JLabel("* Beginn der Lektion");
        footnote.setFont(footnote.getFont().deriveFont(Font.PLAIN, 9));
        firstnameField = new JTextField(" ");
        nameField = new JTextField(" ");
        lectiontypeSelectionBox = new JComboBox(lectionTypes);
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
        top.add(lectiontypeSelectionBox);
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

    public void addEditButtons() {
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
    }

    public void createLectiontypeSelectionListener() {
        lectiontypeSelectionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox selectionBox = (JComboBox) e.getSource();
                lectionLength = (String) selectionBox.getSelectedItem();
//                if ("KGU".equals(type)) {
//                    lectionLength = "45";
//                } else 
                // {
                //      lectionLength = type;
                //  }
            }
        };
    }

    public void addLectiontypeSelectionListener() {
        lectiontypeSelectionBox.addActionListener(lectiontypeSelectionListener);
    }

    public void addCancelButtonListener(DataEntryAndEdit dataEntryAndEdit) {
        cancelButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             //   removeButtonsAndListeners();
                dataEntryAndEdit.dispose();
            }
        };
        cancelButton.addActionListener(cancelButtonListener);
    }

    public void addSaveButtonListener(DataEntryAndEdit dataEntryAndEdit) {
        ScheduleTimeFrame scheduleTimeFrame = dataEntryAndEdit.getScheduleTimeFrame();
        saveButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    studentTimes.checkAndCorrectTimeEntries();
                    studentTimes.initAndCheckScheduleBounds(scheduleTimeFrame, getLectionLengthInFields());
                } catch (IllegalTimeSlotException ex) {
                    Dialogs.showStudentTimeSlotError();
                    return;
                } catch (OutOfBoundException ex) {
                    showAndCorrectInvalidEntryTimes(ex.getMessage(), dataEntryAndEdit.getMainFrame());
                    return;
                }
                studentTimes.setValidStudentDays();
                setStudentData();
                database.addStudent(student);
             //   removeButtonsAndListeners();
                dataEntryAndEdit.dispose();
            }
        };
        saveButton.addActionListener(saveButtonListener);
    }

    public void addEditSaveButtonListener(DataEntryAndEdit dataEntryAndEdit) {
        ScheduleTimeFrame scheduleTimeFrame = dataEntryAndEdit.getScheduleTimeFrame();
        saveButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    studentTimes.checkAndCorrectTimeEntries();
                    studentTimes.initAndCheckScheduleBounds(scheduleTimeFrame, getLectionLengthInFields());
                } catch (IllegalTimeSlotException ex) {
                    Dialogs.showStudentTimeSlotError();
                    return;
                } catch (OutOfBoundException ex) {
                    showAndCorrectInvalidEntryTimes(ex.getMessage(), dataEntryAndEdit.getMainFrame());
                    return;
                }
                studentTimes.updateValidStudentDays();
                setStudentData();
                database.editStudent(student);
             //   removeButtonsAndListeners();
                dataEntryAndEdit.dispose();
            }
        };
        saveButton.addActionListener(saveButtonListener);
    }

    private void showAndCorrectInvalidEntryTimes(String msg, MainFrame mainFrame) {
        int choice = Dialogs.showStudentTimesOutOfBoundOptionMessage(msg);
        if (choice == JOptionPane.YES_OPTION) // Stundenplan anpassen, falls NO_OPTION Schülerzeit anpassen
        {
            JDialog scheduleEdit = new ScheduleEdit(mainFrame);
            scheduleEdit.setVisible(true);
        }
    }

    public void addDeleteButtonListener(StudentEdit studentEdit) {
        deleteButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.deleteStudent(student);
              //  removeButtonsAndListeners();
                studentEdit.dispose();
            }
        };
        deleteButton.addActionListener(deleteButtonListener);
    }

    protected void removeButtonsAndListeners() {
        cancelButton.removeActionListener(cancelButtonListener);
        saveButton.removeActionListener(saveButtonListener);
        deleteButton.removeActionListener(deleteButtonListener);
        bottom.remove(cancelButton);
        bottom.remove(saveButton);
        bottom.remove(deleteButton);
    }

    public void setUpTimeSelectionTable() {
        studentTimes = student.getStudentTimes();
        timeSelectionTable.setParameters(studentTimes);
    }

    private void setStudentData() {
        student.setFirstName(firstName);
        student.setName(name);
        student.setLectionLengthInMinutes(Integer.parseInt(lectionLength));
    }

    public void clearUpperEntryFields() {
        firstnameField.setText("");
        nameField.setText("");
        lectiontypeSelectionBox.setSelectedIndex(0);
    }

    public void updateUpperEntryFields() {
        firstnameField.setText(student.getFirstName());
        nameField.setText(student.getName());
        lectionLength = String.valueOf(student.getLectionLengthInMinutes());
        updateLectiontypeSelectionBox();
    }

    private void updateLectiontypeSelectionBox() {
        switch (lectionLength) {
            case "30":
                lectiontypeSelectionBox.setSelectedIndex(0);
                break;
            case "40":
                lectiontypeSelectionBox.setSelectedIndex(1);
                break;
            case "50":
                lectiontypeSelectionBox.setSelectedIndex(2);
                break;
            case "45":
                lectiontypeSelectionBox.setSelectedIndex(3);
                break;
            default:
                lectiontypeSelectionBox.setSelectedIndex(0);
                break;
        }
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    private int getLectionLengthInFields() {
        return Integer.parseInt(lectionLength) / 5;
    }
}
