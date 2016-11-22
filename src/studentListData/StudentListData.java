/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentListData;

import core.Database;
import core.DatabaseListener;
import core.Profile;
import core.ProfileTypes;
import static core.ProfileTypes.*;
import dataEntryUI.group.GroupEdit;
import dataEntryUI.group.kgu.KGUEdit;
import dataEntryUI.student.StudentEdit;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import mainframe.MainFrame;
import scheduleUI.TimeTable;
import scheduleData.ScheduleData;
import scheduleData.ScheduleFieldData;
import studentlistUI.StudentList;
import userUtilsUI.ColoredStudentDays;
import scheduleData.ScheduleFieldConstants;

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

    public void updateAfterFileEntry() {
        fieldDataMatrix.clear();
        init();
        createStudentRows();
        setIncompatibleStudentDays();
        setStudentListReleased(true);
    }

    public void updateAfterScheduleEdit() {
        numberOfDays = database.getNumberOfDays();
        updateStudentAllocationState();
        fieldDataMatrix.clear();
        createStudentRows();
        coloredStudentDays.update();
        setIncompatibleStudentDays();
        setStudentListReleased(true);
    }

    private void createStudentRows() {
        for (int i = 0; i < numberOfStudents; i++) {
            createStudentRow(database.getProfile(i));
        }
    }

    @Override
    public void profileAdded(int numberOfStudents, Profile profile) {
        this.numberOfStudents = numberOfStudents;
        createStudentRow(profile);
        updateKGUMembersAllocationState();
        studentList.showNumberOfStudents();
        coloredStudentDays.updateIncompatibleStudentDays();
    }

    @Override
    public void profileEdited(Profile profile) {
        updateStudentRow(profile);
        updateKGUMembersAllocationState();
        coloredStudentDays.updateIncompatibleStudentDays();
    }

    @Override
    public void profileDeleted(int numberOfStudents, Profile profile) {
        this.numberOfStudents = numberOfStudents;
        int deletedProfileID = profile.getProfileID();
        removeStudentRow(deletedProfileID);
        updateKGUMembersAllocationState();
        updateStudentIDs();
        studentList.showNumberOfStudents();
        coloredStudentDays.updateIncompatibleStudentDays();
    }

    private void createStudentRow(Profile profile) {
        studentRow = new StudentFieldData[getColumnCount()];
        for (int col = 0; col < getColumnCount(); col++) {
            studentRow[col] = new StudentFieldData(database);
            studentRow[col].setProfileID(profile.getProfileID());
            studentRow[col].setLectionProfileType(profile.getProfileType());
            studentRow[col].setProfileAllocated(profile.isAllocated());
            if (col > 0) {
                studentRow[col].setSingleDay(profile.getDaySelectionStateAt(col - 1));
                studentRow[col].setDayIndex(col - 1);
            }
            setNameAndTimes(col, profile);
        }
        fieldDataMatrix.add(studentRow);
    }

    private void updateStudentRow(Profile profile) {
        studentRow = fieldDataMatrix.get(profile.getProfileID());
        for (int col = 0; col < getColumnCount(); col++) {
            studentRow[col].setLectionProfileType(profile.getProfileType());
            if (col > 0) {
                studentRow[col].setSingleDay(profile.getDaySelectionStateAt(col - 1));
            }
            setNameAndTimes(col, profile);
        }
    }

    private void updateStudentIDs() {
        for (int i = 0; i < numberOfStudents; i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                fieldDataMatrix.get(i)[j].setProfileID(i);
            }
        }
    }

    private void removeStudentRow(int row) {
        fieldDataMatrix.remove(row);
    }

    private void updateKGUMembersAllocationState() {
        for (int i = 0; i < numberOfStudents; i++) {
            Profile profile = database.getProfile(i);
            if (profile.getProfileType() == ProfileTypes.KGU_MEMBER) {
                setRowAllocationState(profile.getProfileID(), profile.isAllocated());
            }
        }
    }

    private void updateStudentAllocationState() {
        for (int i = 0; i < numberOfStudents; i++) {
            database.getProfile(i).setAllocated((getValueAt(i, 0)).isProfileAllocated());
        }
    }

    private void setNameAndTimes(int col, Profile profile) {
        if (col == 0) {
            studentRow[col].setNameString(profile.getFirstName() + " " + profile.getName() + " " + profile.getThirdName());
        } else {
            studentRow[col].setValidTimeString("<html>" + profile.getStudentDay(col - 1) + "<font color=blue>" + profile.getStudentDay(col - 1).favorite().toString() + "</font></html>");
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
            return "  Vorname Name  (" + database.getNumberOfSingleStudents() + ")";
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
                if (!fieldData.isProfileAllocated()) {
                    if (selectedCol > 0 && fieldData.getLectionProfileType() != KGU_MEMBER) { // NameField nicht ansprechbar
                        if (isStudentListReleased()) { // 1. StudentDay selektieren
                            setRowSelected(selectedRow);
                            fieldData.switchSelectionState();
                            blockStudentList();
                        } else if (selectedRow == fieldData.selectedRowIndex()) { // weitere Selections bzw. Selections rückgängig machen
                            fieldData.switchSelectionState();
                            if (isRowReleased(selectedRow)) { // alle Selections gelöscht
                                releaseStudentListAtViewCoordinates(selectedRow);
                            }
                        }
                        mainFrame.setDataEntryButtonsEnabled(isStudentListReleased());
                        mainFrame.setFileButtonsEnabled(isStudentListReleased());
                        fireTableDataChanged();
                    } else if (selectedCol == 0 && m.getClickCount() == 2 && isStudentListReleased()) {
                        Profile profile = fieldData.getProfile();
                        if (profile.getProfileType() == GROUP) { // Gruppenprofil ändern/löschen
                            if (profile.getProfileName().equals(KGU_NAME)) { // KGU
                                KGUEdit kguEdit = new KGUEdit(mainFrame, profile);
                                kguEdit.setVisible(true);
                            } else { // andere Gruppen
                                GroupEdit groupEditDialog = new GroupEdit(mainFrame, profile);
                                groupEditDialog.selectProfile();
                                groupEditDialog.setVisible(true);
                            }
                        } else { // Schülerprofil ändern/löschen
                            StudentEdit studentEditDialog = new StudentEdit(mainFrame, profile);
                            studentEditDialog.setVisible(true);
                        }
                    }
                }
            }
            scheduleData.getJournalEntry().setVisible(false);;
        }
       else if (m.getSource() instanceof TimeTable) {
            TimeTable timeTable = (TimeTable) m.getSource();
            selectedRow = timeTable.rowAtPoint(p);
            selectedCol = timeTable.columnAtPoint(p);
            if (selectedRow >= 0 && selectedCol % 2 == 1) { // keine Events aus TimeColumn 
                ScheduleFieldData fieldData = timeTable.getScheduleFieldDataAt(selectedRow, selectedCol);
                int allocatedRow = fieldData.getProfileID();
                Profile profile = fieldData.getProfile();
                if (fieldData.isMoveEnabled()) {
                    if (fieldData.isLectionAllocated()) { // in MoveMode wechseln
                        if (fieldData.getLectionPanelAreaMark() == ScheduleFieldConstants.HEAD) {
                            blockStudentList();
                            mainFrame.setDataEntryButtonsEnabled(false);
                            mainFrame.setFileButtonsEnabled(false);
                        }
                        if (SwingUtilities.isRightMouseButton(m)) { // Einteilung rückgängig
                            setRowAllocationState(allocatedRow, false);
                            studentListReleased = true;
                            profile.setAllocated(false);
                            releaseStudentListAtModelCoordinates(allocatedRow);
                            mainFrame.setDataEntryButtonsEnabled(true);
                            mainFrame.setFileButtonsEnabled(true);
                        }
                    } else {  // in AllocatedMode wechseln
                        setRowAllocationState(allocatedRow, true);
                        studentListReleased = false;
                        profile.setAllocated(true);
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

    private void setRowSelected(int row) {
        for (int j = 0; j < getColumnCount(); j++) {
            studentList.getStudentFieldDataAtView(row, j).setSelectedRowIndex(row);
        }
    }

    private void setRowAllocationState(int allocatedRow, boolean state) {
        for (int j = 0; j < getColumnCount(); j++) {
            fieldDataMatrix.get(allocatedRow)[j].setProfileAllocated(state);
        }
    }

    private void releaseStudentListAtModelCoordinates(int row) {
        studentListReleased = true;
        for (int i = 0; i < getColumnCount(); i++) {
            fieldDataMatrix.get(row)[i].setSelectedRowIndex(NULL_VALUE);
            fieldDataMatrix.get(row)[i].setFieldSelected(false);
        }
    }

    private void releaseStudentListAtViewCoordinates(int row) {
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
        Profile profile = database.getProfile(StudentID);
        if (isBlocked) {
            return "<html>" + "<font color=red>" + profile.getStudentDay(col - 1) + "<font color=red>" + profile.getStudentDay(col - 1).favorite().toString() + "</font></html>";
        } else {
            return "<html>" + profile.getStudentDay(col - 1) + "<font color=blue>" + profile.getStudentDay(col - 1).favorite().toString() + "</font></html>";
        }
    }

    public void setScheduleData(ScheduleData scheduleData) {
        this.scheduleData = scheduleData;
    }

    public Profile getProfile(int i) {
        return database.getProfile(i);
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
