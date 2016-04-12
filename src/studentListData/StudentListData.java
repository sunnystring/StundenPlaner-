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
import userUtilsUI.ColoredStudentDays;

/**
 *
 * TableModel für {@link StudentList}, jede Zelle ist in der fieldDataMatrix als
 * {@link StudentFieldData} abgebildet
 */
public class StudentListData extends AbstractTableModel implements DatabaseListener, MouseListener {

    private final Database database;
    private final MainFrame mainFrame;
    private StudentList studentList;
    private ScheduleData scheduleData;
    private StudentFieldData[] studentRow;
    private ArrayList<StudentFieldData[]> fieldDataMatrix;
    private ColoredStudentDays coloredStudentDays;
    private int numberOfDays;
    private int numberOfStudents;
    private int selectedRow;
    private int allocatedRow;
    public static final int NULL_VALUE = -1;

    public StudentListData(Database database, MainFrame mainFrame) {
        this.database = database;
        this.mainFrame = mainFrame;
        numberOfDays = 0;
        numberOfStudents = 0;
        fieldDataMatrix = new ArrayList<>();
        coloredStudentDays = new ColoredStudentDays(database, this);
    }

    public void setup() {
        numberOfDays = database.getNumberOfDays();
        coloredStudentDays.init(scheduleData);
    }

    public void update() {
        numberOfDays = database.getNumberOfDays();
        updateStudentAllocationState();
        fieldDataMatrix.clear();
        for (int i = 0; i < numberOfStudents; i++) {
            createStudentRow(database.getStudent(i));
        }
        coloredStudentDays.update();
        setIncompatibleStudentDays();

    }

    @Override
    public void studentAdded(int numberOfStudents, Student student) {
        this.numberOfStudents = numberOfStudents;
        createStudentRow(student);
        int row = student.getID();
        fireTableRowsInserted(row, row);
        studentList.showNumberOfStudents();
        coloredStudentDays.updateIncompatibleStudentDays();
    }

    @Override
    public void studentEdited(Student student) {
        updateStudentRow(student);
        int row = student.getID();
        fireTableRowsUpdated(row, row);
        coloredStudentDays.updateIncompatibleStudentDays();

    }

    @Override
    public void studentDeleted(int numberOfStudents, Student student) {
        this.numberOfStudents = numberOfStudents;
        int deletedStudentID = student.getID();
        removeStudentRow(deletedStudentID);
        fireTableRowsDeleted(deletedStudentID, deletedStudentID);
        updateStudentIDs();
        studentList.showNumberOfStudents();
        coloredStudentDays.updateIncompatibleStudentDays();

    }

    private void createStudentRow(Student student) {
        studentRow = new StudentFieldData[getColumnCount()];
        for (int col = 0; col < getColumnCount(); col++) {
            studentRow[col] = new StudentFieldData(database);
            studentRow[col].setStudentID(student.getID());
            studentRow[col].setAllocationState(student.isLectionAllocated());
            studentRow[col].setStudentListReleased(!student.isLectionAllocated());
            if (col > 0) {
                studentRow[col].setSingleDay(student.getDaySelectionStateAt(col - 1));
                studentRow[col].setDayIndex(col - 1);
            }
            setNameAndTimes(col, student);
        }
        fieldDataMatrix.add(studentRow);
    }

    private void updateStudentRow(Student student) {
        studentRow = fieldDataMatrix.get(student.getID());
        for (int col = 0; col < getColumnCount(); col++) {
            if (col > 0) {
                studentRow[col].setSingleDay(student.getDaySelectionStateAt(col - 1));
            }
            setNameAndTimes(col, student);
        }
    }

    private void updateStudentIDs() {
        for (int i = 0; i < numberOfStudents; i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                fieldDataMatrix.get(i)[j].setStudentID(i);
            }
        }
    }

    private void removeStudentRow(int row) {
        fieldDataMatrix.remove(row);
    }

    private void updateStudentAllocationState() {
        for (int i = 0; i < numberOfStudents; i++) {
            database.getStudent(i).setAllocationState(((StudentFieldData) getValueAt(i, 0)).isStudentAllocated());
        }
    }

    private void setNameAndTimes(int col, Student student) {
        if (col == 0) {
            studentRow[col].setNameString(student.getFirstName() + " " + student.getName());
        } else {
            studentRow[col].setValidTimeString("<html>" + student.getStudentDay(col - 1) + "<font color=blue>" + student.getStudentDay(col - 1).favorite().toString() + "</font></html>");
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
    public Class<?> getColumnClass(int col) {
        return StudentFieldData.class;
    }

    @Override
    public StudentFieldData getValueAt(int row, int col) {
        return fieldDataMatrix.get(row)[col];
    }

    @Override
    public void mousePressed(MouseEvent m) {
        Point p = m.getPoint();
        int selectedCol;
        if (m.getSource() instanceof StudentList) {
            selectedRow = studentList.rowAtPoint(p);
            selectedCol = studentList.columnAtPoint(p);
            if (selectedRow >= 0) { //  ausserhalb JTable: selectedRow = -1 
                StudentFieldData studentFieldData = studentList.getStudentFieldDataAt(selectedRow, selectedCol);
                if (selectedCol > 0) { // NameField nicht ansprechbar
                    if (studentFieldData.isStudentListReleased()) { // StudentDay selektiert 
                        studentFieldData.switchSelectionState();
                        setRowSelected();
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
            selectedRow = timeTable.rowAtPoint(p);
            selectedCol = timeTable.columnAtPoint(p);
            if (selectedRow >= 0 && selectedCol % 2 == 1) { // keine Events aus TimeColumn 
                ScheduleFieldData scheduleFieldData = timeTable.getScheduleFieldDataAt(selectedRow, selectedCol);
                allocatedRow = scheduleFieldData.getTempStudentID();
                if (scheduleFieldData.isMoveEnabled()) {
                    if (scheduleFieldData.isLectionAllocated()) { // in MoveMode wechseln
                        if (scheduleFieldData.getLectionPanelAreaMark() == ScheduleFieldData.HEAD) {
                            blockStudentList();
                            mainFrame.setStudentButtonsEnabled(false);
                            mainFrame.setScheduleButtonEnabled(false);
                        }
                        if (scheduleFieldData.getLectionPanelAreaMark() == ScheduleFieldData.CENTER && m.getClickCount() == 2) { // Einteilung rückgängig
                            setRowAllocated(false);
                            releaseStudentList();
                            mainFrame.setStudentButtonsEnabled(true);
                            mainFrame.setScheduleButtonEnabled(true);
                        }
                    } else {  // in AllocatedMode wechseln
                        setRowAllocated(true);
                        releaseStudentList();
                        mainFrame.setStudentButtonsEnabled(true);
                        mainFrame.setScheduleButtonEnabled(true);
                    }
                    studentList.getStudentField().resetStudentRows();
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

    private void setRowSelected() {
        for (int j = 0; j < getColumnCount(); j++) {
            fieldDataMatrix.get(selectedRow)[j].setSelectedRowIndex(selectedRow);
        }
    }

    private void setRowAllocated(boolean state) {
        for (int j = 0; j < getColumnCount(); j++) {
            fieldDataMatrix.get(allocatedRow)[j].setAllocationState(state);
            fieldDataMatrix.get(allocatedRow)[j].setStudentListReleased(!state);
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

    public void showStudentDaysColored(boolean enabled) {
        coloredStudentDays.setMode(enabled);
        studentList.getStudentField().resetStudentRows();
        setStudentDaysColored();
        fireTableDataChanged();
    }

    public void setIncompatibleStudentDays() {
        coloredStudentDays.findIncompatibleStudentTimes();
        setStudentDaysColored();
    }

    private void setStudentDaysColored() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 1; j < getColumnCount(); j++) {
                StudentFieldData field = fieldDataMatrix.get(i)[j];
                field.setFieldColor(coloredStudentDays.getFieldColorAt(i, j - 1, field.isIncompatible(), field.isSingleDay()));
                field.setValidTimeString(getColoredTimeString(field.isBlocked() || field.isUnallocatable(), i, j));
            }
        }
    }

    private String getColoredTimeString(boolean isBlocked, int StudentID, int col) {
        Student student = database.getStudent(StudentID);
        if (isBlocked) {
            return "<html>" + "<font color=red>" + student.getStudentDay(col - 1) + "<font color=red>" + student.getStudentDay(col - 1).favorite().toString() + "</font></html>";
        } else {
            return "<html>" + student.getStudentDay(col - 1) + "<font color=blue>" + student.getStudentDay(col - 1).favorite().toString() + "</font></html>";
        }
    }

    public void setScheduleData(ScheduleData scheduleData) {
        this.scheduleData = scheduleData;
    }

    public Student getStudent(int i) {
        return database.getStudent(i);
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
