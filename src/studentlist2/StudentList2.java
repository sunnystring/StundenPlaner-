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


/**
 *
 * @author Mathias
 */
public class StudentList2 extends JTable {

  
 
    public StudentList2() {
   
         super(35, 4);
        setShowGrid(true);
        getColumnModel().setColumnSelectionAllowed(true); //  in alle Zellen kann geschrieben werden
        setFillsViewportHeight(true);
         setPreferredSize(new Dimension(600, 0));
    }

 



}
