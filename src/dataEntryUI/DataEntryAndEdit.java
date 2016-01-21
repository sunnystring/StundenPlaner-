/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import javax.swing.JDialog;
import mainframe.MainFrame;

/**
 *
 * Superklasse von {@link ScheduleEntry}, {@link ScheduleEdit}, {@link StudentEntry} und {@link StudentEdit}
 */
public abstract class DataEntryAndEdit extends JDialog {

    protected MainFrame mainFrame;
    protected ScheduleInputMask scheduleInputMask;
    protected StudentInputMask studentInputMask;

    public DataEntryAndEdit(MainFrame mainFrame, String title) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        scheduleInputMask = mainFrame.getScheduleInputMask();
        studentInputMask = mainFrame.getStudentInputMask();
        setTitle(title);
        setModal(true);
        setLocationRelativeTo(mainFrame);
        setResizable(false);
    }

    public abstract void setUpMask();
}
