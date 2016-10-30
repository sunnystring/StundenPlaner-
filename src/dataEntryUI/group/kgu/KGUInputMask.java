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
    private Profile group;
    private int lectionLength;
    private ArrayList<Profile> KGUStudentDataList;
    private JPanel selectionField, buttonField;
    private JLabel textlabel;
    private JRadioButton studentSelection;
    private JButton cancelButton, approveButton, deleteButton;
    private ActionListener approveButtonListener;

    public KGUInputMask(MainFrame mainFrame, Profile group) {
        this.database = mainFrame.getDatabase();
        this.mainFrame = mainFrame;
        this.group = group;
        KGUStudentDataList = new ArrayList<>();
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
                KGUStudentDataList.add(profile);
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

    public void createSelectionField() {
        selectionField.add(textlabel);
        for (Profile profile : KGUStudentDataList) {
            studentSelection = new JRadioButton();
            studentSelection.setForeground(Colors.NAMEFIELD_SELECTED_COLOR);
            studentSelection.setText(profile.getFirstName() + " " + profile.getName());
            selectionField.add(studentSelection);
        }
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
                setProfileData();
                database.addProfile(group);
                dispose();
            }
        };
        approveButton.addActionListener(approveButtonListener);
    }

    public void createEditWidgets() {
        createOtherWidgets();
        selectionField.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        approveButton = new JButton("Profil ändern");
        deleteButton = new JButton("Profil auflösen");
    }

    public void addEditWidgets() {
        addStudentNameLabels();
        addEditButtons();
        add(BorderLayout.CENTER, selectionField);
        add(BorderLayout.PAGE_END, buttonField);
    }

    private void addStudentNameLabels() {
        for (Profile profile : KGUStudentDataList) {
            textlabel = new JLabel();
            textlabel.setText(profile.getFirstName() + " " + profile.getName());
            textlabel.setForeground(Colors.NAMEFIELD_SELECTED_COLOR);
            selectionField.add(textlabel);
        }
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
                KGUEntry newEntry = new KGUEntry(mainFrame, group, "n-Profil ändern");
                newEntry.UpdateUIForSecondEntry(KGUInputMask.this);
                newEntry.setVisible(true);
                //  dispose();
            }
        };
        approveButton.addActionListener(approveButtonListener);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.deleteProfile(group);
                dispose();
            }
        });
    }

    public void UpdateUIForSecondEntry(KGUInputMask editMask) {
        approveButton.setText("Änderung speichern");
        approveButton.removeActionListener(approveButtonListener);
        approveButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setProfileData();
                database.editProfile(group);
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

    private void setProfileData() {
        lectionLength = 45;
        group.getStudentTimes().setValidStudentDays();
        group.setProfileType(ProfileTypes.GROUP);
        group.setProfileName(ProfileTypes.KGU_NAME);
        // group.setFirstName("Marlon Guerra");
        //  group.setName("Valentino Demarco");
        group.setLectionLengthInMinutes(lectionLength);
    }

    private void addCancelButtonListener() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

//    public void addWidgets() {
//        add(BorderLayout.CENTER, selectionField);
//        add(BorderLayout.PAGE_END, buttonField);
//    }
    public void setProfile(Profile group) {
        this.group = group;
    }

    public abstract void setupUI();
}
