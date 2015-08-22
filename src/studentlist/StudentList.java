/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlist;

import core.DataBase;
import core.DatabaseListener;
import core.ScheduleDay;
import core.Student;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import schedule.DayColumn;
import schedule.LectionField;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class StudentList extends JPanel implements MouseListener, DatabaseListener {

    private static StudentRow[] studentList;  // Liste mit allen StudentRow-Instanzen
    private static StudentRow header;
    private static StudentRow emptyRow;

    private static int rows = DataBase.getNumberOfStudents(); // max. Schülerzahl ohne HeaderRow
    private static int temporaryListSize;

    /* Default-Minimalhöhe  */
    private static final int MAX_ROWS = 40;

    public StudentList() {

        studentList = new StudentRow[rows];

      setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        DataBase.addDatabaseListener(this);
        setBackground(Colors.BACKGROUND);

        /*   leere HeaderRow instanzieren   */
        header = new StudentRow();
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));

        /* studentList initialisieren und mit leeren StudentRows instanzieren*/
        for (int i = 0; i < rows; i++) {
            studentList[i] = new StudentRow();
            studentList[i].setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
        }
   }

    /* ganze StudentList zeichnen // ToDo: wenn dynamische Eingabe -> auslösen erst, wenn "ok"- Button gedrückt*/
    public void createStudentList() {
        
        temporaryListSize = DataBase.getStudentIndex();  // bekommt die momentane List-Länge ( = Anzahl Student-Einträge)

        header.createHeaderRow();
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
        add(header);

        for (int i = 0; i < temporaryListSize; i++) {

            studentList[i].setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
            add(studentList[i]);
        }
        
        /* nach dem letzten Schülereintrag mit leeren Reihen auffüllen -> konstante Höhe*/
        for (int i = temporaryListSize; i < MAX_ROWS; i++) {
            emptyRow = new StudentRow();
            emptyRow.createEmptyRow();
            add(emptyRow);
        }
    }

    /*  Implementation für Zugriff auf die einzelnen Rows via die Studentlist,
     es muss somit nur ein Listener an jedes LectionField gehängt werden
     */
    @Override
    public void mouseClicked(MouseEvent m) {

        LectionField l;

        if (m.getSource() instanceof LectionField) {

            l = (LectionField) m.getSource();

            /* StudentRow als eingeteilt markieren, l.isSelected ->klick auf leeren Stundenplan darf keine Wirkung haben*/
            if (l.isSelected() && l.getFieldPosition() == 0 && !DayColumn.isDragEnabled()) {
                studentList[l.getStudentID()].cleanStudentRow(true);  // StudentID = Position in StudentList
            } else {
                StudentRow.setStudentListEnabled(false);  // während Lection-Drag ist Studentlist gesperrt
            }
            /* Doppelklick auf Lectionpanel = StudentRow entsperren  */
            if (m.getClickCount() == 2 && (l.getFieldPosition() == 3 || l.getFieldPosition() == 4) && !DayColumn.isDragEnabled()) {
                studentList[l.getStudentID()].cleanStudentRow(false);
            }
            /* StudentList nur während Dragen gesperrt, wenn Lection gesetzt, muss StudentList wieder aktiv sein */
            if (!DayColumn.isDragEnabled()) {
                StudentRow.setStudentListEnabled(true);
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

    /*  DatabaseListener Implementation  */
    @Override
    public void dayAdded(ScheduleDay d) {
     }
    @Override
    public void dayRemoved(ScheduleDay d) {
    }
    @Override
    public void dayEdited(ScheduleDay d) {
    }
    
    /*   StudentRow instanzieren und befüllen  */
    @Override
    public void studentAdded(Student s) {

        int i = s.getStudentIndex(); // der List-Index (= Student-ID) aus der DataList wird übernommen
        studentList[i].setStudentIndex(i);  // jede StudentRow-Instanz bekommt ihre Position in der StudentList (= Student-ID)
        studentList[i].createStudentRow(s);   // StudentRow befüllen
    }
    
    @Override
    public void studentRemoved(Student s) {
    }
    @Override
    public void studentEdited(Student s) {
    }
}
