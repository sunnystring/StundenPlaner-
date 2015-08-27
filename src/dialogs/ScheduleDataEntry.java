/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dialogs;

import javax.swing.JDialog;
import mainframe.MainFrame;

/**
 *
 * @author Mathias
 */
public class ScheduleDataEntry extends JDialog {

    public ScheduleDataEntry(MainFrame owner) {

        super(owner);
        setModal(true);
        setTitle("Stundenplan erstellen");
        pack();

    }

}
