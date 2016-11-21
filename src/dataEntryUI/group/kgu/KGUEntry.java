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
public class KGUEntry extends KGUInputMask {

    public KGUEntry(MainFrame mainFrame, Profile group, String title) {
        super(mainFrame, group, ProfileTypes.KGU_NAME + title);
        setupUI();
        pack();
    }

    @Override
    public void setupUI() {
        createEntryWidgets();
        // createGroupSelection();
        createStudentSelection();
        addEntryButtonListeners();
        addEntryWidgets();
    }

}
