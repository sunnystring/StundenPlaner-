/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule_new;

import core2.ScheduleData;
import core2.StudentData;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class Schedule_new extends JPanel {

    private JPanel dayColumn;
    private JLabel header;
    private TimeTable timeTable;
    private ScheduleData scheduleData;
    private StudentData studentData;
    private ArrayList<TimeTable> dayColumnList;

    public Schedule_new(ScheduleData scheduleData, StudentData studentData) {

        this.scheduleData = scheduleData;
        this.studentData = studentData;

        dayColumnList = new ArrayList<>();

        setBackground(Colors.BACKGROUND);

    }

    public void createDayColumns() {

        setLayout(new GridLayout(1, scheduleData.getNumberOfDays()));

        for (int i = 0; i < scheduleData.getNumberOfDays(); i++) {

            dayColumn = new JPanel(new BorderLayout());
            header = new DayField(scheduleData.getDayColumnData(i).getDayName());
            timeTable = new TimeTable(scheduleData.getDayColumnData(i));
          //  scheduleData.getDayColumnData(i).setTimeTable(timeTable); // DayColumnData(= TableModel) braucht Referenz auf "seine" TimeTable
            dayColumnList.add(timeTable);   // TimeTables = DayColumns speichern
            dayColumn.add(header);
            dayColumn.add(BorderLayout.NORTH, header);
            dayColumn.add(BorderLayout.CENTER, timeTable);
            add(dayColumn); // zeichnen
        }
    }

    /*  Getter, Setter   */
    public ArrayList<TimeTable> getDayColumnList() {
        return dayColumnList;
    }

//
//    @Override
//    public void mousePressed(MouseEvent m) {
//
//        JTable table = (JTable) m.getSource();
//        int row = table.rowAtPoint(m.getPoint());  //  = StudentID
//        int col = table.columnAtPoint(m.getPoint());
//
//      //  if (m.getSource() instanceof StudentList2) {  // Events von StudentList
//
//      //      System.out.println("StudentList:  " + "row = " + row + "    col = " + col);
//            
//           
//   //   studentData.getStudent(row).getStudentTimes().getStudentDay(col-1)
//     //        dayColumnList.get(col-1).getTimeField().setXXX(studentDay).......
//            
//            
//     //   }
//
//    }
//    
//    // Test
//    
//    
//
//   
//    @Override
//    public void mouseClicked(MouseEvent me) {
//    }
//
//    @Override
//    public void mouseReleased(MouseEvent me) {
//    }
//
//    @Override
//    public void mouseEntered(MouseEvent me) {
//    }
//
//    @Override
//    public void mouseExited(MouseEvent me) {
//    }
}
