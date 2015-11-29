package scheduleData;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import core.Database;
import core.ScheduleTimes;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import core.Student;
import java.awt.Point;
import schedule.TimeTable;
import studentListData.StudentFieldData;
import studentListData.StudentListData;
import studentlist.StudentList;

/**
 *
 * @author Mathias
 */
public class ScheduleData extends AbstractTableModel implements MouseListener {

    private Database database;
    private ScheduleTimes scheduleTimes;
    private ArrayList<DayColumnData> dayColumnDataList;
    private ScheduleTimeFrame timeFrame;
    private ScheduleFieldData[][] scheduleFieldDataMatrix;  // für direkten Zugriff in TableModel
    private int numberOfDays;
    private int selectedRow, selectedCol; // gemeinsam benutzte temporäre JTable-Koordinaten
    //  private int lectionLength, lectionEnd; // temporäre LectionPanel-Höhe
    //   public static final int NO_SELECTED_ROW = -1, NO_ALLOCATED_ROW = -2; // ToDo
    public static final boolean SET_ALL_VALID_TIMES = true, TIME_TABLE_COORDINATES = true, STUDENTLIST_COORDINATES = false;

    public ScheduleData(Database database) {

        this.database = database;
        scheduleTimes = database.getScheduleTimes();
        dayColumnDataList = new ArrayList<>();
        timeFrame = new ScheduleTimeFrame();
        numberOfDays = 0;
        selectedRow = selectedCol = -1;
        setScheduleDisabled();
//        lectionEnd = -1;
//        lectionLength = 0;
    }

    // Initialisierung, in MainFrame aufgerufen
    public void initScheduleData() {

        scheduleTimes.setScheduleDays();  // erstellt dynamische Day-List 0 = 1. Tag, 1 = 2. Tag usw.
        numberOfDays = scheduleTimes.getNumberOfDays();
        database.setNumberOfDays(numberOfDays); // numberOfDays müssen global bekannt sein
        // globaler Zeitrahmen aller ScheduleDays festlegen und DayColumnData damit instantiieren
        for (int i = 0; i < numberOfDays; i++) {
            dayColumnDataList.add(new DayColumnData());
            timeFrame.createTimeFrame(scheduleTimes.getScheduleDay(i));
        }
        // DayColumnData initialisieren, Zeitrahmen in alle Tage einsetzen, FieldDataList erzeugen und initialisieren
        for (int i = 0; i < numberOfDays; i++) {
            dayColumnDataList.get(i).initDayColumn(scheduleTimes.getScheduleDay(i), timeFrame);
        }
        // FieldDataMatrix erzeugen
        scheduleFieldDataMatrix = new ScheduleFieldData[getRowCount()][getColumnCount()];
        // FieldDataMatrix die ScheduleFieldData-Referenzen aus allen DayColumns zuweisen
        int dayCount = 0; // zählt ScheduleDays: 1.Tag = 0 usw.
        for (int j = 0; j < getColumnCount(); j++) { // i = ColumnIndex
            for (int i = 0; i < getRowCount(); i++) { // j = RowIndex
                if (j % 4 == 0 || j % 4 == 1) {  // 1. TimeColumn und 1. LectionColumn: FieldDataList von 0 bis totalNumberOfFields/2
                    scheduleFieldDataMatrix[i][j] = dayColumnDataList.get(dayCount).getFieldData(i);
                }
                if (j % 4 == 2 || j % 4 == 3) { // 2. TimeColumn und 2. LectionColumn: FieldDataList von totalNumberOfFields/2 bis totalNumberOfFields
                    scheduleFieldDataMatrix[i][j] = dayColumnDataList.get(dayCount).getFieldData(i + getRowCount());
                }
            }
            if (j % 4 == 3) { // nach 4. Column neuer Tag
                dayCount++;
            }
        }
    }

    /* Getter, Setter */
    public int getNumberOfDays() { // für direkten Zugriff in Schedule 
        return numberOfDays;
    }

    public DayColumnData getDayColumn(int i) {
        return dayColumnDataList.get(i);
    }

    /*  TableModel */
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
        return scheduleFieldDataMatrix[row][col];
    }

    /* MouseListener */
    @Override
    public void mousePressed(MouseEvent m) {

        Point p = m.getPoint();
        // StudentList 
        if (m.getSource() instanceof StudentList) {
            StudentList studentList = (StudentList) m.getSource();
            StudentListData studentListData = (StudentListData) studentList.getModel();
            selectedRow = studentList.rowAtPoint(p);
            selectedCol = studentList.columnAtPoint(p);
            if (selectedRow >= 0 && selectedCol > 0) { //  ausserhalb JTable: selectedRow = -1, NameField nicht ansprechbar
                StudentFieldData studentFieldData = (StudentFieldData) studentListData.getValueAt(selectedRow, selectedCol);
                int studentDayID = selectedCol - 1; // ohne NameField
                DayColumnData dayColumn = getDayColumn(studentDayID);  // richtige DayColumn wählen
                // StudentDay selected 
                if (studentFieldData.isFieldSelected()) {
                    int studentID = selectedRow;
                    Student student = studentListData.getStudent(studentID);
                    dayColumn.setValidTimeMarks(student.getStudentDay(studentDayID));
                    setMoveMode(student, STUDENTLIST_COORDINATES, !SET_ALL_VALID_TIMES);
                } // Selektion in StudentList rückgängig gemacht
                else if (studentFieldData.isStudentListEnabled()) {
                    setScheduleDisabled();
                }
                fireTableDataChanged();
            }
        }
        // Schedule (TimeTable)
        if (m.getSource() instanceof TimeTable) {
            TimeTable timeTable = (TimeTable) m.getSource();
            ScheduleData scheduleData = (ScheduleData) timeTable.getModel();
            selectedRow = timeTable.rowAtPoint(p);
            selectedCol = timeTable.columnAtPoint(p);
            ScheduleFieldData scheduleFieldData = (ScheduleFieldData) scheduleData.getValueAt(selectedRow, selectedCol);
            Student student = scheduleFieldData.getStudent();
            // keine Events aus TimeColumn 
            if (selectedCol % 2 == 1) {
                // Schedule darf nicht gesperrt sein
                if (scheduleFieldData.isScheduleEnabled()) {
                    // von Allocated-State in Move-State wechseln
                    if (scheduleFieldData.isLectionAllocated() && scheduleFieldData.getLectionPanelArea() == ScheduleFieldData.HEAD) {
                        setMoveMode(student, TIME_TABLE_COORDINATES, SET_ALL_VALID_TIMES);
                    } // von Move-State in Allocated-State wechseln
                    else {
                        createLectionPanel(student.getLectionType());
                        setAllocatedMode();
                    }
                    fireTableDataChanged();
                }
            }
        }
    }

    /* gesetzte Lections sperren, Sperrzonen setzen, der restliche Schedule einteilbar machen und current Student-Daten global setzen*/
    private void setMoveMode(Student student, boolean timeTableCoordinates, boolean allValidTimes) {

        int lectionLength = student.getLectionType();
        int headRow = -1;
        int selectedDayColumnRow = -1;
        // Falls TimeTable-Koordinaten, in DayColumn-Koordinaten konvertieren
        if (timeTableCoordinates) {
            selectedDayColumnRow = selectedRow;
            if (selectedCol % 4 == 3) // 2. LectionColumn
            {
                selectedDayColumnRow = selectedRow + getRowCount();
            }
        }
        int rowCount = timeFrame.getTotalNumberOfFields();
        // über alle DayColumns iterieren
        for (int studentDayID = 0; studentDayID < dayColumnDataList.size(); studentDayID++) {
            DayColumnData dayColumn = getDayColumn(studentDayID);
            // über FieldDataList rückwärts iterieren
            for (int i = rowCount - 1; i >= 0; i--) {
                // nicht einteilbarer Bereich am Schluss des Tages immer sperren
                if (i < rowCount && i >= rowCount - lectionLength) {
                    dayColumn.getFieldData(i).setScheduleEnabled(false);
                } // gesetzte Lektionen sperren
                else if (dayColumn.getFieldData(i).isLectionAllocated()) {
                    dayColumn.getFieldData(i).setScheduleEnabled(false);
                    // Falls TimeTable-Koordinaten, HEAD-Row identifizieren, ausser die der angeklickten Lection
                    if (timeTableCoordinates && i != selectedDayColumnRow && dayColumn.getFieldData(i).getLectionPanelArea() == ScheduleFieldData.HEAD) {
                        headRow = i;
                    }
                } // vor allen eingeteilten Lections Sperrzone setzen 
                else if (i < headRow && i >= headRow - lectionLength) {
                    dayColumn.getFieldData(i).setScheduleEnabled(false);
                } // einteilbarer Bereich
                else {
                    dayColumn.getFieldData(i).setScheduleEnabled(true);
                    dayColumn.getFieldData(i).setStudent(student);// current Student muss in allen einteilbaren Feldern bekannt sein
                }
                // Falls TimeTable-Koordinaten, die angeklickte Lection freigeben und zurücksetzen
                if (timeTableCoordinates && i == selectedDayColumnRow) {
                    for (int k = selectedDayColumnRow; k < selectedDayColumnRow + dayColumn.getFieldData(selectedDayColumnRow).getStudent().getLectionType(); k++) {
                        dayColumn.getFieldData(k).setScheduleEnabled(true);
                        dayColumn.getFieldData(k).setLectionPanelArea(ScheduleFieldData.NO_VALUE);
                    }
                }
                // falls Lection im Schedule angeklickt, alle ValidTimes setzen
                if (allValidTimes) {
                    dayColumn.setValidTimeMarks(student.getStudentDay(studentDayID));
                }
            }
        }
    }

    /* Position, Höhe und Panel-Bereiche festlegen*/
    private void createLectionPanel(int lectionLength) {
        // TimeTable-Koordinaten in DayColumn-Koordinaten konvertieren
        int dayColRow = selectedRow;
        if (selectedCol % 4 == 3) // falls 2. LectionColumn
        {
            dayColRow = selectedRow + getRowCount();
        }
        int lectionEnd = dayColRow + lectionLength;
        // DayColumn wählen
        DayColumnData dayColumn = getDayColumn(selectedCol / 4);
        for (int i = dayColRow; i < lectionEnd; i++) {
            // HEAD
            if (i == dayColRow) {
                dayColumn.getFieldData(i).setLectionPanelArea(ScheduleFieldData.HEAD);
            }
            // CENTER
            if (i > dayColRow && i < lectionEnd - 2) {
                dayColumn.getFieldData(i).setLectionPanelArea(ScheduleFieldData.CENTER);
                // Vorname
                if (i == dayColRow + 1) {
                    dayColumn.getFieldData(i).setLectionContent(ScheduleFieldData.FIRST_NAME);
                } // Name
                else if (i == dayColRow + 2) {
                    dayColumn.getFieldData(i).setLectionContent(ScheduleFieldData.NAME);
                }
            }
            // BOTTOM
            if (i == lectionEnd - 2 || i == lectionEnd - 1) { // BOTTOM
                dayColumn.getFieldData(i).setLectionPanelArea(ScheduleFieldData.BOTTOM);
                if (i == lectionEnd - 1) {
                    dayColumn.getFieldData(i).setLastRow(true);
                }
            }
            // ganze Lection in allocated-State setzen, Student ist bereits gesetzt
            dayColumn.getFieldData(i).setLectionAllocated(true);
        }
    }

    /* Schedule sperren, ausser den gesetzten Lections, validTimeMarks löschen, einteilbare Bereiche cleanen */
    private void setAllocatedMode() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                // gesetzte Lections entsperren, restliche StudentList sperren
                scheduleFieldDataMatrix[i][j].setScheduleEnabled(scheduleFieldDataMatrix[i][j].isLectionAllocated());
                // validTimes zurücksetzen
                scheduleFieldDataMatrix[i][j].setValidTimeMark(ScheduleFieldData.NO_VALUE);
            }
        }
    }

    /* ganzer Schedule sperren, validTimeMarks löschen */
    public void setScheduleDisabled() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                scheduleFieldDataMatrix[i][j].setScheduleEnabled(false);
                scheduleFieldDataMatrix[i][j].setValidTimeMark(ScheduleFieldData.NO_VALUE); // ValidTimeMarks löschen
            }
        }
    }

    // -----------------
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
