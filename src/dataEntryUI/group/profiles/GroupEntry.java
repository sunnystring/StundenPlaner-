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
 * Dialogfenster Eingabe neues Gruppenprofil
 */
public class GroupEntry extends DataEntryAndEdit {

    private Profile group;

    public GroupEntry(MainFrame mainFrame, String profileName, Profile group) {
        super(mainFrame, profileName + " erstellen");
        this.group = group;
        setUpMask();
        pack();
    }

    public void setGroupProfileMask() {
        groupInputMask.createSelectionEntry();
    }

    @Override
    public void setUpMask() {
        group.setProfileType(ProfileTypes.REGULAR_GROUP);
        groupInputMask.setProfile(group);
        groupInputMask.setupEntryUI(this);
        groupInputMask.clearUpperEntryFields();
        add(groupInputMask);
    }
}
