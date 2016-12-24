/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import utils.GUIConstants;
import dataEntryUI.student.StudentInputMask;
import dataEntryUI.schedule.ScheduleInputMask;
import dataEntryUI.group.GroupInputMask;
import dataEntryUI.group.profiles.GroupEdit;
import dataEntryUI.group.profiles.GroupEntry;
import dataEntryUI.schedule.ScheduleEdit;
import dataEntryUI.schedule.ScheduleEntry;
import dataEntryUI.student.StudentEdit;
import dataEntryUI.student.StudentEntry;
import javax.swing.JDialog;
import mainframe.MainFrame;
import scheduleData.ScheduleTimeFrame;
import studentListData.StudentListData;

/**
 *
 * Superklasse aller Eingabe-Dialogfenster {@link ScheduleEntry}, {@link ScheduleEdit}, {@link StudentEntry}, {@link StudentEdit},
 *  {@link GroupEntry}, {@link GroupEdit}
 */
public abstract class DataEntryAndEdit extends JDialog {

    protected MainFrame mainFrame;
    protected ScheduleInputMask scheduleInputMask;
    protected StudentInputMask studentInputMask;
    protected GroupInputMask groupInputMask;

    public DataEntryAndEdit(MainFrame mainFrame, String title) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        scheduleInputMask = mainFrame.getScheduleInputMask();
        studentInputMask = mainFrame.getStudentInputMask();
        groupInputMask = mainFrame.getGroupInputMask();
        setTitle(title);
        setModal(true);
        setMinimumSize(GUIConstants.DEFAULT_ENTRY_DIMENSION);
        setLocation((int) (mainFrame.getSize().getWidth() / 2), 200);
        setResizable(false);
    }

    public int getLocationXCoordinate() {
        return (int) (mainFrame.getSize().getWidth() / 2) - (int) (this.getSize().getWidth() / 2);
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public ScheduleTimeFrame getScheduleTimeFrame() {
        return mainFrame.getScheduleData().getTimeFrame();
    }

    public StudentListData getStudentListData() {
        return mainFrame.getStudentListData();
    }

    public abstract void setUpMask();
}
