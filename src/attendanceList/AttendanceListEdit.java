/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceList;

import javax.swing.JDialog;
import mainframe.MainFrame;
import static utils.GUIConstants.DEFAULT_ENTRY_DIMENSION;

/**
 *
 * @author mathiaskielholz
 */
public class AttendanceListEdit extends JDialog {

    private MainFrame mainFrame;

    public AttendanceListEdit(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        setTitle("Unterrichtswoche erstellen oder bearbeiten");
        setModal(true);
        setLocation(getLocationXCoordinate(), 200);
        setResizable(false);
        setMinimumSize(DEFAULT_ENTRY_DIMENSION);
    }

    private int getLocationXCoordinate() {
        return (int) (mainFrame.getSize().getWidth() / 2) - (int) (this.getSize().getWidth() / 2);
    }
}
