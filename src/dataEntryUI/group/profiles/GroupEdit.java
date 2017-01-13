/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group.profiles;

import core.Profile;
import core.ProfileTypes;
import dataEntryUI.DataEntryAndEdit;
import mainframe.MainFrame;

/**
 *
 * Dialogfenster Gruppenprofil ändern oder löschen
 */
public class GroupEdit extends DataEntryAndEdit {

    private Profile group;

    public GroupEdit(MainFrame mainFrame, Profile group) {
        super(mainFrame, "Gruppenprofil ändern oder löschen");
        this.group = group;
        setUpMask();
        pack();
    }

    public void selectProfile() {
        groupInputMask.restoreSelectionEntry(group);
    }

    @Override
    public void setUpMask() {
        group.setProfileType(ProfileTypes.REGULAR_GROUP);
        groupInputMask.setProfile(group);
        groupInputMask.setupEditUI(this);
        groupInputMask.updateUpperEntryFields();
        add(groupInputMask);
    }

}
