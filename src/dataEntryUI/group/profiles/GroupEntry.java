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
 * @author mathiaskielholz
 */
public class GroupEntry extends DataEntryAndEdit {

    private Profile group;

    public GroupEntry(MainFrame mainFrame, String profileName, Profile group) {
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
    }

    public void setChorProfile() {
        groupInputMask.chorProfile();
    }

    public void setGrundschulungProfile() {
        groupInputMask.grundschulungProfile();
    }

    public void setOtherProfile() {
        groupInputMask.otherProfile();
    }

    @Override
    public void setUpMask() {
        group.setProfileType(ProfileTypes.GROUP);
        groupInputMask.setProfile(group);
        groupInputMask.setupEntryUI(this);
        groupInputMask.clearUpperEntryFields();
        add(groupInputMask);
    }
}
