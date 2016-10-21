/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.schedule;

import dataEntryUI.DataEntryAndEdit;
import mainframe.MainFrame;

/**
 *
 * Zeitrahmen des Stundenplans ändern
 */
public class ScheduleEdit extends DataEntryAndEdit {

    public ScheduleEdit(MainFrame mainFrame) {
        super(mainFrame, "Stundenplan ändern");
        this.mainFrame = mainFrame;
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
