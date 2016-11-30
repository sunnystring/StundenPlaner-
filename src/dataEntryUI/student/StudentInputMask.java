/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.student;

import core.Database;
import dataEntryUI.ProfileInputMask;
import core.ProfileTypes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentInputMask extends ProfileInputMask {

    public StudentInputMask(Database database) {
        super(database);
        lectionLength = "30";
        createLectionTypeSelectionListener();
    }

    public void restoreLectiontypeSelectionBox() {
        switch (lectionLength) {
            case "30":
                lectiontypeSelectionBox.setSelectedIndex(0);
                break;
            case "40":
                lectiontypeSelectionBox.setSelectedIndex(1);
                break;
            case "50":
                lectiontypeSelectionBox.setSelectedIndex(2);
                break;
            case "45": // = KGU
                lectiontypeSelectionBox.setSelectedIndex(3);
                break;
            case "0": // = SDG
                lectiontypeSelectionBox.setSelectedIndex(4);
                break;
            default:
                break;
        }
    }

    @Override
    public void createLectionTypeSelectionListener() {
        lectiontypeSelectionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemType = (String) lectiontypeSelectionBox.getSelectedItem();
                if (itemType.equals("KGU")) {
                    profile.setProfileType(ProfileTypes.KGU_MEMBER);
                    profile.setProfileName(ProfileTypes.KGU_NAME);
                    lectionLength = "45";
                    timeSelectionTable.adjustTimeSlotsToKGUEntry(true);
                } else if (itemType.equals("SDG")) {
                    profile.setProfileType(ProfileTypes.SDG_MEMBER);
                    profile.setProfileName(ProfileTypes.SDG_NAME);
                    lectionLength = "0";
                    timeSelectionTable.adjustTimeSlotsToKGUEntry(true);
                } else {
                    profile.setProfileType(ProfileTypes.SINGLE_LECTION);
                    profile.setProfileName(ProfileTypes.SINGLE_LECTION_NAME);
                    lectionLength = itemType;
                    timeSelectionTable.adjustTimeSlotsToKGUEntry(false);
                }
            }
        };
    }
}
