/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainframe;

import core2.ScheduleData;
import core2.StudentData;
import dialogs.ScheduleDataEntry;
import dialogs.StudentDataEntry;
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
import schedule2.Schedule2;
import schedule2.TimeTable_Only;
import studentlist2.StudentList2;
import util.Icons;

/**
 *
 * @author Mathias
 */
public class MainFrame extends JFrame { // alte Version: implements DatabaseListener 

    //  private final DataBase database;
    private final ScheduleData scheduleData;
    private final StudentData studentData;
    private final WidgetInteraction widgetInteraction;  // Model für globale Schalter GUI-Management
    private Schedule2 schedule;
    private StudentList2 studentList;
    private JPanel toolBar;
    private ScheduleButton openButton, saveButton, printButton, createScheduleButton, addStudentButton, addKGUButton, automaticButton;
    private JToggleButton timeFilterButton;
    private JSplitPane splitPane;
    private JScrollPane leftScroll, rightScroll;
    private JToggleButton timeFilter;
    private JSplitPane split;
    private JScrollPane left, right;

    public MainFrame() {

        setLayout(new BorderLayout(0, 0)); // nicht nötig ??

        setTitle("StundenPlaner");
        setIconImage(Icons.getImage("table.png"));
        setExtendedState(Frame.MAXIMIZED_BOTH);

        //      database = new DataBase();
        scheduleData = new ScheduleData();
        studentData = new StudentData();
        widgetInteraction = new WidgetInteraction(); // globale Variablen für GUI-Management

        createWidgets();
        addWidgets();
        addListeners();

        pack();
        setLocation(0, 0);
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
        automaticButton = new ScheduleButton("coffee.png", "Automatischer Einteilungsvorschlag machen");
        timeFilterButton = new JToggleButton(Icons.setIcon("color.png"));
        timeFilterButton.setToolTipText("Verteilung der Zeiten anzeigen: je später, desto dunkler");
        timeFilterButton.setPreferredSize(new Dimension(60, 0));

//        schedule = new Schedule(widgetInteraction);
        schedule = new Schedule2();
        
        
        leftScroll = new JScrollPane(new TimeTable_Only());
        leftScroll.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        //  leftScroll.setBackground(Colors.BACKGROUND);

        //       studentList = new StudentList(database, widgetInteraction);
        studentList = new StudentList2();
        rightScroll = new JScrollPane(studentList);
        rightScroll.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        //  rightScroll.setBackground(Colors.BACKGROUND);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScroll, rightScroll);
        splitPane.setContinuousLayout(true);
        splitPane.setResizeWeight(0.5);  // ToDo: anpassen, wenn gefüllt?

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

        //   database.addDatabaseListener(this);
        createScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                ScheduleDataEntry mask = new ScheduleDataEntry(scheduleData, MainFrame.this);
                mask.setVisible(true);
            }
        });

        addStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StudentDataEntry mask = new StudentDataEntry(studentData, MainFrame.this);
                mask.setVisible(true);
            }
        });
    }


    /* Implementierung DataBaseListener */
//    @Override
//    public void studentAdded(Student student) {
//        studentList.addStudentRow(student, schedule);
//    }
//
//    @Override
//    public void studentRemoved(Student s) {
//    }
//
//    @Override
//    public void studentEdited(Student s) {
//    }
//
//    @Override
//    /* Schedule erzeugen */
//    public void scheduleAdded(ScheduleTimes scheduleTimes) {
//        schedule.initSchedule(scheduleTimes); // DayColumns initialisieren
//        schedule.drawSchedule();
//        schedule.addListener(studentList); 
//        studentList.createEmptyStudentList(scheduleTimes);
//      }
//
//    @Override
//    public void scheduleRemoved(ScheduleTimes t) {
//    }
//
//    @Override
//    public void scheduleEdited(ScheduleTimes t) {
//    }

    /* innere Klassen */
    private class ScheduleButton extends JButton {

        public ScheduleButton(String iconName, String toolTip) {
            setIcon(Icons.setIcon(iconName));
            setToolTipText(toolTip);
            setPreferredSize(new Dimension(60, 0));
        }
    }
}
