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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
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
    private int scheduleDayID, studentID, studentDayID; // Koordinaten StudentList und TimeTable;
    private JTable timeTable;
    private int rowIndex, colIndex; // Koordinaten TimeTable während Rendering

    public TimeField(DayColumnData dayColumnData) {

        this.dayColumnData = dayColumnData;
        scheduleDayID = dayColumnData.getScheduleDay().getDayID();

        dayColumnData.resetValidTimeMarks();

        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        FieldData fieldData = (FieldData) value;

        timeTable = table;  // Referenz auf die zugeordnete TimeTable
        colIndex = col;
        rowIndex = row;

        // Spalte auswählen
        if (colIndex == 0) {
            rowIndex = row;
        }
        if (colIndex == 2) {
            rowIndex = row + dayColumnData.getTotalNumberOfFields() / 2;
        }
        // Text ausgeben
        if (fieldData.isMinute(rowIndex)) {
            setText(fieldData.getMinute(rowIndex));
        } else {
            setText(fieldData.getHour(rowIndex));
        }
        // Foreground zeichen
        if (dayColumnData.isValidTime(rowIndex)) {  // validTime = vom Lehrer vorgegebene Unterrichtszeit
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
        if (!fieldData.isMinute(rowIndex) && fieldData.getValidTime() != FieldData.FAVORITE) {
            if (fieldData.getValidTime() == FieldData.TIME_INTERVAL_1 || fieldData.getValidTime() == FieldData.TIME_INTERVAL_2) { // falls Einzellektion auf volle Stunde fällt 
                setBackground(Colors.LIGHT_GREEN);
            } else {
                setBackground(Colors.TIMEFIELD_HOUR);
            }
        }
        return this;
    }

    /*  MouseListener Implementation */
    @Override
    public void mousePressed(MouseEvent m) {

        dayColumnData.resetValidTimeMarks();

        if (m.getSource() instanceof StudentList) {

            StudentList studentList = (StudentList) m.getSource();
            studentID = studentList.rowAtPoint(m.getPoint());
            studentDayID = studentList.columnAtPoint(m.getPoint()) - 1;
            StudentDay studentDay;

            if (scheduleDayID == studentDayID) {  // Tag wählen

                if (studentDayID >= 0) {  // 1. Column ist NameField -> ArrayOutOfBounds
                    studentDay = studentList.getStudentData().getStudent(studentID).getStudentDay(studentDayID); // ????

                    dayColumnData.setValidTimeMarks(studentDay);  // setzt die Timemarks des angeklickten Tages
                    timeTable.repaint();
                }
            }
        }

        // Events von TimeTable     
        if (m.getSource() instanceof TimeTable) {
            TimeTable timeTable = (TimeTable) m.getSource();
        }
    }

    /*  MouseMotionListener Implementation */
    @Override
    public void mouseMoved(MouseEvent me) {
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
