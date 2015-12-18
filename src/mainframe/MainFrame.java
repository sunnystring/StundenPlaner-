/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainframe;

import core.Database;
import studentListData.StudentListData;
import dataEntryUI.StudentEntryMask;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import scheduleData.ScheduleData;
import core.Student;
import dataEntryUI.ScheduleEntryDialog;
import dataEntryUI.ScheduleEntryMask;
import dataEntryUI.StudentEntryDialog;
import javax.swing.JDialog;
import scheduleUI.Schedule;
import studentlistUI.StudentList;
import util.Icons;

/**
 *
 * Erzeugung und Initialisierung der StundenPlaner-GUI
 */
public class MainFrame extends JFrame {

    private Database database;
    private ScheduleData scheduleData;
    private StudentListData studentListData;
    private Schedule schedule;
    private StudentList studentList;
    private ScheduleEntryMask scheduleEntryMask;
    private StudentEntryMask studentEntryMask;
    private JPanel toolBar;
    private ScheduleButton openButton, saveButton, printButton, createScheduleButton, addStudentButton, addKGUButton, automaticAllocationButton;
    private JToggleButton timeFilterButton;
    private JSplitPane splitPane;
    private JScrollPane leftScroll, rightScroll;

    public MainFrame() {
        setTitle("StundenPlaner");
        setIconImage(Icons.getImage("table.png"));
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000, 400));
        database = new Database();
        scheduleData = new ScheduleData(database.getScheduleTimes());
        studentListData = new StudentListData(database, this);
        createWidgets();
        addWidgets();
        addListeners();
        setStudentButtonsEnabled(false);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createWidgets() {
        toolBar = new JPanel();
        toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.LINE_AXIS));
        toolBar.setPreferredSize(new Dimension(0, 30));
        toolBar.setBorder(BorderFactory.createEmptyBorder(2, 12, 0, 15));
        openButton = new ScheduleButton("openFile.png", "Bestehender Stundenplan öffnen");
        saveButton = new ScheduleButton("disk.png", "Stundenplan und Schülerdaten speichern");
        printButton = new ScheduleButton("printer.png", "Stundenplan drucken");
        createScheduleButton = new ScheduleButton("calendar.png", "Stundenplan erstellen oder ändern");
        addStudentButton = new ScheduleButton("boy.png", "Schülerprofil erstellen");
        addKGUButton = new ScheduleButton("boy&girl.png", "Gruppen-Profil erstellen");
        automaticAllocationButton = new ScheduleButton("coffee.png", "Automatischer Einteilungsvorschlag machen");
        timeFilterButton = new JToggleButton(Icons.setIcon("color.png"));
        timeFilterButton.setToolTipText("Verteilung der Zeiten anzeigen: je später, desto dunkler");
        timeFilterButton.setPreferredSize(new Dimension(60, 0));
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JPanel(), new JPanel());
        splitPane.setContinuousLayout(true);
        splitPane.setResizeWeight(0.5);
        scheduleEntryMask = new ScheduleEntryMask(database);
        studentEntryMask = new StudentEntryMask(database);
    }

    private void addWidgets() {
        add(BorderLayout.CENTER, splitPane);
        add(BorderLayout.PAGE_START, toolBar);
        toolBar.add(openButton);
        toolBar.add(saveButton);
        toolBar.add(printButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(createScheduleButton);
        toolBar.add(addStudentButton);
        toolBar.add(addKGUButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(automaticAllocationButton);
        toolBar.add(timeFilterButton);
    }

    private void addListeners() {
        database.addDatabaseListener(studentListData);
        createScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JDialog scheduleEntryDialog = new ScheduleEntryDialog(MainFrame.this);
                scheduleEntryDialog.setVisible(true);
            }
        });
        addStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JDialog studentEntryDialog = new StudentEntryDialog(MainFrame.this, new Student());
                studentEntryMask.clearTextFields();
                studentEntryDialog.setVisible(true);
            }
        });
    }

    public void setScheduleData() {
        scheduleData.setTableData();
    }

    public void createSchedule() {
        schedule = new Schedule(scheduleData, studentListData);
        leftScroll = new JScrollPane(schedule);
        leftScroll.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        splitPane.setLeftComponent(leftScroll);
    }

    public void setStudentListData() {
        studentListData.setScheduleData(scheduleData);
        studentListData.setNumberOfDays();
    }

    public void createStudentList() {
        studentList = new StudentList(studentListData, schedule.getTimeTable());  // timeTable für Listener-Registrierung
        rightScroll = new JScrollPane(studentList);
        rightScroll.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        splitPane.setRightComponent(rightScroll);
    }

    public void setStudentListToStudentListData() {
        studentListData.setStudentList(studentList);
    }

    public void setStudentButtonsEnabled(boolean state) {
        addStudentButton.setEnabled(state);
        addKGUButton.setEnabled(state);
    }

    private class ScheduleButton extends JButton {

        public ScheduleButton(String iconName, String toolTip) {
            setIcon(Icons.setIcon(iconName));
            setToolTipText(toolTip);
            setPreferredSize(new Dimension(60, 0));
        }
    }

    public ScheduleEntryMask getScheduleEntryMask() {
        return scheduleEntryMask;
    }

    public StudentEntryMask getStudentEntryMask() {
        return studentEntryMask;
    }

}
