/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.schedule;

import dataEntryUI.DataEntryAndEdit;
import utils.GUIConstants;
import mainframe.MainFrame;

/**
 *
 * Zeitrahmen des Stundenplans ändern
 */
public class ScheduleEdit extends DataEntryAndEdit {

    public ScheduleEdit(MainFrame mainFrame) {
        super(mainFrame, "Stundenplan ändern");
        this.mainFrame = mainFrame;
        setMinimumSize(GUIConstants.SCHEDULE_DIMENSION);
        setUpMask();
        pack();
        setLocation(getLocationXCoordinate(), 200);
    }

    @Override
    public void setUpMask() {
        scheduleInputMask.removeButtonListeners();
        scheduleInputMask.addCancelButtonListener(this);
        scheduleInputMask.addEditSaveButtonListener(this);
        add(scheduleInputMask);
    }
}
