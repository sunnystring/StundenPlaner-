/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group;

import core.Database;
import core.Group;
import dataEntryUI.ProfileInputMask;
import javax.swing.JComboBox;

/**
 *
 * @author mathiaskielholz
 */
public class GroupInputMask extends ProfileInputMask {

    public GroupInputMask(Database database) {
        super(database);
        adjustNameLabels();
    }

    public void workshopProfile() {
        lectionLength = "50";
        removeLectionTypeEntry();
    }

    public void instrumentalformationProfile() {
        lectionTypes = new String[]{"45", "60", "90", "105", "120", "180"};
        addLectionTypeEntry();
    }

    public void chorProfile() {
        lectionTypes = new String[]{"30", "45", "60", "90", "120"};
        addLectionTypeEntry();
    }

    public void grundschulungProfile() {
        lectionLength = "45";
        removeLectionTypeEntry();
    }

    public void otherProfile() {
        lectionTypes = new String[]{"45", "60", "75", "90"};
        addLectionTypeEntry();
    }

    private void removeLectionTypeEntry() {
        top.remove(lectiontypeLabel);
        top.remove(lectiontypeSelectionBox);
    }

    private void addLectionTypeEntry() {
        removeLectionTypeEntry();
        lectiontypeSelectionBox = new JComboBox(lectionTypes);
        addLectiontypeSelectionListener();
        top.add(lectiontypeLabel);
        top.add(lectiontypeSelectionBox);
    }

    private void adjustNameLabels() {
        firstnameLabel.setText("Name:");
        nameLabel.setText(" Zusatz:");
    }

    public void setGroup(Group group) {
        super.setProfile(group);
    }
}
