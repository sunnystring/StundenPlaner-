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
    private int selectedRow, selectedCol; // gemeinsam benutzte temporäre JTable-Koordinaten
    public static final boolean VALID_TIMES_SET = true;

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
                    setMoveMode(student, !VALID_TIMES_SET);
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
            if (selectedRow >= 0) { //  ausserhalb JTable: selectedRow = -1
                ScheduleFieldData scheduleFieldData = (ScheduleFieldData) scheduleData.getValueAt(selectedRow, selectedCol);
                Student student = scheduleFieldData.getStudent(); // = current Student
                // keine Events aus TimeColumn 
                if (selectedCol % 2 == 1) {
                    // Schedule darf nicht gesperrt sein
                    if (scheduleFieldData.isScheduleEnabled()) {
                        // von Allocated-State in Move-State wechseln
                        if (scheduleFieldData.isLectionAllocated() && scheduleFieldData.getLectionPanelAreaMark() == ScheduleFieldData.HEAD) {
                            eraseLectionPanel(student.getLectionType());
                            setMoveMode(student, VALID_TIMES_SET);
                        } // von Move-State in Allocated-State wechseln
                        else if (!scheduleFieldData.isLectionAllocated()) {
                            createLectionPanel(student.getLectionType());
                            setAllocatedMode();
                        }
                        fireTableDataChanged();
                    }
                }
            }
        }
    }

    /* gesetzte Lections sperren, Sperrzonen setzen, der restliche Schedule einteilbar machen und current Student-Daten global setzen*/
    private void setMoveMode(Student student, boolean setValidTimes) {
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
                    fieldData.setScheduleEnabled(false);
                    // alle HEAD-Rows identifizieren
                    if (fieldData.getLectionPanelAreaMark() == ScheduleFieldData.HEAD) {
                        headRow = i;
                    }
                } // Sperrzone: ausserhalb Lection
                else {
                    // lectionLength vor allen eingeteilten Lektionen
                    if (i < headRow && i > headRow - lectionLength) {
                        fieldData.setScheduleEnabled(false); // ToDo andere grösse
                    } // Freie Zone: einteilbarer Bereich updaten/rücksetzen 
                    else {
                        // nicht einteilbarer Bereich am Schluss des Tages immer sperren
                        fieldData.setScheduleEnabled(i <= rowCount - lectionLength);
                        // current Student muss in allen einteilbaren Feldern bekannt sein
                        fieldData.setStudent(student);
                        //lectionContent, lectionPanelArea, lastRow löschen
                        fieldData.resetPanelAreaMarks();
                    }
                }
                // falls Lection im Schedule angeklickt, alle ValidTimes setzen
                if (setValidTimes) {
                    dayColumn.setValidTimeMark(student.getStudentDay(studentDayID), i);
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
        ScheduleFieldData fieldData;
        int allocatedTime = -1;
        for (int i = dayColRow; i < lectionEnd; i++) {
            fieldData = dayColumn.getFieldData(i);
            // HEAD
            if (i == dayColRow) {
                fieldData.setLectionPanelAreaMark(ScheduleFieldData.HEAD);
                // validTime übernehmen
                allocatedTime = fieldData.getValidTime();
            }
            // CENTER
            if (i > dayColRow && i < lectionEnd - 2) {
                fieldData.setLectionPanelAreaMark(ScheduleFieldData.CENTER);
                // Vorname
                if (i == dayColRow + 1) {
                    fieldData.setLectionContent(ScheduleFieldData.FIRST_NAME);
                } // Name
                else if (i == dayColRow + 2) {
                    fieldData.setLectionContent(ScheduleFieldData.NAME);
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
            fieldData.setAllocatedTime(allocatedTime);
        }
    }

    private void eraseLectionPanel(int lectionLength) {
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
            dayColumn.getFieldData(i).setLectionAllocated(false);
            dayColumn.getFieldData(i).resetPanelAreaMarks();
            dayColumn.getFieldData(i).setAllocatedTime(ScheduleFieldData.NULL_VALUE);
            dayColumn.getFieldData(i).setValidTime(ScheduleFieldData.NULL_VALUE);
        }
    }

    /* Schedule sperren, ausser den gesetzten Lections, einteilbare Bereiche cleanen, alle validTimeMarks löschen */
    private void setAllocatedMode() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                // gesetzte Lections entsperren
                if (scheduleFieldDataMatrix[i][j].isLectionAllocated()) {
                    scheduleFieldDataMatrix[i][j].setScheduleEnabled(true);
                } // restliche StudentList sperren und cleanen
                else {
                    scheduleFieldDataMatrix[i][j].setScheduleEnabled(false);
                    scheduleFieldDataMatrix[i][j].resetPanelAreaMarks();
                }
                scheduleFieldDataMatrix[i][j].setValidTime(ScheduleFieldData.NULL_VALUE);
            }
        }
    }

    /* ganzer Schedule sperren, validTimeMarks löschen */
    public void setScheduleDisabled() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                scheduleFieldDataMatrix[i][j].setScheduleEnabled(false);
                scheduleFieldDataMatrix[i][j].setValidTime(ScheduleFieldData.NULL_VALUE); // ValidTimeMarks löschen
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
