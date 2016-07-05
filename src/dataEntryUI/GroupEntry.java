/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import java.awt.Dimension;
import javax.swing.JDialog;
import mainframe.MainFrame;

/**
 *
 * @author mathiaskielholz
 */
public class GroupEntry extends JDialog {

    public GroupEntry(MainFrame mainFrame) {
        setTitle("Gruppenprofile erstellen");
        setModal(true);
        setLocationRelativeTo(mainFrame);
        setSize(new Dimension(300, 100));
        setResizable(false);
    }

}
