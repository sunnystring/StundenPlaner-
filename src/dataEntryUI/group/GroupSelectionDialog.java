/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group;

import dataEntryUI.group.profiles.GroupEntry;
import static core.ProfileTypes.*;
import core.Profile;
import static utils.GUIConstants.*;
import dataEntryUI.group.kgu.KGUEntry;
import dataEntryUI.group.sdg.SDGEntry;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
 * Auswahl-Dialogfenster für alle Gruppenprofile
 */
public class GroupSelectionDialog extends JDialog {

    private MainFrame mainFrame;
    private Profile group;
    private GroupEntry selectedEntry;
    private KGUEntry kguEntry;
    private SDGEntry sdgEntry;
    private String profileDescription;
    private JPanel selectionField, buttonField;
    private ButtonGroup allSelections;
    private JRadioButton allGroupsSelection, kguSelection, selfDefinedGroupSelection;
    private JButton cancelButton, approveButton;

    public GroupSelectionDialog(MainFrame mainFrame, Profile group) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        this.group = group;
        selectedEntry = null;
        kguEntry = null;
        sdgEntry = null;
        setTitle("Gruppenprofil auswählen");
        profileDescription = "Instrumentalformation, Ensemble, MEZ, Theater, Chor usw.";
        setModal(true);
        setLocation((int) (mainFrame.getSize().getWidth() / 2), 200);
        setResizable(false);
        setMinimumSize(DEFAULT_ENTRY_DIMENSION);
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
        selectionField.setBorder(DEFAULT_BORDER);
        kguSelection = new JRadioButton(KGU_NAME + " (KGU)");
        allGroupsSelection = new JRadioButton(profileDescription);
        selfDefinedGroupSelection = new JRadioButton(SDG_NAME + " (SDG)");
        createButtonGroup();
        buttonField = new JPanel();
        buttonField.setLayout(new BoxLayout(buttonField, BoxLayout.LINE_AXIS));
        buttonField.setBorder(DEFAULT_BORDER);
        cancelButton = new JButton("Abbrechen");
        approveButton = new JButton("Übernehmen");
    }

    private void createButtonGroup() {
        allSelections = new ButtonGroup();
        allSelections.add(allGroupsSelection);
        allSelections.add(kguSelection);
        allSelections.add(selfDefinedGroupSelection);
    }

    private void addWidgets() {
        selectionField.add(kguSelection);
        selectionField.add(allGroupsSelection);
        selectionField.add(selfDefinedGroupSelection);
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
                kguEntry = new KGUEntry(mainFrame, group, "n-Profil erstellen");
                selectedEntry = null;
                sdgEntry = null;
            }
        });
        allGroupsSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                  group.setProfileName(GROUP_NAME);
                selectedEntry = new GroupEntry(mainFrame, "Gruppenprofil", group);
                selectedEntry.setGroupProfileMask();
                kguEntry = null;
                sdgEntry = null;
            }
        });
        selfDefinedGroupSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                sdgEntry = new SDGEntry(mainFrame, group, "Selbstdefiniertes Gruppenprofil erstellen");
                selectedEntry = null;
                kguEntry = null;
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
                } else if (kguEntry != null) {
                    kguEntry.setVisible(true);
                } else if (sdgEntry != null) {
                    sdgEntry.setVisible(true);
                }
            }
        });
    }
}
