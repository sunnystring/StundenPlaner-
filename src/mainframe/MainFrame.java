/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainframe;

import core.Database;
import core.DatabaseListener;
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
import scheduleData.ScheduleData;
import core.Student;
import dataEntryUI.ScheduleEdit;
import dataEntryUI.ScheduleEntry;
import dataEntryUI.ScheduleInputMask;
import dataEntryUI.StudentEntry;
import javax.swing.JButton;
import javax.swing.JDialog;
import scheduleUI.Schedule;
import studentlistUI.StudentList;
import utils.Icons;
import static userUtilsUI.ColoredStudentDays.DEFAULT_COLORS;

/**
 *
 * Erzeugung und Initialisierung der StundenPlaner-GUI
 */
public class MainFrame extends JFrame implements DatabaseListener {
    
    private Database database;
    private ScheduleTimes scheduleTimes;
    private ScheduleData scheduleData;
    private StudentListData studentListData;
    private Schedule schedule;
    private StudentList studentList;
    private ScheduleInputMask scheduleInputMask;
    private StudentInputMask studentInputMask;
    private JPanel toolBar;
    private ScheduleButton openButton, saveButton, printButton, scheduleButton,
            studentButton, KGUButton, zoomButton, coloredStudentTimesButton;
    private boolean buttonState;
    private JSplitPane splitPane;
    private JScrollPane leftScroll, rightScroll;
    
    public MainFrame() {
        setTitle("StundenPlaner");
        setIconImage(Icons.getImage("table.png"));
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000, 400));
        database = new Database();
        scheduleTimes = database.getScheduleTimes();
        studentListData = new StudentListData(database, this);
        scheduleData = new ScheduleData(database, studentListData);
        createWidgets();
        addWidgets();
        setScheduleDataParameters();
        setStudentListDataParameters();
        addListeners();
        setStudentButtonsEnabled(false);
        buttonState = false;
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
        printButton.setEnabled(false);
        scheduleButton = new ScheduleButton("calendar.png", "Stundenplan erstellen oder ändern");
        studentButton = new ScheduleButton("boy.png", "Schülerprofil erstellen");
        KGUButton = new ScheduleButton("boy&girl.png", "Gruppen-Profil erstellen");
        KGUButton.setEnabled(false);
        zoomButton = new ScheduleButton("resize.png", "Stundenplan: Höhe anpassen");
        zoomButton.setEnabled(false);
        coloredStudentTimesButton = new ScheduleButton("color.png", "Schülerliste: Zeitliche Verteilung der Schülerzeiten anzeigen");
        coloredStudentTimesButton.setEnabled(false);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setContinuousLayout(true);
        splitPane.setResizeWeight(0.75);
        schedule = new Schedule(scheduleData, studentListData);
        leftScroll = new JScrollPane(schedule);
        leftScroll.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        studentList = new StudentList(studentListData, schedule.getTimeTable());
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
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(zoomButton);
        toolBar.add(coloredStudentTimesButton);
    }
    
    private void setScheduleDataParameters() {
        scheduleData.getBreakWatcher().setDatabase(database);
        scheduleData.getBreakWatcher().setSchedule(schedule);
    }
    
    private void setStudentListDataParameters() {
        studentListData.setScheduleData(scheduleData);
        studentListData.setStudentList(studentList);
    }
    
    private void addListeners() {
        database.addDatabaseListener(studentListData);
        database.addDatabaseListener(scheduleData);
        database.addDatabaseListener(this);
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
        zoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                schedule.fireNextScheduleSize();
            }
        });
        coloredStudentTimesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonState = !buttonState;
                studentListData.showStudentDaysColored(buttonState);
            }
        });
    }
    
    public void resetColoredStudentTimesButtonState() {
        buttonState = false;
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
        zoomButton.setEnabled(true);
    }
    
    private void setScheduleData() {
        scheduleData.setTableData();
    }
    
    private void setupAndShowSchedule() {
        schedule.createHeader();
        schedule.getTimeTable().update();
    }
    
    private void setupStudentListData() {
        studentListData.setup();
    }
    
    private void setupAndShowStudentList() {
        studentList.getTableHeader().setVisible(true);
        studentList.update();
    }
    
    private void fireUIDataChanged() {
        scheduleData.fireTableDataChanged();
        studentListData.fireTableDataChanged();
    }

    //------------- Schedule Edit -------------
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
        schedule.getTimeTable().update();
    }
    
    private void updateStudentListData() {
        studentListData.update();
    }
    
    private void updateAndShowStudentList() {
        studentList.update();
    }

    //--------------------------------
    public void setScheduleButtonEnabled(boolean state) {
        scheduleButton.setEnabled(state);
    }
    
    public void setStudentButtonsEnabled(boolean state) {
        studentButton.setEnabled(state);
        // KGUButton.setEnabled(state);
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
    
    public ScheduleData getScheduleData() {
        return scheduleData;
    }
    
    public StudentListData getStudentListData() {
        return studentListData;
    }
    
    @Override
    public void studentAdded(int numberOfStudents, Student student) {
        coloredStudentTimesButton.setEnabled(numberOfStudents > 0);
        studentListData.showStudentDaysColored(DEFAULT_COLORS);
        resetColoredStudentTimesButtonState();
    }
    
    @Override
    public void studentEdited(Student student) {
        studentListData.showStudentDaysColored(DEFAULT_COLORS);
        resetColoredStudentTimesButtonState();
    }
    
    @Override
    public void studentDeleted(int numberOfStudents, Student student) {
        coloredStudentTimesButton.setEnabled(numberOfStudents > 0);
        studentListData.showStudentDaysColored(DEFAULT_COLORS);
        resetColoredStudentTimesButtonState();
    }
}
