/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group;

import core.ProfileTypes;
import core.Profile;
import dataEntryUI.group.kgu.KGUEntry;
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
    private Profile group;
    private GroupEntry selectedEntry;
    private KGUEntry kguEntry;
    private String profileName;
    private JPanel selectionField, buttonField;
    private ButtonGroup allSelections;
    private JRadioButton kguSelection, workshopSelection, instrumentalformationSelection, chorSelection, grundschulungSelection, otherSelection;
    private JButton cancelButton, approveButton;
    
    public GroupSelectionDialog(MainFrame mainFrame, Profile group) {
        this.mainFrame = mainFrame;
        this.group = group;
        selectedEntry = null;
        kguEntry = null;
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
        kguSelection = new JRadioButton(ProfileTypes.KGU_NAME);
        workshopSelection = new JRadioButton(ProfileTypes.WORKSHOP_NAME);
        instrumentalformationSelection = new JRadioButton(ProfileTypes.INSTR_FORMATION_NAME);
        chorSelection = new JRadioButton(ProfileTypes.CHOR_NAME);
        grundschulungSelection = new JRadioButton(ProfileTypes.GRUNDSCHULUNG_NAME);
        otherSelection = new JRadioButton(ProfileTypes.ANDERES_NAME);
        createButtonGroup();
        buttonField = new JPanel();
        buttonField.setLayout(new BoxLayout(buttonField, BoxLayout.LINE_AXIS));
        buttonField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cancelButton = new JButton("Abbrechen");
        approveButton = new JButton("Übernehmen");
    }
    
    private void createButtonGroup() {
        allSelections = new ButtonGroup();
        allSelections.add(kguSelection);
        allSelections.add(workshopSelection);
        allSelections.add(instrumentalformationSelection);
        allSelections.add(chorSelection);
        allSelections.add(grundschulungSelection);
        allSelections.add(otherSelection);
    }
    
    private void addWidgets() {
        selectionField.add(kguSelection);
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
        kguSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                selectedEntry = null;
                kguEntry = new KGUEntry(mainFrame, group, "n-Profil erstellen");
            }
        });
        workshopSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                profileName = ProfileTypes.WORKSHOP_NAME;
                group.setProfileName(profileName);
                selectedEntry = new GroupEntry(mainFrame, profileName + "-Profil", group);
                selectedEntry.setWorkshopProfile();
            }
        });
        instrumentalformationSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                profileName = ProfileTypes.INSTR_FORMATION_NAME;
                group.setProfileName(profileName);
                selectedEntry = new GroupEntry(mainFrame, profileName + "-Profil", group);
                selectedEntry.setInstrumentalformationProfile();
            }
        });
        chorSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                profileName = ProfileTypes.CHOR_NAME;
                group.setProfileName(profileName);
                selectedEntry = new GroupEntry(mainFrame, profileName + "-Profil", group);
                selectedEntry.setChorProfile();
            }
        });
        grundschulungSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                profileName = ProfileTypes.GRUNDSCHULUNG_NAME;
                group.setProfileName(profileName);
                selectedEntry = new GroupEntry(mainFrame, profileName + "-Profil", group);
                selectedEntry.setGrundschulungProfile();
            }
        });
        otherSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                profileName = ProfileTypes.ANDERES_NAME;
                group.setProfileName(profileName);
                selectedEntry = new GroupEntry(mainFrame, profileName + " Profil", group);
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
                if (kguEntry != null) {
                    kguEntry.setVisible(true);
                }
            }
        });
    }
}
