/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group.sdg;

import core.Profile;
import core.ProfileTypes;
import dataEntryUI.group.SelectableMemberGroupInputMask;
import java.util.ArrayList;
import mainframe.MainFrame;

/**
 *
 * Dialogfenster Eingabe neue "Selbstdefinierte Gruppe"
 */
public class SDGEntry extends SelectableMemberGroupInputMask {

    public SDGEntry(MainFrame mainFrame, Profile group, String title) {
        super(mainFrame, group, title);
        getSelectableStudents();
        setupUI();
        pack();
    }

    @Override
    public void setupUI() {
        createEntryWidgets();
        createStudentSelection();
        createAndAddLectionTypeSelection();
        addEntryWidgets();
        addEntryButtonListeners(ProfileTypes.SDG_NAME);
    }

    @Override
    public void getSelectableStudents() {
        ArrayList<Profile> studentDataList = database.getStudentDataList();
        for (Profile profile : studentDataList) {
            if (!profile.isAllocated()) {
                if (profile.getProfileType() == ProfileTypes.SDG_MEMBER) {
                    selectableStudents.add(profile);
                }
            }
        }
    }
}
