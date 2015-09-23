/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dialogs;

import core.Main;
import core.Student;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableModel;
import mainframe.MainFrame;

/**
 *
 * @author Mathias
 */
public class StudentDataEntry extends JDialog {

    private Student student;

    private JPanel top, bottom;
    private JScrollPane center;
    private JLabel firstnameLabel, nameLabel, lectiontypeLabel, footnote;
    private JTextField firstname, name, lectiontype;
    private JTable selectionTable;
    private JButton cancel, save, delete;

    private static JFrame owner; // = MainFrame

    public StudentDataEntry() {

        super(owner);
        student = new Student();

        setLocationRelativeTo(null);
        setModal(true);
        setTitle("Schülerprofil");
        setPreferredSize(new Dimension(500, 273));
        createWidgets();
        addWidgets();
        addListener();
        pack();

    }

    private void createWidgets() {

        selectionTable = new JTable(student.getStudentTimeModel()); // TableModel (StudentTimes) übergeben
        selectionTable.setShowGrid(true);
        selectionTable.getColumnModel().setColumnSelectionAllowed(true); //  in alle Zellen kann geschrieben werden
        selectionTable.setFillsViewportHeight(true);
        selectionTable.setRowHeight(25); // ToDo: dynamisch

        top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
        top.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        center = new JScrollPane(selectionTable, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        firstnameLabel = new JLabel("Vorname:");
        nameLabel = new JLabel("  Name:");
        lectiontypeLabel = new JLabel("  Lektionsdauer:");
        footnote = new JLabel("* Beginn der Lektion");
        footnote.setFont(this.getFont().deriveFont(Font.PLAIN, 9));

        firstname = new JTextField(" ");
        name = new JTextField(" ");
        lectiontype = new JTextField("30");
        lectiontype.setMaximumSize(lectiontype.getPreferredSize());

        cancel = new JButton("Abbrechen");
        save = new JButton("Speichern");
        delete = new JButton("Profil löschen");

    }

    private void addWidgets() {

        top.add(firstnameLabel);
        top.add(firstname);
        top.add(Box.createHorizontalGlue());
        top.add(nameLabel);
        top.add(name);
        top.add(Box.createHorizontalGlue());
        top.add(lectiontypeLabel);
        top.add(lectiontype);

        bottom.add(footnote);
        bottom.add(Box.createHorizontalGlue());
        bottom.add(cancel);
        bottom.add(delete);
        bottom.add(save);

        add(BorderLayout.PAGE_START, top);
        add(BorderLayout.CENTER, center);
        add(BorderLayout.PAGE_END, bottom);

    }

    private void addListener() {

        cancel.addActionListener(new CancelButtonListener());
        delete.addActionListener(null);
        save.addActionListener(new SaveButtonListener());

    }

    public static void setOwner(JFrame mainframe) {
        owner = mainframe;
    }

    private class CancelButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            StudentDataEntry.this.dispose();
        }
    }

    private class SaveButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(student.getStudentTime(0, 0));

        }

    }

}
