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

    public GroupEdit(MainFrame mainFrame, Group group) {
        super(mainFrame, group.getProfileName()+"-Profil ändern oder löschen");
        this.group = group;
        setUpMask();
        pack();
    }

    public void selectProfile() {
        switch (group.getProfileName()) {
            case ProfileNames.KGU:
                //....
                break;
            case ProfileNames.WORKSHOP:
                groupInputMask.workshopProfile();
                break;
            case ProfileNames.INSTR_FORMATION:
                groupInputMask.instrumentalformationProfile(group);
                break;
            case ProfileNames.CHOR:
                groupInputMask.chorProfile(group);
                break;
            case ProfileNames.GRUNDSCHULUNG:
                groupInputMask.grundschulungProfile();
                break;
            case ProfileNames.ANDERES:
                groupInputMask.otherProfile(group);
                break;
            default:
                break;

        }
    }

    @Override
    public void setUpMask() {
        groupInputMask.setGroup(group);
        groupInputMask.setupEditUI(this);
        groupInputMask.updateUpperEntryFields();
        add(groupInputMask);
    }

}
