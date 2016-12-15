/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceListUI;

import attendanceListData.AttendanceListData;
import attendanceListData.AttendanceListEdit;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import mainframe.MainFrame;
import utils.Dialogs;
import static utils.GUIConstants.*;

/**
 *
 * @author mathiaskielholz
 */
public class AttendanceListUI extends JDialog {

    private MainFrame mainFrame;
    private AttendanceListData attendanceListData;
    private AttendanceTable attendanceTable;
    private JScrollPane centerField;
    private JPanel topField, buttonField;
    private JButton closeButton, deleteAllButton, createAndEditWeekButton, saveButton;
    private JCheckBox journalCollectionEnabled;

    public AttendanceListUI(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        attendanceListData = mainFrame.getAttendanceListData();
        setTitle("Unterrichtskontrolle");
        setModal(true);
        setResizable(true);
        setPreferredSize(ATTENDANCELIST_DIMENSION);
        setLayout(new BorderLayout());
        createWidgets();
        addWidgets();
        addListeners();
        pack();
    }

    private void createWidgets() {
        topField = new JPanel();
        topField.setLayout(new BoxLayout(topField, BoxLayout.LINE_AXIS));
        attendanceTable = new AttendanceTable(attendanceListData, mainFrame);
        centerField = new JScrollPane(attendanceTable);
        centerField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        buttonField = new JPanel();
        buttonField.setLayout(new BoxLayout(buttonField, BoxLayout.LINE_AXIS));
        buttonField.setBorder(LIGHT_BORDER);
        closeButton = new JButton("Schliessen");
        deleteAllButton = new JButton("Alle Einträge löschen");
        createAndEditWeekButton = new JButton("Woche erstellen oder bearbeiten");
        saveButton = new JButton("Einträge speichern");
        journalCollectionEnabled = new JCheckBox("Journale archivieren");
        journalCollectionEnabled.setSelected(false);
        journalCollectionEnabled.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
    }

    private void addWidgets() {
        add(BorderLayout.CENTER, centerField);
        buttonField.add(closeButton);
        buttonField.add(deleteAllButton);
        buttonField.add(Box.createHorizontalGlue());
        buttonField.add(journalCollectionEnabled);
        buttonField.add(createAndEditWeekButton);
        buttonField.add(saveButton);
        add(BorderLayout.PAGE_END, buttonField);
    }

    private void addListeners() {
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        deleteAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Dialogs.showAffirmDeleteAttendanceListMessage() == JOptionPane.YES_OPTION) {
                    attendanceListData.deleteAll();
                    dispose();
                }
            }
        });
        journalCollectionEnabled.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    attendanceListData.setJournalArchiveEnabled(true);
                    attendanceListData.setCurrentWeekIndex(attendanceListData.getNumberOfWeeks()-1);
                    attendanceListData.update();
                    attendanceTable.getTableHeader().resizeAndRepaint();
                } else {
                    attendanceListData.setJournalArchiveEnabled(false);
                    attendanceListData.setCurrentWeekIndex(-1);
                    attendanceListData.update();
                    attendanceTable.getTableHeader().resizeAndRepaint();
                }
            }
        });
        createAndEditWeekButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AttendanceListEdit editDialog = new AttendanceListEdit(mainFrame);
                editDialog.setLocation();
                editDialog.setVisible(true);
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attendanceListData.saveAbsenceEntries();
                dispose();
            }
        });
    }

    public void setLocation() {
        super.setLocation(getXCoordinate(), mainFrame.getLocation().y);
    }

    private int getXCoordinate() {
        return (int) (mainFrame.getSize().getWidth() / 2) - (int) (this.getSize().getWidth() / 2);
    }

    public void update() {
        attendanceTable.update();
    }

    public AttendanceTable getAttendanceTable() {
        return attendanceTable;
    }
}
