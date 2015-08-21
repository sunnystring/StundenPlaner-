/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlist;

import core.DataBase;
import core.StudentDay;
import core.ValidTimeListener;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class StudentField extends JLabel {

    private String name, firstName;
    private int studentID;  // jedes StudentField "kennt" seine Position in der Studentlist
    private StudentDay studentDay;
    private int lectionType;

    private StudentDay[] studentDayList;  // Referenz auf Liste aller Tage eines Schülers (class Student)

    private final ValidTimeListener[] validTimeListener;
    private int listCount;

    // Schalter
    private boolean fieldSelected;

    public StudentField() {

        this.firstName = firstName;
        this.name = name;

        fieldSelected = false;

        validTimeListener = new ValidTimeListener[DataBase.getNumberOfDays()];
        listCount = 0;

        setHorizontalAlignment(SwingConstants.LEADING);
        setBorder(BorderFactory.createEmptyBorder(5, 3, 5, 3));
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
        setBackground(Colors.STUDENT_FIELD_BLUE);
        setOpaque(true);

    }

    /*   Schalter */
    public boolean isFieldSelected() {
        return fieldSelected;
    }

    public void setFieldSelected(boolean fieldSelected) {
        this.fieldSelected = fieldSelected;
    }

    /* Getter, Setter */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStudentID(int id) {
        studentID = id;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setDay(StudentDay day) {
        studentDay = day;
    }

    public StudentDay getDay() {
        return studentDay;
    }

    public int getLectionType() {
        return lectionType;
    }

    public void setLectionType(int lectionType) {
        this.lectionType = lectionType;
    }

    /* ValidTimeListener */
    public void addValidTimeListener(ValidTimeListener l) {
        validTimeListener[listCount] = l;
        listCount++;
    }

    public void setStudentDays() {
        for (int i = 0; i < validTimeListener.length; i++) {
            validTimeListener[i].studentSelected(studentDayList[i]);
        }
    }

    /* -----------------Rohfassung: Referenz auf studentDayList */
    public StudentDay[] getStudentDayList() {
        return studentDayList;
    }

    public void setStudentDayList(StudentDay[] list) {
        studentDayList = list;
    }

    /* formatierte Textausgabe der Zeiten aus studentDay */
    public void showAvailableTimes() {
        super.setText("<html>" + studentDay + "<font color=blue>" + studentDay.getFavoriteAsString() + "</font></html>");
    }

}
