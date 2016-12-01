package scheduleData;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import core.Database;
import core.DatabaseListener;
import core.Profile;
import core.ScheduleTimes;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import exceptions.IllegalLectionEraseException;
import java.awt.Point;
import javax.swing.SwingUtilities;
import scheduleUI.TimeTable;
import studentListData.StudentFieldData;
import studentListData.StudentListData;
import studentlistUI.StudentList;
import utils.Time;
import static scheduleData.ScheduleFieldConstants.*;
import userUtils.BreakWatcher;
import userUtils.LectionGapFiller;
import studentJournal.JournalEntry;

/**
 *
 * TableModel für {@link TimeTable}, jede Zelle ist in der fieldDataMatrix als
 * {@link ScheduleFieldData} abgebildet
 *
 */
public class ScheduleData extends AbstractTableModel implements DatabaseListener, MouseListener {

    private Database database;
    private ScheduleTimes scheduleTimes;
    private StudentListData studentListData;
    private ArrayList<DayColumnData> dayColumnDataList;
    private ScheduleTimeFrame timeFrame;
    private BreakWatcher breakWatcher;
    private ScheduleFieldData[][] fieldDataMatrix;
    private int numberOfValidDays;
    private int dayIndex;
    private int dayColumnFieldIndex;
    private JournalEntry journalEntry;

    public ScheduleData(Database database, StudentListData studentListData) {
        this.database = database;
        this.scheduleTimes = database.getScheduleTimes();
        this.studentListData = studentListData;
        dayColumnDataList = new ArrayList<>();
        timeFrame = new ScheduleTimeFrame();
        breakWatcher = new BreakWatcher(database, this);
        journalEntry = new JournalEntry(database);
        numberOfValidDays = 0;
        dayIndex = -1;
        dayColumnFieldIndex = -1;
        setScheduleDisabled();
    }

    public void setTableData() {
        numberOfValidDays = scheduleTimes.getNumberOfValidDays();
        defineTimeFrame();
        createDayColumns();
        createFieldDataMatrix();
    }

    public void updateTableData() {
        timeFrame.reset();
        numberOfValidDays = scheduleTimes.getNumberOfValidDays();
        defineTimeFrame();
        updateDayColumns();
        createFieldDataMatrix();
    }

    public void defineTimeFrame() {
        for (int i = 0; i < numberOfValidDays; i++) {
            timeFrame.setBounds(scheduleTimes.getValidScheduleDayAt(i));
        }
    }

    public void checkIfScheduleDayErasable() {
        boolean illegalDayEntry = false;
        String illegalDayString = " ";
        for (int i = 0; i < numberOfValidDays; i++) {
            if (scheduleTimes.isExistingDayToBeErasedAt(i)) {
                if (dayColumnDataList.get(i).hasAllocatedLections()) {
                    illegalDayEntry = true;
                    illegalDayString += dayColumnDataList.get(i).getDayName() + " ";
                }
            }
        }
        if (illegalDayEntry) {
            String msg = "Der" + illegalDayString + "kann nicht gelöscht werden!\n"
                    + "Gesetzte Lektionen zurücklegen oder verschieben.";
            throw new IllegalLectionEraseException(msg);
        }
    }

    public void checkIfLectionsWithinTimeFrame() {
        ScheduleTimeFrame tempFrame = scheduleTimes.createTemporaryTimeFrame();
        boolean illegalTimeEntry = false;
        String illegalDayString = " ";
        for (int i = 0; i < numberOfValidDays; i++) {
            if (dayColumnDataList.get(i).hasAllocatedLections()) {
                if (dayColumnDataList.get(i).hasLectionsOutOfBounds(tempFrame.getAbsoluteStart(), tempFrame.getAbsoluteEnd())) {
                    illegalTimeEntry = true;
                    illegalDayString += dayColumnDataList.get(i).getDayName() + " ";
                }
            }
        }
        if (illegalTimeEntry) {
            String msg = "Der" + illegalDayString + "hat noch Lektionen ausserhalb\n"
                    + "des gewählten Zeitrahmens von " + tempFrame.getAbsoluteStart()
                    + " bis " + tempFrame.getAbsoluteEnd();
            throw new IllegalLectionEraseException(msg);
        }
    }

    private void createDayColumns() {
        for (int dayIndex = 0; dayIndex < numberOfValidDays; dayIndex++) {
            DayColumnData dayColumn = new DayColumnData(database, studentListData);
            dayColumn.create(scheduleTimes.getValidScheduleDayAt(dayIndex), timeFrame);
            dayColumnDataList.add(dayColumn);
        }
    }

    private void updateDayColumns() {
        dayColumnDataList.clear();
        for (int dayIndex = 0; dayIndex < numberOfValidDays; dayIndex++) {
            DayColumnData dayColumn = new DayColumnData(database, studentListData);
            dayColumn.update(scheduleTimes.getValidScheduleDayAt(dayIndex), timeFrame);
            dayColumnDataList.add(dayColumn);
        }
    }

    private void createFieldDataMatrix() {
        fieldDataMatrix = new ScheduleFieldData[getRowCount()][getColumnCount()];
        int dayCount = 0;
        for (int j = 0; j < getColumnCount(); j++) {
            for (int i = 0; i < getRowCount(); i++) {
                if (j % 4 == 0 || j % 4 == 1) {
                    fieldDataMatrix[i][j] = dayColumnDataList.get(dayCount).getFieldDataAt(i);
                }
                if (j % 4 == 2 || j % 4 == 3) {
                    fieldDataMatrix[i][j] = dayColumnDataList.get(dayCount).getFieldDataAt(i + getRowCount());
                }
            }
            if (j % 4 == 3) {
                dayCount++;
            }
        }
    }

    @Override
    public int getRowCount() {
        return timeFrame.getTotalNumberOfFields() / 2;
    }

    @Override
    public int getColumnCount() {
        return 4 * numberOfValidDays;
    }

    @Override
    public ScheduleFieldData getValueAt(int row, int col) {
        if (row < 0 || col < 0) {
            return null;
        }
        return fieldDataMatrix[row][col];
    }

    @Override
    public void mousePressed(MouseEvent m) {
        Point p = m.getPoint();
        int row, col;
        if (m.getSource() instanceof StudentList) {
            StudentList studentList = (StudentList) m.getSource();
            row = studentList.rowAtPoint(p);
            col = studentList.columnAtPoint(p);
            if (SwingUtilities.isLeftMouseButton(m) && row >= 0) { //  ausserhalb JTable: selectedRow = -1
                StudentFieldData fieldData;
                Profile profile;
                if (col > 0) {
                    fieldData = studentList.getStudentFieldDataAtView(row, col);
                    DayColumnData dayColumn = getDayColumn(fieldData.getDayIndex());
                    if (fieldData.isFieldSelected()) {  // 1. StudentDay selektiert 
                        profile = fieldData.getProfile();
                        dayColumn.getLectionGapFiller().clear();
                        dayColumn.setValidTimeMarks(fieldData.getStudentDay());
                        setMoveMode(profile);
                    } else if (row == fieldData.selectedRowIndex()) { // weitere StudentDay-Selections, bzw. rückgängig machen
                        dayColumn.clearValidTimeMarks();
                    } else if (studentListData.isStudentListReleased()) { // alle Selections gelöscht
                        setAllocatedMode();
                    }
                } else if (col == 0) {
                    fieldData = studentList.getStudentFieldDataAtView(row, col);
                    if (fieldData.isFieldSelected()) {  // alle StudentDays selektiert 
                        profile = fieldData.getProfile();
                        clearLectionGapFillers();
                        setAllValidTimeMarks(profile);
                        setMoveMode(profile);
                    } else if (row == fieldData.selectedRowIndex()) { // alle StudentDay-Selections rückgängig machen
                        clearAllTimeMarks();
                    } else if (studentListData.isStudentListReleased()) { // alle Selections gelöscht
                        setAllocatedMode();
                    }
                }
                fireTableDataChanged();
            }
            journalEntry.setVisible(false);
        } else if (m.getSource() instanceof TimeTable) {
            TimeTable timeTable = (TimeTable) m.getSource();
            row = timeTable.rowAtPoint(p);
            col = timeTable.columnAtPoint(p);
            if (row >= 0 && col % 2 == 1) { // keine Events aus TimeColumn
                ScheduleFieldData fieldData = timeTable.getScheduleFieldDataAt(row, col);
                Profile profile = fieldData.getProfile();
                int dayIndex = col / 4;
                LectionGapFiller lectionGapFiller = dayColumnDataList.get(dayIndex).getLectionGapFiller();
                if (fieldData.isMoveEnabled()) {
                    convertTableToDayColumnCoordinates(row, col);
                    if (fieldData.isLectionAllocated()) {
                        if (SwingUtilities.isLeftMouseButton(m) && (fieldData.getLectionPanelAreaMark() == HEAD
                                || fieldData.getLectionPanelAreaMark() == NAME_ROW)) { // in MoveMode wechseln
                            eraseLection(profile.getLectionLengthInFields());
                            setAllValidTimeMarks(profile);
                            setMoveMode(profile);
                          //  journalEntry.setVisible(false);
                        }
                        if (SwingUtilities.isLeftMouseButton(m) && fieldData.getLectionPanelAreaMark() == JOURNAL_ROWS
                                || fieldData.getLectionPanelAreaMark() == LAST_ROW) { // JournalEntry öffnen
                            journalEntry.dispose();
                            journalEntry.showTextOf(profile);
                            journalEntry.setVisible(true);
                            return;
                        } 
                        if (SwingUtilities.isRightMouseButton(m)) { // Einteilung rückgängig
                            eraseLection(profile.getLectionLengthInFields());
                        }
                    } else { // in AllocatedMode wechseln
                        createLection(profile.getLectionLengthInFields());
                        setAllocatedMode();
                    }
                    studentListData.setIncompatibleStudentDays();
                    lectionGapFiller.clear();
                    breakWatcher.check(dayIndex);
                } else if (!fieldData.isLectionAllocated()) {  // LectionGapFiller aktivieren/deaktivieren
                    lectionGapFiller.showAvailableTimes(fieldData.getFieldTime(), dayIndex);
                }
                    journalEntry.setVisible(false);
                fireTableDataChanged();
                studentListData.fireTableDataChanged();
            }
        } else {
            journalEntry.setVisible(false);
        }
    }

    /* gesetzte Lections sperren, Sperrzonen setzen, der restliche Schedule einteilbar machen und current Student global setzen*/
    private void setMoveMode(Profile profile) {
        int lectionLength = profile.getLectionLengthInFields();
        int rowCount = timeFrame.getTotalNumberOfFields();
        ScheduleFieldData fieldData;
        for (int studentDayID = 0; studentDayID < numberOfValidDays; studentDayID++) {
            int headRow = -1;
            DayColumnData dayColumn = getDayColumn(studentDayID);
            for (int i = rowCount - 1; i >= 0; i--) {
                fieldData = dayColumn.getFieldDataAt(i);
                if (fieldData.isLectionAllocated()) { // Sperrzone: gesetzte Lection
                    fieldData.setMoveEnabled(false);
                    if (fieldData.getLectionPanelAreaMark() == HEAD) {
                        headRow = i;
                    }
                } else { // einteilbare Zone, ausser lectionLength unterhalb Lection und vor Stundenplan-Ende 
                    boolean isAllocatable = !(i < headRow && i > headRow - lectionLength) && (i <= rowCount - lectionLength);
                    fieldData.setMoveEnabled(isAllocatable);
                    fieldData.setProfileID(profile.getID());
                    fieldData.setLectionProfileType(profile.getProfileType());
                    fieldData.resetPanelAreaMarks();
                }
            }
        }
    }

    /* Schedule sperren und cleanen, gesetzte Lections bleiben ansprechbar */
    private void setAllocatedMode() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                if (fieldDataMatrix[i][j].isLectionAllocated()) {
                    fieldDataMatrix[i][j].setMoveEnabled(true);
                } else {
                    fieldDataMatrix[i][j].setMoveEnabled(false);
                    fieldDataMatrix[i][j].resetPanelAreaMarks();
                }
                fieldDataMatrix[i][j].setValidTimeMark(UNVALID);
            }
        }
    }

    private void createLection(int lectionLength) {
        int lectionEnd = dayColumnFieldIndex + lectionLength;
        DayColumnData dayColumn = getDayColumn(dayIndex);
        ScheduleFieldData field;
        LectionData lection = new LectionData(dayIndex);
        Time startTime = null;
        int allocatedTimeMark = UNVALID;
        for (int i = dayColumnFieldIndex; i < lectionEnd; i++) {
            field = dayColumn.getFieldDataAt(i);
            if (i == dayColumnFieldIndex) {
                field.setLectionPanelAreaMark(HEAD);
                allocatedTimeMark = field.getValidTimeMark();
                startTime = field.getFieldTime();
            }
            if (i > dayColumnFieldIndex && i < lectionEnd - 2) {
                if (i == dayColumnFieldIndex + 1) {
                    field.setLectionPanelAreaMark(NAME_ROW);
                    field.setNameMark(FIRST_NAME);
                } else if (i == dayColumnFieldIndex + 2) {
                    field.setNameMark(NAME);
                } else if (i == dayColumnFieldIndex + 3) {
                    field.setNameMark(THIRD_NAME);
                }
            }
            if (i > dayColumnFieldIndex + 2 && i < lectionEnd - 1) {
                field.setLectionPanelAreaMark(JOURNAL_ROWS);
            }
            if (i == lectionEnd - 1) {
                field.setLectionPanelAreaMark(LAST_ROW);
            }
            field.setLectionAllocated(true);
            field.setAllocatedTimeMark(allocatedTimeMark);
            lection.add(field);
        }
        dayColumn.addLection(startTime, lection);
    }

    private void eraseLection(int lectionLength) {
        DayColumnData dayColumn = getDayColumn(dayIndex);
        ScheduleFieldData field;
        Time startTime = null;
        while (dayColumn.getFieldDataAt(dayColumnFieldIndex).getLectionPanelAreaMark() != HEAD) { // Start-Row bestimmen
            dayColumnFieldIndex--;
        }
        int lectionEnd = dayColumnFieldIndex + lectionLength;
        for (int i = dayColumnFieldIndex; i < lectionEnd; i++) {
            field = dayColumn.getFieldDataAt(i);
            field.setLectionAllocated(false);
            field.setMoveEnabled(false);
            field.resetPanelAreaMarks();
            field.resetTimeMarks();
            if (i == dayColumnFieldIndex) {
                startTime = field.getFieldTime();
            }
        }
        dayColumn.removeLection(startTime);
    }

    private void convertTableToDayColumnCoordinates(int selectedRow, int selectedCol) {
        dayColumnFieldIndex = selectedRow;
        if (selectedCol % 4 == 3) {
            dayColumnFieldIndex = selectedRow + getRowCount();
        }
        dayIndex = selectedCol / 4;
    }

    private void clearLectionGapFillers() {
        for (int i = 0; i < numberOfValidDays; i++) {
            DayColumnData dayColumn = dayColumnDataList.get(i);
            dayColumn.getLectionGapFiller().clear();
        }
    }

    public void setAllValidTimeMarks(Profile profile) {
        for (int i = 0; i < numberOfValidDays; i++) {
            DayColumnData dayColumn = dayColumnDataList.get(i);
            dayColumn.setValidTimeMarks(profile.getStudentDay(i));
        }
    }

    public void clearAllTimeMarks() {
        for (int i = 0; i < numberOfValidDays; i++) {
            DayColumnData dayColumn = dayColumnDataList.get(i);
            dayColumn.clearValidTimeMarks();
        }
    }

    public final void setScheduleDisabled() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                fieldDataMatrix[i][j].setMoveEnabled(false);
                fieldDataMatrix[i][j].setValidTimeMark(UNVALID);
            }
        }
    }

    private void updateLectionData(int deletedStudentID) {
        for (DayColumnData dayColumn : dayColumnDataList) {
            dayColumn.updateStudentID(deletedStudentID);
        }
    }

    public int getNumberOfValidDays() {
        return numberOfValidDays;
    }

    public String getDayNameAt(int dayIndex) {
        return getDayColumn(dayIndex).getDayName();
    }

    public DayColumnData getDayColumn(int dayIndex) {
        return dayColumnDataList.get(dayIndex);
    }

    public ArrayList<DayColumnData> getDayColumnDataList() {
        return dayColumnDataList;
    }

    public ScheduleTimeFrame getTimeFrame() {
        return timeFrame;
    }

    public BreakWatcher getBreakWatcher() {
        return breakWatcher;
    }

    public JournalEntry getJournalEntry() {
        return journalEntry;
    }

    public void setScheduleTimes(ScheduleTimes scheduleTimes) {
        this.scheduleTimes = scheduleTimes;
    }

    @Override
    public void profileDeleted(int numberOfStudents, Profile profile) {
        updateLectionData(profile.getID());
    }

    @Override
    public void profileAdded(int numberOfStudents, Profile profile) {
    }

    @Override
    public void profileEdited(Profile profile) {
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent m) {
    }

    @Override
    public void mouseExited(MouseEvent m) {
    }
}
