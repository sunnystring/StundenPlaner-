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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import mainframe.MainFrame;
import static utils.GUIConstants.*;

/**
 *
 * Druckansicht anzeigen, bearbeiten und ausdrucken
 */
public class PrinterDialog extends JDialog {

    private MainFrame mainFrame;
    private PrinterTextPane textPane;
    private JButton cancelButton, printButton;

    public PrinterDialog(MainFrame mainFrame, Database database) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        textPane = new PrinterTextPane(database);
        setTitle("Druckansicht");
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(350, 500));
        setModal(true);
        createAndAddWidgets();
        addListeners();
        pack();
        setDialogLocation();
    }

    private void createAndAddWidgets() {
        JComponent center = new JScrollPane(textPane);
        center.setBorder(VERY_LIGHT_BORDER);
        center.setMinimumSize(textPane.getPreferredSize());
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
        bottom.setBorder(LIGHT_BORDER);
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

    private void setDialogLocation() {
        int x = (int) (mainFrame.getSize().getWidth() - getSize().width) / 2;
        int y = 120;
        setLocation(x, y);
    }
}
