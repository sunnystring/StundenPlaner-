/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainframe;

import core.DataBase;
import core.Main;
import core.ScheduleDay;
import core.Student;
import core.StudentDay;
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
import javax.swing.table.TableModel;

import studentlist.StudentList;
import schedule.Schedule;
import util.Colors;
import util.Icons;
import util.Time;

/**
 *
 * @author Mathias
 */
public class MainFrame extends JFrame {

    private static DataBase database;

    private static Schedule schedule;
    private static StudentList studentList;
    private JPanel toolBar;
    private ScheduleButton open, save, print, createSchedule, addStudent, addKGU, automatic;
    private JToggleButton timeFilter;
    private JSplitPane split;
    private JScrollPane left, right;

    public MainFrame() {

        setTitle("StundenPlaner");
        setIconImage(Icons.getImage("table.png"));
        setExtendedState(Frame.MAXIMIZED_BOTH);

        database = new DataBase();

        createWidget();
        addWidget();
        addListener();
        pack();
        setLocation(0, 0);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createWidget() {

        toolBar = new JPanel();
        toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.LINE_AXIS));
        toolBar.setPreferredSize(new Dimension(0, 30));
        toolBar.setBorder(BorderFactory.createEmptyBorder(2, 12, 0, 15));

        open = new ScheduleButton("openFile.png", "Bestehender Stundenplan öffnen");
        save = new ScheduleButton("disk.png", "Stundenplan und Schülerdaten speichern");
        print = new ScheduleButton("printer.png", "Stundenplan drucken");
        createSchedule = new ScheduleButton("calendar.png", "Stundenplan erstellen oder ändern");
        addStudent = new ScheduleButton("boy.png", "Neues Schülerprofil erstellen");
        addKGU = new ScheduleButton("boy&girl.png", "KGU-Profil erstellen");
        automatic = new ScheduleButton("coffee.png", "Automatischer Einteilungsvorschlag machen");
        timeFilter = new JToggleButton(Icons.setIcon("color.png"));
        timeFilter.setToolTipText("Verteilung der Zeiten anzeigen: je später, desto dunkler");
        timeFilter.setPreferredSize(new Dimension(60, 0));

        schedule = new Schedule();
        left = new JScrollPane(schedule);
        left.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        left.setBackground(Colors.BACKGROUND);

        /*---------------Rohfassung: statische Eingabe Schedule-Daten-----------------------*/
        database.addDay(new ScheduleDay("Montag", new Time("13.00"), new Time("21.00")));
        database.addDay(new ScheduleDay("Dienstag", new Time("15.00"), new Time("20.30")));
        database.addDay(new ScheduleDay("Mittwoch", new Time("12.30"), new Time("19.30")));
        /*---------------------------------------------------------------------------*/
        schedule.createSchedule();

        studentList = new StudentList();
        right = new JScrollPane(studentList);
        right.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        /*---------------Rohfassung: statische Eingabe Student-Daten -----------------------*/
        // Parameter day: startTime1, endTime1, startTime2, endTime2, favorite
        StudentDay day1, day2, day3;

        day1 = new StudentDay("17.15", "18.00", "18.00", "18.05", "");  // ok
        day2 = new StudentDay("", "", "", "", "");
        day3 = new StudentDay("", "", "", "", "");
        database.addStudent(new Student("Jarno", "Suda", day1, day2, day3, 30));

        day1 = new StudentDay("16.00", "18.00", "", "", "16.00"); // ok 
        day2 = new StudentDay("", "", "", "", "");
        day3 = new StudentDay("", "", "", "", "");
        database.addStudent(new Student("Yann", "Pelda", day1, day2, day3, 30));

        day1 = new StudentDay("16.15", "17.00", "", "", "16.15"); // ok
        day2 = new StudentDay("16.15", "17.00", "", "", "16.15");
        day3 = new StudentDay("", "", "", "", "");
        database.addStudent(new Student("Nicola", "Mussmann", day1, day2, day3, 30));

        day1 = new StudentDay("17.10", "19.00", "", "", "");  // ok
        day2 = new StudentDay("", "", "", "", "");
        day3 = new StudentDay("", "", "", "", "");
        database.addStudent(new Student("Yves", "Henz", day1, day2, day3, 30));

        day1 = new StudentDay("", "", "", "", ""); // ok
        day2 = new StudentDay("17.30", "18.00", "", "", "");
        day3 = new StudentDay("", "", "", "", "");
        database.addStudent(new Student("Julian", "Merz", day1, day2, day3, 30));

        day1 = new StudentDay("", "", "", "", "");
        day2 = new StudentDay("18.00", "20.00", "", "", "");
        day3 = new StudentDay("13.30", "14.00", "", "", "");
        database.addStudent(new Student("Gabriel", "Sturm", day1, day2, day3, 40));

        day1 = new StudentDay("", "", "", "", "17.45"); // ok
        day2 = new StudentDay("18.00", "20.00", "", "", "18.00"); // nach Julian
        day3 = new StudentDay("", "", "", "", "");
        database.addStudent(new Student("Annalisa", "Strübin", day1, day2, day3, 30));

        day1 = new StudentDay("", "", "", "", ""); // ok
        day2 = new StudentDay("", "", "", "", "");
        day3 = new StudentDay("12.45", "18.00", "", "", "17.15");
        database.addStudent(new Student("Juan", "Meier", day1, day2, day3, 30));

        day1 = new StudentDay("17.00", "18.00", "", "", "17.00"); // ok
        day2 = new StudentDay("16.15", "17.00", "", "", "16.15");
        day3 = new StudentDay("12.20", "15.00", "", "", "");
        database.addStudent(new Student("Jan", "Egli", day1, day2, day3, 30));

        day1 = new StudentDay("", "", "", "", ""); // ok
        day2 = new StudentDay("", "", "", "", "");
        day3 = new StudentDay("13.30", "14.30", "", "", "");
        database.addStudent(new Student("David", "Deja", day1, day2, day3, 30));

        day1 = new StudentDay("16.00", "17.00", "", "", "");
        day2 = new StudentDay("", "", "", "", "");
        day3 = new StudentDay("", "", "", "", "");
        database.addStudent(new Student("Jan", "Suter", day1, day2, day3, 30));

        day1 = new StudentDay("", "", "", "", "");  // ok
        day2 = new StudentDay("", "", "", "", "");
        day3 = new StudentDay("", "", "", "", "18.00");
        database.addStudent(new Student("Josua", "Gmür", day1, day2, day3, 30));

        day1 = new StudentDay("", "", "", "", ""); // ok
        day2 = new StudentDay("17.40", "20.00", "", "", "17.40");
        day3 = new StudentDay("", "", "", "", "");
        database.addStudent(new Student("Lucien", "Badoux", day1, day2, day3, 30));

        day1 = new StudentDay("17.30", "19.00", "", "", ""); // ok
        day2 = new StudentDay("18.00", "19.30", "", "", "18.00");
        day3 = new StudentDay("", "", "", "", "");
        database.addStudent(new Student("Liam", "Narbutas", day1, day2, day3, 30));

        day1 = new StudentDay("18.00", "21.00", "", "", "");
        day2 = new StudentDay("18.00", "21.00", "", "", "");
        day3 = new StudentDay("18.00", "21.00", "", "", "");
        database.addStudent(new Student("Jürg", "Stehlin", day1, day2, day3, 30));

        day1 = new StudentDay("", "", "", "", "");
        day2 = new StudentDay("19.30", "21.00", "", "", "20.00");
        day3 = new StudentDay("", "", "", "", "");
        database.addStudent(new Student("Shadia", "Handschin", day1, day2, day3, 30));

        day1 = new StudentDay("13.45", "14.00", "17.35", "17.45", "14.00"); // ok
        day2 = new StudentDay("", "", "", "", "");
        day3 = new StudentDay("", "", "", "", "");
        database.addStudent(new Student("Benny", "Amacher", day1, day2, day3, 30));

        day1 = new StudentDay("17.15", "19.30", "", "", ""); // ok
        day2 = new StudentDay("", "", "", "", "");
        day3 = new StudentDay("", "", "", "", "");
        database.addStudent(new Student("Aruthiran", "Sivaligam", day1, day2, day3, 30));

        day1 = new StudentDay("16.30", "19.00", "", "", ""); // ok
        day2 = new StudentDay("16.30", "19.00", "", "", "");
        day3 = new StudentDay("13.45", "19.00", "", "", "");
        database.addStudent(new Student("Florian", "Tognella", day1, day2, day3, 30));

        day1 = new StudentDay("", "", "", "", ""); // ok
        day2 = new StudentDay("", "", "", "", "");
        day3 = new StudentDay("13.00", "15.00", "", "", "");
        database.addStudent(new Student("Sean", "Kurath", day1, day2, day3, 30));

        day1 = new StudentDay("", "", "", "", "");  // ok
        day2 = new StudentDay("", "", "", "", "");
        day3 = new StudentDay("13.45", "18.00", "", "", "");
        database.addStudent(new Student("Anis", "Ameti", day1, day2, day3, 30));

        day1 = new StudentDay("17.00", "19.00", "", "", ""); // ok
        day2 = new StudentDay("", "", "", "", "");
        day3 = new StudentDay("13.15", "13.30", "17.00", "18.30", "13.15");
        database.addStudent(new Student("Sante", "Demarco", day1, day2, day3, 30));

        day1 = new StudentDay("", "", "", "", ""); // ok
        day2 = new StudentDay("", "", "", "", "");
        day3 = new StudentDay("14.30", "17.30", "", "", "14.30");
        database.addStudent(new Student("Timosh", "Rueff", day1, day2, day3, 30));

        day1 = new StudentDay("", "", "", "", ""); // ok
        day2 = new StudentDay("", "", "", "", "");
        day3 = new StudentDay("14.00", "14.05", "18.00", "18.05", "18.00");
        database.addStudent(new Student("Severin", "Schneider", day1, day2, day3, 30));

        day1 = new StudentDay("", "", "", "", ""); // ok
        day2 = new StudentDay("", "", "", "", "");
        day3 = new StudentDay("16.30", "17.00", "", "", "");
        database.addStudent(new Student("Vancy", "Ravindran", day1, day2, day3, 40));

        day1 = new StudentDay("15.25", "18.00", "", "", ""); // ok
        day2 = new StudentDay("15.25", "18.00", "", "", "");
        day3 = new StudentDay("13.30", "18.00", "", "", "");
        database.addStudent(new Student("Paul", "Weiland", day1, day2, day3, 30));

        day1 = new StudentDay("16.30", "18.00", "", "", ""); // ok
        day2 = new StudentDay("17.30", "18.00", "", "", "");
        day3 = new StudentDay("13.45", "17.00", "", "", "");
        database.addStudent(new Student("Arthit", "Gmür", day1, day2, day3, 30));

        day1 = new StudentDay("", "", "", "", ""); // ok
        day2 = new StudentDay("", "", "", "", "");
        day3 = new StudentDay("13.45", "17.15", "", "", "");
        database.addStudent(new Student("Niclas", "Nyfeler", day1, day2, day3, 30));

        day1 = new StudentDay("", "", "", "", ""); // ok
        day2 = new StudentDay("19.30", "20.30", "", "", "");
        day3 = new StudentDay("", "", "", "", "");
        database.addStudent(new Student("Sophie", "Zurlinden", day1, day2, day3, 30));


        /*---------------------------------------------------------------------------*/
        studentList.createStudentList();

        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setContinuousLayout(true);
        split.setResizeWeight(0.9);

    }

    private void addWidget() {

        setLayout(new BorderLayout(0, 0));
        add(BorderLayout.CENTER, split);
        add(BorderLayout.PAGE_START, toolBar);

        toolBar.add(open);
        toolBar.add(save);
        toolBar.add(print);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(createSchedule);
        toolBar.add(addStudent);
        toolBar.add(addKGU);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(automatic);
        toolBar.add(timeFilter);

    }

    private void addListener() {

        createSchedule.addActionListener(new ScheduleEntryListener());
        addStudent.addActionListener(new StudentEntryListener());
    }

    public static StudentList getStudentList() { // static: es gibt nur eine MainFrame
        return studentList;
    }

    public static TableModel getTableModel() {
        return database;
    }

    private class ScheduleButton extends JButton {

        public ScheduleButton(String iconName, String toolTip) {

            setIcon(Icons.setIcon(iconName));
            setToolTipText(toolTip);
            setPreferredSize(new Dimension(60, 0));
        }
    }

    private class StudentEntryListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            StudentDataEntry mask = new StudentDataEntry();
            //           mask.setLocationRelativeTo(Main.getMainFrame()); // ??? statt MainFrame.this
            mask.setVisible(true);
        }
    }

    private class ScheduleEntryListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            ScheduleDataEntry mask = new ScheduleDataEntry(MainFrame.this);
            mask.setLocationRelativeTo(MainFrame.this);
            mask.setVisible(true);
        }
    }

}
