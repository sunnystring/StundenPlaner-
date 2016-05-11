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
import io.DataTransferManager;
import io.PrinterDialog;
import io.PrinterText;
import java.awt.print.PrinterException;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicSliderUI;
import scheduleUI.Schedule;
import studentlistUI.StudentList;
import userUtilsUI.ColoredStudentDays;
import utils.Icons;
import static userUtilsUI.ColoredStudentDays.DEFAULT_COLORS;

/**
 *
 * Erzeugung und Initialisierung der StundenPlaner-GUI
 */
public class MainFrame extends JFrame implements DatabaseListener {

    private final Database database;
    private ScheduleTimes scheduleTimes;
    private final ScheduleData scheduleData;
    private final StudentListData studentListData;
    private Schedule schedule;
    private StudentList studentList;
    private ScheduleInputMask scheduleInputMask;
    private StudentInputMask studentInputMask;
    private JPanel toolBar;
    private ScheduleButton openButton, saveButton, printButton, exitButton, scheduleButton,
            studentButton, KGUButton, zoomButton, coloredStudentTimesButton, infoButton;
    private boolean buttonState;
    private JSplitPane splitPane;
    private JScrollPane leftScroll, rightScroll;
    private JFileChooser fileChooser;
    private DataTransferManager dataTransferManager;

    public MainFrame() {
        setTitle("StundenPlaner");
        setIconImage(Icons.getImage("table.png"));
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000, 400));
        database = new Database();
        scheduleTimes = database.getScheduleTimes();
        studentListData = new StudentListData(database, this);
        scheduleData = new ScheduleData(database, studentListData);
        dataTransferManager = new DataTransferManager(database);
        createWidgets();
        addWidgets();
        setScheduleDataParameters();
        setStudentListDataParameters();
        initFileChooser();
        addListeners();
        setStudentButtonsEnabled(false);
        buttonState = false;
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createWidgets() {
        toolBar = new JPanel();
        toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.LINE_AXIS));
        toolBar.setPreferredSize(new Dimension(0, 32));
        toolBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 0, 4));
        openButton = new ScheduleButton("openFile.png", "Bestehender Stundenplan öffnen");
        saveButton = new ScheduleButton("disk.png", "Stundenplan und Schülerdaten speichern");
        saveButton.setEnabled(false);
        printButton = new ScheduleButton("printer.png", "Stundenplan drucken");
        printButton.setEnabled(false);
        exitButton = new ScheduleButton("exit.png", "StundenPlaner beenden");
        scheduleButton = new ScheduleButton("schedule.png", "Stundenplan erstellen oder ändern");
        studentButton = new ScheduleButton("boy.png", "Schülerprofil erstellen");
        KGUButton = new ScheduleButton("group.png", "Gruppen-Profil erstellen");
        KGUButton.setEnabled(false);
        zoomButton = new ScheduleButton("resize.png", "Stundenplan: Höhe anpassen");
        zoomButton.setEnabled(false);
        coloredStudentTimesButton = new ScheduleButton("color.png", "Schülerliste: Verteilung der Zeiten anzeigen");
        coloredStudentTimesButton.setEnabled(false);
        infoButton = new ScheduleButton("info.png", "Alle Funktionen auf einen Blick");
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
        fileChooser = new JFileChooser();
    }

    private void addWidgets() {
        splitPane.setLeftComponent(leftScroll);
        splitPane.setRightComponent(rightScroll);
        add(BorderLayout.CENTER, splitPane);
        add(BorderLayout.PAGE_START, toolBar);
        toolBar.add(infoButton);
        toolBar.add(openButton);
        toolBar.add(saveButton);
        toolBar.add(printButton);
        toolBar.add(exitButton);
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

    private void initFileChooser() {
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                return dataTransferManager.isValidExtension(f);
            }

            @Override
            public String getDescription() {
                return "StundenPlaner";
            }
        });
    }

    private void addListeners() {
        database.addDatabaseListener(studentListData);
        database.addDatabaseListener(scheduleData);
        database.addDatabaseListener(this);
        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    dataTransferManager.save(fileChooser.getSelectedFile());
                }
            }
        });
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    dataTransferManager.load(fileChooser.getSelectedFile());
                    updateDataAfterFileEntry();
                    updateWidgetsAfterFileEntry();
                    showStundenPlaner();
                }
            }
        });
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrinterDialog printerDialog = new PrinterDialog(MainFrame.this, database);
//                JDialog dialog = new JDialog(MainFrame.this);
//                dialog.add(scheduleText);
//                dialog.setMinimumSize(new Dimension(350, 500));
//                dialog.setModal(true);
//                dialog.setLocationRelativeTo(MainFrame.this);
//                dialog.setVisible(true);
//                try {
//
//                    scheduleText.print();
//                } catch (PrinterException ex) {
//                    ex.printStackTrace();
//                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    dataTransferManager.save(fileChooser.getSelectedFile());
                }
                System.exit(0);
            }
        });
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
                Student student = new Student();
                student.getStudentTimes().setScheduleTimes(scheduleTimes);
                JDialog studentEntry = new StudentEntry(MainFrame.this, student);
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

    private void updateDataAfterFileEntry() {
        scheduleTimes = dataTransferManager.getScheduleTimes();
        scheduleTimes.updateValidDays();
        database.updateAfterFileEntry(scheduleTimes, dataTransferManager);
        scheduleData.setScheduleTimes(scheduleTimes);
        studentListData.setNumberOfStudents(database.getNumberOfStudents());
    }

    private void updateWidgetsAfterFileEntry() {
        scheduleInputMask = new ScheduleInputMask(database, this);
        studentInputMask = new StudentInputMask(database);
        studentListData.setColoredStudentDays(new ColoredStudentDays(database, studentListData));
        coloredStudentTimesButton.setEnabled(true);
        printButton.setEnabled(true);
        saveButton.setEnabled(true);
    }

    private void showStundenPlaner() {
        scheduleData.updateTableData();
        updateSchedule();
        studentListData.updateAfterFileEntry();
        setupStudentList();
        showUI();
        setStudentButtonsEnabled(true);
        zoomButton.setEnabled(true);
    }

    public void showStundenPlanerAfterScheduleEntry() {
        scheduleData.setTableData();
        setupSchedule();
        studentListData.init();
        setupStudentList();
        showUI();
        setStudentButtonsEnabled(true);
        zoomButton.setEnabled(true);
        printButton.setEnabled(true);
        saveButton.setEnabled(true);
    }

    private void setupSchedule() {
        schedule.createHeader();
        schedule.getTimeTable().update();
    }

    private void setupStudentList() {
        studentList.getTableHeader().setVisible(true);
        studentList.setup();
    }

    private void showUI() {
        scheduleData.fireTableDataChanged();
        studentListData.fireTableDataChanged();
    }

    public void showStundenPlanerAfterScheduleEdit() {
        scheduleTimes.updateValidDays();
        database.adjustStudentDaysToScheduleChange();
        database.updateUserUtilsCollections();
        scheduleData.updateTableData();
        updateSchedule();
        studentListData.updateAfterScheduleEdit();
        setupStudentList();
        showUI();
    }

    private void updateSchedule() {
        schedule.updateHeader();
        schedule.getTimeTable().update();
    }

    public void setButtonsEnabled(boolean state) {
        setDataEntryButtonsEnabled(state);
        setFileButtonsEnabled(state);
    }

    public void setDataEntryButtonsEnabled(boolean state) {
        setScheduleButtonEnabled(state);
        setStudentButtonsEnabled(state);
    }

    public void setFileButtonsEnabled(boolean state) {
        saveButton.setEnabled(state);
        openButton.setEnabled(state);
        printButton.setEnabled(state);
        exitButton.setEnabled(state);
    }

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
            setPreferredSize(new Dimension(40, 32));
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

    public void resetColoredStudentTimesButtonState() {
        buttonState = false;
    }
}
