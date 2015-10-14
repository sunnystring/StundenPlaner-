/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlist2;

import core.ScheduleDay;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import schedule_new.DayField_new;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class StudentList2 extends JTable {

    public StudentList2() {

        super(35, 4);
        setShowGrid(true);
        // getColumnModel().setColumnSelectionAllowed(true); //  in alle Zellen kann geschrieben werden
        setFillsViewportHeight(true);
         setSelectionBackground(Colors.LIGHT_GREEN);
        setBackground(Colors.LIGHT_GRAY);
        setColumnSelectionAllowed(true);
        setRowSelectionAllowed(true);
        setCellSelectionEnabled(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        getTableHeader().setDefaultRenderer(new DayField_new());  //dynamisch

    }

}
