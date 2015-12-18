/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import javax.swing.JDialog;
import mainframe.MainFrame;
import scheduleData.ScheduleData;
import scheduleUI.Schedule;

/**
 *
 * Eingabe der Unterrichtstage und -zeiten über {@link ScheduleEntryMask} für die Erstellung des {@link Schedule}
 */
public class ScheduleEntryDialog extends JDialog {

    public ScheduleEntryDialog(MainFrame mainFrame ) {
        super(mainFrame);
        setTitle("Stundenplan erstellen");
        setModal(true);
        setLocationRelativeTo(mainFrame);
        setResizable(false);
        ScheduleEntryMask scheduleEntryMask = mainFrame.getScheduleEntryMask();
        scheduleEntryMask.setMainFrame(mainFrame);
        scheduleEntryMask.setScheduleEntryDialog(this);
        add(scheduleEntryMask);
        pack();

    }
}
