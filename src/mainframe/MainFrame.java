/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainframe;

import studentListData.StudentListData;
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
import scheduleData_new.ScheduleData_new;
import scheduleData_new.ScheduleTimes_new;
import schedule_new.Schedule_new;
import studentlistGUI.StudentList;
import util.Icons;

/**
 *
 * @author Mathias
 */
public class MainFrame extends JFrame { // alte Version: implements DatabaseListener 

    //  private final DataBase database;
    private ScheduleData_new scheduleData;
    private StudentListData studentListData;
    private Schedule_new schedule;
    private StudentList studentList;
    private JPanel toolBar;
    private ScheduleButton openButton, saveButton, printButton, createScheduleButton, addStudentButton, addKGUButton, automaticButton;
    private JToggleButton timeFilterButton;
    private JSplitPane splitPane;
    private JScrollPane leftScroll, rightScroll;

    public MainFrame() {

        setLayout(new BorderLayout(0, 0)); // nicht nötig ??

        setTitle("StundenPlaner");
        setIconImage(Icons.getImage("table.png"));
        setExtendedState(Frame.MAXIMIZED_BOTH);

        scheduleData = new ScheduleData_new();
        studentListData = new StudentListData();

        createWidgets();
        addWidgets();
        addListeners();
        setMinimumSize(new Dimension(1000, 400));
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
        addStudentButton.setEnabled(false);
        addKGUButton = new ScheduleButton("boy&girl.png", "Gruppen-Profil erstellen");
        addKGUButton.setEnabled(false);
        automaticButton = new ScheduleButton("coffee.png", "Automatischer Einteilungsvorschlag machen");
        timeFilterButton = new JToggleButton(Icons.setIcon("color.png"));
        timeFilterButton.setToolTipText("Verteilung der Zeiten anzeigen: je später, desto dunkler");
        timeFilterButton.setPreferredSize(new Dimension(60, 0));

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JPanel(), new JPanel());
        splitPane.setContinuousLayout(true);
        splitPane.setResizeWeight(0.5);

    }

    public void createSchedule(ScheduleTimes_new scheduleTimes) {  // in ScheduleDataEntry aufgerufen

        scheduleData.initScheduleData(scheduleTimes);
        schedule = new Schedule_new(scheduleData);
        leftScroll = new JScrollPane(schedule);
        leftScroll.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        splitPane.setLeftComponent(leftScroll); // muss hier in SplitPane geaddet werden, damit sofort sichtbar...
    }

    public void prepareStudentList() {  // in ScheduleDataEntry aufgerufen

        if (scheduleData.getNumberOfDays() > 0) {
            studentListData.setScheduleData(scheduleData);  // StudentListData braucht ScheduleData: numberOfDays, validDays
            studentList = new StudentList(studentListData, scheduleData);  // schedule für Listener Registrierung
            rightScroll = new JScrollPane(studentList);
            rightScroll.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            splitPane.setRightComponent(rightScroll);
            addStudentButton.setEnabled(true);
            addKGUButton.setEnabled(true);
        }
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
                StudentDataEntry mask = new StudentDataEntry(studentListData, MainFrame.this);
                mask.setVisible(true);
            }
        });
    }

    /* innere Klassen */
    private class ScheduleButton extends JButton {

        public ScheduleButton(String iconName, String toolTip) {
            setIcon(Icons.setIcon(iconName));
            setToolTipText(toolTip);
            setPreferredSize(new Dimension(60, 0));
        }
    }
}
