/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import core.Database;
import core.Group;
import javax.swing.JComboBox;

/**
 *
 * @author mathiaskielholz
 */
public class GroupInputMask extends StudentInputMask {

    public GroupInputMask(Database database) {
        super(database);
        adjustNameLabels();
        
    }

    public void fitToWorkshopProfile() {
        lectionLength = "50";
        removeLectionTypeEntry();
    }

    public void fitToInstrumentalformationProfile() {
        lectionTypes = new String[]{"45", "60", "90", "105", "120", "180"};
        addLectionTypeEntry();
    }

    public void fitToChorProfile() {
        lectionTypes = new String[]{"30", "45", "60", "90", "120"};
        addLectionTypeEntry();
    }

    public void fitToGrundschulungProfile() {
        lectionLength = "45";
        removeLectionTypeEntry();
    }

    public void fitToOtherProfile() {
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
        super.setStudent(group);
    }

}
