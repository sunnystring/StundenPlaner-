package scheduleData;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import core.Student;
import studentListData.StudentDay;
import studentListData.StudentListData;
import studentlist.StudentList;

/**
 *
 * @author Mathias
 */
public class ScheduleData extends AbstractTableModel implements MouseListener {

    private ScheduleTimes scheduleTimes;
    private int numberOfDays;
    private ArrayList<DayColumnData> dayColumnDataList;
    private ScheduleTimeFrame timeFrame;
    private FieldData[][] fieldDataMatrix;  // für direkten Zugriff in TableModel

    public ScheduleData() {

        scheduleTimes = new ScheduleTimes();
        dayColumnDataList = new ArrayList<>();
        timeFrame = new ScheduleTimeFrame();
        numberOfDays = 0;
    }

    // Initialisierung, in MainFrame aufgerufen
    public void initScheduleData(ScheduleTimes scheduleTimes) {

        this.scheduleTimes = scheduleTimes;
        scheduleTimes.createScheduleDayList();  // erstellt dynamische Day-List 0 = 1. Tag, 1 = 2. Tag usw.
        numberOfDays = scheduleTimes.getNumberOfDays();

        // globaler Zeitrahmen aller ScheduleDays festlegen und DayColumnData damit instantiieren
        for (int i = 0; i < numberOfDays; i++) {
            dayColumnDataList.add(new DayColumnData());
            timeFrame.createTimeFrame(scheduleTimes.getScheduleDay(i));
        }
        // DayColumnData initialisieren, Zeitrahmen in alle Tage einsetzen, FieldDataList erzeugen und initialisieren
        for (int i = 0; i < numberOfDays; i++) {
            dayColumnDataList.get(i).initDayColumn(scheduleTimes.getScheduleDay(i), timeFrame);
        }
        // FieldDataMatrix erhält die Referenzen auf alle FieldData-Felder der DayColumns
        fieldDataMatrix = new FieldData[getColumnCount()][getRowCount()];
        int dayCount = 0; // zählt ScheduleDays: 1.Tag = 0 usw.
        for (int i = 0; i < getColumnCount(); i++) { // i = ColumnIndex
            for (int j = 0; j < getRowCount(); j++) { // j = RowIndex
                if (i % 4 == 0 || i % 4 == 1) {  // 1. TimeColumn und 1. LectionColumn: FieldDataList von 0 bis totalNumberOfFields/2
                    fieldDataMatrix[i][j] = dayColumnDataList.get(dayCount).getFieldData(j);
                }
                if (i % 4 == 2 || i % 4 == 3) { // 2. TimeColumn und 2. LectionColumn: FieldDataList von totalNumberOfFields/2 bis totalNumberOfFields
                    fieldDataMatrix[i][j] = dayColumnDataList.get(dayCount).getFieldData(j + getRowCount());
                }
            }
            if (i % 4 == 3) { // nach 4. Column neuer Tag
                dayCount++;
            }
        }
    }

    /* Getter, Setter */
    public ScheduleTimes getScheduleTimes() {
        return scheduleTimes;
    }

    public int getNumberOfDays() {
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
        return fieldDataMatrix[col][row];
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
            StudentListData studenListData = (StudentListData) studentList.getModel();
            Student student = studenListData.getStudent(studentID);
            StudentDay studentDay = student.getStudentDay(studentDayID);
            DayColumnData dayColumn;

            if (studentDayID >= 0) {  // 1. Column ist NameField -> ArrayOutOfBounds
                dayColumn = getDayColumn(studentDayID);  // richtige DayColumn wählen
                dayColumn.resetValidTimeMarks();
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
