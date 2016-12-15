/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceListData;

import attendanceListUI.AttendanceTable;
import attendanceListUI.AttendanceListUI;
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
import static utils.Colors.*;
import static utils.GUIConstants.*;

/**
 *
 * @author mathiaskielholz
 */
public class AttendanceListEdit extends JDialog {

    private MainFrame mainFrame;
    private Database database;
    private AttendanceListData attendanceListData;
    private AttendanceListUI attendanceListUI;
    private AttendanceTable attendanceTable;
    private JPanel top, center, bottom;
    private JTextField weekNameEntry;
    private JButton cancelButton, currentWeekButton, deleteButton, renameButton, createButton;
    private ButtonGroup weekSelection;
    private int weekIndex;

    public AttendanceListEdit(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        database = mainFrame.getDatabase();
        attendanceListData = mainFrame.getAttendanceListData();
        attendanceListUI = mainFrame.getAttendanceListUI();
        attendanceTable = attendanceListUI.getAttendanceTable();
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
        cancelButton = new JButton("Abbrechen");
        currentWeekButton = new JButton("Aktuelle Woche");
        currentWeekButton.setEnabled(false);
        deleteButton = new JButton("Löschen");
        deleteButton.setEnabled(false);
        renameButton = new JButton("Umbenennen");
        renameButton.setEnabled(false);
        createButton = new JButton("Erstellen");
        createButton.setEnabled(true);
        createWeekSelection();

    }

    private void createWeekSelection() {
        weekSelection = new ButtonGroup();
        JRadioButton rButton;
        for (int i = database.getNumberOfWeeks() - 1; i >= 0; i--) {
            rButton = new JRadioButton(database.getWeekNameAt(i));
            rButton.setForeground(i == attendanceListData.getCurrentWeekIndex() ? VERY_DARK_GREEN : DARK_GREEN);
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
        boolean weekSelected = weekSelection.getSelection() != null;
        currentWeekButton.setEnabled(weekSelected);
        deleteButton.setEnabled(weekSelected);
        renameButton.setEnabled(weekSelected);
        createButton.setEnabled(false);
    }

    private void addWidgets() {
        if (attendanceListData.isJournalArchiveEnabled()) {
            top.add(Box.createHorizontalStrut(190));
        } else {
            top.add(Box.createHorizontalStrut(130));
        }
        top.add(weekNameEntry);
        if (attendanceListData.isJournalArchiveEnabled()) {
            top.add(Box.createHorizontalStrut(190));
        } else {
            top.add(Box.createHorizontalStrut(130));
        }
        bottom.add(cancelButton);
        if (attendanceListData.isJournalArchiveEnabled()) {
            bottom.add(currentWeekButton);
        }
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
        currentWeekButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attendanceListData.setDefaultCurrentWeekIndex(false);
                attendanceListData.setCurrentWeekIndex(weekIndex);
                attendanceTable.getTableHeader().resizeAndRepaint();
                dispose();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.removeWeek(weekIndex);
                updateCurrentWeekIndex();
                updateAttendanceListUI();
                attendanceTable.getTableHeader().resizeAndRepaint();
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

    private void updateCurrentWeekIndex() {
        if (weekIndex == attendanceListData.getCurrentWeekIndex()) {
            attendanceListData.setCurrentWeekIndex(attendanceListData.getNumberOfWeeks() - 2);
        }
        else if (weekIndex < attendanceListData.getCurrentWeekIndex()) {
            attendanceListData.setCurrentWeekIndex(attendanceListData.getCurrentWeekIndex()-1);
        }
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
