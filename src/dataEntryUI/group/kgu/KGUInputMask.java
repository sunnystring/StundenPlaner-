/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group.kgu;

import core.ProfileTypes;
import core.Database;
import core.Profile;
import core.StudentDay;
import static utils.GUIConstants.*;
import exceptions.NoEntryException;
import java.awt.BorderLayout;
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
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import mainframe.MainFrame;
import scheduleData.ScheduleData;
import utils.Colors;
import static utils.Colors.*;
import utils.Dialogs;

/**
 *
 * @author mathiaskielholz
 */
public abstract class KGUInputMask extends JDialog {

    private Database database;
    private MainFrame mainFrame;
    private ScheduleData scheduleData;
    private Profile kgu;
    private int lectionLength;
    private JPanel selectionField;
    private JScrollPane scrollField;
    private JPanel buttonField;
    private JLabel textLabel;
    private JButton cancelButton, approveButton, deleteButton;
    private ArrayList<JRadioButton> studentSelections;
    private ActionListener approveButtonListener;
    private ArrayList<Profile> kguMembers;
    private ArrayList<Profile> allocatedMembers;
    private ArrayList<Integer> selectableDayIndizes;


    public KGUInputMask(MainFrame mainFrame, Profile kgu, String title) {
        setTitle(title);
        this.database = mainFrame.getDatabase();
        this.mainFrame = mainFrame;
        scheduleData = mainFrame.getScheduleData();
        this.kgu = kgu;
        kguMembers = new ArrayList<>();
        getStudents();
        allocatedMembers = new ArrayList<>();
        selectableDayIndizes = database.getScheduleTimes().getValidDaysAsAbsoluteIndizes();
        studentSelections = new ArrayList<>();
        setLocation((int) (mainFrame.getSize().getWidth() / 2), 200);
        setPreferredSize(KGU_DIMENSION);
        setModal(true);
        setResizable(true);
        setLayout(new BorderLayout());
    }

    private void getStudents() {
        ArrayList<Profile> studentDataList = database.getStudentDataList();
        for (Profile profile : studentDataList) {
            if (profile.getProfileType() == ProfileTypes.KGU_MEMBER && !profile.isAllocated()) {
                kguMembers.add(profile);
            }
        }
    }

    private void createWidgets() {
        selectionField = new JPanel();
        selectionField.setLayout(new BoxLayout(selectionField, BoxLayout.PAGE_AXIS));
        selectionField.setBorder(DEFAULT_BORDER);
        scrollField = new JScrollPane(selectionField);
        buttonField = new JPanel();
        buttonField.setLayout(new BoxLayout(buttonField, BoxLayout.LINE_AXIS));
        buttonField.setBorder(DEFAULT_BORDER);
        cancelButton = new JButton("Abbrechen");
    }

    // Entry
    public void createEntryWidgets() {
        textLabel = new TextLabel("Geeignete Mitglieder auswählen und Gruppe zusammenstellen:");
        approveButton = new JButton("Profil speichern");
        approveButton.setEnabled(kguMembers.size() > 1);
        createWidgets();
    }

    public void createStudentSelection() {
        selectionField.add(textLabel);
        for (Profile member : kguMembers) {
            JRadioButton studentSelection = new JRadioButton();
            studentSelection.setBorder(BorderFactory.createEmptyBorder(2, 5, 0, 0));
            studentSelection.setForeground(NAMEFIELD_SELECTED_COLOR);
            studentSelection.setText(member.getFirstName() + " " + member.getName());
            selectionField.add(studentSelection);
            studentSelections.add(studentSelection);
            studentSelection.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        allocatedMembers.add(member);
                        getCommonTimesAndSetProfile();
                    } else {
                        allocatedMembers.remove(member);
                        getCommonTimesAndSetProfile();
                    }
                    kgu.getStudentTimes().updateValidStudentDays();
                    showCommonTimes();
                }
            });
        }
    }

    private void getCommonTimesAndSetProfile() {
        CommonTimes commonTimes;
        for (Integer dayIndex : selectableDayIndizes) {
            commonTimes = new CommonTimes(allocatedMembers); // selectableStudentDays in ein Gefäss (=commonTimes)
            commonTimes.findSelectedMemberBoundsAt(dayIndex);
            commonTimes.setStudentDayData(kgu, dayIndex); // neue Zeiten usw. in KGU-Profil setzen
        }
    }

    private void showCommonTimes() {
        scheduleData.clearAllTimeMarks();
        scheduleData.setAllValidTimeMarks(kgu);
        scheduleData.fireTableDataChanged();
    }

    public void addEntryWidgets() {
        buttonField.add(Box.createHorizontalGlue());
        buttonField.add(cancelButton);
        buttonField.add(approveButton);
        add(BorderLayout.CENTER, scrollField);
        add(BorderLayout.PAGE_END, buttonField);
    }

    public void addEntryButtonListeners() {
        addCancelButtonListener();
        approveButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!legalNumberOfMembers()) {
                    Dialogs.showIllegalKGUEntryErrorMessage();
                } else {
                    setLectionLength();
                    try {
                        setProfileData();
                    } catch (NoEntryException ex) {
                        Dialogs.showNoInputError("Es kann kein leeres KGU-Profil erstellt werden!");
                        return;
                    }
                    database.addProfile(kgu);
                    clearSchedule();
                    dispose();
                }
            }
        };
        approveButton.addActionListener(approveButtonListener);
    }

    private void setLectionLength() {
        if (allocatedMembers.size() == 2) {
            lectionLength = 45;
        }
        if (allocatedMembers.size() == 3) {
            lectionLength = 60;
        }
    }

    // Edit
    public void updateEditData() {
        getMembers();
    }

    private void getMembers() {
        for (Integer memberID : kgu.getKGUMemberIDs()) {
            allocatedMembers.add(database.getProfile(memberID));
        }
    }

    public void createEditWidgets() {
        setPreferredSize(DEFAULT_ENTRY_DIMENSION);
        createWidgets();
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
        for (int i = 0; i < allocatedMembers.size(); i++) {
            textLabel = new JLabel();
            textLabel.setText(allocatedMembers.get(i).getFirstName() + " " + allocatedMembers.get(0).getName());
            textLabel.setForeground(Colors.NAMEFIELD_SELECTED_COLOR);
            selectionField.add(textLabel);
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
        textLabel.setText("Auswahl ändern:");
        approveButton.setText("Änderung speichern");
        approveButton.removeActionListener(approveButtonListener);
        approveButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!legalNumberOfMembers()) {
                    Dialogs.showIllegalKGUEntryErrorMessage();
                } else {
                    removeMemberIDs();
                    removeMemberNames();
                    setLectionLength();
                    try {
                        setProfileData();
                    } catch (NoEntryException ex) {
                        Dialogs.showNoInputError("Es kann kein leeres KGU-Profil erstellt werden!");
                        return;
                    }
                    database.editProfile(kgu);
                    clearSchedule();
                    editMask.dispose();
                    dispose();
                }
            }
        };
        approveButton.addActionListener(approveButtonListener);
    }

    private boolean legalNumberOfMembers() {
        return allocatedMembers.size() >= 2 && allocatedMembers.size() <= 3;
    }

    private void addCancelButtonListener() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearSchedule();
                dispose();
            }
        });
    }

    private void clearSchedule() {
        scheduleData.clearAllTimeMarks();
        scheduleData.fireTableDataChanged();
    }

    private void setProfileData() {
        if (noTimeEntries()) {
            throw new NoEntryException();
        }
        setMembersAllocated(true);
        setMemberIDs();
        kgu.setProfileType(ProfileTypes.GROUP);
        kgu.setProfileName(ProfileTypes.KGU_NAME);
        kgu.setLectionLengthInMinutes(lectionLength);
        setStudentNames();
    }

    private boolean noTimeEntries() {
        boolean noEntries = true;
        for (StudentDay day : kgu.getStudentTimes().getValidStudentDayList()) {
            if (!day.isEmpty()) {
                noEntries = false;
            }
        }
        return noEntries;
    }

    private void setMembersAllocated(boolean state) {
        for (Profile member : allocatedMembers) {
            member.setAllocated(state);
        }
    }

    private void setMemberIDs() {
        for (Profile member : allocatedMembers) {
            kgu.addKGUMemberID(member.getID());
        }
    }

    private void removeMemberIDs() {
        kgu.getKGUMemberIDs().clear();
    }

    private void removeMemberNames() {
        kgu.setFirstName("");
        kgu.setName("");
        kgu.setThirdName("");
    }

    private void setStudentNames() {
        kgu.setFirstName(allocatedMembers.get(0).getFirstName() + " " + allocatedMembers.get(0).getName());
        kgu.setName(allocatedMembers.get(1).getFirstName() + " " + allocatedMembers.get(1).getName());
        if (allocatedMembers.size() == 3) {
            kgu.setThirdName(allocatedMembers.get(2).getFirstName() + " " + allocatedMembers.get(2).getName());
        }
    }

    public void setProfile(Profile group) {
        this.kgu = group;
    }

    public abstract void setupUI();

    private class TextLabel extends JLabel {

        public TextLabel(String text) {
            setText(text);
            setHorizontalAlignment(SwingConstants.LEADING);
            setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        }
    }
}
