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
import studentlistGUI.StudentList;
import util.Colors;
import util.Time;

/**
 *
 * @author mathiaskielholz
 */
public class TimeField extends JLabel implements MouseMotionListener, MouseListener, TableCellRenderer {

    private DayColumnData dayColumnData;
    private int scheduleDayID, studentID, studentDayID; // Koordinaten StudentList und TimeTable;
    private JTable timeTable;
    private int rowIndex, colIndex; // Koordinaten TimeTable während Rendering
    private int validStart1, validStart2, validEnd1, validEnd2, favorite;

    public TimeField(DayColumnData dayColumnData) {

        this.dayColumnData = dayColumnData;
        scheduleDayID = dayColumnData.getScheduleDay().getDayID();

        dayColumnData.resetValidTimeMarks();
  //      validStart1 = validStart2 = dayColumnData.getTotalNumberOfFields() + 2;  // init: sicher ausserhalb der validTime-Range
  //      validEnd1 = validEnd2 = favorite = -2;

        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
        setOpaque(true);
    }
//  fort
//    private void setValidTimeMarks(StudentDay day) {
//
//        for (int i = 0; i < dayColumnData.getTotalNumberOfFields(); i++) {
//
//            Time columnTime = dayColumnData.getTimeColumn().get(i);
//
//            if (columnTime.equals(day.getStartTime1())) {
//                validStart1 = i;
//            }
//            if (columnTime.equals(day.getEndTime1())) {
//                validEnd1 = i;
//            }
//            if (columnTime.equals(day.getStartTime2())) {
//                validStart2 = i;
//            }
//            if (columnTime.equals(day.getEndTime2())) {
//                validEnd2 = i;
//            }
//            if (columnTime.equals(day.getFavorite())) {
//                favorite = i;
//            }
//        }
//    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        System.out.println("row= " + row + "  col= " + col + "  value= " + value);

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
        if (dayColumnData.isMinute(rowIndex)) {
            setText(dayColumnData.getMinute(rowIndex));
        } else {
            setText(dayColumnData.getHour(rowIndex));
        }
        // Foreground zeichen
        if (dayColumnData.isValidTime(rowIndex)) {  // validTime = vom Lehrer vorgegebene Unterrichtszeit
            setForeground(Color.BLACK);
        } else {
            setForeground(Color.LIGHT_GRAY);
        }
        // Background zeichnen
        if (favorite == rowIndex) {
            setBackground(Colors.FAVORITE);
        } else if ((validStart1 <= rowIndex && validEnd1 >= rowIndex) || (validStart2 <= rowIndex && validEnd2 >= rowIndex)) {
            setBackground(Colors.LIGHT_GREEN);
        } else {
            setBackground(Colors.BACKGROUND);
        }
        if (!dayColumnData.isMinute(rowIndex) && favorite != rowIndex) {
            if ((validStart1 == rowIndex && validEnd1 == rowIndex) || (validStart2 == rowIndex && validEnd2 == rowIndex)) { // falls Einzellektion auf volle Stunde fällt 
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
 //fort
  //      validStart1 = validStart2 = dayColumnData.getTotalNumberOfFields() + 2;  // Reset
    //    validEnd1 = validEnd2 = favorite = -2;  // Reset
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
