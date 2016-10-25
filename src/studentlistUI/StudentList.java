/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlistUI;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import studentListData.StudentListData;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import scheduleUI.TimeTable;
import scheduleData.ScheduleData;
import scheduleUI.LectionField;
import scheduleUI.TimeField;
import studentListData.StudentFieldData;
import utils.Colors;

/**
 *
 * View der ganzen Schülerliste
 */
public class StudentList extends JTable {

    private StudentField studentField;
    private HeaderField headerField;
    private final StudentListData studentListData;
    private final ScheduleData scheduleData;
    private LectionField lectionField;
    private TimeField timeField;

    public StudentList(StudentListData studentListData, TimeTable timeTable) {
        this.studentListData = studentListData;
        setModel(studentListData);
        headerField = new HeaderField();
        getTableHeader().setDefaultRenderer(headerField);
        getTableHeader().setVisible(false);
        getTableHeader().setDefaultRenderer(headerField);
        scheduleData = (ScheduleData) timeTable.getModel();
        studentField = new StudentField(this, studentListData);
        setDefaultRenderer(StudentFieldData.class, studentField);
        lectionField = timeTable.getLectionField();
        timeField = timeTable.getTimeField();
        addMouseListener(studentListData);
        addMouseMotionListener(studentField);
        addMouseListener(lectionField);
        addMouseListener(timeField);
        addMouseListener(scheduleData);
        setShowGrid(true);
        setGridColor(Colors.BACKGROUND_COLOR);
        setFillsViewportHeight(true);
        setBackground(Colors.BACKGROUND_COLOR);
        setRowHeight(25);

    }

    private void addRowSorter() {
        TableRowSorter<StudentListData> rowSorter = new TableRowSorter<>(studentListData);
        for (int col = 1; col < getColumnCount(); col++) {
            rowSorter.setComparator(col, new Comparator<StudentFieldData>() {
                @Override
                public int compare(StudentFieldData field1, StudentFieldData field2) {
                    return field1.getStudentDay().compareTo(field2.getStudentDay());
                }
            });
        }
        rowSorter.setSortable(0, false);
        setRowSorter(rowSorter);
    }

    public void setup() {
        createDefaultColumnsFromModel(); 
        addRowSorter();
        studentField.setColumnCount(studentListData.getColumnCount());
        studentField.resetRowIndices();
    }

    public void showNumberOfStudents() {
        JTableHeader header = getTableHeader();
        header.getColumnModel().getColumn(0).setHeaderValue(getColumnName(0));
        header.repaint();
    }

    public StudentFieldData getStudentFieldDataAtView(int rowInView, int columnInView) {
        return (StudentFieldData) getValueAt(rowInView, columnInView);
    }

    public StudentField getStudentField() {
        return studentField;
    }

    @Override
    public String getToolTipText(MouseEvent e) {
        Point p = e.getPoint();
        int row = rowAtPoint(p), col = columnAtPoint(p);
        int validCol = convertColumnIndexToModel(col);
        if (row >= 0) {
            StudentFieldData field = (StudentFieldData) getValueAt(row, validCol);
            if (field.isUnallocatable()) {
                return "nicht einteilbar";
            } else if (field.isBlocked()) {
                return "gesperrte Zeit";
            } else if (field.isIncompatible()) {
                return "unvereinbare Zeit";
            }
        }
        return null;
    }
}
