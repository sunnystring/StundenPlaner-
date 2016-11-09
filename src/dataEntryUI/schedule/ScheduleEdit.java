/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.schedule;

import dataEntryUI.DataEntryAndEdit;
import dataEntryUI.DataEntryUIConstants;
import mainframe.MainFrame;

/**
 *
 * Zeitrahmen des Stundenplans ändern
 */
public class ScheduleEdit extends DataEntryAndEdit {

    public ScheduleEdit(MainFrame mainFrame) {
        super(mainFrame, "Stundenplan ändern");
        this.mainFrame = mainFrame;
        setMinimumSize(DataEntryUIConstants.SCHEDULE_DIMENSION);
        setUpMask();
        pack();
    }

    @Override
    public void setUpMask() {
        scheduleInputMask.removeButtonListeners();
        scheduleInputMask.addCancelButtonListener(this);
        scheduleInputMask.addEditSaveButtonListener(this);
        add(scheduleInputMask);
    }
}
