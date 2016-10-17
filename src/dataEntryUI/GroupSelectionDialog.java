/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import core.Group;
import dataEntryUI.groupProfiles.BandEntry;
import dataEntryUI.groupProfiles.EnsembleEntry;
import dataEntryUI.groupProfiles.OrchesterEntry;
import dataEntryUI.groupProfiles.OtherEntry;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import mainframe.MainFrame;

/**
 *
 * @author mathiaskielholz
 */
public class GroupSelectionDialog extends JDialog {

    private MainFrame mainFrame;
    private Group group;
    private GroupEntry selectedEntry;
    private JPanel selectionField, buttonField;
    private ButtonGroup allSelections;
    private JRadioButton KGUSelection, bandSelection, ensembleSelection, orchesterSelection, otherSelection;
    private JButton cancelButton, approveButton;

    public GroupSelectionDialog(MainFrame mainFrame, Group group) {
        this.mainFrame = mainFrame;
        this.group = group;
        selectedEntry = null;
        setTitle("Gruppenprofil auswählen");
        setModal(true);
        setLocation((int) (mainFrame.getSize().getWidth() / 2), 150);
        setResizable(false);
        setMinimumSize(new Dimension(300, 100));
        setLayout(new BorderLayout());
        createWidgets();
        addWidgets();
        addSelectionItemListeners();
        addCancelButtonListener();
        addApproveButtonListener();
        pack();
    }

    private void createWidgets() {
        selectionField = new JPanel();
        selectionField.setLayout(new BoxLayout(selectionField, BoxLayout.PAGE_AXIS));
        selectionField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        KGUSelection = new JRadioButton("KGU");
        bandSelection = new JRadioButton("Band");
        ensembleSelection = new JRadioButton("Ensemble");
        orchesterSelection = new JRadioButton("Orchester");
        otherSelection = new JRadioButton("Eigenes Gruppenprofil");
        createButtonGroup();
        buttonField = new JPanel();
        buttonField.setLayout(new BoxLayout(buttonField, BoxLayout.LINE_AXIS));
        buttonField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cancelButton = new JButton("Abbrechen");
        approveButton = new JButton("Übernehmen");
    }

    private void createButtonGroup() {
        allSelections = new ButtonGroup();
        allSelections.add(KGUSelection);
        allSelections.add(bandSelection);
        allSelections.add(ensembleSelection);
        allSelections.add(orchesterSelection);
        allSelections.add(otherSelection);
    }

    private void addWidgets() {
        selectionField.add(KGUSelection);
        selectionField.add(bandSelection);
        selectionField.add(ensembleSelection);
        selectionField.add(orchesterSelection);
        selectionField.add(otherSelection);
        buttonField.add(Box.createHorizontalGlue());
        buttonField.add(cancelButton);
        buttonField.add(approveButton);
        add(BorderLayout.CENTER, selectionField);
        add(BorderLayout.PAGE_END, buttonField);
    }

    private void addSelectionItemListeners() {
        KGUSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                selectedEntry = null;
                //..........
            }
        });
        bandSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    selectedEntry = new BandEntry(mainFrame, "Bandprofil erstellen", group);
                }
            }
        });
        ensembleSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                selectedEntry = new EnsembleEntry(mainFrame, "Ensembleprofil erstellen", group);
            }
        });
        orchesterSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                selectedEntry = new OrchesterEntry(mainFrame, "Orchesterprofil erstellen", group);
            }
        });
        otherSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                selectedEntry = new OtherEntry(mainFrame, "Eigenes Gruppenprofil erstellen", group);
            }
        });
    }

    private void addCancelButtonListener() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void addApproveButtonListener() {
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                if (selectedEntry != null) {
                    selectedEntry.setVisible(true);
                }
            }
        });
    }
}
