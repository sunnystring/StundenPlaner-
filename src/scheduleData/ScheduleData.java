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
import core.StudentDay;
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
    private int selectedRow, selectedCol; // gemeinsam benutzte temporäre JTable-Koordinaten aus MouseEvent

    public ScheduleData(Database database) {

        this.database = database;
        scheduleTimes = database.getScheduleTimes();
        dayColumnDataList = new ArrayList<>();
        timeFrame = new ScheduleTimeFrame();
        numberOfDays = 0;
        selectedRow = selectedCol = -1;
        setScheduleDisabled();
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
                // StudentDay selektiert 
                if (studentFieldData.isFieldSelected()) {
                    int studentID = selectedRow;
                    Student student = studentListData.getStudent(studentID);
                    dayColumn.setValidTimeMarks(student.getStudentDay(studentDayID));
                    setMoveMode(student);
                } // falls Selektion rückgängig gemacht, aber noch im Selektions-Modus 
                else if (selectedRow == studentFieldData.getSelectedRowIndex()) {
                    dayColumn.resetValidTimeMarks();
                } // falls alle Selektionen in StudentList rückgängig gemacht (= StudentList entsperrt)
                else if (studentFieldData.isStudentListEnabled()) {
                    // Schedule sperren (ausser bereits gesetzte Lections)
                    setAllocatedMode();
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
            if (selectedRow >= 0 &&selectedCol % 2 == 1 ) { //  ausserhalb JTable: selectedRow = -1,  keine Events aus TimeColumn
                ScheduleFieldData scheduleFieldData = (ScheduleFieldData) scheduleData.getValueAt(selectedRow, selectedCol);
                Student student = scheduleFieldData.getStudent(); // = current Student
               // aus gesperrten Bereichen keine Clicks
                if ( scheduleFieldData.isMoveEnabled()) {
                    
                    // ab hier wird über DayColumnData/fieldDataList auf ScheduleFieldData zugegriffen
                    convertTableToListCoordinates(); // toDo.....
                    if (scheduleFieldData.isLectionAllocated()) {
                        // von Allocated-State in Move-State wechseln
                        if (scheduleFieldData.getLectionPanelAreaMark() == ScheduleFieldData.HEAD) {
                            eraseLectionPanel(student.getLectionType());
                            setAllValidTimeMarks(student);
                            setMoveMode(student);
                        }
                        // Einteilung rückgängig machen 
                        if (scheduleFieldData.getLectionPanelAreaMark() == ScheduleFieldData.CENTER && m.getClickCount() == 2) {
                            eraseLectionPanel(student.getLectionType());
                        }
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
    private void setMoveMode(Student student) {
        int lectionLength = student.getLectionType();
        int rowCount = timeFrame.getTotalNumberOfFields();
        ScheduleFieldData fieldData;
        // über alle DayColumns iterieren
        for (int studentDayID = 0; studentDayID < dayColumnDataList.size(); studentDayID++) {
            int headRow = -1;
            DayColumnData dayColumn = getDayColumn(studentDayID);
            // über FieldDataList rückwärts iterieren
            for (int i = rowCount - 1; i >= 0; i--) {
                fieldData = dayColumn.getFieldData(i);
                // Sperrzone: innerhalb Lection
                if (fieldData.isLectionAllocated()) {
                    // eingeteilte Lektionen 
                    fieldData.setMoveEnabled(false);
                    // alle HEAD-Rows identifizieren
                    if (fieldData.getLectionPanelAreaMark() == ScheduleFieldData.HEAD) {
                        headRow = i;
                    }
                } else {
                    // Sperrzone: lectionLength unterhalb Lection und vor Stundenplan-Ende 
                    boolean isAllocatable = !(i < headRow && i > headRow - lectionLength) && (i <= rowCount - lectionLength);
                    fieldData.setMoveEnabled(isAllocatable);
                    // current Student muss in allen einteilbaren Feldern bekannt sein
                    fieldData.setStudent(student);
                    fieldData.resetPanelAreaMarks();
                }
            }
        }
    }

    /* Schedule sperren, ausser den gesetzten Lections, einteilbare Bereiche cleanen, alle validTimeMarks löschen */
    private void setAllocatedMode() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                // gesetzte Lections entsperren
                if (scheduleFieldDataMatrix[i][j].isLectionAllocated()) {
                    scheduleFieldDataMatrix[i][j].setMoveEnabled(true);
                } // restliche StudentList sperren und cleanen
                else {
                    scheduleFieldDataMatrix[i][j].setMoveEnabled(false);
                    scheduleFieldDataMatrix[i][j].resetPanelAreaMarks();
                }
                scheduleFieldDataMatrix[i][j].setValidTimeMark(ScheduleFieldData.NO_VALUE);
            }
        }
    }

    /* Position, Höhe und Panel-Bereiche festlegen*/
    private void createLectionPanel(int lectionLength) {
        // TimeTable-Koordinaten in DayColumn-Koordinaten konvertieren
        int dayColumnRow = selectedRow;
        if (selectedCol % 4 == 3) // falls 2. LectionColumn
        {
            dayColumnRow = selectedRow + getRowCount();
        }
        int lectionEnd = dayColumnRow + lectionLength;
        // DayColumn wählen
        DayColumnData dayColumn = getDayColumn(selectedCol / 4);

        ScheduleFieldData fieldData;
        int allocatedTime = ScheduleFieldData.NO_VALUE;
        for (int i = dayColumnRow; i < lectionEnd; i++) {
            fieldData = dayColumn.getFieldData(i);
            // HEAD
            if (i == dayColumnRow) {
                fieldData.setLectionPanelAreaMark(ScheduleFieldData.HEAD);
                // Lektionsbeginn
                allocatedTime = fieldData.getValidTimeMark();
            }
            // CENTER
            if (i > dayColumnRow && i < lectionEnd - 2) {
                fieldData.setLectionPanelAreaMark(ScheduleFieldData.CENTER);
                // Vorname
                if (i == dayColumnRow + 1) {
                    fieldData.setNameMark(ScheduleFieldData.FIRST_NAME);
                } // Name
                else if (i == dayColumnRow + 2) {
                    fieldData.setNameMark(ScheduleFieldData.NAME);
                }
            }
            // SECOND_LAST_ROW
            if (i == lectionEnd - 2) {
                fieldData.setLectionPanelAreaMark(ScheduleFieldData.SECOND_LAST_ROW);
            }
            // SECOND_LAST_ROW
            if (i == lectionEnd - 1) {
                fieldData.setLectionPanelAreaMark(ScheduleFieldData.LAST_ROW);
            }
            // ganze Lection in allocated-State setzen, Student ist bereits gesetzt
            fieldData.setLectionAllocated(true);
            // ganze Lection muss ihre allocatedTime wissen (validTimes werden gelöscht)
            fieldData.setAllocatedTimeMark(allocatedTime);
        }
    }

    private void eraseLectionPanel(int lectionLength) {
        // TimeTable-Koordinaten in DayColumn-Koordinaten konvertieren
        int dayColumnRow = selectedRow;
        if (selectedCol % 4 == 3) // falls 2. LectionColumn
        {
            dayColumnRow = selectedRow + getRowCount();
        }
        // DayColumn wählen
        DayColumnData dayColumn = getDayColumn(selectedCol / 4);
        ScheduleFieldData fieldData;
        // Start-Row bestimmen
        while (dayColumn.getFieldData(dayColumnRow).getLectionPanelAreaMark() != ScheduleFieldData.HEAD) {
            dayColumnRow--;
        }
        int lectionEnd = dayColumnRow + lectionLength;
        for (int i = dayColumnRow; i < lectionEnd; i++) {
            fieldData = dayColumn.getFieldData(i);
            fieldData.setLectionAllocated(false);
            fieldData.setMoveEnabled(false);
            fieldData.resetPanelAreaMarks();
            fieldData.resetTimeMarks();
        }
    }

    private void convertTableToListCoordinates() {
    }

    private void setAllValidTimeMarks(Student student) {
        for (int studentDayID = 0; studentDayID < dayColumnDataList.size(); studentDayID++) {
            DayColumnData dayColumn = dayColumnDataList.get(studentDayID);
            dayColumn.setValidTimeMarks(student.getStudentDay(studentDayID));
        }
    }

    /* ganzer Schedule sperren, validTimeMarks löschen */
    public void setScheduleDisabled() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                scheduleFieldDataMatrix[i][j].setMoveEnabled(false);
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
