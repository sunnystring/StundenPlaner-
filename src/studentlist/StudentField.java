/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlist;

import core.StudentDay;
import core.StudentTimes;
import core.ValidTimeListener;
import java.awt.Font;
import java.util.ArrayList;
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
    private int studentID;
    private StudentDay studentDay;  // der dem StudentField entsprechende Tag (= column)
    private int lectionType;
    private StudentTimes studentTimes; // alle Tage des Students (Referenz von class Student)
    private ArrayList<ValidTimeListener> validTimeListener; // Referenzen auf alle DayColumns

    // Schalter
    private boolean fieldSelected;

    public StudentField() {

        validTimeListener = new ArrayList<>();
        fieldSelected = false;

        setHorizontalAlignment(SwingConstants.LEADING);
        setBorder(BorderFactory.createEmptyBorder(5, 3, 5, 3));
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
        setOpaque(true);

    }
    /* markiert ValidDays */

    public void setColor() {
        if (studentDay.isValidDay()) {
            setBackground(Colors.STUDENT_FIELD_BLUE);
        } else {
            setBackground(Colors.LIGHT_GRAY);
        }
    }

    /*   Schalter */
    public boolean isFieldSelected() {
        return fieldSelected;
    }

    public void setFieldSelected(boolean fieldSelected) {
        this.fieldSelected = fieldSelected;
    }
    
    /* Getter, Setter */
    public StudentTimes getStudentTimes() { // nötig für die Datentransfer StudentField - LectionField
        return studentTimes;
    }

    public void setStudentTimes(StudentTimes studentTimes) {
        this.studentTimes = studentTimes;
    }

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

    public void setStudentID(int ID) {
        studentID = ID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentDay(StudentDay day) {
        this.studentDay = day;
    }

    public StudentDay getStudentDay() {
        return studentDay;
    }

    public int getLectionType() {
        return lectionType;
    }

    public void setLectionType(int lectionType) {
        this.lectionType = lectionType;
    }

    /* ValidTimeListener = Referenz auf alle DayColumns */
    public void addValidTimeListener(ValidTimeListener l) {
        validTimeListener.add(l);
    }

    /* wird beim Klick auf StudentField in DayColumn aufgerufen: jeder DayColumn wird der 
     entsprechende StudentDay des "selected Students" zugewiesen, damit die ValidTimes beim Lectionpanel farbig angezeigt werden */
    public void setStudentDays() {
        for (int i = 0; i < validTimeListener.size(); i++) {
            validTimeListener.get(i).studentSelected(studentTimes.getStudentDay(i));  // in allen DayColumns werden die entspr. studentDays gesetzt
        }
    }

    /* formatierte Textausgabe der Zeiten aus actualDay */
    public void showValidTimes() {
        super.setText("<html>" + studentDay + "<font color=blue>" + studentDay.getFavoriteAsString() + "</font></html>");
    }

}
