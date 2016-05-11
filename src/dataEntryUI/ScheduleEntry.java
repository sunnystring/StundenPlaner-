/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import mainframe.MainFrame;
import scheduleUI.Schedule;

/**
 *
 * Eingabe der Unterrichtstage/-zeiten für die Erstellung des {@link Schedule}
 */
public class ScheduleEntry extends DataEntryAndEdit {

    public ScheduleEntry(MainFrame mainFrame) {
        super(mainFrame, "Stundenplan erstellen");
        setUpMask();
        pack();
    }

    @Override
    public void setUpMask() {
        scheduleInputMask.addCancelButtonListener(this);
        scheduleInputMask.addSaveButtonListener(this);
        add(scheduleInputMask);
    }
}
