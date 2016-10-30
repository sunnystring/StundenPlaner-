/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group.kgu;

import core.Profile;
import core.ProfileTypes;
import mainframe.MainFrame;

/**
 *
 * @author mathiaskielholz
 */
public class KGUEdit extends KGUInputMask {

    public KGUEdit(MainFrame mainFrame, Profile group) {
        super(mainFrame, group);
        setTitle(ProfileTypes.KGU_NAME + "n-Profil ändern oder auflösen");
        setupUI();
        pack();
    }

    @Override
    public void setupUI() {
        createEditWidgets();
        addEditButtonListeners();
        addEditWidgets();
    }

}
