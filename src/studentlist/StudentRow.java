/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlist;

import mainframe.WidgetInteraction;
import core.ScheduleTimes;
import core.Student;
import dialogs.StudentDataEntry;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import schedule.DayColumn;
import schedule.Schedule;

import util.Colors;

/**
 *
 * @author Mathias
 */
public class StudentRow extends JPanel implements MouseListener {

    private WidgetInteraction wi; //globale  Variablen/Schalter für GUI-Management
    private StudentField[] studentFieldList; // Field-Arrays pro StudentRow
    private VariableField[] emptyFieldList; // Field-Arrays für Filler-Rows
    private VariableField[] headerFieldList;
    private int columns; // Anzahl Tage plus nameField
       /* Lokaler Schalter */
    private boolean studentRowEnabled;

    public StudentRow(WidgetInteraction wi, int numberOfDays) {

        this.wi = wi;
        columns = numberOfDays + 1; // alle Tage plus NameField

        headerFieldList = new VariableField[columns];
        studentFieldList = new StudentField[columns];
        studentRowEnabled = true;  // selektierte Row = eingeteilter Student ist nicht mehr ansprechbar

        setLayout(new GridLayout(1, columns, 1, 0));
        setBackground(Colors.BACKGROUND);
    }

    public StudentRow(int numberOfDays) {  // Konstruktor für EmptyRows

        columns = numberOfDays + 1;
        emptyFieldList = new VariableField[columns];

        setBackground(Colors.BACKGROUND);
        setLayout(new GridLayout(1, columns, 1, 0));
    }

    /* HeaderRow initialisieren */
    public void createHeaderRow(ScheduleTimes scheduleTimes) {

        headerFieldList[0] = new VariableField("Vorname Name", Colors.NAME_FIELD_SELECTED);
        headerFieldList[0].setFont(this.getFont().deriveFont(Font.BOLD, 10));
        headerFieldList[0].setForeground(Color.WHITE);
        add(headerFieldList[0]);

        for (int i = 1; i < columns; i++) {
            headerFieldList[i] = new DayField(scheduleTimes.getDayName(i - 1), Colors.DAY_FIELD);
            headerFieldList[i].setFont(this.getFont().deriveFont(Font.BOLD, 10));
            add(headerFieldList[i]);
        }
    }

    public void createEmptyRow() {

        for (int i = 0; i < columns; i++) {
            emptyFieldList[i] = new VariableField(" Schülerdaten... ", Colors.LIGHT_GRAY);
            emptyFieldList[i].setFont(this.getFont().deriveFont(Font.ITALIC, 9));
            emptyFieldList[i].setPreferredSize(new Dimension(20, 20));
            add(emptyFieldList[i]);
        }
    }

    /* StudentFields mit Student-Daten befüllen und initialisieren */
    public void createStudentRow(Student student, Schedule schedule) {

        studentFieldList[0] = new NameField(student.getFirstName(), student.getName());
        studentFieldList[0].addMouseListener(new NameFieldListener());
        studentFieldList[0].setStudentID(student.getStudentID());  // unnötig?  NameField kennt seine Position (= 0) in der Liste
        add(studentFieldList[0]);

        for (int i = 1; i < columns; i++) {   // Zeitfelder adden: 1 = 1. Tag usw.

            studentFieldList[i] = new StudentField();
            studentFieldList[i].addMouseListener(this);  // Referenz auf StudentRow
            studentFieldList[i].addMouseListener(schedule.getDayColumn(i - 1)); // Referenz auf entspr. DayColumn, (i-1), weil counter mit i = 1 beginnt
           /* jedes StudentField bekommt Referenzen auf alle DayColumns, damit die ValidTimes aller Tage farbig angezeigt werden können*/
            for (DayColumn dayColumn : schedule.getDayColumnList()) {
                studentFieldList[i].addValidTimeListener(dayColumn);
            }
            studentFieldList[i].setStudentID(student.getStudentID()); // jedes StudentField kennt seine Position (= ID) in der Liste
            studentFieldList[i].setFirstName(student.getFirstName());
            studentFieldList[i].setName(student.getName());
            studentFieldList[i].setLectionType(student.getLectionType());
            studentFieldList[i].setStudentDay(student.getStudentDay(i - 1));  // jedes StudentField kennt seinen Tag 
            studentFieldList[i].setStudentTimes(student.getStudentTimes()); // jedes StudentField kennt alle StudentDays des Students
            studentFieldList[i].setColor();  // markiert ob ValidDay
            studentFieldList[i].showValidTimes(); // die formatierten TimeStrings der ValidTimes
            add(studentFieldList[i]);
        }
    }

    public void cleanStudentRow(boolean cleanMode) {

        wi.setStudentListEnabled(true);

        if (cleanMode) {   // StudentRow wird markiert und gesperrt 
            studentFieldList[0].setBackground(Colors.LIGHT_GRAY);
            studentFieldList[0].setForeground(Color.GRAY);
            studentFieldList[0].setFont(this.getFont().deriveFont(Font.PLAIN, 10));

            for (int i = 1; i < columns; i++) {
                studentFieldList[i].setBackground(Colors.LIGHT_GRAY);
                studentFieldList[i].setForeground(Color.GRAY);
                studentFieldList[i].setFieldSelected(false);
            }
            studentRowEnabled = !cleanMode;

        } else { // StudentRow wird entsperrt 
            studentFieldList[0].setBackground(Colors.NAME_FIELD);
            studentFieldList[0].setForeground(Color.BLACK);
            studentFieldList[0].setFont(this.getFont().deriveFont(Font.PLAIN, 10));

            for (int i = 1; i < columns; i++) {
                if (studentFieldList[i].getStudentDay().isValidDay()) {
                    studentFieldList[i].setBackground(Colors.STUDENT_FIELD_BLUE);
                    studentFieldList[i].setForeground(Color.BLACK);
                } else {
                    studentFieldList[i].setBackground(Colors.LIGHT_GRAY);
                }
                studentFieldList[i].setFieldSelected(false);
            }
            studentRowEnabled = !cleanMode;
        }
        wi.setNoRowSelected(true);
        wi.setNoFieldSelected(true);
    }

    /* Getter, Setter */
    public VariableField getHeaderField(int i) {
        return headerFieldList[i];
    }

    /* MouseListener-Implementationen */
    @Override
    public void mouseClicked(MouseEvent m) {

        StudentField studentField = (StudentField) m.getSource();

        if (wi.studentListEnabled() && studentField.getStudentDay().isValidDay()) {
            if (wi.noRowSelected() && studentRowEnabled) { // Row-"Hauptschalter" on = jede Row kann selektiert werden
                if (wi.noFieldSelected()) {  // StudentField selektierbar, falls kein NameField selektiert 
                    drawRow(studentField);
                    wi.setNoFieldSelected(false);
                } else // Row-"Hauptschalter" off = nur die aktuelle Row ist selektierbar
                if (fieldsStillSelected()) { //  weitere StudentFields selektieren, es kann keine andere Row selektiert werden, solange Felder in dieser Row selektiert sind 
                    drawRow(studentField);
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent m) {

        if (wi.studentListEnabled()) {
            if (wi.noRowSelected() && wi.noFieldSelected() && studentRowEnabled) {   // Feld nicht selektiert
                for (int i = 0; i < columns; i++) {
                    if (i == 0) {
                        studentFieldList[i].setBackground(Colors.NAME_FIELD_SELECTED);
                        studentFieldList[i].setForeground(Color.WHITE);
                        studentFieldList[i].setFont(this.getFont().deriveFont(Font.BOLD, 10));
                    } else {
                        studentFieldList[i].setBackground(Colors.LIGHT_GREEN);

                    }
                }
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent m) {

        if (wi.studentListEnabled()) {
            if (wi.noRowSelected() && wi.noFieldSelected() && studentRowEnabled) {
                for (int i = 0; i < columns; i++) {
                    if (i == 0) {
                        studentFieldList[i].setBackground(Colors.NAME_FIELD);
                        studentFieldList[i].setForeground(Color.BLACK);
                        studentFieldList[i].setFont(this.getFont().deriveFont(Font.PLAIN, 10));
                    } else {
                        if (studentFieldList[i].getStudentDay().isValidDay()) {
                            studentFieldList[i].setBackground(Colors.STUDENT_FIELD_BLUE);
                        } else {
                            studentFieldList[i].setBackground(Colors.LIGHT_GRAY);
                        }
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
    private void drawRow(StudentField studentField) {

        for (int i = 1; i < columns; i++) {
            if (studentField.equals(studentFieldList[i])) {  //if( source.getStudentDay().isValidDay())
                if (studentFieldList[i].isFieldSelected()) {
                    studentFieldList[i].setBackground(Colors.LIGHT_GREEN);
                    studentFieldList[i].setFieldSelected(false);
                } else {
                    studentFieldList[i].setBackground(Colors.DARK_GREEN);
                    studentFieldList[i].setFieldSelected(true);
                }
            }
        }
        if (!fieldsStillSelected()) {
            wi.setNoFieldSelected(true);
        }
    }

    /* Hilfsmethode für Listener-Implementation Schalter: wenn mind. ein StudentField selektiert ist */
    private boolean fieldsStillSelected() {

        for (int i = 0; i < columns; i++) {
            if (studentFieldList[i].isFieldSelected()) {
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
            if (wi.studentListEnabled() && wi.noFieldSelected() && studentRowEnabled && m.getClickCount() == 2) {  // Namefield nur selektierbar, wenn kein StudentField selektiert
                studentFieldList[0].setBackground(Colors.NAME_FIELD_SELECTED);
                studentFieldList[0].setForeground(Color.WHITE);
                studentFieldList[0].setFont(studentFieldList[0].getFont().deriveFont(Font.BOLD, 10));

                mask = new StudentDataEntry(null); // ToDo...
                mask.setVisible(true);
            }
        }

        @Override
        public void mouseEntered(MouseEvent m) {

            if (wi.studentListEnabled()) {
                if (wi.noFieldSelected() && wi.noRowSelected() && studentRowEnabled) {
                    studentFieldList[0].setBackground(Colors.NAME_FIELD_SELECTED);
                    studentFieldList[0].setFont(studentFieldList[0].getFont().deriveFont(Font.BOLD, 10));
                    studentFieldList[0].setForeground(Color.WHITE);
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent m) {

            if (wi.studentListEnabled()) {
                if (wi.noFieldSelected() && wi.noRowSelected() && studentRowEnabled) {
                    studentFieldList[0].setBackground(Colors.NAME_FIELD);
                    studentFieldList[0].setFont(studentFieldList[0].getFont().deriveFont(Font.PLAIN, 10));
                    studentFieldList[0].setForeground(Color.BLACK);
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
