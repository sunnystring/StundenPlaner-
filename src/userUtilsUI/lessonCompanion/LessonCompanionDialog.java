/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userUtilsUI.lessonCompanion;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import javax.swing.JDialog;

/**
 *
 * @author mathiaskielholz
 */
public abstract class LessonCompanionDialog extends JDialog {

    public LessonCompanionDialog() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        setLayout(new BorderLayout());
        setLocation(location);
        setMinimumSize(new Dimension(250, 150));
    }

    private void createAndAddWidgets() {

    }
}
