/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainframe;

import core.Database;
import core.ScheduleTimes;
import studentListData.StudentListData;
import dataEntryUI.StudentInputMask;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import scheduleData.ScheduleData;
import core.Student;
import dataEntryUI.ScheduleEdit;
import dataEntryUI.ScheduleEntry;
import dataEntryUI.ScheduleInputMask;
import dataEntryUI.StudentEntry;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
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
    private ScheduleTimes scheduleTimes;
    private ScheduleData scheduleData;
    private StudentListData studentListData;
    private Schedule schedule;
    private StudentList studentList;
    private ScheduleInputMask scheduleInputMask;
    private StudentInputMask studentInputMask;
    private JPanel toolBar;
    private ScheduleButton openButton, saveButton, printButton, scheduleButton, studentButton, KGUButton, automaticAllocationButton, resizeButton;
    private JToggleButton timeScopeButton;
    private JSplitPane splitPane;
    private JScrollPane leftScroll, rightScroll;

    public MainFrame() {
        setTitle("StundenPlaner");
        setIconImage(Icons.getImage("table.png"));
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000, 400));
        database = new Database();
        scheduleTimes = database.getScheduleTimes();
        scheduleData = new ScheduleData(database);
        studentListData = new StudentListData(database, this);
        createWidgets();
        addWidgets();
        setStudentListDataParameters();
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
        scheduleButton = new ScheduleButton("calendar.png", "Stundenplan erstellen oder ändern");
        studentButton = new ScheduleButton("boy.png", "Schülerprofil erstellen");
        KGUButton = new ScheduleButton("boy&girl.png", "Gruppen-Profil erstellen");
        automaticAllocationButton = new ScheduleButton("coffee.png", "Automatischer Einteilungsvorschlag machen");
        automaticAllocationButton.setEnabled(false);  // ToDo...
        resizeButton = new ScheduleButton("resize.png", "Stundenplan in verschiedenen Grössen anzeigen", 30);
        timeScopeButton = new JToggleButton(Icons.setIcon("color.png"));
        timeScopeButton.setToolTipText("Verteilung der Schülerzeiten anzeigen: je später, desto dunkler");
        timeScopeButton.setPreferredSize(new Dimension(35, 0));
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setContinuousLayout(true);
        splitPane.setResizeWeight(0.75);
        schedule = new Schedule(scheduleData, studentListData);
        leftScroll = new JScrollPane(schedule);
        leftScroll.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        studentList = new StudentList(studentListData, schedule.getTimeTable());  // timeTable für Listener-Registrierung
        rightScroll = new JScrollPane(studentList);
        rightScroll.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        scheduleInputMask = new ScheduleInputMask(database, this);
        studentInputMask = new StudentInputMask(database);
    }

    private void addWidgets() {
        splitPane.setLeftComponent(leftScroll);
        splitPane.setRightComponent(rightScroll);
        add(BorderLayout.CENTER, splitPane);
        add(BorderLayout.PAGE_START, toolBar);
        toolBar.add(openButton);
        toolBar.add(saveButton);
        toolBar.add(printButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(scheduleButton);
        toolBar.add(studentButton);
        toolBar.add(KGUButton);
        toolBar.add(automaticAllocationButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(resizeButton);
        toolBar.add(timeScopeButton);

    }

    private void setStudentListDataParameters() {
        studentListData.setScheduleData(scheduleData);
        studentListData.setStudentList(studentList);
    }

    private void addListeners() {
        database.addDatabaseListener(studentListData);
        database.addDatabaseListener(scheduleData);
        scheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (scheduleTimes.isEmpty()) {
                    JDialog scheduleEntry = new ScheduleEntry(MainFrame.this);
                    scheduleEntry.setVisible(true);
                } else {
                    JDialog scheduleEdit = new ScheduleEdit(MainFrame.this);
                    scheduleEdit.setVisible(true);
                }
            }
        });
        studentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JDialog studentEntry = new StudentEntry(MainFrame.this, new Student());
                studentInputMask.clearTextFields();
                studentEntry.setVisible(true);
            }
        });
        resizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                schedule.getTimeTable().fireNextScheduleSizeOption();
            }
        });
        timeScopeButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    studentListData.setColoredStudentTimes();
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    studentListData.setDefaultBlue();
                }
            }
        });
    }

//------------- Schedule Entry -----------
    public void setupAndShowUI() {
        scheduleTimes.setValidScheduleDays();
        setScheduleData();
        setupAndShowSchedule();
        setupStudentListData();
        setupAndShowStudentList();
        fireUIDataChanged();
        setStudentButtonsEnabled(true);
    }

    private void setScheduleData() {
        scheduleData.setTableData();
    }

    private void setupAndShowSchedule() {
        schedule.createHeader();
        schedule.getTimeTable().updateParameters();
    }

    private void setupStudentListData() {
        studentListData.setTableData();
    }

    private void setupAndShowStudentList() {
        studentList.getTableHeader().setVisible(true);
        studentList.updateParameters();
    }

    private void fireUIDataChanged() {
        scheduleData.fireTableDataChanged();
        studentListData.fireTableDataChanged();
    }

    //------------- Schedule Edit -------------
    public void validateNewEntry() {
        scheduleData.checkLectionAllocationState();
        scheduleData.checkTimeFrame();
        scheduleTimes.validateDayEntry();
    }

    public void updateAndShowUI() {
        scheduleTimes.updateValidDays();
        database.updateStudentDays();
        updateScheduleData();
        updateAndShowSchedule();
        updateStudentListData();
        updateAndShowStudentList();
        fireUIDataChanged();
    }

    private void updateScheduleData() {
        scheduleData.updateTableData();
    }

    private void updateAndShowSchedule() {
        schedule.removeHeader();
        schedule.createHeader();
        schedule.getTimeTable().updateParameters();
    }

    private void updateStudentListData() {
        studentListData.updateTableData();
    }

    private void updateAndShowStudentList() {
        studentList.updateParameters();
    }

    //--------------Other-------------
    public void setScheduleButtonEnabled(boolean state) {
        scheduleButton.setEnabled(state);
    }

    public void setStudentButtonsEnabled(boolean state) {
        studentButton.setEnabled(state);
        KGUButton.setEnabled(state);
    }

    private class ScheduleButton extends JButton {

        public ScheduleButton(String iconName, String toolTip) {
            setIcon(Icons.setIcon(iconName));
            setToolTipText(toolTip);
            setPreferredSize(new Dimension(50, 0));
        }

        public ScheduleButton(String iconName, String toolTip, int width) {
            setIcon(Icons.setIcon(iconName));
            setToolTipText(toolTip);
            setPreferredSize(new Dimension(width, 0));

        }
    }

    public ScheduleInputMask getScheduleInputMask() {
        return scheduleInputMask;
    }

    public StudentInputMask getStudentInputMask() {
        return studentInputMask;
    }

}
