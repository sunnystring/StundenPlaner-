/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlist;

import core.DataBase;
import mainframe.WidgetInteraction;
import core.ScheduleTimes;
import core.Student;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import schedule.LectionField;
import schedule.Schedule;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class StudentList extends JComponent implements MouseListener {

    private DataBase database;
    private ArrayList<StudentRow> studentRowList;  // i = 0: HeaderRow
    private WidgetInteraction wi; // Variablen/Schalter für GUI-Management
    private static final int MAX_ROWS = 35;  // Default-Minimalhöhe

    public StudentList(DataBase database, WidgetInteraction wi) {

        this.database = database;  // Referenz auf DataBase (aus MainFrame), wird hier nirgends benutzt
        this.wi = wi;
        studentRowList = new ArrayList<>();

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Colors.BACKGROUND);
    }

    /* StudentList initialisieren (noch ohne Student-Einträge) */
    public void createEmptyStudentList(ScheduleTimes scheduleTimes) { // Liste mit MAX_ROWS EmptyRows anfüllen

        StudentRow headerRow = new StudentRow(wi, database.getNumberOfDays());
        headerRow.createHeaderRow(scheduleTimes);
        headerRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
        studentRowList.add(headerRow);  // HeaderRow in Liste

        for (int i = 0; i < MAX_ROWS - 1; i++) {
            StudentRow emptyRow = new StudentRow(database.getNumberOfDays());
            emptyRow.createEmptyRow();
            emptyRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
            studentRowList.add(emptyRow);  // EmptyRows in Liste
        }
        // studentRowList zeichnen      
        for (StudentRow r : studentRowList) {
            add(r);
        }
       
    }

    public void addStudentRow(Student student, Schedule schedule) {  // Referenz auf Schedule für Zugriff auf alle DayColumns

        int index = student.getStudentID() + 1; // HeaderRow: index = 0

        StudentRow studentRow = new StudentRow(wi, database.getNumberOfDays());
        studentRow.createStudentRow(student, schedule); // StudentFields mit Daten befüllen und StudentRow zeichnen
        showNumberOfStudents(database.getNumberOfStudents());
        studentRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
        studentRowList.add(index, studentRow); // StudentRow in Liste einfügen

        // ToDo: besseres Design, ab MAX_ROWS keine emptyRows mehr
         for (StudentRow r : studentRowList) {
            add(r);    // studentRowList zeichnen  
        }
    }

    private void showNumberOfStudents(int number) {
        studentRowList.get(0).getHeaderField(0).setText("<html>" + "Vorname Name  " + "<font color=yellow>" + "(" + String.valueOf(number) + ")" + "</font></html>");
    }

    /*  Implementation für Zugriff auf die einzelnen Rows via die Studentlist, es muss somit nur ein Listener an 
    jedes LectionField gehängt werden */
    @Override
    public void mouseClicked(MouseEvent m) {

        LectionField l;

        if (m.getSource() instanceof LectionField) {

            l = (LectionField) m.getSource();

            /* StudentRow als eingeteilt markieren, l.isSelected ->klick auf leeren Stundenplan darf keine Wirkung haben*/
            if (l.isSelected() && l.getFieldPosition() == 0 && !wi.dragEnabled()) {
                studentRowList.get(l.getStudentID() + 1).cleanStudentRow(true);  // wegen HeaderRow: l.getStudentID() + 1
            } else {
                wi.setStudentListEnabled(false);  // während Lection-Drag ist Studentlist gesperrt
            }
            /* Doppelklick auf Lectionpanel = StudentRow entsperren  */
            if (m.getClickCount() == 2 && (l.getFieldPosition() == 3 || l.getFieldPosition() == 4) && !wi.dragEnabled()) {
                studentRowList.get(l.getStudentID() + 1).cleanStudentRow(false);  // wegen HeaderRow: l.getStudentID() + 1
            }
            /* StudentList nur während Dragen gesperrt, wenn Lection gesetzt, muss StudentList wieder aktiv sein */
            if (!wi.dragEnabled()) {
                wi.setStudentListEnabled(true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

}
