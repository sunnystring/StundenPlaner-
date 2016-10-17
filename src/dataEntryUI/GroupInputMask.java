/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import core.Database;
import core.Group;

/**
 *
 * @author mathiaskielholz
 */
public class GroupInputMask extends StudentInputMask {

    public GroupInputMask(Database database) {
        super(database);
    }

    public void fitToBandProfile() {

    }

    public void setGroup(Group group) {
        super.setStudent(group);
    }

}
