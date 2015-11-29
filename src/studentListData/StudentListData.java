/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentListData;

import core.Database;
import core.DatabaseListener;
import core.Student;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import mainframe.MainFrame;
import schedule.TimeTable;
import scheduleData.ScheduleData;
import scheduleData.ScheduleFieldData;
import studentlist.StudentList;

/**
 *
 * @author Mathias
 */
public class StudentListData extends AbstractTableModel implements DatabaseListener, MouseListener {

    private Database database;
    private MainFrame mainFrame;
    private StudentList studentList;
    private ScheduleData scheduleData;
    private StudentFieldData[] studentFieldDataRow; // ändert sich nicht zu Laufzeit
    private ArrayList<StudentFieldData[]> studentFieldDataMatrix; // für direkten Zugriff in getValueAt()
    private int numberOfDays;
    private int numberOfStudents;
    private int selectedRow, selectedCol; // die temporär selektierten JTable-Koordinaten
    public static final int NULL_ROW = -1;
    public static final boolean ENABLE_STUDENTLIST = true;

    public StudentListData(Database database, MainFrame mainFrame) {

        this.database = database;
        this.mainFrame = mainFrame;
        numberOfDays = 0;
        numberOfStudents = 0;
        studentFieldDataMatrix = new ArrayList<>();
    }

    /* DataBaseListerner: ausgeführt bei jedem geaddeten Student in Database*/
    @Override
    public void studentAdded(int numberOfStudents, Student student) {

        this.numberOfStudents = numberOfStudents;  // Update von DataBase
        createStudentFieldDataMatrix(student);
        fireTableDataChanged();
        studentList.showNumberOfStudents();
    }

    private void createStudentFieldDataMatrix(Student student) {  // = studentRowList

        StudentFieldData studentFieldData;
        studentFieldDataRow = new StudentFieldData[getColumnCount()];
        for (int i = 0; i < getColumnCount(); i++) {
            studentFieldData = new StudentFieldData();
            if (i == 0) {
                studentFieldData.setNameString(student.getFirstName() + " " + student.getName());
            } else {
                studentFieldData.setValidTimeString("<html>" + student.getStudentDay(i - 1) + "<font color=blue>" + student.getStudentDay(i - 1).getFavoriteAsString() + "</font></html>");
            }
            studentFieldDataRow[i] = studentFieldData;
        }
        studentFieldDataMatrix.add(getRowCount() - 1, studentFieldDataRow);
    }

    /*  Getter, Setter */
    public void initStudentList(ScheduleData scheduleData) {  // in MainFrame aufgerufen
        numberOfDays = database.getNumberOfDays();
        this.scheduleData = scheduleData;
    }

    public Student getStudent(int i) {
        return database.getStudentList().get(i);
    }

    public void setStudentList(StudentList studentList) {
        this.studentList = studentList;
    }

    /* TableModel */
    @Override
    public int getRowCount() {
        return numberOfStudents;
    }

    @Override
    public int getColumnCount() {
        return numberOfDays + 1;
    }

    @Override
    public String getColumnName(int col) {
        if (col == 0) {
            return "  Vorname Name  (" + String.valueOf(numberOfStudents) + ")"; //numberOfStudentString
        } else {
            return "  " + scheduleData.getDayColumn(col - 1).getDayName();
        }
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return String.class;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return studentFieldDataMatrix.get(row)[col];
    }

    /*  MouseListener */
    @Override
    public void mousePressed(MouseEvent m) {

        Point p = m.getPoint();
        // StudentList
        if (m.getSource() instanceof StudentList) {
            selectedRow = studentList.rowAtPoint(p);
            selectedCol = studentList.columnAtPoint(p);
            if (selectedRow >= 0 && selectedCol > 0) { // NameField hier nicht ansprechbar,   ausserhalb JTable: selectedRow = -1
                StudentFieldData studentFieldData = studentFieldDataMatrix.get(selectedRow)[selectedCol]; // selektiertes StudentField
                // 1. Selektion 
                if (studentFieldData.isStudentListEnabled()) {
                    studentFieldData.switchSelectionState();
                    updateStudentList(selectedRow, NULL_ROW, !ENABLE_STUDENTLIST); // selectedRow setzen, StudentList sperren 
                } // weitere Selektionen: es kann nur die selektierte Row angesprochen werden
                else if (selectedRow == studentFieldData.getSelectedRowIndex()) {
                    studentFieldData.switchSelectionState();
                    // falls alle Selektionen wieder gelöscht, 
                    if (isRowReleased(selectedRow)) {
                        updateStudentList(NULL_ROW, NULL_ROW, ENABLE_STUDENTLIST); // StudentList entsperren, ausser gesetzte allocatedRows
                    }
                }
                if (!studentFieldData.isStudentAllocated()) {
                    mainFrame.setStudentButtonsEnabled(studentFieldData.isStudentListEnabled());
                }
                fireTableRowsUpdated(selectedRow, selectedRow);
            }
        }
        // Schedule (TimeTable)
        if (m.getSource() instanceof TimeTable) {
            TimeTable timeTable = (TimeTable) m.getSource();
            ScheduleData scheduleData = (ScheduleData) timeTable.getModel();
            selectedRow = timeTable.rowAtPoint(p);
            selectedCol = timeTable.columnAtPoint(p);
            ScheduleFieldData scheduleFieldData = (ScheduleFieldData) scheduleData.getValueAt(selectedRow, selectedCol);
            int allocatedRow = scheduleFieldData.getStudent().getStudentID();  // StudentID = RowIndex studentFieldDataMatrix
            if (selectedCol % 2 == 1) { // keine Events aus TimeColumn 
                if (scheduleFieldData.isScheduleEnabled()) { // Schedule darf nicht gesperrt sein
                    // in Allocated-State
                    if (scheduleFieldData.isLectionAllocated() && scheduleFieldData.getLectionPanelArea() == ScheduleFieldData.HEAD) {
                        updateStudentList(NULL_ROW, allocatedRow, ENABLE_STUDENTLIST); // allocatedRow setzen, StudentList entsperren, ausser gesetzte allocatedRows
                        mainFrame.setStudentButtonsEnabled(true);
                    } // in Move-State 
                    else {
                        updateStudentList(NULL_ROW, NULL_ROW, !ENABLE_STUDENTLIST); // ganze StudentList sperren
                        mainFrame.setStudentButtonsEnabled(false);
                    }
                    fireTableRowsUpdated(allocatedRow, allocatedRow);
                }
            }
        }
    }

    private boolean isRowReleased(int selectedRow) {

        for (int i = 0; i < getColumnCount(); i++) {
            if (studentFieldDataMatrix.get(selectedRow)[i].isFieldSelected()) {
                return false;
            }
        }
        return true;
    }

    /* setzt TableModel für die verschiedenen Zustände der StudentList */
    private void updateStudentList(int selectedRow, int allocatedRow, boolean studentListEnabled) {

        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                // StudentList entsperren, ausser gesetzte allocatedRows
                if (studentListEnabled) {
                    studentFieldDataMatrix.get(i)[j].setStudentListEnabled(!studentFieldDataMatrix.get(i)[j].isStudentAllocated());
                    studentFieldDataMatrix.get(i)[j].setSelectedRowIndex(NULL_ROW);
                    studentFieldDataMatrix.get(i)[j].setFieldSelected(false);
                    // allocatedRow setzen
                    if (i == allocatedRow) {
                        studentFieldDataMatrix.get(i)[j].setStudentAllocated(true);
                        studentFieldDataMatrix.get(i)[j].setStudentListEnabled(false);
                    }
                } // StudentList sperren 
                else {
                    studentFieldDataMatrix.get(i)[j].setStudentListEnabled(false);
                    // selectedRow setzen
                    if (i == selectedRow) {
                        studentFieldDataMatrix.get(i)[j].setSelectedRowIndex(selectedRow);
                    }
                }
            }
        }
    }

    //--------------------
    @Override
    public void mouseClicked(MouseEvent me) {
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
