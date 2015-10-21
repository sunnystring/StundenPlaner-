/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule_new;

import core.Student;
import core.StudentDay;
import core2.DayColumnData;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import studentlist_new.StudentList2;
import util.Colors;
import util.Time;

/**
 *
 * @author mathiaskielholz
 */
public class TimeField_new extends JLabel implements TableCellRenderer, MouseMotionListener, MouseListener {

    private DayColumnData dayColumnData;
    private int scheduleDayID;
    private JTable timeTable;
    private int rowIndex, colIndex; //  Indexes TimeTable ( = Position in TimeColumn in DayColumndata)
    private static final int TIME_COL1 = 0, TIME_COL2 = 2;
    private int colMoved, studentID, studentDayID; // Koordinaten StudentList und TimeTable
    private int validStart1, validStart2, validEnd1, validEnd2, favorite;

    public TimeField_new(DayColumnData dayColumnData) {

        this.dayColumnData = dayColumnData;
        scheduleDayID = dayColumnData.getScheduleDay().getDayID();

        System.out.println("ScheduleDay Name (Konstruktor): " + dayColumnData.getScheduleDay().getDayName());
        System.out.println("ScheduleDayID (Konstruktor): " + scheduleDayID + "\n");

        rowIndex = -2;
//        colMoved = -2;
//        rowClicked = -2;
//        colClicked = -2;

        validStart1 = validStart2 = validEnd1 = validEnd2 = favorite = -2; // init. ausserhalb Range

        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
        setOpaque(true);
    }

    private void setValidTimeMarks(StudentDay day) {
        for (int i = 0; i < dayColumnData.getTotalNumberOfFields(); i++) {

            Time columnTime = dayColumnData.getTimeColumn().get(i);

            if (columnTime.equals(day.getStartTime1())) {
                validStart1 = i;
            }
            if (columnTime.equals(day.getEndTime1())) {
                validEnd1 = i;
            }
            if (columnTime.equals(day.getStartTime2())) {
                validStart2 = i;
            }
            if (columnTime.equals(day.getEndTime2())) {
                validEnd1 = i;
            }
            if (columnTime.equals(day.getFavorite())) {
                favorite = i;
            }
        }
    }

//    private void paintValidTimes(TimeTable table) {
//
//        if (colIndex >= validStart1 && colIndex <= validEnd1) {
//            table.repaint(table.getCellRect(rowIndex, colIndex, false));
//        }
//        if (colIndex >= validStart2 && colIndex <= validEnd2) {
//            table.repaint(table.getCellRect(rowIndex, colIndex, false));
//        }
//        if (colIndex == favorite) {
//            table.repaint(table.getCellRect(rowIndex, colIndex, false));
//        }
//    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        timeTable = table;  // Referenz auf die zugeordnete TimeTable
        colIndex = col;
        
        // Spalte auswählen
        if (colIndex == TIME_COL1) {
            rowIndex = row;
        }
        if (colIndex == TIME_COL2) {
            rowIndex = row + dayColumnData.getTotalNumberOfFields() / 2;
        }

        // Background zeichnen
        if (dayColumnData.isMinute(rowIndex)) {
            setText(dayColumnData.getMinute(rowIndex));
            setBackground(Color.WHITE);
        } else {
            setText(dayColumnData.getHour(rowIndex));
            setBackground(Colors.TIMEFIELD_HOUR);
        }

        // Foreground zeichen
        if (dayColumnData.isValidTime(rowIndex)) {
            setForeground(Color.BLACK);
        } else {
            setForeground(Color.LIGHT_GRAY);
        }

        // ValidTimes zeichnen
        if (colIndex >= validStart1 && colIndex <= validEnd1) {
            setBackground(Colors.LIGHT_GREEN);
        }
        if (colIndex >= validStart2 && colIndex <= validEnd2) {
            setBackground(Colors.LIGHT_GREEN);
        }
        if (colIndex == favorite) {
            setBackground(Colors.FAVORITE);
        }

        return this;
    }

    /*  MouseListener Implementation */
    @Override
    public void mousePressed(MouseEvent m) {

        // Events von StudentList
        
        if (m.getSource() instanceof StudentList2) {

            StudentList2 studentList = (StudentList2) m.getSource();
            studentID = studentList.rowAtPoint(m.getPoint());
            studentDayID = studentList.columnAtPoint(m.getPoint()) - 1;

         //   System.out.println("ScheduleDayID: " + scheduleDayID);
            //  System.out.println("StudentDayID: " + (studentList.columnAtPoint(m.getPoint()) - 1));
            if (scheduleDayID == studentDayID) {  // Tag wählen

                if (studentDayID >= 0) {  // 1. Column ist NameField -> ArrayOutOfBounds
                    StudentDay studentDay = studentList.getStudentData().getStudent(studentID).getStudentDay(studentDayID);
                    setValidTimeMarks(studentDay);
                    timeTable.repaint();  // timeTable.getCellRect(rowIndex, colIndex, false)
                }
                System.out.println("StudentList:  ");
                System.out.println("studentID = " + studentID + "    studentDayID = " + studentDayID + "\n");
                System.out.println("TimeTable:  ");
                System.out.println("ScheduleDayID: " + scheduleDayID);
                System.out.println("rowIndex = " + rowIndex + "    colIndex = " + colIndex);
                System.out.println("validStart1 = " + validStart1 + "    validEnd1 = " + validEnd1 + "\n");
            }
        }

        // Events von TimeTable     
        if (m.getSource() instanceof TimeTable) {
            TimeTable timeTable = (TimeTable) m.getSource();
//            studentIndex = timeTable.rowAtPoint(m.getPoint());
//            studentDayindex = timeTable.columnAtPoint(m.getPoint()) - 1;
//
//            System.out.println("TimeTable: " + "row = " + studentIndex + "    col = " + studentDayindex);
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
