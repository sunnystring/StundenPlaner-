package scheduleData;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import core.ScheduleTimes;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import core.Student;
import exceptions.IllegalLectionEraseException;
import java.awt.Point;
import scheduleUI.TimeTable;
import studentListData.StudentFieldData;
import studentListData.StudentListData;
import studentlistUI.StudentList;
import util.Time;

/**
 *
 * TableModel für {@link TimeTable}, jede Zelle ist in der fieldDataMatrix als
 * {@link ScheduleFieldData} abgebildet
 *
 */
public class ScheduleData extends AbstractTableModel implements MouseListener {

    private ScheduleTimes scheduleTimes;
    private ArrayList<DayColumnData> dayColumnDataList;
    private ScheduleTimeFrame timeFrame;
    private ScheduleFieldData[][] fieldDataMatrix;
    private int numberOfDays;
    private int dayColumnIndex;
    private int dayColumnFieldIndex;

    public ScheduleData(ScheduleTimes scheduleTimes) {
        this.scheduleTimes = scheduleTimes;
        dayColumnDataList = new ArrayList<>();
        timeFrame = new ScheduleTimeFrame();
        numberOfDays = 0;
        dayColumnFieldIndex = dayColumnIndex = -1;
        setScheduleDisabled();
    }

    public void setTableData() {
        numberOfDays = scheduleTimes.getNumberOfValidDays();
        defineTimeFrame();
        createDayColumns();
        createFieldDataMatrix();
    }

    public void updateTableData() {
        numberOfDays = scheduleTimes.getNumberOfValidDays();
        dayColumnDataList.clear();
        restoreDayColumns();
        createFieldDataMatrix();
    }

//    private void resetTableData() {
//        dayColumnDataList.clear();
//        timeFrame.reset();
//    }
    public void defineTimeFrame() {
        for (int i = 0; i < numberOfDays; i++) {
            timeFrame.createTimeFrame(scheduleTimes.getValidScheduleDay(i));
        }
    }

    public void verifyAllocationState() {

        // ToDo.....
    }

    public void validateTimeFrame() {
        ScheduleTimeFrame tempFrame = new ScheduleTimeFrame();
        scheduleTimes.setSelectedScheduleDays();
        for (int i = 0; i < scheduleTimes.getNumberOfSelectedDays(); i++) {
            tempFrame.createTimeFrame(scheduleTimes.getSelectedScheduleDay(i)); // neuer, noch nicht definitiver TimeFrame bestimmen
        }
        boolean illegalEntry = false;
        String illegalDayString = " ";
        for (int i = 0; i < numberOfDays; i++) {  // checkt neuer TimeFrame in den bestehenden DayColumns
            illegalEntry = dayColumnDataList.get(i).checkAllocatedLectionBounds(tempFrame.getAbsoluteStart(), tempFrame.getAbsoluteEnd());
            illegalDayString += dayColumnDataList.get(i).getDayName() + " ";
        }
        if (illegalEntry) {
            throw new IllegalLectionEraseException(illegalDayString);
        }
        scheduleTimes.clearTemporaryScheduleDays();
    }

    private void createDayColumns() {
        for (int i = 0; i < numberOfDays; i++) {
            DayColumnData day = new DayColumnData();
            day.createDayColumn(scheduleTimes.getValidScheduleDay(i), timeFrame);
            dayColumnDataList.add(day);
        }
    }

    private void restoreDayColumns() {
        // ToDo.......
    }

    private void createFieldDataMatrix() {
        fieldDataMatrix = new ScheduleFieldData[getRowCount()][getColumnCount()];
        int dayCount = 0;
        for (int j = 0; j < getColumnCount(); j++) {
            for (int i = 0; i < getRowCount(); i++) {
                if (j % 4 == 0 || j % 4 == 1) {
                    fieldDataMatrix[i][j] = dayColumnDataList.get(dayCount).getFieldData(i);
                }
                if (j % 4 == 2 || j % 4 == 3) {
                    fieldDataMatrix[i][j] = dayColumnDataList.get(dayCount).getFieldData(i + getRowCount());
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
        return 4 * numberOfDays;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return fieldDataMatrix[row][col];
    }

    @Override
    public void mousePressed(MouseEvent m) {
        Point p = m.getPoint();
        int selectedRow, selectedCol;
        if (m.getSource() instanceof StudentList) {
            StudentList studentList = (StudentList) m.getSource();
            StudentListData studentListData = (StudentListData) studentList.getModel();
            selectedRow = studentList.rowAtPoint(p);
            selectedCol = studentList.columnAtPoint(p);
            if (selectedRow >= 0 && selectedCol > 0) { //  ausserhalb JTable: selectedRow = -1, NameField nicht ansprechbar
                StudentFieldData studentFieldData = (StudentFieldData) studentListData.getValueAt(selectedRow, selectedCol);
                int studentDayID = selectedCol - 1; // NameField = 0
                DayColumnData dayColumn = getDayColumn(studentDayID);
                if (studentFieldData.isFieldSelected()) {  // StudentDay selektiert 
                    int studentID = selectedRow;
                    Student student = studentListData.getStudent(studentID);
                    dayColumn.setValidTimeMarks(student.getStudentDay(studentDayID));
                    setMoveMode(student);
                } else if (selectedRow == studentFieldData.getSelectedRowIndex()) { // Selection rückgängig gemacht, aber noch in SelectionState
                    dayColumn.resetValidTimeMarks();
                } else if (studentFieldData.isStudentListReleased()) { // alle Selections gelöscht
                    setAllocatedMode();
                }
                fireTableDataChanged();
            }
        }
        if (m.getSource() instanceof TimeTable) {
            TimeTable timeTable = (TimeTable) m.getSource();
            ScheduleData scheduleData = (ScheduleData) timeTable.getModel();
            selectedRow = timeTable.rowAtPoint(p);
            selectedCol = timeTable.columnAtPoint(p);
            if (selectedRow >= 0 && selectedCol % 2 == 1) { // keine Events aus TimeColumn
                ScheduleFieldData scheduleFieldData = (ScheduleFieldData) scheduleData.getValueAt(selectedRow, selectedCol);
                Student student = scheduleFieldData.getStudent();
                if (scheduleFieldData.isMoveEnabled()) {
                    convertTableToDayColumnCoordinates(selectedRow, selectedCol);
                    if (scheduleFieldData.isLectionAllocated()) { // in MoveMode wechseln
                        if (scheduleFieldData.getLectionPanelAreaMark() == ScheduleFieldData.HEAD) {
                            eraseLection(student.getLectionLength());
                            setAllValidTimeMarks(student);
                            setMoveMode(student);
                        }
                        if (scheduleFieldData.getLectionPanelAreaMark() == ScheduleFieldData.CENTER && m.getClickCount() == 2) { // Einteilung rückgängig
                            eraseLection(student.getLectionLength());
                        }
                    } else { // in AllocatedMode wechseln
                        createLection(student.getLectionLength());
                        setAllocatedMode();
                    }
                    fireTableDataChanged();
                }
            }
        }
    }

    /* gesetzte Lections sperren, Sperrzonen setzen, der restliche Schedule einteilbar machen und current Student global setzen*/
    private void setMoveMode(Student student) {
        int lectionLength = student.getLectionLength();
        int rowCount = timeFrame.getTotalNumberOfFields();
        ScheduleFieldData fieldData;
        for (int studentDayID = 0; studentDayID < dayColumnDataList.size(); studentDayID++) {
            int headRow = -1;
            DayColumnData dayColumn = getDayColumn(studentDayID);
            for (int i = rowCount - 1; i >= 0; i--) {
                fieldData = dayColumn.getFieldData(i);
                if (fieldData.isLectionAllocated()) { // Sperrzone: gesetzte Lection
                    fieldData.setMoveEnabled(false);
                    if (fieldData.getLectionPanelAreaMark() == ScheduleFieldData.HEAD) {
                        headRow = i;
                    }
                } else { // Sperrzonen: lectionLength unterhalb Lection und vor Stundenplan-Ende 
                    boolean isAllocatable = !(i < headRow && i > headRow - lectionLength) && (i <= rowCount - lectionLength);
                    fieldData.setMoveEnabled(isAllocatable);
                    fieldData.setStudent(student);
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
                fieldDataMatrix[i][j].setValidTimeMark(ScheduleFieldData.NO_VALUE);
            }
        }
    }

    private void createLection(int lectionLength) {
        int lectionEnd = dayColumnFieldIndex + lectionLength;
        DayColumnData dayColumn = getDayColumn(dayColumnIndex);
        ScheduleFieldData field;
        LectionData lection = new LectionData();
        Time time = null;
        int allocatedTimeMark = ScheduleFieldData.NO_VALUE;
        for (int i = dayColumnFieldIndex; i < lectionEnd; i++) {
            field = dayColumn.getFieldData(i);
            if (i == dayColumnFieldIndex) {
                field.setLectionPanelAreaMark(ScheduleFieldData.HEAD);
                allocatedTimeMark = field.getValidTimeMark();
                time = field.getFieldTime();
            }
            if (i > dayColumnFieldIndex && i < lectionEnd - 2) {
                field.setLectionPanelAreaMark(ScheduleFieldData.CENTER);
                if (i == dayColumnFieldIndex + 1) {
                    field.setNameMark(ScheduleFieldData.FIRST_NAME);
                } else if (i == dayColumnFieldIndex + 2) {
                    field.setNameMark(ScheduleFieldData.NAME);
                }
            }
            if (i == lectionEnd - 2) {
                field.setLectionPanelAreaMark(ScheduleFieldData.SECOND_LAST_ROW);
            }
            if (i == lectionEnd - 1) {
                field.setLectionPanelAreaMark(ScheduleFieldData.LAST_ROW);
            }
            field.setLectionAllocated(true);
            field.setAllocatedTimeMark(allocatedTimeMark);
            lection.add(field);
        }
        System.out.println("time addLection: " + time);
        dayColumn.addLection(time, lection);
    }

    private void eraseLection(int lectionLength) {
        DayColumnData dayColumn = getDayColumn(dayColumnIndex);
        ScheduleFieldData field;
        Time time = null;
        while (dayColumn.getFieldData(dayColumnFieldIndex).getLectionPanelAreaMark() != ScheduleFieldData.HEAD) { // Start-Row bestimmen
            dayColumnFieldIndex--;
        }
        int lectionEnd = dayColumnFieldIndex + lectionLength;
        for (int i = dayColumnFieldIndex; i < lectionEnd; i++) {
            field = dayColumn.getFieldData(i);
            field.setLectionAllocated(false);
            field.setMoveEnabled(false);
            field.resetPanelAreaMarks();
            field.resetTimeMarks();
            if (i == dayColumnFieldIndex) {
                time = field.getFieldTime();
            }
        }
        System.out.println("time removeLection: " + time);
        dayColumn.removeLection(time);
    }

    private void convertTableToDayColumnCoordinates(int selectedRow, int selectedCol) {
        dayColumnFieldIndex = selectedRow;
        if (selectedCol % 4 == 3) {
            dayColumnFieldIndex = selectedRow + getRowCount();
        }
        dayColumnIndex = selectedCol / 4;
    }

    private void setAllValidTimeMarks(Student student) {
        for (int studentDayID = 0; studentDayID < dayColumnDataList.size(); studentDayID++) {
            DayColumnData dayColumn = dayColumnDataList.get(studentDayID);
            dayColumn.setValidTimeMarks(student.getStudentDay(studentDayID));
        }
    }

    public void setScheduleDisabled() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                fieldDataMatrix[i][j].setMoveEnabled(false);
                fieldDataMatrix[i][j].setValidTimeMark(ScheduleFieldData.NO_VALUE);
            }
        }
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public DayColumnData getDayColumn(int i) {
        return dayColumnDataList.get(i);
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
