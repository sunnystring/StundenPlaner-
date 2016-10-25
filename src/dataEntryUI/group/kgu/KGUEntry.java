/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group.kgu;

import core.Profile;
import dataEntryUI.KGUInputMask;
import core.ProfileTypes;
import mainframe.MainFrame;

/**
 *
 * @author mathiaskielholz
 */
public class KGUEntry extends KGUInputMask {

    public KGUEntry(MainFrame mainFrame, Profile group) {
        super(mainFrame, group);
        setTitle(ProfileTypes.KGU_NAME + "n-Profil erstellen");
    }

}
