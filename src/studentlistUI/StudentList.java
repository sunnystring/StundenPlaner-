/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlistUI;

import java.awt.Point;
import java.awt.event.MouseEvent;
import studentListData.StudentListData;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
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
        setDefaultRenderer(String.class, studentField);
        lectionField = timeTable.getLectionField();
        timeField = timeTable.getTimeField();
        addMouseListener(studentListData);
        addMouseMotionListener(studentField);
        addMouseListener(lectionField);
        addMouseListener(timeField);
        addMouseListener(scheduleData);
        setShowGrid(true);
        setGridColor(Colors.BACKGROUND);
        setFillsViewportHeight(true);
        setBackground(Colors.BACKGROUND);
        setRowHeight(25);
    }

    public void update() {
        createDefaultColumnsFromModel();
        studentField.setColumnCount(studentListData.getColumnCount());
        studentField.resetStudentRows();
    }

    public void showNumberOfStudents() {
        JTableHeader header = getTableHeader();
        header.getColumnModel().getColumn(0).setHeaderValue(getColumnName(0));
        header.repaint();
    }

    public StudentFieldData getStudentFieldDataAt(int row, int col) {
        return (StudentFieldData) getValueAt(row, col);
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
            if (field.isIncompatible() && !field.isBlocked()) {
                return "unvereinbar";
            } else if (field.isBlocked()) {
                return "gesperrt";
            } else {
                return null;
            }
        }
        return null;
    }
}
