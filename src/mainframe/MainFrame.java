/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainframe;

import core.Database;
import studentListData.StudentListData;
import dataEntryUI.StudentDataEntryMask;
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
import core.ScheduleTimes;
import core.Student;
import dataEntryUI.ScheduleDataEntry;
import dataEntryUI.ScheduleDataEntryMask;
import dataEntryUI.StudentDataEntry;
import javax.swing.JDialog;
import scheduleUI.Schedule;
import studentlistUI.StudentList;
import util.Icons;

/**
 *
 * @author Mathias
 */
public class MainFrame extends JFrame {

    private Database database;
    private ScheduleTimes scheduleTimes;
    private ScheduleData scheduleData;
    private StudentListData studentListData;
    private Schedule schedule;
    private StudentList studentList;
    private ScheduleDataEntryMask scheduleDataEntryMask;
    private StudentDataEntryMask studentDataEntryMask;
    private JPanel toolBar;
    private ScheduleButton openButton, saveButton, printButton, createScheduleButton, addStudentButton, addKGUButton, automaticButton;
    private JToggleButton timeFilterButton;
    private JSplitPane splitPane;
    private JScrollPane leftScroll, rightScroll;

    public MainFrame() {
        setTitle("StundenPlaner");
        setIconImage(Icons.getImage("table.png"));
        setExtendedState(Frame.MAXIMIZED_BOTH);
        database = new Database();
        scheduleTimes = database.getScheduleTimes();
        scheduleData = new ScheduleData(scheduleTimes);
        studentListData = new StudentListData(database, this);
        createWidgets();
        addWidgets();
        addListeners();
        setStudentButtonsEnabled(false);
        setMinimumSize(new Dimension(1000, 400));
        pack();
        setLocation(0, 0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void createSchedule() {
        scheduleData.setTableData();
        schedule = new Schedule(scheduleData, studentListData);
        leftScroll = new JScrollPane(schedule);
        leftScroll.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        splitPane.setLeftComponent(leftScroll);
    }

    public void prepareStudentList() {
        if (database.getNumberOfDays() > 0) {
            studentListData.initStudentListData(scheduleData);
            studentList = new StudentList(studentListData, schedule.getTimeTable());  // timeTable für Listener-Registrierung
            studentListData.setStudentList(studentList); // für Anzeige von numberOfStudents in studentList (HeaderField)
            setStudentButtonsEnabled(true);
            rightScroll = new JScrollPane(studentList);
            rightScroll.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            splitPane.setRightComponent(rightScroll);
        }
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
        automaticButton = new ScheduleButton("coffee.png", "Automatischer Einteilungsvorschlag machen");
        timeFilterButton = new JToggleButton(Icons.setIcon("color.png"));
        timeFilterButton.setToolTipText("Verteilung der Zeiten anzeigen: je später, desto dunkler");
        timeFilterButton.setPreferredSize(new Dimension(60, 0));
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JPanel(), new JPanel());
        splitPane.setContinuousLayout(true);
        splitPane.setResizeWeight(0.5);
        scheduleDataEntryMask = new ScheduleDataEntryMask(scheduleTimes);
        studentDataEntryMask = new StudentDataEntryMask(scheduleTimes);
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
        toolBar.add(automaticButton);
        toolBar.add(timeFilterButton);
    }

    private void addListeners() {
        database.addDatabaseListener(studentListData);
        createScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ScheduleDataEntry scheduleDataEntry = new ScheduleDataEntry(MainFrame.this, database);
                scheduleDataEntryMask.addListeners(scheduleDataEntry);
                scheduleDataEntry.add(scheduleDataEntryMask);
                scheduleDataEntry.pack();
                scheduleDataEntry.setVisible(true);
            }
        });
        addStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Student student = new Student();
                StudentDataEntry studentDataEntry = new StudentDataEntry(MainFrame.this, database);
                studentDataEntryMask.addListeners(studentDataEntry);
                studentDataEntry.add(studentDataEntryMask);
                studentDataEntry.pack();
                studentDataEntry.setVisible(true);
            }
        });
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

}
