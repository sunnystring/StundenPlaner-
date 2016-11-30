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

    private AttendanceListData attendanceListData;
    private JTable attendanceTable;
    private JScrollPane centerField;
    private JPanel buttonField;
    private JButton deleteAllButton, closeButton, editWeekButton, createWeekButton;

    public AttendanceListUI(MainFrame mainFrame) {
        super(mainFrame);
        attendanceListData = new AttendanceListData(mainFrame.getDatabase());
        setTitle("Unterrichtskontrolle");
        setModal(true);
      //  setMinimumSize(ATTENDANCELIST_DIMENSION);
        setLocation(120, 120);
        setResizable(true);
        setLayout(new BorderLayout());
        createWidgets();
        addWidgets();
        addListeners();
        pack();
    }

    private void createWidgets() {
        attendanceTable = new JTable(attendanceListData);
        attendanceTable.setPreferredScrollableViewportSize(attendanceTable.getPreferredSize());
        attendanceTable.setFillsViewportHeight(true);
        centerField = new JScrollPane(attendanceTable);
        buttonField = new JPanel();
        buttonField.setLayout(new BoxLayout(buttonField, BoxLayout.LINE_AXIS));
        buttonField.setBorder(LIGHT_BORDER);
        deleteAllButton = new JButton("Unterrichtskontrolle löschen");
        closeButton = new JButton("Schliessen");
        editWeekButton = new JButton("Woche bearbeiten");
        createWeekButton = new JButton("Neue Woche erstellen");
    }

    private void addWidgets() {
        add(BorderLayout.CENTER, centerField);
        buttonField.add(deleteAllButton);
        buttonField.add(Box.createHorizontalGlue());
        buttonField.add(closeButton);
        buttonField.add(editWeekButton);
        buttonField.add(createWeekButton);
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
        editWeekButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        createWeekButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

    }
}
