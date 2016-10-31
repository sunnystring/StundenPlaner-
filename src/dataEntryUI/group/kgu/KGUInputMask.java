/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group.kgu;

import core.ProfileTypes;
import core.Database;
import core.Profile;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import mainframe.MainFrame;
import utils.Colors;

/**
 *
 * @author mathiaskielholz
 */
public abstract class KGUInputMask extends JDialog {

    private Database database;
    private MainFrame mainFrame;
    private Profile kgu;
    private int lectionLength;
    private ArrayList<Profile> studentSelectionList;
    private ArrayList<Profile> allocatedMemberList;
    private JPanel selectionField, buttonField;
    private JLabel textlabel;
    private JRadioButton studentSelection;
    private JButton cancelButton, approveButton, deleteButton;
    private ActionListener approveButtonListener;

    public KGUInputMask(MainFrame mainFrame, Profile kgu) {
        this.database = mainFrame.getDatabase();
        this.mainFrame = mainFrame;
        this.kgu = kgu;
        studentSelectionList = new ArrayList<>();
        getStudents();
        allocatedMemberList = new ArrayList<>();
        setModal(true);
        setLocation((int) (mainFrame.getSize().getWidth() / 2), 200);
        setResizable(false);
        setMinimumSize(new Dimension(300, 100));
        setLayout(new BorderLayout());
    }

    private void getStudents() {
        ArrayList<Profile> studentDataList = database.getStudentDataList();
        for (Profile profile : studentDataList) {
            if (profile.getProfileType() == ProfileTypes.KGU_MEMBER && !profile.isAllocated()) {
                studentSelectionList.add(profile);
            }
        }
    }

    // Entry
    public void createEntryWidgets() {
        textlabel = new JLabel("SchülerInnen auswählen:");
        textlabel.setHorizontalAlignment(SwingConstants.LEADING);
        textlabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        approveButton = new JButton("Profil speichern");
        approveButton.setEnabled(studentSelectionList.size() > 1);
        createOtherWidgets();
    }

    public void createEntrySelection() {
        selectionField.add(textlabel);
        for (Profile profile : studentSelectionList) {
            studentSelection = new JRadioButton();
            studentSelection.setForeground(Colors.NAMEFIELD_SELECTED_COLOR);
            studentSelection.setText(profile.getFirstName() + " " + profile.getName());
            selectionField.add(studentSelection);
            studentSelection.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        allocatedMemberList.add(profile);
                    } else {
                        allocatedMemberList.remove(profile);
                    }
                }
            });
        }
    }

    public void addEntryWidgets() {
        buttonField.add(Box.createHorizontalGlue());
        buttonField.add(cancelButton);
        buttonField.add(approveButton);
        add(BorderLayout.CENTER, selectionField);
        add(BorderLayout.PAGE_END, buttonField);

    }

    public void addEntryButtonListeners() {
        addCancelButtonListener();
        approveButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (numberOfMembersAllowed()) {
                    setLectionLength();
                    setProfileData();
                    database.addProfile(kgu);
                }
                dispose();
            }
        };
        approveButton.addActionListener(approveButtonListener);
    }

    private void setLectionLength() {
        if (allocatedMemberList.size() == 2) {
            lectionLength = 45;
        }
        if (allocatedMemberList.size() == 3) {
            lectionLength = 60;
        }
    }

    // Edit
    public void updateEditData() {
        getMembers();
    }

    private void getMembers() {
        for (Integer memberID : kgu.getKGUMemberIDs()) {
            allocatedMemberList.add(database.getProfile(memberID));
        }
    }

    public void createEditWidgets() {
        createOtherWidgets();
        selectionField.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        approveButton = new JButton("Profil ändern");
        deleteButton = new JButton("Profil auflösen");
    }

    public void addEditWidgets() {
        addEditStudentNameLabels();
        buttonField.add(Box.createHorizontalGlue());
        buttonField.add(cancelButton);
        buttonField.add(approveButton);
        buttonField.add(deleteButton);
        add(BorderLayout.CENTER, selectionField);
        add(BorderLayout.PAGE_END, buttonField);
    }

    private void addEditStudentNameLabels() {
        for (int i = 0; i < allocatedMemberList.size(); i++) {
            textlabel = new JLabel();
            textlabel.setText(allocatedMemberList.get(i).getFirstName() + " " + allocatedMemberList.get(0).getName());
            textlabel.setForeground(Colors.NAMEFIELD_SELECTED_COLOR);
            selectionField.add(textlabel);
        }
    }

    public void addEditButtonListeners() {
        addCancelButtonListener();
        approveButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMembersAllocated(false); // Edit-KGU-Members freischalten
                KGUEntry secondEntry = new KGUEntry(mainFrame, kgu, "n-Profil ändern");
                secondEntry.updateSecondEntryUI(KGUInputMask.this);
                secondEntry.setVisible(true);
            }
        };
        approveButton.addActionListener(approveButtonListener);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMembersAllocated(false); // Edit-KGU-Members freischalten
                database.deleteProfile(kgu);
                dispose();
            }
        });
    }

    public void updateSecondEntryUI(KGUInputMask editMask) {
        textlabel.setText("Auswahl ändern:");
        approveButton.setText("Änderung speichern");
        approveButton.removeActionListener(approveButtonListener);
        approveButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (numberOfMembersAllowed()) {
                    removeMemberIDs();
                    setProfileData();
                    database.editProfile(kgu);
                }
                editMask.dispose();
                dispose();
            }
        };
        approveButton.addActionListener(approveButtonListener);
    }

    private boolean numberOfMembersAllowed() {
        return allocatedMemberList.size() >= 2 && allocatedMemberList.size() <= 3;
    }

    private void createOtherWidgets() {
        selectionField = new JPanel();
        selectionField.setLayout(new BoxLayout(selectionField, BoxLayout.PAGE_AXIS));
        selectionField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonField = new JPanel();
        buttonField.setLayout(new BoxLayout(buttonField, BoxLayout.LINE_AXIS));
        buttonField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cancelButton = new JButton("Abbrechen");
    }

    private void addCancelButtonListener() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void setProfileData() {
        setMembersAllocated(true);
        setMemberIDs();
        kgu.getStudentTimes().setValidStudentDays();
        kgu.setProfileType(ProfileTypes.GROUP);
        kgu.setProfileName(ProfileTypes.KGU_NAME);
        kgu.setLectionLengthInMinutes(lectionLength);
        setStudentNames();
    }

    private void setMembersAllocated(boolean state) {
        for (Profile member : allocatedMemberList) {
            member.setAllocated(state);
        }
    }

    private void setMemberIDs() {
        for (Profile member : allocatedMemberList) {
            kgu.addKGUMemberID(member.getProfileID());
        }
    }

    private void removeMemberIDs() {
        kgu.getKGUMemberIDs().clear();
    }

    private void setStudentNames() {
        kgu.setFirstName(allocatedMemberList.get(0).getFirstName() + " " + allocatedMemberList.get(0).getName());
        kgu.setName(allocatedMemberList.get(1).getFirstName() + " " + allocatedMemberList.get(1).getName());
        if (allocatedMemberList.size() == 3) {
            kgu.setThirdName(allocatedMemberList.get(2).getFirstName() + " " + allocatedMemberList.get(2).getName());
        }
    }

    public void setProfile(Profile group) {
        this.kgu = group;
    }

    public abstract void setupUI();
}
