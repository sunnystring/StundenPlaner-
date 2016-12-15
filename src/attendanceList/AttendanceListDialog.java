/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceList;

import core.Database;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import mainframe.MainFrame;
import utils.Colors;
import static utils.GUIConstants.*;

/**
 *
 * @author mathiaskielholz
 */
public class AttendanceListDialog extends JDialog {

    private MainFrame mainFrame;
    private Database database;
    private AttendanceListData attendanceListData;
    private AttendanceListUI attendanceListUI;
    private JPanel top, center, bottom;
    private JTextField weekNameEntry;
    private JButton cancelButton, createButton, renameButton, deleteButton;
    private ButtonGroup weekSelection;
    private int weekIndex;

    public AttendanceListDialog(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        database = mainFrame.getDatabase();
        attendanceListData = mainFrame.getAttendanceListData();
        attendanceListUI = mainFrame.getAttendanceListUI();
        setTitle("Unterrichtswoche");
        setModal(true);
        setResizable(false);
        setMinimumSize(new Dimension(300, 150));
        createWidgets();
        addWidgets();
        addButtonListeners();
        pack();
    }

    private void createWidgets() {
        top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
        bottom.setBorder(DEFAULT_BORDER);
        weekNameEntry = new JTextField();
        createWeekSelection();
        cancelButton = new JButton("Abbrechen");
        deleteButton = new JButton("Löschen");
        deleteButton.setEnabled(false);
        renameButton = new JButton("Umbenennen");
        renameButton.setEnabled(false);
        createButton = new JButton("Erstellen");
        createButton.setEnabled(true);

    }

    private void createWeekSelection() {
        weekSelection = new ButtonGroup();
        JRadioButton rButton;
        for (int i = database.getNumberOfWeeks() - 1; i >= 0; i--) {
            rButton = new JRadioButton(database.getWeekNameAt(i));
            rButton.setForeground(Colors.VERY_DARK_GREEN);
            rButton.setFont(this.getFont().deriveFont(Font.BOLD, 10));
            rButton.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String weekName = ((JRadioButton) e.getSource()).getText();
                        weekIndex = attendanceListData.getWeekIndex(weekName);
                        weekNameEntry.setText(weekName);
                        weekNameEntry.requestFocusInWindow();
                        setButtonEnabledState();
                    }
                }
            });
            weekSelection.add(rButton);
            center.add(rButton);
        }
    }

    private void setButtonEnabledState() {
        deleteButton.setEnabled(weekSelection.getSelection() != null);
        renameButton.setEnabled(weekSelection.getSelection() != null);
        createButton.setEnabled(false);
    }

    private void addWidgets() {
        top.add(Box.createHorizontalStrut(130));
        top.add(weekNameEntry);
        top.add(Box.createHorizontalStrut(130));
        bottom.add(cancelButton);
        bottom.add(deleteButton);
        bottom.add(renameButton);
        bottom.add(createButton);
        setLayout(new BorderLayout());
        add(BorderLayout.PAGE_START, top);
        add(BorderLayout.CENTER, center);
        add(BorderLayout.PAGE_END, bottom);
    }

    private void addButtonListeners() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.removeWeek(weekIndex);
                updateAttendanceListUI();
                dispose();
            }
        });
        renameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.renameWeek(weekIndex, weekNameEntry.getText());
                updateAttendanceListUI();
                dispose();
            }
        });
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.addWeek(weekNameEntry.getText());
                updateAttendanceListUI();
                dispose();
            }
        });
    }

    private void updateAttendanceListUI() {
        attendanceListData.update();
        attendanceListUI.update();
        attendanceListData.fireTableDataChanged();
    }

    public void setLocation() {
        super.setLocation(getXCoordinate(), 200);
    }

    private int getXCoordinate() {
        return (int) (mainFrame.getSize().getWidth() / 2) - (int) (this.getSize().getWidth() / 2);
    }
}
