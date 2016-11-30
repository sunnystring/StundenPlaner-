/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainframe;

import attendanceList.AttendanceListUI;
import core.Database;
import core.DatabaseListener;
import core.Profile;
import core.ScheduleTimes;
import studentListData.StudentListData;
import dataEntryUI.student.StudentInputMask;
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
import dataEntryUI.group.GroupInputMask;
import dataEntryUI.group.GroupSelectionDialog;
import dataEntryUI.schedule.ScheduleEdit;
import dataEntryUI.schedule.ScheduleEntry;
import dataEntryUI.schedule.ScheduleInputMask;
import dataEntryUI.student.StudentEntry;
import io.FileIO;
import io.PrinterDialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import scheduleUI.DayField;
import scheduleUI.Schedule;
import studentlistUI.StudentList;
import userUtilsUI.ColoredStudentDays;
import utils.Icons;
import static userUtilsUI.ColoredStudentDays.DEFAULT_COLORS;
import utils.Dialogs;
import utils.GUIConstants;

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
    private GroupInputMask groupInputMask;
    private JPanel toolBar;
    private ScheduleButton openButton, saveButton, printButton, exitButton, attendanceListButton, scheduleButton,
            studentButton, groupButton, zoomButton, coloredStudentTimesButton, infoButton;
    private boolean buttonState;
    private JSplitPane splitPane;
    private JScrollPane leftScroll, rightScroll;
    private JFileChooser fileChooser;
    private FileIO fileIO;
    private ArrayList<ScheduleButton> scheduleButtons;
  //  private AttendanceListUI attendanceList;

    public MainFrame() {
        setTitle("StundenPlaner");
        setIconImage(Icons.getImage("table.png"));
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000, 400));
        database = new Database();
        scheduleTimes = database.getScheduleTimes();
        studentListData = new StudentListData(database, this);
        scheduleData = new ScheduleData(database, studentListData);
        fileIO = new FileIO(database);
        scheduleButtons = new ArrayList<>();
        createWidgets();
        addWidgets();
        setScheduleDataParameters();
        setStudentListDataParameters();
        initFileChooser();
        addListeners();
        setStudentButtonsEnabled(false);
        buttonState = false;
        pack();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private void createWidgets() {
        toolBar = new JPanel();
        toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.LINE_AXIS));
        toolBar.setPreferredSize(new Dimension(0, 32));
        toolBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 0, 4));
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setContinuousLayout(true);
        splitPane.setResizeWeight(0.75);
        schedule = new Schedule(database, scheduleData, studentListData);
        leftScroll = new JScrollPane(schedule);
        leftScroll.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        studentList = new StudentList(studentListData, schedule.getTimeTable());
        rightScroll = new JScrollPane(studentList);
        rightScroll.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        scheduleInputMask = new ScheduleInputMask(database, this);
        studentInputMask = new StudentInputMask(database);
        groupInputMask = new GroupInputMask(database);
      //  attendanceList = new AttendanceListUI(this);
        fileChooser = new JFileChooser();
        createButtons();

    }

    private void createButtons() {
        openButton = new ScheduleButton("openFile.png", "Bestehender Stundenplan öffnen");
        exitButton = new ScheduleButton("exit.png", "StundenPlaner beenden");
        saveButton = new ScheduleButton("disk.png", "Stundenplan und Schülerdaten speichern");
        saveButton.setEnabled(false);
        saveButton = new ScheduleButton("disk.png", "Stundenplan und Schülerdaten speichern");
        saveButton.setEnabled(false);
        printButton = new ScheduleButton("printer.png", "Stundenplan drucken");
        printButton.setEnabled(false);
        attendanceListButton = new ScheduleButton("attendanceList.png", "Unterrichtskontrolle öffnen");
        attendanceListButton.setEnabled(false);
        scheduleButton = new ScheduleButton("schedule.png", "Stundenplan erstellen oder ändern");
        studentButton = new ScheduleButton("boy.png", "Schülerprofil erstellen");
        groupButton = new ScheduleButton("group.png", "Gruppenprofile erstellen");
        zoomButton = new ScheduleButton("resize.png", "Stundenplan: Höhe anpassen");
        zoomButton.setEnabled(false);
        coloredStudentTimesButton = new ScheduleButton("color.png", "Schülerliste: Verteilung der Zeiten anzeigen");
        coloredStudentTimesButton.setEnabled(false);
        infoButton = new ScheduleButton("info.png", "Die wichtigsten Klicks auf einen Blick");
    }

    private void addWidgets() {
        splitPane.setLeftComponent(leftScroll);
        splitPane.setRightComponent(rightScroll);
        add(BorderLayout.CENTER, splitPane);
        add(BorderLayout.PAGE_START, toolBar);
        addButtons();
    }

    private void addButtons() {
        toolBar.add(exitButton);
        toolBar.add(openButton);
        toolBar.add(saveButton);
        toolBar.add(printButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(attendanceListButton);
        toolBar.add(scheduleButton);
        toolBar.add(studentButton);
        toolBar.add(groupButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(zoomButton);
        toolBar.add(coloredStudentTimesButton);
        toolBar.add(infoButton);
        createScheduleButtonList();
    }

    private void createScheduleButtonList() {
        scheduleButtons.add(exitButton);
        scheduleButtons.add(openButton);
        scheduleButtons.add(saveButton);
        scheduleButtons.add(printButton);
        scheduleButtons.add(attendanceListButton);
        scheduleButtons.add(scheduleButton);
        scheduleButtons.add(studentButton);
        scheduleButtons.add(groupButton);
        scheduleButtons.add(zoomButton);
        scheduleButtons.add(coloredStudentTimesButton);
        scheduleButtons.add(infoButton);
    }

    private void setScheduleDataParameters() {
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
                return fileIO.isValidExtension(f);
            }

            @Override
            public String getDescription() {
                return "StundenPlaner";
            }
        });
    }

    private void addListeners() {
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                closeStundenPlaner();
            }
        });
        database.addDatabaseListener(studentListData);
        database.addDatabaseListener(scheduleData);
        database.addDatabaseListener(this);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeStundenPlaner();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrinterDialog printerDialog = new PrinterDialog(MainFrame.this, database);
                printerDialog.setVisible(true);
            }
        });
        attendanceListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog attendanceList = new AttendanceListUI(MainFrame.this);
                attendanceList.setVisible(true);
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
                Profile student = new Profile();
                student.getStudentTimes().setScheduleTimes(scheduleTimes);
                StudentEntry studentEntry = new StudentEntry(MainFrame.this, student);
                studentEntry.setVisible(true);
            }
        });
        groupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Profile group = new Profile();
                group.getStudentTimes().setScheduleTimes(scheduleTimes);
                GroupSelectionDialog groupSelection = new GroupSelectionDialog(MainFrame.this, group);
                groupSelection.setVisible(true);
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
        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog infoFrame = new JDialog(MainFrame.this);
                JScrollPane pane = new JScrollPane(new JLabel(Icons.getIcon("screenshot.png")));
                infoFrame.setMinimumSize(GUIConstants.BIG_DIALOG_DIMENSION);
                infoFrame.add(pane);
                infoFrame.setVisible(true);
            }
        });
    }

    private void openFile() {
        if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
            fileIO.load(fileChooser.getSelectedFile());
            updateDataAfterFileEntry();
            updateWidgetsAfterFileEntry();
            showStundenPlaner();
        }
    }

    private void saveFile() {
        if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
            fileIO.save(fileChooser.getSelectedFile());
        }
    }

    private void closeStundenPlaner() {
        int option = Dialogs.showSaveOptionMessage();
        File file = fileChooser.getSelectedFile();
        if (option == JOptionPane.YES_OPTION) {
            if (file != null) {
                fileIO.save(file);
                System.exit(0);
            } else if (scheduleTimes.getNumberOfValidDays() > 0) {
                saveFile();
                System.exit(0);
            } else {
                System.exit(0);
            }
        }
        if (option == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }

    private void updateDataAfterFileEntry() {
        scheduleTimes = fileIO.getScheduleTimes();
        scheduleTimes.updateValidDays();
        database.updateAfterFileEntry(scheduleTimes, fileIO);
        scheduleData.setScheduleTimes(scheduleTimes);
        studentListData.setNumberOfProfiles(database.getNumberOfStudents());
    }

    private void updateWidgetsAfterFileEntry() {
        scheduleInputMask = new ScheduleInputMask(database, this);
        studentInputMask = new StudentInputMask(database);
        groupInputMask = new GroupInputMask(database);
        studentListData.setColoredStudentDays(new ColoredStudentDays(database, studentListData));
        coloredStudentTimesButton.setEnabled(true);
        printButton.setEnabled(true);
        saveButton.setEnabled(true);
        attendanceListButton.setEnabled(true);
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
        addListenersForDayViewDisposal();
        addListenersForStudentJournalDisposal();
    }

    private void addListenersForStudentJournalDisposal() {
        for (ScheduleButton scheduleButton : scheduleButtons) {
            scheduleButton.addMouseListener(scheduleData);
        }
        for (DayField dayField : schedule.getHeaderFieldList()) {
            dayField.addMouseListener(scheduleData);
        }
        studentList.getTableHeader().addMouseListener(scheduleData);
    }

    private void addListenersForDayViewDisposal() {
        for (DayField dayField : schedule.getHeaderFieldList()) {
            for (ScheduleButton scheduleButton : scheduleButtons) {
                scheduleButton.addMouseListener(dayField);
            }
            schedule.getTimeTable().addMouseListener(dayField);
            studentList.addMouseListener(dayField);
            studentList.getTableHeader().addMouseListener(dayField);
        }
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
        addListenersForDayViewDisposal();
        addListenersForStudentJournalDisposal();
    }

    public void setDataEntryButtonsEnabled(boolean state) {
        setScheduleButtonEnabled(state);
        setStudentButtonsEnabled(state);
    }

    public void setFileButtonsEnabled(boolean state) {
        exitButton.setEnabled(state);
        saveButton.setEnabled(state);
        openButton.setEnabled(state);
        printButton.setEnabled(state);
        attendanceListButton.setEnabled(state);
    }

    public void setScheduleButtonEnabled(boolean state) {
        scheduleButton.setEnabled(state);
    }

    public void setStudentButtonsEnabled(boolean state) {
        studentButton.setEnabled(state);
        groupButton.setEnabled(state);
    }

    public ScheduleInputMask getScheduleInputMask() {
        return scheduleInputMask;
    }

    public StudentInputMask getStudentInputMask() {
        return studentInputMask;
    }

    public GroupInputMask getGroupInputMask() {
        return groupInputMask;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public ScheduleData getScheduleData() {
        return scheduleData;
    }

    public StudentListData getStudentListData() {
        return studentListData;
    }

    public Database getDatabase() {
        return database;
    }

    @Override
    public void profileAdded(int numberOfStudents, Profile profile) {
        coloredStudentTimesButton.setEnabled(numberOfStudents > 0);
        attendanceListButton.setEnabled(numberOfStudents > 0);
        studentListData.showStudentDaysColored(DEFAULT_COLORS);
        resetColoredStudentTimesButtonState();
    }

    @Override
    public void profileEdited(Profile profile) {
        studentListData.showStudentDaysColored(DEFAULT_COLORS);
        resetColoredStudentTimesButtonState();
    }

    @Override
    public void profileDeleted(int numberOfStudents, Profile profile) {
        coloredStudentTimesButton.setEnabled(numberOfStudents > 0);
        attendanceListButton.setEnabled(numberOfStudents > 0);
        studentListData.showStudentDaysColored(DEFAULT_COLORS);
        resetColoredStudentTimesButtonState();
    }

    public void resetColoredStudentTimesButtonState() {
        buttonState = false;
    }

    public class ScheduleButton extends JButton {

        public ScheduleButton(String iconName, String toolTip) {
            setIcon(Icons.getIcon(iconName));
            setToolTipText(toolTip);
            setPreferredSize(new Dimension(40, 32));

        }
    }
}
