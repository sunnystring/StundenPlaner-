/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule;

import core.StudentTimes;
import core.ValidTimeListener;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import studentlist.StudentField;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class LectionField extends JLabel {

    private String name, firstName;
    private int studentID;
    private int lectionType;
    private StudentTimes studentTimes; // alle Tage des Students (Referenz von Student-> StudentField) 

  //  private StudentDay[] studentDayList;  // Referenz auf Liste aller Tage eines Schülers (class Student)
    private ArrayList<ValidTimeListener> validTimeListener;  // ValidTimeListener = Refernz auf eine DayColumn

    // bei firstEntry darf nur der aktuelle Tag aktiv sein für validTimes
    private boolean timeColumnEnabled;
    // falls Schülerzeit ausserhalb Lehrerzeit ( = scheduleStart, scheduleEnd) kein TimeColumn-Scrolling
    private boolean outOfBounds;

    /* Schalter, Panel-Konstruktions-Variablen */
    private boolean lectionSelected; // Lection definitiv  gesetzt oder wieder verschiebbar
    private boolean lectionTemporarySelected; // Switch: als provisorisch markieren
    private int indexLectionField; // Position in LectionField-Liste
    private int lectionID;  // = rowIndexLectionField des 1. Fields
    private int fieldPosition;  // Position des LectionFields innerhalb des Lectionpanels

    public LectionField() {

        validTimeListener = new ArrayList<>();

        lectionSelected = false;
        lectionTemporarySelected = false;
        lectionID = 0;
        fieldPosition = 0;
        studentID = 0;

        timeColumnEnabled = false;  // enabled: DayColumn#markValidTime(), disabled: DayColumn#cleanValidTimeMark() 
        outOfBounds = false;

        setHorizontalAlignment(SwingConstants.LEADING);
        setFont(this.getFont().deriveFont(Font.BOLD, 10));
        setText(" ");
        setBackground(Colors.BACKGROUND);
        setPreferredSize(new Dimension(0, 11));
        setOpaque(true);

    }

    /*   Schalter   */
    public void setFieldSelected(boolean setSelected) {
        this.lectionSelected = setSelected;
    }

    public boolean isSelected() {
        return lectionSelected;
    }

    public void setTemporarySelected(boolean setSelected) {
        lectionTemporarySelected = setSelected;
    }

    public boolean isTemporarySelected() {
        return lectionTemporarySelected;
    }

    /* überträgt relevante Schülerdaten von StudentField zu LectionField*/
    public void transferStudentDataFrom(StudentField studentField) {
        this.studentID = studentField.getStudentID();
        this.name = studentField.getName();
        this.firstName = studentField.getFirstName();
        this.lectionType = studentField.getLectionType();
        this.studentTimes = studentField.getStudentTimes(); // für Zugriff auf alle StudentDays
        //    this.studentDayList = studentField.getStudentDayList(); // für Zugriff auf alle StudentDays
    }

    /* Pseudo-clone(): überträgt relevante Schülerdaten von LectionField zu LectionField */
    public void transferStudentDataFrom(LectionField lectionField) {
        this.studentID = lectionField.getStudentID();
        this.name = lectionField.getName();
        this.firstName = lectionField.getFirstName();
        this.lectionType = lectionField.getLectionType();
        this.studentTimes = lectionField.getStudentTimes();  // für Zugriff auf alle StudentDays
        //     this.studentDayList = lectionField.getStudentDayList(); // für Zugriff auf alle SchülerDays
    }

    /* Getter, Setter für Lection-Panel-Konstruktion */
    public void setRowIndex(int rowIndex) {
        this.indexLectionField = rowIndex;
    }

    public int getRowIndex() {
        return indexLectionField;
    }

    // macht LectionFields als Gruppe (= Lectionpanel) ansprechbar
    public void setLectionID(int lectionID) {
        this.lectionID = lectionID;
    }

    public int getLectionID() {
        return lectionID;
    }

    public void setFieldPosition(int fieldPosition) {
        this.fieldPosition = fieldPosition;
    }

    public int getFieldPosition() {
        return fieldPosition;
    }

    /* Getter für Student-Daten */
    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }
    public String getName() {
        return name;
    }


    public String getFirstName() {
        return firstName;
    }

    public int getLectionType() {
        return lectionType;
    }


    public StudentTimes getStudentTimes() {   // nötig für die Datentransfer LectionField - LectionField
        return studentTimes;
    }

    public void setTimeColumnEnabled(boolean timeColumnEnabled) {
        this.timeColumnEnabled = timeColumnEnabled;
    }

    public boolean isTimeColumnEnabled() {
        return timeColumnEnabled;
    }

    public void setOutOfBounds(boolean outOfBounds) {
        this.outOfBounds = outOfBounds;
    }

    public boolean isOutOfBounds() {
        return outOfBounds;
    }

    /* malt TimeColumns in allen DayColumn-Instanzen */
    public void markTimeColumns() {

        for (int i = 0; i < validTimeListener.size(); i++) {
            validTimeListener.get(i).validTimeSelected(studentTimes.getStudentDay(i));  //
        }
    }

    /* löscht Markierungen in allen TimeColumns */
    public void cleanTimeColumns() {

        for (ValidTimeListener l : validTimeListener) {
            l.validTimeDeselected();
        }
    }

    /*  setzt validTimes in allen DayColumns */
    public void setStudentDays() {

        for (int i = 0; i < validTimeListener.size(); i++) {
            validTimeListener.get(i).studentSelected(studentTimes.getStudentDay(i));  // in allen DayColumns werden die entspr. studentDays gesetzt
        }
    }

    /* ValidTimeListener*/
    public void addValidTimeListener(ValidTimeListener l) {
        validTimeListener.add(l);
    }

    public void removeValidTimeListener(ValidTimeListener l) {
        validTimeListener.remove(l);
    }

}
