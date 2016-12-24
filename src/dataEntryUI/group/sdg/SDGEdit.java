/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group.sdg;

import core.Profile;
import core.ProfileTypes;
import dataEntryUI.group.SelectableMemberGroupInputMask;
import mainframe.MainFrame;

/**
 *
 * Dialogfenster "Selbstdefinierte Gruppe" ändern oder löschen
 */
public class SDGEdit extends SelectableMemberGroupInputMask {

    public SDGEdit(MainFrame mainFrame, Profile sdg) {
        super(mainFrame, sdg, "Selbstdefiniertes Gruppenprofil ändern oder auflösen");
        setupUI();
        pack();
    }

    @Override
    public void setupUI() {
        updateEditData();
        createEditWidgets();
        addEditWidgets();
        addEditButtonListeners(ProfileTypes.SDG_NAME);
    }

    @Override
    public void getSelectableStudents() {
    }

}
