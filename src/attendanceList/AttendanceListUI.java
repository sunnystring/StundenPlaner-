/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import mainframe.MainFrame;
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
    private JPanel buttonField;
    private JButton deleteAllButton, closeButton, editButton, saveButton;

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
        attendanceTable = new AttendanceTable(attendanceListData);
        centerField = new JScrollPane(attendanceTable);
        centerField.setMinimumSize(attendanceTable.getPreferredScrollableViewportSize());
        buttonField = new JPanel();
        buttonField.setLayout(new BoxLayout(buttonField, BoxLayout.LINE_AXIS));
        buttonField.setBorder(LIGHT_BORDER);
        closeButton = new JButton("Schliessen");
        deleteAllButton = new JButton("Alles löschen");
        editButton = new JButton("Woche erstellen");
        saveButton = new JButton("Einträge speichern");
    }

    private void addWidgets() {
        add(BorderLayout.CENTER, centerField);
        buttonField.add(closeButton);
        buttonField.add(Box.createHorizontalGlue());
        buttonField.add(deleteAllButton);
        buttonField.add(editButton);
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
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AttendanceListDialog attendanceListEdit = new AttendanceListDialog(mainFrame);
                attendanceListEdit.setLocation();
                attendanceListEdit.setVisible(true);
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
        super.setLocation(getXCoordinate(), 80);
    }

    private int getXCoordinate() {
        return (int) (mainFrame.getSize().getWidth() / 2) - (int) (this.getSize().getWidth() / 2);
    }

    public void updateTable() {
        attendanceTable.update();
    }
}
