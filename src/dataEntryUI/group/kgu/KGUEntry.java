/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group.kgu;

import dataEntryUI.group.SelectableMemberGroupInputMask;
import core.Profile;
import core.ProfileTypes;
import java.util.ArrayList;
import mainframe.MainFrame;

/**
 *
 * Dialogfenster Eingabe neues KGU-Profil
 */
public class KGUEntry extends SelectableMemberGroupInputMask {

    public KGUEntry(MainFrame mainFrame, Profile group, String title) {
        super(mainFrame, group, ProfileTypes.KGU_NAME + title);
        getSelectableStudents();
        setupUI();
        pack();
    }

    @Override
    public void setupUI() {
        createEntryWidgets();
        createStudentSelection();
        addEntryWidgets();
        addEntryButtonListeners(ProfileTypes.KGU_NAME);
    }

    @Override
    public void getSelectableStudents() {
        ArrayList<Profile> studentDataList = database.getStudentDataList();
        for (Profile profile : studentDataList) {
            if (!profile.isAllocated()) {
                if (profile.getProfileType() == ProfileTypes.KGU_MEMBER) {
                    selectableStudents.add(profile);
                }
            }
        }
    }
}
