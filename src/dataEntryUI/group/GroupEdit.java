/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group;

import core.Group;
import dataEntryUI.DataEntryAndEdit;
import mainframe.MainFrame;

/**
 *
 * @author mathiaskielholz
 */
public class GroupEdit extends DataEntryAndEdit {

    private Group group;

    public GroupEdit(MainFrame mainFrame, String groupType, Group group) {
        //  super(mainFrame, groupType + " erstellen");
        super(mainFrame, "Schülerprofil ändern oder löschen");
        this.group = group;
        setUpMask();
        pack();
    }

    @Override
    public void setUpMask() {
        groupInputMask.setGroup(group);
        //    studentInputMask.setUpTimeSelectionTable();
        //    studentInputMask.updateUpperEntryFields();
        groupInputMask.setupEditUI(this);
        add(groupInputMask);
    }

}
