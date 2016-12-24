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

    public void workshopProfile() {
        removeExistingLectionTypeEntry();
        lectionLength = "50";
    }

    public void instrumentalformationProfile() {
        removeExistingLectionTypeEntry();
        lectionLength = "45";
        lectionTypes = new String[]{"45", "60", "90", "105", "120", "180"};
        lectiontypeSelectionBox = new JComboBox(lectionTypes);
        addNewLectionTypeEntry();
    }

    public void chorProfile() {
        removeExistingLectionTypeEntry();
        lectionLength = "30";
        lectionTypes = new String[]{"30", "45", "60", "90", "120"};
        lectiontypeSelectionBox = new JComboBox(lectionTypes);
        addNewLectionTypeEntry();
    }

    public void grundschulungProfile() {
        lectionLength = "45";
        removeExistingLectionTypeEntry();
    }

    public void otherProfile() {
        removeExistingLectionTypeEntry();
        lectionLength = "45";
        lectionTypes = new String[]{"45", "60", "75", "90"};
        lectiontypeSelectionBox = new JComboBox(lectionTypes);
        addNewLectionTypeEntry();
    }

    public void restoreWorkshopProfile() {
        workshopProfile();
    }

    public void restoreInstrumentalformationProfile(Profile group) {
        instrumentalformationProfile();
        lectionLength = String.valueOf(group.getLectionLengthInMinutes());
        switch (lectionLength) {
            case "45":
                lectiontypeSelectionBox.setSelectedIndex(0);
                break;
            case "60":
                lectiontypeSelectionBox.setSelectedIndex(1);
                break;
            case "90":
                lectiontypeSelectionBox.setSelectedIndex(2);
                break;
            case "105":
                lectiontypeSelectionBox.setSelectedIndex(3);
                break;
            case "120":
                lectiontypeSelectionBox.setSelectedIndex(4);
                break;
            case "180":
                lectiontypeSelectionBox.setSelectedIndex(5);
                break;
            default:
                lectiontypeSelectionBox.setSelectedIndex(0);
                break;
        }
        addNewLectionTypeEntry();
    }

    public void restoreChorProfile(Profile group) {
        chorProfile();
        lectionLength = String.valueOf(group.getLectionLengthInMinutes());
        switch (lectionLength) {
            case "30":
                lectiontypeSelectionBox.setSelectedIndex(0);
                break;
            case "45":
                lectiontypeSelectionBox.setSelectedIndex(1);
                break;
            case "60":
                lectiontypeSelectionBox.setSelectedIndex(2);
                break;
            case "90":
                lectiontypeSelectionBox.setSelectedIndex(3);
                break;
            case "120":
                lectiontypeSelectionBox.setSelectedIndex(4);
                break;
            default:
                lectiontypeSelectionBox.setSelectedIndex(0);
                break;
        }
        addNewLectionTypeEntry();
    }

    public void restoreGrundschulungProfile() {
        grundschulungProfile();
    }

    public void restoreOtherProfile(Profile group) {
        otherProfile();
        lectionLength = String.valueOf(group.getLectionLengthInMinutes());
        switch (lectionLength) {
            case "45":
                lectiontypeSelectionBox.setSelectedIndex(0);
                break;
            case "60":
                lectiontypeSelectionBox.setSelectedIndex(1);
                break;
            case "75":
                lectiontypeSelectionBox.setSelectedIndex(2);
                break;
            case "90":
                lectiontypeSelectionBox.setSelectedIndex(3);
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
