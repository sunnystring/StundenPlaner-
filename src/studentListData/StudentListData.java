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
    private boolean studentListReleased;
    public static final int NULL_VALUE = -1;

    public StudentListData(Database database, MainFrame mainFrame) {
        this.database = database;
        this.mainFrame = mainFrame;
        numberOfDays = 0;
        numberOfStudents = 0;
        studentListReleased = true;
        fieldDataMatrix = new ArrayList<>();
        coloredStudentDays = new ColoredStudentDays(database, this);
    }

    public void init() {
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

    public void setStudentData() {
        for (int i = 0; i < numberOfStudents; i++) {
            createStudentRow(database.getStudent(i));
        }
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
            studentRow[col].setStudentAllocated(student.isAllocated());
            setStudentListReleased(!student.isAllocated());
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
            database.getStudent(i).setAllocated((getValueAt(i, 0)).isStudentAllocated());
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
        int selectedRow, selectedCol;
        if (m.getSource() instanceof StudentList) {
            selectedRow = studentList.rowAtPoint(p);
            selectedCol = studentList.columnAtPoint(p);
            if (selectedRow >= 0) { //  ausserhalb JTable: selectedRow = -1 
                StudentFieldData fieldData = studentList.getStudentFieldDataAtView(selectedRow, selectedCol);
                if (!fieldData.isStudentAllocated()) {
                    if (selectedCol > 0) { // NameField nicht ansprechbar
                        if (isStudentListReleased()) { // 1. StudentDay selektieren
                            setSelectedRow(selectedRow);
                            fieldData.switchSelectionState();
                            blockStudentList();
                        } else if (selectedRow == fieldData.selectedRowIndex()) { // weitere Selections bzw. Selections rückgängig machen
                            fieldData.switchSelectionState();
                            if (isRowReleased(selectedRow)) { // alle Selections gelöscht
                                releaseStudentAtViewCoordinates(selectedRow);
                            }
                        }
                        mainFrame.setDataEntryButtonsEnabled(isStudentListReleased());
                        mainFrame.setFileButtonsEnabled(isStudentListReleased());
                        fireTableDataChanged();
                    } else if (m.getClickCount() == 2 && isStudentListReleased()) { // Schülerprofil ändern/löschen
                        JDialog studentEditDialog = new StudentEdit(mainFrame, fieldData.getStudent());
                        studentEditDialog.setVisible(true);
                    }
                }
            }
        }
        if (m.getSource() instanceof TimeTable) {
            TimeTable timeTable = (TimeTable) m.getSource();
            selectedRow = timeTable.rowAtPoint(p);
            selectedCol = timeTable.columnAtPoint(p);
            if (selectedRow >= 0 && selectedCol % 2 == 1) { // keine Events aus TimeColumn 
                ScheduleFieldData fieldData = timeTable.getScheduleFieldDataAt(selectedRow, selectedCol);
                int allocatedRow = fieldData.getStudentID();
                Student student = fieldData.getStudent();
                if (fieldData.isMoveEnabled()) {
                    if (fieldData.isLectionAllocated()) { // in MoveMode wechseln
                        if (fieldData.getLectionPanelAreaMark() == ScheduleFieldData.HEAD) {
                            blockStudentList();
                            mainFrame.setDataEntryButtonsEnabled(false);
                            mainFrame.setFileButtonsEnabled(false);
                        }
                        if (fieldData.getLectionPanelAreaMark() == ScheduleFieldData.CENTER && m.getClickCount() == 2) { // Einteilung rückgängig
                            setRowAllocated(allocatedRow, false);
                            student.setAllocated(false);
                            releaseStudentListAtModelCoordinates(allocatedRow);
                            mainFrame.setDataEntryButtonsEnabled(true);
                            mainFrame.setFileButtonsEnabled(true);
                        }
                    } else {  // in AllocatedMode wechseln
                        setRowAllocated(allocatedRow, true);
                        student.setAllocated(true);
                        releaseStudentListAtModelCoordinates(allocatedRow);
                        mainFrame.setDataEntryButtonsEnabled(true);
                        mainFrame.setFileButtonsEnabled(true);
                    }
                }
                studentList.getStudentField().resetRowIndices();
            }
        }
    }

    private boolean isRowReleased(int row) {
        for (int i = 0; i < getColumnCount(); i++) {
            if (studentList.getStudentFieldDataAtView(row, i).isFieldSelected()) {
                return false;
            }
        }
        return true;
    }

    private void setSelectedRow(int row) {
        for (int j = 0; j < getColumnCount(); j++) {
            studentList.getStudentFieldDataAtView(row, j).setSelectedRowIndex(row);
        }
    }

    private void setRowAllocated(int allocatedRow, boolean state) {
        studentListReleased = !state;
        for (int j = 0; j < getColumnCount(); j++) {
            fieldDataMatrix.get(allocatedRow)[j].setStudentAllocated(state);
        }
    }

    private void releaseStudentListAtModelCoordinates(int row) {
        studentListReleased = true;
        for (int i = 0; i < getColumnCount(); i++) {
            fieldDataMatrix.get(row)[i].setSelectedRowIndex(NULL_VALUE);
            fieldDataMatrix.get(row)[i].setFieldSelected(false);
        }
    }

    private void releaseStudentAtViewCoordinates(int row) {
        studentListReleased = true;
        for (int i = 0; i < getColumnCount(); i++) {
            studentList.getStudentFieldDataAtView(row, i).setSelectedRowIndex(NULL_VALUE);
            studentList.getStudentFieldDataAtView(row, i).setFieldSelected(false);
        }
    }

    private void blockStudentList() {
        studentListReleased = false;
    }

    public void showStudentDaysColored(boolean enabled) {
        coloredStudentDays.setMode(enabled);
        studentList.getStudentField().resetRowIndices();
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

    public void setStudentListReleased(boolean studentListReleased) {
        this.studentListReleased = studentListReleased;
    }

    public boolean isStudentListReleased() {
        return studentListReleased;
    }

    public void setStudentList(StudentList studentList) {
        this.studentList = studentList;
    }

    public void setColoredStudentDays(ColoredStudentDays coloredStudentDays) {
        this.coloredStudentDays = coloredStudentDays;
    }

    public ColoredStudentDays getColoredStudentDays() {
        return coloredStudentDays;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
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
