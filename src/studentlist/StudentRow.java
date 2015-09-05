/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlist;

import core.DataBase;
import core.Main;
import core.Student;
import dialogs.StudentDataEntry;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import mainframe.MainFrame;
import schedule.DayColumn;
import schedule.Schedule;

import util.Colors;

/**
 *
 * @author Mathias
 */
public class StudentRow extends JPanel implements MouseListener {

    private final StudentField[] studentRow;     // Field-Arrays pro StudentRow
    private final VariableField[] emptyRow;  // Field-Arrays für restliche Rows
    private final VariableField[] headerRow;

    private int studentIndex;  // Position in der StudentList

    // Schalter für GUI-Management
    private static boolean studentListEnabled;
    private static boolean noRowSelected;
    private static boolean noFieldSelected;
    private boolean studentRowEnabled;

    private static int columns = DataBase.getNumberOfDays() + 1; // Anzahl Tage plus nameField

    public StudentRow() {

        headerRow = new VariableField[columns];
        studentRow = new StudentField[columns];
        emptyRow = new VariableField[columns];
        setBackground(Colors.BACKGROUND);

        /* Schalter */
        studentListEnabled = true;  // Hauptschalter StudentList: diese ist als ganzes ansprechbar, verhindert Eingaben in StudentList im Lectionfield-Dragmodus 
        noRowSelected = true;  // jede Row ist scrollbar
        noFieldSelected = true;  // falls mind. ein Feld selektiert
        studentRowEnabled = true;  // selektierte Row = eingeteilter Student ist nicht mehr ansprechbar

        setLayout(new GridLayout(1, columns, 1, 0));

    }

    /* HeaderRow mit Daten befüllen und zeichnen  */
    public void createHeaderRow() {

        headerRow[0] = new VariableField("Vorname Name", Colors.NAME_FIELD_SELECTED);
        headerRow[0].setFont(this.getFont().deriveFont(Font.BOLD, 10));
        headerRow[0].setForeground(Color.WHITE);
        add(headerRow[0]);

        for (int i = 1; i < columns; i++) {
            headerRow[i] = new DayField(DataBase.getDayDataList(i - 1).getDayName(), Colors.DAY_FIELD);
            headerRow[i].setFont(this.getFont().deriveFont(Font.BOLD, 10));
        //    headerRow[i].addMouseListener(new DayFieldListener());
            add(headerRow[i]);
        }
    }

    /* StudentFields mit Student-Daten befüllen und initialisieren */
    public void createStudentRow(Student s) {

        studentRow[0] = new NameField(s.getFirstName(), s.getName());
        studentRow[0].addMouseListener(new NameFieldListener());
        studentRow[0].setStudentID(studentIndex);  // Student-ID wird an NameField weitergeleitet
        add(studentRow[0]);

        for (int i = 1; i < columns; i++) {   // Zeitfelder adden -> i = 1 -> 1. Tag usw.

            studentRow[i] = new StudentField();
            studentRow[i].addMouseListener(this);  // Referenz auf StudentRow
            studentRow[i].addMouseListener(Schedule.getDayColumn(i - 1)); // Referenz auf den Tag(1. Tag-> i=0) für DayColumn-Aktionen
            studentRow[i].setStudentID(studentIndex); // Student-ID wird an jedes StudentField weitergeleitet
            studentRow[i].setFirstName(s.getFirstName());
            studentRow[i].setName(s.getName());
            studentRow[i].setDay(s.getStudentDay(i - 1));  // StudentField bekommt die gekapselten Time-Daten des entspr. Tages(= StudentDay)

            //------------Rohfassung: Referenz auf studentDayList----------
            studentRow[i].setStudentDayList(s.getStudentDayList());

            for (DayColumn d : Schedule.getDayColumnList()) {   // jedes StudentField bekommt eine Referenz auf alle DayColumns
                studentRow[i].addValidTimeListener(d);
            }

            studentRow[i].setLectionType(s.getLectionType());
            studentRow[i].showAvailableTimes();
            add(studentRow[i]);
        }
    }

    public void createEmptyRow() {

        for (int i = 0; i < columns; i++) {   // leere Felder adden
            emptyRow[i] = new VariableField(" ", Colors.BACKGROUND);
            emptyRow[i].setPreferredSize(new Dimension(20, 20));
            add(emptyRow[i]);
        }
    }

    public void cleanStudentRow(boolean cleanMode) {

        studentListEnabled = true;

        if (cleanMode) {   // StudentRow wird markiert und gesperrt 
            studentRow[0].setBackground(Colors.LIGHT_GRAY);
            studentRow[0].setForeground(Color.GRAY);
            studentRow[0].setFont(this.getFont().deriveFont(Font.PLAIN, 10));

            for (int i = 1; i < columns; i++) {
                studentRow[i].setBackground(Colors.LIGHT_GRAY);
                studentRow[i].setForeground(Color.GRAY);
                studentRow[i].setFieldSelected(false);
            }
            studentRowEnabled = !cleanMode;

        } else { // StudentRow wird entsperrt 
            studentRow[0].setBackground(Colors.NAME_FIELD);
            studentRow[0].setForeground(Color.BLACK);
            studentRow[0].setFont(this.getFont().deriveFont(Font.PLAIN, 10));

            for (int i = 1; i < columns; i++) {
                studentRow[i].setBackground(Colors.STUDENT_FIELD_BLUE);
                studentRow[i].setForeground(Color.BLACK);
                studentRow[i].setFieldSelected(false);
            }
            studentRowEnabled = !cleanMode;
        }
        noRowSelected = true;
        noFieldSelected = true;
    }

    /* Schalter */
    public static void setStudentListEnabled(boolean enabled) {
        studentListEnabled = enabled;
    }

    public static boolean isStudentListEnabled() {
        return studentListEnabled;
    }

    /* Getter, Setter usw.  */
    public void setStudentIndex(int studentIndex) {
        this.studentIndex = studentIndex;
    }

    public static boolean noFieldSelected() {
        return noFieldSelected;
    }

    public StudentField getStudentFieldAt(int index) {
        return studentRow[index];
    }

    /* MouseListener-Implementationen */
    @Override
    public void mouseClicked(MouseEvent m) {

        if (studentListEnabled) {
            if (noRowSelected && studentRowEnabled) { // Row-"Hauptschalter" on = jede Row kann selektiert werden
                if (noFieldSelected) {  // StudentField selektierbar, falls kein NameField selektiert 
                    drawRow(m.getSource());
                    noFieldSelected = false;
                } else // Row-"Hauptschalter" off = nur die aktuelle Row ist selektierbar
                if (fieldsStillSelected()) { //  weitere StudentFields selektieren, es kann keine andere Row selektiert werden, solange Felder in dieser Row selektiert sind 
                    drawRow(m.getSource());
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent m) {

        if (studentListEnabled) {
            if (noRowSelected && noFieldSelected && studentRowEnabled) {   // Feld nicht selektiert
                for (int i = 0; i < columns; i++) {
                    if (i == 0) {
                        studentRow[i].setBackground(Colors.NAME_FIELD_SELECTED);
                        studentRow[i].setForeground(Color.WHITE);
                        studentRow[i].setFont(this.getFont().deriveFont(Font.BOLD, 10));
                    } else {
                        studentRow[i].setBackground(Colors.LIGHT_GREEN);
                    }
                }
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent m) {

        if (studentListEnabled) {
            if (noRowSelected && noFieldSelected && studentRowEnabled) {
                for (int i = 0; i < columns; i++) {
                    if (i == 0) {
                        studentRow[i].setBackground(Colors.NAME_FIELD);
                        studentRow[i].setForeground(Color.BLACK);
                        studentRow[i].setFont(this.getFont().deriveFont(Font.PLAIN, 10));
                    } else {
                        studentRow[i].setBackground(Colors.STUDENT_FIELD_BLUE);
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent m) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    /* Hilfsmethode für MouseListener Implementation */
    private void drawRow(Object source) {

        for (int i = 1; i < columns; i++) {
            if (source.equals(studentRow[i])) {
                if (studentRow[i].isFieldSelected()) {
                    studentRow[i].setBackground(Colors.LIGHT_GREEN);
                    studentRow[i].setFieldSelected(false);
                } else {
                    studentRow[i].setBackground(Colors.DARK_GREEN);
                    studentRow[i].setFieldSelected(true);
                }
            }
        }
        if (!fieldsStillSelected()) {
            noFieldSelected = true;
        }
    }

    /* Hilfsmethode für Listener-Implementation Schalter: wenn mind. ein StudentField selektiert ist */
    private boolean fieldsStillSelected() {

        for (int i = 0; i < columns; i++) {
            if (studentRow[i].isFieldSelected()) {
                return true;
            }
        }
        return false;
    }

    /* innere Listenerklassen  */
    private class NameFieldListener extends MouseAdapter {

        private StudentDataEntry mask;

        @Override
        public void mouseClicked(MouseEvent m) {
            if (studentListEnabled && noFieldSelected && studentRowEnabled && m.getClickCount() == 2) {  // Namefield nur selektierbar, wenn kein StudentField selektiert
                studentRow[0].setBackground(Colors.NAME_FIELD_SELECTED);
                studentRow[0].setForeground(Color.WHITE);
                studentRow[0].setFont(studentRow[0].getFont().deriveFont(Font.BOLD, 10));

                mask = new StudentDataEntry(); // ToDo
                mask.setVisible(true);
            }

        }

        @Override
        public void mouseEntered(MouseEvent m) {

            if (studentListEnabled) {
                if (noFieldSelected && noRowSelected && studentRowEnabled) {
                    studentRow[0].setBackground(Colors.NAME_FIELD_SELECTED);
                    studentRow[0].setFont(studentRow[0].getFont().deriveFont(Font.BOLD, 10));
                    studentRow[0].setForeground(Color.WHITE);
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent m) {

            if (studentListEnabled) {
                if (noFieldSelected && noRowSelected && studentRowEnabled) {
                    studentRow[0].setBackground(Colors.NAME_FIELD);
                    studentRow[0].setFont(studentRow[0].getFont().deriveFont(Font.PLAIN, 10));
                    studentRow[0].setForeground(Color.BLACK);
                }
            }
        }
    }

//    private class DayFieldListener extends MouseAdapter {
//
//        @Override
//        public void mouseClicked(MouseEvent m) {
//
//            for (int i = 1; i < columns; i++) {
//                if (m.getSource().equals(headerRow[i])) {
//                    if (!headerRow[i].isSelected()) {
//                        headerRow[i].setBackground(Colors.DAY_FIELD_SELECTED);
//                        headerRow[i].switchSelectionState();
//                    } else {
//                        headerRow[i].setBackground(Colors.DAY_FIELD);
//                        headerRow[i].switchSelectionState();
//                    }
//                }
//            }
//        }
//    }
}
