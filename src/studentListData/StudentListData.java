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
    private int allocatedRow;
    public static final int NULL_ROW = -1;
    public static final boolean STUDENTLIST_RELEASED = true, STUDENTLIST_BLOCKED = false;

    public StudentListData(Database database, MainFrame mainFrame) {

        this.database = database;
        this.mainFrame = mainFrame;
        numberOfDays = 0;
        numberOfStudents = 0;
        resetRows();
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

    public void initStudentList(ScheduleData scheduleData) {  // in MainFrame aufgerufen
        numberOfDays = database.getNumberOfDays();
        this.scheduleData = scheduleData;
    }

    /*  Getter, Setter */
    private void resetRows() {
        selectedCol = selectedRow = allocatedRow = NULL_ROW;
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
                    setSelectedRow();
                    blockStudentList();
                } // weitere Selektionen: es kann nur die selektierte Row angesprochen werden
                else if (selectedRow == studentFieldData.getSelectedRowIndex()) {
                    studentFieldData.switchSelectionState();
                    // falls alle Selektionen wieder gelöscht, 
                    if (isRowReleased(selectedRow)) {
                        releaseStudentList();
                    }
                }
                // falls keine AllocatedRow und nicht imSelektions-Modus
                if (!studentFieldData.isStudentAllocated()) {
                    mainFrame.setStudentButtonsEnabled(studentFieldData.isStudentListEnabled());
                }
                fireTableDataChanged();
                resetRows();
            }
        }
        // Schedule (TimeTable)
        if (m.getSource() instanceof TimeTable) {
            TimeTable timeTable = (TimeTable) m.getSource();
            ScheduleData scheduleData = (ScheduleData) timeTable.getModel();
            selectedRow = timeTable.rowAtPoint(p);
            selectedCol = timeTable.columnAtPoint(p);
            if (selectedRow >= 0 && selectedCol % 2 == 1) { //  ausserhalb JTable: selectedRow = -1, keine Events aus TimeColumn 
                ScheduleFieldData scheduleFieldData = (ScheduleFieldData) scheduleData.getValueAt(selectedRow, selectedCol);
                allocatedRow = scheduleFieldData.getStudent().getStudentID();  // StudentID = RowIndex studentFieldDataMatrix
                // aus gesperrten Bereichen keine Clicks
                if (scheduleFieldData.isMoveEnabled()) {
                    if (scheduleFieldData.isLectionAllocated()) {
                        // von Allocated-State in Move-State gewechselt
                        if (scheduleFieldData.getLectionPanelAreaMark() == ScheduleFieldData.HEAD) {
                            blockStudentList();
                            mainFrame.setStudentButtonsEnabled(false);
                        }
                        // Einteilung rückgängig gemacht
                        if (scheduleFieldData.getLectionPanelAreaMark() == ScheduleFieldData.CENTER && m.getClickCount() == 2) {
                            setAndDisableAllocatedRow(false);
                            releaseStudentList();
                            mainFrame.setStudentButtonsEnabled(true);
                        }
                    } // Lection gesetzt, von Move-State in Allocated-State gewechselt
                    else {
                        setAndDisableAllocatedRow(true);
                        releaseStudentList();
                        mainFrame.setStudentButtonsEnabled(true);
                    }
                    fireTableDataChanged();
                    resetRows();
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

    private void setSelectedRow() {
        for (int j = 0; j < getColumnCount(); j++) {
            studentFieldDataMatrix.get(selectedRow)[j].setSelectedRowIndex(selectedRow);
        }
    }

    private void setAndDisableAllocatedRow(boolean allocated) {
        for (int j = 0; j < getColumnCount(); j++) {
            studentFieldDataMatrix.get(allocatedRow)[j].setStudentAllocated(allocated);
            studentFieldDataMatrix.get(allocatedRow)[j].setStudentListEnabled(!allocated);
        }
    }

    private void releaseStudentList() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                studentFieldDataMatrix.get(i)[j].setStudentListEnabled(!studentFieldDataMatrix.get(i)[j].isStudentAllocated());
                studentFieldDataMatrix.get(i)[j].setSelectedRowIndex(NULL_ROW);
                studentFieldDataMatrix.get(i)[j].setFieldSelected(false);
            }
        }
    }

    private void blockStudentList() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                studentFieldDataMatrix.get(i)[j].setStudentListEnabled(false);
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
