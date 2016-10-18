/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import core.Group;
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
    private JRadioButton KGUSelection, workshopSelection, instrumentalformationSelection, chorSelection, grundschulungSelection, otherSelection;
    private JButton cancelButton, approveButton;
    
    public GroupSelectionDialog(MainFrame mainFrame, Group group) {
        this.mainFrame = mainFrame;
        this.group = group;
        selectedEntry = null;
        setTitle("Gruppenprofil auswählen");
        setModal(true);
        setLocation((int) (mainFrame.getSize().getWidth() / 2), 200);
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
        workshopSelection = new JRadioButton("Workshop/Semesterkurs");
        instrumentalformationSelection = new JRadioButton("Instrumentalformation");
        chorSelection = new JRadioButton("Singschule/Chor");
        grundschulungSelection = new JRadioButton("Musikalische Grundschulung");
        otherSelection = new JRadioButton("Andere");
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
        allSelections.add(workshopSelection);
        allSelections.add(instrumentalformationSelection);
        allSelections.add(chorSelection);
        allSelections.add(grundschulungSelection);
        allSelections.add(otherSelection);
    }
    
    private void addWidgets() {
        selectionField.add(KGUSelection);
        selectionField.add(workshopSelection);
        selectionField.add(instrumentalformationSelection);
        selectionField.add(chorSelection);
        selectionField.add(grundschulungSelection);
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
        workshopSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    selectedEntry = new GroupEntry(mainFrame, "Workshop/Semesterkurs-Profil", group);
                    selectedEntry.setWorkshopProfile();
                }
            }
        });
        instrumentalformationSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                selectedEntry = new GroupEntry(mainFrame, "Instrumentalformation-Profil", group);
                selectedEntry.setInstrumentalformationProfile();
            }
        });
        chorSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                selectedEntry = new GroupEntry(mainFrame, "Singschule/Chor-Profil", group);
                selectedEntry.setChorProfile();
            }
        });
        grundschulungSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                selectedEntry = new GroupEntry(mainFrame, "Musikalische Grundschulung-Profil", group);
                selectedEntry.setGrundschulungProfile();
            }
        });
        otherSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                selectedEntry = new GroupEntry(mainFrame, "Anderes Gruppenprofil", group);
                selectedEntry.setOtherProfile();
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
