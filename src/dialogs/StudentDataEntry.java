/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dialogs;

import core.Main;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;
import mainframe.MainFrame;

/**
 *
 * @author Mathias
 */
public class StudentDataEntry extends JDialog {

    private TableModel tableModel;
 
    private JPanel top, center, bottom;
    private JLabel footnote;
    private JTextField firstname, name, lectiontype;
    private JTable selectionTable;
    private JButton cancel, save, delete;

    public StudentDataEntry() {

        super(Main.getMainFrame());
        this.tableModel = Main.getMainFrame().getTableModel();

        setModal(true);
        setLocationRelativeTo(Main.getMainFrame());
        setTitle("Schülerprofil");
        setPreferredSize(new Dimension(500, 300));
        createWidgets();
        addWidgets();
        addListener();
        pack();

    }

    private void createWidgets() {

        top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
        top.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        center = new JPanel();
        bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        footnote = new JLabel("* Beginn der Lektion");
        footnote.setFont(this.getFont().deriveFont(Font.PLAIN, 9));

        firstname = new JTextField(" Vorname");
        name = new JTextField(" Name");
        lectiontype = new JTextField(" Lektionsdauer");

        selectionTable = new JTable(tableModel);
        selectionTable.setShowGrid(true);
        selectionTable.getColumnModel().setColumnSelectionAllowed(true); //  in alle Zellen kann geschrieben werden
        selectionTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        cancel = new JButton("Abbrechen");
        save = new JButton("Speichern");
        delete = new JButton("Profil löschen");

    }

    private void addWidgets() {

        top.add(firstname);
        top.add(name);
        top.add(lectiontype);
        center.add(BorderLayout.PAGE_START, selectionTable.getTableHeader());
        center.add(BorderLayout.CENTER, selectionTable);
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

        cancel.addActionListener(new CancelListener());
        delete.addActionListener(null);
        save.addActionListener(null);

    }

    private class CancelListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            StudentDataEntry.this.dispose();
        }

    }

}
