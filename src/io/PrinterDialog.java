/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import core.Database;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import mainframe.MainFrame;

/**
 *
 * Druckansicht anzeigen, bearbeiten und ausdrucken
 */
public class PrinterDialog extends JDialog {

    private PrinterText textPane;
    JButton cancelButton, printButton;

    public PrinterDialog(MainFrame mainFrame, Database database) {
        super(mainFrame);
        textPane = new PrinterText(database);
        setTitle("Druckansicht");
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(350, 500));
        setModal(true);
        setLocationRelativeTo(mainFrame);
        createAndAddWidgets();
        addListeners();
        pack();
    }

    private void createAndAddWidgets() {
        JComponent center = new JScrollPane(textPane);
        center.setMinimumSize(textPane.getPreferredSize());
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        cancelButton = new JButton("Abbrechen");
        printButton = new JButton("Drucken");
        bottom.add(Box.createHorizontalGlue());
        bottom.add(cancelButton);
        bottom.add(printButton);
        add(BorderLayout.CENTER, center);
        add(BorderLayout.PAGE_END, bottom);
    }

    private void addListeners() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    textPane.print();
                    dispose();
                } catch (PrinterException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
