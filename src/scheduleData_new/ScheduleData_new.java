package scheduleData_new;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import studentListData.StudentDay;
import studentlistGUI.StudentList;

/**
 *
 * @author Mathias
 */
public class ScheduleData_new extends AbstractTableModel implements MouseListener {

    private ScheduleTimes_new scheduleTimes;
    private int numberOfDays;
    private ArrayList<DayColumnData_new> dayColumnDataList;
    private ScheduleTimeFrame_new timeFrame;
    private FieldData_new[][] fieldDataMatrix;

    public ScheduleData_new() {

        scheduleTimes = new ScheduleTimes_new();
        dayColumnDataList = new ArrayList<>();
        timeFrame = new ScheduleTimeFrame_new();
        numberOfDays = 0;
    }

    // Initialisierung, in MainFrame aufgerufen
    public void initScheduleData(ScheduleTimes_new scheduleTimes) {

        this.scheduleTimes = scheduleTimes;
        scheduleTimes.createList();  // erstellt dynamische Day-List 0 = 1. Tag, 1 = 2. Tag usw.
        numberOfDays = scheduleTimes.getNumberOfDays();

        // ColumnModels erzeugen
        // DayColumnData instantiieren und globaler Zeitrahmen aller ScheduleDays festlegen
        for (int i = 0; i < numberOfDays; i++) {
            dayColumnDataList.add(new DayColumnData_new());
            timeFrame.initTimeFrame(scheduleTimes.getScheduleDay(i));
        }
        // DayColumnData initialisieren, Zeitrahmen in alle Tage einsetzen, FieldDataList erzeugen und initialisieren
        for (int i = 0; i < numberOfDays; i++) {
            dayColumnDataList.get(i).initDayColumn(scheduleTimes.getScheduleDay(i), timeFrame);
        }
        // FieldDataMatrix erzeugen
        fieldDataMatrix = new FieldData_new[getColumnCount()][getRowCount()];
        int dayIndex = 0;
        for (int i = 0; i < getColumnCount(); i++) { // i = ColumnIndex
            for (int j = 0; j < getRowCount(); j++) { // j = RowIndex
                if (i % 4 == 0 || i % 4 == 1) {  // 1. TimeColumn / 1. LectionColumn: FieldDataList von 0 bis totalNumberOfFields/2
                    fieldDataMatrix[i][j] = dayColumnDataList.get(dayIndex).getFieldData(j);
                }
                if (i % 4 == 2 || i % 4 == 3) { // 2. TimeColumn / 2. LectionColumn: FieldDataList von totalNumberOfFields/2 bis totalNumberOfFields
                    fieldDataMatrix[i][j] = dayColumnDataList.get(dayIndex).getFieldData(j + getRowCount());
                }
            }
            if (i % 4 == 3) {
                dayIndex++;
            }
        }
    }

    /* Getter, Setter */
    public ScheduleTimes_new getScheduleTimes() {
        return scheduleTimes;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public DayColumnData_new getDayColumn(int i) {
        return dayColumnDataList.get(i);
    }

    /*  TabelModel */
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

        if (col % 2 == 0) {   // nur TimeColumns zeichnen
            return fieldDataMatrix[col][row];
        } else {
            return null;
        }
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return false;
    }

    @Override
    public void mousePressed(MouseEvent m) {

        // StudentList 
        if (m.getSource() instanceof StudentList) {
            StudentList studentList = (StudentList) m.getSource();
            int studentID = studentList.rowAtPoint(m.getPoint());
            int studentDayID = studentList.columnAtPoint(m.getPoint()) - 1;
            if (studentDayID >= 0) {  // 1. Column ist NameField -> ArrayOutOfBounds
                DayColumnData_new dayColumn = getDayColumn(studentDayID);  // richtige DayColumn wählen
                dayColumn.resetValidTimeMarks();
                StudentDay studentDay = studentList.getStudentData().getStudent(studentID).getStudentDay(studentDayID); // ????
                dayColumn.setValidTimeMarks(studentDay);  // setzt die Timemarks des angeklickten StudentList-Tages
                fireTableDataChanged();
            }
        }
    }

    // ----unbenutzt-------------
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
