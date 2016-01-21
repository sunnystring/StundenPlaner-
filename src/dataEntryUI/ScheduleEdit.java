/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import mainframe.MainFrame;

/**
 *
 *  Zeitrahmen des Stundenplans ändern
 */
public class ScheduleEdit extends DataEntryAndEdit {

    public ScheduleEdit(MainFrame mainFrame) {
        super(mainFrame, "Stundenplan ändern");
        setUpMask();
        pack();
    }

    @Override
    public void setUpMask() {
        scheduleInputMask.addCancelButtonListener(this);
        scheduleInputMask.addEditSaveButtonListener(this);
        add(scheduleInputMask);
    }

}
