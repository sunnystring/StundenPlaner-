/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dialogs;

import core.Student;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import mainframe.MainFrame;

/**
 *
 * @author Mathias
 */
public class StudentDataEntry extends JDialog {

    private JPanel top, bottom;
    private JLabel footnote;
    private JTextField firstname, name, lectiontype;
    private JTable selectionTable;
    private JButton cancel, save;

    public StudentDataEntry(MainFrame owner) {

        super(owner);
        setModal(true);
        setTitle("Schülerdaten eingeben");
        setLayout(new BorderLayout());
        createWidgets();
        addWidgets();
        pack();

    }

    private void createWidgets() {

        top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
        top.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        footnote = new JLabel("* Beginn der Lektion");
        footnote.setFont(this.getFont().deriveFont(Font.PLAIN, 9));

        firstname = new JTextField(" Vorname");
        name = new JTextField(" Name");
        lectiontype = new JTextField(" Lektionsdauer");

        selectionTable = new JTable(6, 5);

        cancel = new JButton("Abbrechen");
        save = new JButton("Speichern");

    }

    private void addWidgets() {

        top.add(firstname);
        top.add(name);
        top.add(lectiontype);

        bottom.add(footnote);
        bottom.add(Box.createHorizontalGlue());
        bottom.add(cancel);
        bottom.add(save);

        add(BorderLayout.PAGE_START, top);
        add(BorderLayout.CENTER, selectionTable);
        add(BorderLayout.PAGE_END, bottom);

    }

}
