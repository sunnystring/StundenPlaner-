/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceList;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import mainframe.MainFrame;
import static utils.GUIConstants.*;

/**
 *
 * @author mathiaskielholz
 */
public class AttendanceListUI extends JDialog {

    private MainFrame mainFrame;
    private AttendanceListData attendanceListData;
    private JTable attendanceTable;
    private JScrollPane centerField;
    private JPanel buttonField;
    private JButton deleteAllButton, closeButton, editButton, createWeekButton, saveButton;

    public AttendanceListUI(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        attendanceListData = mainFrame.getAttendanceListData();
        setTitle("Unterrichtskontrolle");
        setModal(true);
        setMinimumSize(ATTENDANCELIST_DIMENSION);
        setResizable(true);
        setLayout(new BorderLayout());
        createWidgets();
        addWidgets();
        addListeners();
        pack();
    }

    private void createWidgets() {
        createAttendanceTable();
        centerField = new JScrollPane(attendanceTable);
        buttonField = new JPanel();
        buttonField.setLayout(new BoxLayout(buttonField, BoxLayout.LINE_AXIS));
        buttonField.setBorder(LIGHT_BORDER);
        deleteAllButton = new JButton("Unterrichtskontrolle löschen");
        closeButton = new JButton("Schliessen");
        editButton = new JButton("Bearbeiten");
        //  createWeekButton = new JButton("Neue Woche erstellen");
        saveButton = new JButton("Einträge speichern");
    }

    private void createAttendanceTable() {
        AttendanceField attendanceField = new AttendanceField();
        attendanceTable = new JTable(attendanceListData);
        attendanceTable.setPreferredScrollableViewportSize(attendanceTable.getPreferredSize());
        attendanceTable.setFillsViewportHeight(true);
        attendanceTable.setShowGrid(true);
        attendanceTable.addMouseListener(attendanceField);
        attendanceTable.setDefaultRenderer(AttendanceField.class, attendanceField);
    }

    private void addWidgets() {
        add(BorderLayout.CENTER, centerField);
        buttonField.add(deleteAllButton);
        buttonField.add(Box.createHorizontalGlue());
        buttonField.add(closeButton);
        buttonField.add(editButton);
        //   buttonField.add(createWeekButton);
        buttonField.add(saveButton);
        add(BorderLayout.PAGE_END, buttonField);
    }

    private void addListeners() {
        deleteAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog attendanceListEdit = new AttendanceListEdit(mainFrame);
                attendanceListEdit.setVisible(true);
            }
        });
//        createWeekButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//            }
//        });
        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

    public void setLocation() {
        super.setLocation(getLocationXCoordinate(), 120);
    }

    private int getLocationXCoordinate() {
        return (int) (mainFrame.getSize().getWidth() / 2) - (int) (this.getSize().getWidth() / 2);
    }
}
