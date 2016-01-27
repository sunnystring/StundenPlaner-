/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentListData;

import core.Database;
import core.DatabaseListener;
import core.Student;
import dataEntryUI.StudentEdit;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.table.AbstractTableModel;
import mainframe.MainFrame;
import scheduleUI.TimeTable;
import scheduleData.ScheduleData;
import scheduleData.ScheduleFieldData;
import studentlistUI.StudentList;

/**
 *
 * TableModel für {@link StudentList}, jede Zelle ist in der fieldDataMatrix als
 * {@link StudentFieldData} abgebildet
 */
public class StudentListData extends AbstractTableModel implements DatabaseListener, MouseListener {

    private Database database;
    private MainFrame mainFrame;
    private StudentList studentList;
    private ScheduleData scheduleData;
    private StudentFieldData[] studentRow;
    private ArrayList<StudentFieldData[]> fieldDataMatrix;
    private int numberOfDays;
    private int numberOfStudents;
    private int selectedRow, selectedCol;
    private int allocatedRow;
    public static final int NULL_VALUE = -1;

    public StudentListData(Database database, MainFrame mainFrame) {
        this.database = database;
        this.mainFrame = mainFrame;
        numberOfDays = 0;
        numberOfStudents = 0;
        fieldDataMatrix = new ArrayList<>();
    }

    @Override
    public void studentAdded(int numberOfStudents, Student student) {
        this.numberOfStudents = numberOfStudents;
        createStudentRow(student);
        int studentID = student.getStudentID();
        fireTableRowsInserted(studentID, studentID);
        studentList.showNumberOfStudents();
    }

    @Override
    public void studentEdited(Student student) {
        updateStudentRow(student);
        int studentID = student.getStudentID();
        fireTableRowsUpdated(studentID, studentID);
    }

    @Override
    public void studentDeleted(int numberOfStudents, int studentID) {
        this.numberOfStudents = numberOfStudents;
        removeStudentRow(studentID);
        fireTableRowsDeleted(studentID, studentID);
        studentList.showNumberOfStudents();
    }

    private void createStudentRow(Student student) {
        studentRow = new StudentFieldData[getColumnCount()];
        for (int i = 0; i < getColumnCount(); i++) {
            StudentFieldData studentFieldData = new StudentFieldData();
            studentFieldData.setStudent(student);
            if (i == 0) {
                studentFieldData.setNameString(student.getFirstName() + " " + student.getName());
            } else {
                studentFieldData.setValidTimeString("<html>" + student.getStudentDay(i - 1) + "<font color=blue>" + student.getStudentDay(i - 1).getFavorite().toString() + "</font></html>");
            }
            studentRow[i] = studentFieldData;
        }
        fieldDataMatrix.add(studentRow);//getRowCount() - 1, 
    }

    private void updateStudentRow(Student student) {
        studentRow = fieldDataMatrix.get(student.getStudentID());
        for (int i = 0; i < getColumnCount(); i++) {
            if (i == 0) {
                studentRow[i].setNameString(student.getFirstName() + " " + student.getName());
            } else {
                studentRow[i].setValidTimeString("<html>" + student.getStudentDay(i - 1) + "<font color=blue>" + student.getStudentDay(i - 1).getFavorite().toString() + "</font></html>");
            }
        }
    }

    private void removeStudentRow(int studentID) {
        fieldDataMatrix.remove(studentID);
    }

    public void adjustColumnsToDayChange() {
        fieldDataMatrix.clear();
        for (Student student : database.getStudentDataList()) {
            student.getStudentTimes().updateValidStudentDays();
            createStudentRow(student);
        }
    }

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
            return "  Vorname Name  (" + String.valueOf(numberOfStudents) + ")";
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
        return fieldDataMatrix.get(row)[col];
    }

    @Override
    public void mousePressed(MouseEvent m) {
        Point p = m.getPoint();
        if (m.getSource() instanceof StudentList) {
            selectedRow = studentList.rowAtPoint(p);
            selectedCol = studentList.columnAtPoint(p);
            if (selectedRow >= 0) { //  ausserhalb JTable: selectedRow = -1 
                StudentFieldData studentFieldData = fieldDataMatrix.get(selectedRow)[selectedCol];
                if (selectedCol > 0) { // NameField nicht ansprechbar
                    if (studentFieldData.isStudentListReleased()) { // StudentDay selektiert 
                        studentFieldData.switchSelectionState();
                        setSelectedRow();
                        blockStudentList();
                    } else if (selectedRow == studentFieldData.getSelectedRowIndex()) { // Selection rückgängig gemacht, aber noch in SelectionState
                        studentFieldData.switchSelectionState();
                        if (isRowReleased(selectedRow)) { // alle Selections gelöscht
                            releaseStudentList();
                        }
                    }
                    if (!studentFieldData.isStudentAllocated()) {
                        mainFrame.setStudentButtonsEnabled(studentFieldData.isStudentListReleased());
                        mainFrame.setScheduleButtonEnabled(studentFieldData.isStudentListReleased());
                    }
                    fireTableRowsUpdated(selectedRow, selectedRow);
                } else if (m.getClickCount() == 2 && studentFieldData.isStudentListReleased()) { // Schülerprofil ändern/löschen
                    JDialog studentEditDialog = new StudentEdit(mainFrame, studentFieldData.getStudent());
                    studentEditDialog.setVisible(true);
                }
            }
        }
        if (m.getSource() instanceof TimeTable) {
            TimeTable timeTable = (TimeTable) m.getSource();
            ScheduleData scheduleData = (ScheduleData) timeTable.getModel();
            selectedRow = timeTable.rowAtPoint(p);
            selectedCol = timeTable.columnAtPoint(p);
            if (selectedRow >= 0 && selectedCol % 2 == 1) { // keine Events aus TimeColumn 
                ScheduleFieldData scheduleFieldData = (ScheduleFieldData) scheduleData.getValueAt(selectedRow, selectedCol);
                allocatedRow = scheduleFieldData.getStudent().getStudentID();
                if (scheduleFieldData.isMoveEnabled()) {
                    if (scheduleFieldData.isLectionAllocated()) { // in MoveMode wechseln
                        if (scheduleFieldData.getLectionPanelAreaMark() == ScheduleFieldData.HEAD) {
                            blockStudentList();
                            mainFrame.setStudentButtonsEnabled(false);
                            mainFrame.setScheduleButtonEnabled(false);
                        }
                        if (scheduleFieldData.getLectionPanelAreaMark() == ScheduleFieldData.CENTER && m.getClickCount() == 2) { // Einteilung rückgängig
                            setAndDisableAllocatedRow(false);
                            releaseStudentList();
                            mainFrame.setStudentButtonsEnabled(true);
                            mainFrame.setScheduleButtonEnabled(true);
                        }
                    } else {  // in AllocatedMode wechseln
                        setAndDisableAllocatedRow(true);
                        releaseStudentList();
                        mainFrame.setStudentButtonsEnabled(true);
                        mainFrame.setScheduleButtonEnabled(true);
                    }
                    fireTableDataChanged();
                }
            }
        }
    }

    private boolean isRowReleased(int selectedRow) {
        for (int i = 0; i < getColumnCount(); i++) {
            if (fieldDataMatrix.get(selectedRow)[i].isFieldSelected()) {
                return false;
            }
        }
        return true;
    }

    private void setSelectedRow() {
        for (int j = 0; j < getColumnCount(); j++) {
            fieldDataMatrix.get(selectedRow)[j].setSelectedRowIndex(selectedRow);
        }
    }

    private void setAndDisableAllocatedRow(boolean allocated) {
        for (int j = 0; j < getColumnCount(); j++) {
            fieldDataMatrix.get(allocatedRow)[j].setStudentAllocated(allocated);
            fieldDataMatrix.get(allocatedRow)[j].setStudentListReleased(!allocated);
        }
    }

    private void releaseStudentList() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                fieldDataMatrix.get(i)[j].setStudentListReleased(!fieldDataMatrix.get(i)[j].isStudentAllocated());
                fieldDataMatrix.get(i)[j].setSelectedRowIndex(NULL_VALUE);
                fieldDataMatrix.get(i)[j].setFieldSelected(false);
            }
        }
    }

    private void blockStudentList() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                fieldDataMatrix.get(i)[j].setStudentListReleased(false);
            }
        }
    }

    public void setScheduleData(ScheduleData scheduleData) {
        this.scheduleData = scheduleData;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public Student getStudent(int i) {
        return database.getStudentDataList().get(i);
    }

    public void setStudentList(StudentList studentList) {
        this.studentList = studentList;
    }

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
