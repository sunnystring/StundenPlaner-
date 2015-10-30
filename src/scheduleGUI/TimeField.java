/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleGUI;

import studentListData.StudentDay;
import scheduleData.DayColumnData;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import scheduleData.FieldData;
import studentlistGUI.StudentList;
import util.Colors;

/**
 *
 * @author mathiaskielholz
 */
public class TimeField extends JLabel implements MouseMotionListener, MouseListener, TableCellRenderer {

    private DayColumnData dayColumnData;
    private int scheduleDayID;
    // private JTable timeTable;
    private Schedule schedule; //  Zentrale Verwaltung der DayColumns
    private int selectedRow, selectedCol; // lokale TimeTable-Koordinaten

    public TimeField(DayColumnData dayColumnData, Schedule schedule) {

        this.dayColumnData = dayColumnData;
        scheduleDayID = dayColumnData.getScheduleDay().getDayID();
        this.schedule = schedule;
        selectedRow = -2;

        dayColumnData.resetValidTimeMarks();
//        selectedRow = schedule.getTempSelectedRow();
//        selectedCol = schedule.getTempSelectedCol();

        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
        setOpaque(true);
    }

    public void cleanTimeColumns() {

        TimeTable timeTable = schedule.getTimeTable(schedule.getTempScheduleDayID());
        timeTable.getTimeField().resetSelectedRow();
        timeTable.repaint();
    }

    public void resetSelectedRow() {
        selectedRow = -2;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        FieldData fieldData = (FieldData) value;
    //    timeTable = table;  // Referenz auf die entspr. TimeTable

        // Text ausgeben
        if (fieldData.isMinute(row)) {
            setText(fieldData.getMinute(row));
        } else {
            setText(fieldData.getHour(row));
        }
        // Foreground zeichen
        if (dayColumnData.isValidTime(row)) {  // validTime = vom Lehrer vorgegebene Unterrichtszeit
            setForeground(Color.BLACK);
        } else {
            setForeground(Color.LIGHT_GRAY);
        }

        // Background zeichnen
        if (fieldData.getValidTime() == FieldData.FAVORITE) {
            setBackground(Colors.FAVORITE);
        } else if (fieldData.getValidTime() == FieldData.TIME_INTERVAL_1 || fieldData.getValidTime() == FieldData.TIME_INTERVAL_2) {
            setBackground(Colors.LIGHT_GREEN);
        } else {
            setBackground(Colors.BACKGROUND);
        }
        if (!fieldData.isMinute(row) && fieldData.getValidTime() != FieldData.FAVORITE) {
            if (fieldData.getValidTime() == FieldData.TIME_INTERVAL_1 || fieldData.getValidTime() == FieldData.TIME_INTERVAL_2) { // falls Einzellektion auf volle Stunde fällt 
                setBackground(Colors.LIGHT_GREEN);
            } else {
                setBackground(Colors.TIMEFIELD_HOUR);
            }
        }
        // Mouseover 
        if (row == selectedRow && col == selectedCol) {
            setBackground(Color.GRAY);
            setForeground(Color.WHITE);
        }
        return this;
    }

    /*  MouseListener Implementation */
    @Override
    public void mousePressed(MouseEvent m) {

        // Events von StudentList 
        if (m.getSource() instanceof StudentList) {

            dayColumnData.resetValidTimeMarks();
            StudentList studentList = (StudentList) m.getSource();
            int studentID = studentList.rowAtPoint(m.getPoint());
            int studentDayID = studentList.columnAtPoint(m.getPoint()) - 1;
            if (scheduleDayID == studentDayID) {  // Tag wählen
                if (studentDayID >= 0) {  // 1. Column ist NameField -> ArrayOutOfBounds
                    StudentDay studentDay = studentList.getStudentData().getStudent(studentID).getStudentDay(studentDayID); // ????
                    dayColumnData.setValidTimeMarks(studentDay);  // setzt die Timemarks des angeklickten Tages
                    schedule.getTimeTable(scheduleDayID).repaint();
                }
            }
        }
    }

    /*  MouseMotionListener Implementation */
    @Override
    public void mouseMoved(MouseEvent m) {

        // Events von allen TimeTables     
        if (m.getSource() instanceof TimeTable) {

            TimeTable timeTable = (TimeTable) m.getSource();

            DayColumnData dayColumnData = (DayColumnData) timeTable.getModel();
            if (dayColumnData.getScheduleDay().getDayID() != schedule.getTempScheduleDayID()) { // prüft ob DayColumn gewechselt hat
                cleanTimeColumns();
            }
            Point point = m.getPoint();
            if (timeTable.columnAtPoint(point) == 1 || timeTable.columnAtPoint(point) == 0) {
                selectedCol = 0;
                //  schedule.setTempSelectedCol(0);
            } else if (timeTable.columnAtPoint(point) == 3 || timeTable.columnAtPoint(point) == 2) {
                selectedCol = 2;
                //  schedule.setTempSelectedCol(2);
            }
            selectedRow = timeTable.rowAtPoint(point);
            timeTable.repaint();
            schedule.setTempScheduleDayID(scheduleDayID);
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
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void mouseDragged(MouseEvent me) {
    }

}
