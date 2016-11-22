/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentJournal;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import static utils.GUIConstants.*;

/**
 *
 * @author mathiaskielholz
 */
public class JournalDayView extends JDialog {

    protected JScrollPane centerField;
    protected JPanel bottomField;
    protected JButton closeButton;
    private JTextArea dayViewText;

    public JournalDayView() {
        setMinimumSize(new Dimension(250, 600));
        createAndAddWidgets();
        setResizable(false);
        pack();
    }

    public void setDialogLocation() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        location.setLocation(location.getX(), location.getY() + 20.0);
        setLocation(location);
    }

    public void createAndAddWidgets() {
        bottomField = new JPanel();
        bottomField.setLayout(new BoxLayout(bottomField, BoxLayout.LINE_AXIS));
        bottomField.setBorder(LIGHT_BORDER);
        dayViewText = new JTextArea();
        dayViewText.setEditable(false);
        centerField = new JScrollPane(dayViewText);
        centerField.setMinimumSize(dayViewText.getPreferredSize());
        centerField.setBorder(VERY_LIGHT_BORDER);
        closeButton = new JButton("Schliessen");
        bottomField.add(Box.createHorizontalGlue());
        bottomField.add(closeButton);
        add(BorderLayout.CENTER, centerField);
        add(BorderLayout.PAGE_END, bottomField);
    }

}
