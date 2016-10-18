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
public class GroupEntry extends DataEntryAndEdit {

    private Group group;

    public GroupEntry(MainFrame mainFrame, String groupType, Group group) {
        super(mainFrame, groupType + " erstellen");
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
        groupInputMask.setGroup(group);
        groupInputMask.setupEntryUI(this);
        add(groupInputMask);
    }

 //   public abstract void fitInputMaskToGroupProfile();
}
