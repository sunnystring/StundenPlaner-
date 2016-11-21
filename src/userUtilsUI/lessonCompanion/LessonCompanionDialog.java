/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userUtilsUI.lessonCompanion;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author mathiaskielholz
 */
public abstract class LessonCompanionDialog extends JDialog {

    protected JScrollPane centerField;
    protected JPanel topField, bottomField;
    protected JButton attendanceListButton, cancelButton, saveButton;

    public LessonCompanionDialog() {
        setLayout(new BorderLayout());

    }

    public abstract void createAndAddWidgets();
}
