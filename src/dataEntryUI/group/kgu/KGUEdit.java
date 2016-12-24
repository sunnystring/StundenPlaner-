/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group.kgu;

import dataEntryUI.group.SelectableMemberGroupInputMask;
import core.Profile;
import core.ProfileTypes;
import mainframe.MainFrame;

/**
 *
 * Dialogfenster KGU-Profil ändern oder löschen
 */
public class KGUEdit extends SelectableMemberGroupInputMask {

    public KGUEdit(MainFrame mainFrame, Profile kgu) {
        super(mainFrame, kgu, ProfileTypes.KGU_NAME + "n-Profil ändern oder auflösen");
        setupUI();
        pack();
    }

    @Override
    public void setupUI() {
        updateEditData();
        createEditWidgets();
        addEditWidgets();
        addEditButtonListeners(ProfileTypes.KGU_NAME);

    }

    @Override
    public void getSelectableStudents() {
    }

}
