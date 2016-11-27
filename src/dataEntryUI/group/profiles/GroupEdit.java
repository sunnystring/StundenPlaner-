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
public class GroupEdit extends DataEntryAndEdit {

    private Profile group;

    public GroupEdit(MainFrame mainFrame, Profile group) {
        super(mainFrame, group.getProfileName() + "-Profil ändern oder löschen");
        this.group = group;
        setUpMask();
        pack();
    }

    public void selectProfile() {
        switch (group.getProfileName()) {
            case ProfileTypes.KGU_NAME:
                //....
                break;
            case ProfileTypes.WORKSHOP_NAME:
                groupInputMask.restoreWorkshopProfile();
                break;
            case ProfileTypes.INSTR_FORMATION_NAME:
                groupInputMask.restoreInstrumentalformationProfile(group);
                break;
            case ProfileTypes.CHOR_NAME:
                groupInputMask.restoreChorProfile(group);
                break;
            case ProfileTypes.GRUNDSCHULUNG_NAME:
                groupInputMask.restoreGrundschulungProfile();
                break;
            case ProfileTypes.ANDERES_NAME:
                groupInputMask.restoreOtherProfile(group);
                break;
            default:
                break;

        }
    }

    @Override
    public void setUpMask() {
        group.setProfileType(ProfileTypes.GROUP);
        groupInputMask.setProfile(group);
        groupInputMask.setupEditUI(this);
        groupInputMask.updateUpperEntryFields();
        add(groupInputMask);
    }

}
