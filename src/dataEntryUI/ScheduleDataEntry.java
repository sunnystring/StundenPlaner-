/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import core.Database;
import core.ScheduleTimes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import mainframe.MainFrame;

/**
 *
 * @author Mathias
 */
public class ScheduleDataEntry extends JDialog {

    private MainFrame mainFrame;
    private Database database;
    private ScheduleTimes scheduleTimes;

    public ScheduleDataEntry(MainFrame mainFrame, Database database) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        this.database = database;
        scheduleTimes = database.getScheduleTimes();
        setTitle("Stundenplan erstellen");
        setModal(true);
        setLocationRelativeTo(mainFrame);
        setResizable(false);
        //  scheduleDataEntryMask.addListeners(this);
        //   add(scheduleDataEntryMask); 
        //  pack();
    }

    public class CancelButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ScheduleDataEntry.this.dispose();
        }
    }

    public class SaveButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            scheduleTimes.setValidScheduleDays();
            database.setNumberOfDays(scheduleTimes.getNumberOfDays());
            mainFrame.createSchedule();
            mainFrame.prepareStudentList();
            ScheduleDataEntry.this.dispose();
        }
    }

}
