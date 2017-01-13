/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group;

import core.Database;
import core.Profile;
import dataEntryUI.ProfileInputMask;
import dataEntryUI.group.profiles.GroupEdit;
import dataEntryUI.group.profiles.GroupEntry;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

/**
 *
 * Eingabemaske Gruppenprofil von {@link GroupEntry}, {@link GroupEdit}
 */
public class GroupInputMask extends ProfileInputMask {

    public GroupInputMask(Database database) {
        super(database);
        adjustNameLabels();
        createLectionTypeSelectionListener();
    }

    private void adjustNameLabels() {
        lectiontypeLabel.setText("  Lektionsdauer:");
        firstnameLabel.setText("Name:");
        nameLabel.setText(" Zusatz:");
    }

    public void createSelectionEntry() {
        removeExistingLectionTypeEntry();
        lectionLength = "45";
        lectionTypes = new String[]{"45", "50", "60", "75", "90", "105", "120", "180"};
        lectiontypeSelectionBox = new JComboBox(lectionTypes);
        addNewLectionTypeEntry();
    }

    public void restoreSelectionEntry(Profile group) {
        createSelectionEntry();
        lectionLength = String.valueOf(group.getLectionLengthInMinutes());
        switch (lectionLength) {
            case "45":
                lectiontypeSelectionBox.setSelectedIndex(0);
                break;
            case "50":
                lectiontypeSelectionBox.setSelectedIndex(1);
                break;
            case "60":
                lectiontypeSelectionBox.setSelectedIndex(2);
                break;
            case "75":
                lectiontypeSelectionBox.setSelectedIndex(3);
                break;
            case "90":
                lectiontypeSelectionBox.setSelectedIndex(4);
                break;
            case "105":
                lectiontypeSelectionBox.setSelectedIndex(5);
                break;
            case "120":
                lectiontypeSelectionBox.setSelectedIndex(6);
                break;
            case "180":
                lectiontypeSelectionBox.setSelectedIndex(7);
                break;
            default:
                lectiontypeSelectionBox.setSelectedIndex(0);
                break;
        }
        addNewLectionTypeEntry();
    }

    private void removeExistingLectionTypeEntry() {
        top.remove(lectiontypeLabel);
        top.remove(lectiontypeSelectionBox);
    }

    public void addNewLectionTypeEntry() {
        addLectionTypeSelectionListener();
        top.add(lectiontypeLabel);
        top.add(lectiontypeSelectionBox);
    }

    @Override
    public void createLectionTypeSelectionListener() {
        lectiontypeSelectionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lectionLength = (String) lectiontypeSelectionBox.getSelectedItem();
            }
        };
    }
}
