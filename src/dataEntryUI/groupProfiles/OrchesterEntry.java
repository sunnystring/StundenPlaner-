/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.groupProfiles;

import core.Group;
import dataEntryUI.GroupEntry;
import mainframe.MainFrame;

/**
 *
 * @author mathiaskielholz
 */
public class OrchesterEntry extends GroupEntry{

    public OrchesterEntry(MainFrame mainFrame, String title, Group group) {
        super(mainFrame, title, group);
        fitInputMaskToGroupProfile();
    }

    @Override
    public void fitInputMaskToGroupProfile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
