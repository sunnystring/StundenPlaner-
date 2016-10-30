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
        allocatedMemberList = new ArrayList<>();
        getKGUStudents();
        setModal(true);
        setLocation((int) (mainFrame.getSize().getWidth() / 2), 200);
        setResizable(false);
        setMinimumSize(new Dimension(300, 100));
        setLayout(new BorderLayout());
    }
    
    private void getKGUStudents() {
        ArrayList<Profile> studentDataList = database.getStudentDataList();
        for (Profile profile : studentDataList) {
            if (profile.getProfileType() == ProfileTypes.KGU_MEMBER) {
                studentSelectionList.add(profile);
            }
        }
    }
    
    public void createEntryWidgets() {
        textlabel = new JLabel("SchülerInnen auswählen:");
        textlabel.setHorizontalAlignment(SwingConstants.LEADING);
        textlabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        approveButton = new JButton("Profil speichern");
        createOtherWidgets();
    }
    
    public void createEntrySelectionField() {
        selectionField.add(textlabel);
        for (Profile profile : studentSelectionList) {
            if (!profile.isAllocated()) {
                createStudentSelection(profile);
            }
        }
    }
    
    private void createStudentSelection(Profile profile) {
        studentSelection = new JRadioButton();
        studentSelection.setForeground(Colors.NAMEFIELD_SELECTED_COLOR);
        studentSelection.setText(profile.getFirstName() + " " + profile.getName());
        selectionField.add(studentSelection);
        studentSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                kgu.setKGUMemberID(profile.getProfileID());
                allocatedMemberList.add(profile);
            }
        });
    }
    
    public void addEntryWidgets() {
        addEntryButtons();
        add(BorderLayout.CENTER, selectionField);
        add(BorderLayout.PAGE_END, buttonField);
        
    }
    
    private void addEntryButtons() {
        buttonField.add(Box.createHorizontalGlue());
        buttonField.add(cancelButton);
        buttonField.add(approveButton);
    }
    
    public void addEntryButtonListeners() {
        addCancelButtonListener();
        approveButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMembersAllocationState(true);
                setProfileData();
                database.addProfile(kgu);
                dispose();
            }
        };
        approveButton.addActionListener(approveButtonListener);
    }
    
    public void updateEditData() {
        setAllocatedMemberList();
        setMembersAllocationState(false);
    }
    
    public void createEditWidgets() {
        createOtherWidgets();
        selectionField.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        approveButton = new JButton("Profil ändern");
        deleteButton = new JButton("Profil auflösen");
    }
    
    public void addEditWidgets() {
      //  setAllocatedMemberList();
        addStudentNameLabels();
        addEditButtons();
        add(BorderLayout.CENTER, selectionField);
        add(BorderLayout.PAGE_END, buttonField);
        //   }
    }
    
    private void setAllocatedMemberList() {
        for (Integer memberID : kgu.getKGUMemberIDs()) {
            allocatedMemberList.add(database.getProfile(memberID));
        }
    }
    
    private void addStudentNameLabels() {
        textlabel = new JLabel();
        textlabel.setText(allocatedMemberList.get(0).getFirstName() + " " + allocatedMemberList.get(0).getName());
        textlabel.setForeground(Colors.NAMEFIELD_SELECTED_COLOR);
        selectionField.add(textlabel);
        textlabel = new JLabel();
        textlabel.setText(allocatedMemberList.get(1).getFirstName() + " " + allocatedMemberList.get(1).getName());
        textlabel.setForeground(Colors.NAMEFIELD_SELECTED_COLOR);
        selectionField.add(textlabel);
    }
    
    private void addEditButtons() {
        buttonField.add(Box.createHorizontalGlue());
        buttonField.add(cancelButton);
        buttonField.add(approveButton);
        buttonField.add(deleteButton);
    }
    
    public void addEditButtonListeners() {
        addCancelButtonListener();
        approveButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KGUEntry secondEntry = new KGUEntry(mainFrame, kgu, "n-Profil ändern");
                secondEntry.updateUIForSecondEntry(KGUInputMask.this);
                secondEntry.setVisible(true);
            }
        };
        approveButton.addActionListener(approveButtonListener);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.deleteProfile(kgu);
                setMembersAllocationState(false);
                dispose();
            }
        });
    }
    
    public void updateUIForSecondEntry(KGUInputMask editMask) {
        textlabel.setText("Auswahl ändern:");
        approveButton.setText("Änderung speichern");
        approveButton.removeActionListener(approveButtonListener);
        approveButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMembersAllocationState(true);
                setProfileData();
                database.editProfile(kgu);
                dispose();
                editMask.dispose();
            }
        };
        approveButton.addActionListener(approveButtonListener);
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
    
    private void setMembersAllocationState(boolean state) {
        for (Profile profile : allocatedMemberList) {
            profile.setAllocated(state);
        }
    }
    
    private void setProfileData() {
        lectionLength = 45;
        kgu.getStudentTimes().setValidStudentDays();
        kgu.setProfileType(ProfileTypes.GROUP);
        kgu.setProfileName(ProfileTypes.KGU_NAME);
        kgu.setLectionLengthInMinutes(lectionLength);
        setStudentNames();
    }
    
    private void setStudentNames() {
        kgu.setFirstName(allocatedMemberList.get(0).getFirstName() + " " + allocatedMemberList.get(0).getName());
        kgu.setName(allocatedMemberList.get(1).getFirstName() + " " + allocatedMemberList.get(1).getName());
    }
    
    private void addCancelButtonListener() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    public void setProfile(Profile group) {
        this.kgu = group;
    }
    
    public abstract void setupUI();
}
