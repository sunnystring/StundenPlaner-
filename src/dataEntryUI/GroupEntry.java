/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import core.Group;
import mainframe.MainFrame;

/**
 *
 * @author mathiaskielholz
 */
public abstract class GroupEntry extends DataEntryAndEdit {

    private Group group;

    public GroupEntry(MainFrame mainFrame, String title, Group group) {
        super(mainFrame, title);
        this.group = group;
        setUpMask();
        pack();
    }

    @Override
    public void setUpMask() {
        groupInputMask.addEntryButtons();
        groupInputMask.addCancelButtonListener(this);
        groupInputMask.addSaveButtonListener(this);
        groupInputMask.setGroup(group);
        groupInputMask.setUpTimeSelectionTable();
        add(groupInputMask);
    }

    public abstract void fitInputMaskToGroupProfile();
}
