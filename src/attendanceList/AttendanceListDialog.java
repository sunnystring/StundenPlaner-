/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceList;

import core.Database;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import mainframe.MainFrame;
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
    // private ArrayList<JRadioButton> weekSelection;
    //  private ButtonGroup allSelections;
    private JButton cancelButton, createButton, renameButton, deleteButton;

    public AttendanceListDialog(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        database = mainFrame.getDatabase();
        attendanceListData = mainFrame.getAttendanceListData();
        attendanceListUI = mainFrame.getAttendanceListUI();
        setTitle("Unterrichtswoche");
        //  weekSelection = new ArrayList<>();
        getWeeks();
        setModal(true);
        setResizable(false);
        setMinimumSize(new Dimension(250, 250));
        // setMinimumSize(DEFAULT_ENTRY_DIMENSION);
        createWidgets();
        addWidgets();
        addButtonListeners();
        pack();
    }

    private void getWeeks() {
    }

    private void createWidgets() {
        top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
        top.setBorder(DEFAULT_BORDER);
        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
        bottom.setBorder(DEFAULT_BORDER);
        weekNameEntry = new JTextField();
//        // ToDo footmark setzen statt tooltip....
//        weekNameEntry.setToolTipText("Bezeichnung der Woche eingeben (zB. 17.12.16, Woche 3 usw.)");
        //     createWeekSelection();
        cancelButton = new JButton("abbrechen");
        deleteButton = new JButton("Löschen");
        deleteButton.setEnabled(false);
        renameButton = new JButton("Umbenennen");
        renameButton.setEnabled(false);
        createButton = new JButton("Erstellen");
    }

//    private void createWeekSelection() {
//        allSelections = new ButtonGroup();
//        for (int i = 0; i < 11; i++) {
//            JRadioButton selectionButton = new JRadioButton("12.12.16"); //attendanceListData.getColumnName(i)
//            selectionButton.addItemListener(new ItemListener() {
//                @Override
//                public void itemStateChanged(ItemEvent e) {
//                    if (e.getStateChange() == ItemEvent.SELECTED) {
//                        System.out.println(((JRadioButton) e.getSource()).getText());
//                    }
//                }
//            });
//            allSelections.add(selectionButton);
//            center.add(selectionButton);
//        }
//    }
    private void addWidgets() {
        top.add(Box.createHorizontalStrut(70));
        top.add(weekNameEntry);
        top.add(Box.createHorizontalStrut(70));
        bottom.add(Box.createHorizontalGlue());
        bottom.add(cancelButton);

        // bottom.add(deleteButton);
        //  bottom.add(renameButton);
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
            }
        });
        renameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.addWeek(weekNameEntry.getText());
                attendanceListData.update();
                attendanceListUI.updateTable();
                attendanceListData.fireTableDataChanged();
                dispose();
            }
        });
    }

    public void setLocation() {
        super.setLocation(getXCoordinate(), 200);
    }

    private int getXCoordinate() {
        return (int) (mainFrame.getSize().getWidth() / 2) - (int) (this.getSize().getWidth() / 2);
    }
}
