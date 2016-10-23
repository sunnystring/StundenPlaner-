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
public class GroupEntry extends DataEntryAndEdit {

    private Group group;

    public GroupEntry(MainFrame mainFrame, String profileName, Group group) {
        super(mainFrame, profileName + " erstellen");
        this.group = group;
        setUpMask();
        pack();
    }

    public void setWorkshopProfile() {
        groupInputMask.workshopProfile();
    }

    public void setInstrumentalformationProfile() {
        groupInputMask.instrumentalformationProfile();
        groupInputMask.addNewLectionTypeEntry();
    }

    public void setChorProfile() {
        groupInputMask.chorProfile();
        groupInputMask.addNewLectionTypeEntry();
    }

    public void setGrundschulungProfile() {
        groupInputMask.grundschulungProfile();
    }

    public void setOtherProfile() {
        groupInputMask.otherProfile();
        groupInputMask.addNewLectionTypeEntry();
    }

    @Override
    public void setUpMask() {
        groupInputMask.setGroup(group);
        groupInputMask.setupEntryUI(this);
        add(groupInputMask);
    }
}
