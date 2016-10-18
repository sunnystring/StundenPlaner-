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
public abstract class GroupEdit extends DataEntryAndEdit {

    public GroupEdit(MainFrame mainFrame, String groupType, Group group) {
        super(mainFrame, groupType + " erstellen");
    }

    @Override
    public void setUpMask() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
