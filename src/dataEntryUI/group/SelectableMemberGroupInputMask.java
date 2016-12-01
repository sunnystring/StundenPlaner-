/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group;

import static core.ProfileTypes.*;
import core.Database;
import core.Profile;
import core.StudentDay;
import dataEntryUI.group.kgu.KGUEntry;
import dataEntryUI.group.sdg.SDGEntry;
import dataEntryUI.schedule.ScheduleEdit;
import static utils.GUIConstants.*;
import exceptions.NoEntryException;
import exceptions.OutOfBoundException;
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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import mainframe.MainFrame;
import scheduleData.ScheduleData;
import scheduleData.ScheduleTimeFrame;
import utils.Colors;
import static utils.Colors.*;
import utils.Dialogs;

/**
 *
 * @author mathiaskielholz
 */
public abstract class SelectableMemberGroupInputMask extends JDialog {

    protected Database database;
    private MainFrame mainFrame;
    private ScheduleData scheduleData;
    private ScheduleTimeFrame timeFrame;
    private Profile group;
    protected int lectionLength;
    private JPanel selectionArea;
    private JScrollPane scrollField;
    private JPanel buttonField;
    private JLabel textLabel, lectiontypeLabel;
    private JButton cancelButton, approveButton, deleteButton;
    private ArrayList<JRadioButton> studentSelections;
    private ActionListener approveButtonListener;
    protected ArrayList<Profile> selectableStudents;
    protected ArrayList<Profile> allocatedMembers;
    private ArrayList<Integer> selectableDayIndizes;

    public SelectableMemberGroupInputMask(MainFrame mainFrame, Profile group, String title) {
        super(mainFrame);
        setTitle(title);
        this.mainFrame = mainFrame;
        database = mainFrame.getDatabase();
        scheduleData = mainFrame.getScheduleData();
        timeFrame = scheduleData.getTimeFrame();
        this.group = group;
        selectableStudents = new ArrayList<>();
        allocatedMembers = new ArrayList<>();
        selectableDayIndizes = database.getScheduleTimes().getValidDaysAsAbsoluteIndizes();
        studentSelections = new ArrayList<>();
        setLocation((int) (mainFrame.getSize().getWidth() / 2), 200);
        setPreferredSize(KGU_DIMENSION);
        setModal(true);
        setResizable(false);
        setLayout(new BorderLayout());
    }

    public void createEntryWidgets() {
        textLabel = new JLabel("Geeignete Mitglieder auswählen und Gruppe zusammenstellen:");
        textLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        textLabel.setHorizontalAlignment(SwingConstants.LEADING);
        lectiontypeLabel = new JLabel("Lektionsdauer:");
        approveButton = new JButton("Profil speichern");
        approveButton.setEnabled(false);
        createWidgets();
    }

    private void createWidgets() {
        selectionArea = new JPanel();
        selectionArea.setLayout(new BoxLayout(selectionArea, BoxLayout.PAGE_AXIS));
        selectionArea.setBorder(DEFAULT_BORDER);
        scrollField = new JScrollPane(selectionArea);
        buttonField = new JPanel();
        buttonField.setLayout(new BoxLayout(buttonField, BoxLayout.LINE_AXIS));
        buttonField.setBorder(DEFAULT_BORDER);
        cancelButton = new JButton("Abbrechen");
    }

    public void createStudentSelection() {
        selectionArea.add(textLabel);
        for (Profile student : selectableStudents) {
            JRadioButton studentSelection = new JRadioButton();
            studentSelection.setBorder(BorderFactory.createEmptyBorder(2, 5, 0, 0));
            studentSelection.setForeground(NAMEFIELD_SELECTED_COLOR);
            studentSelection.setText(student.getFirstName() + " " + student.getName());
            selectionArea.add(studentSelection);
            studentSelections.add(studentSelection);
            studentSelection.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        allocatedMembers.add(student);
                        getCommonTimesAndSetProfile(allocatedMembers);
                    } else {
                        allocatedMembers.remove(student);
                        getCommonTimesAndSetProfile(allocatedMembers);
                    }
                    group.getStudentTimes().updateValidStudentDays();
                    showCommonTimes();
                    approveButton.setEnabled(legalNumberOfAllocatedMembers());
                }
            });
        }
    }

    private void getCommonTimesAndSetProfile(ArrayList<Profile> allocatedMembers) {
        CommonTimes commonTimes;
        for (Integer dayIndex : selectableDayIndizes) {
            commonTimes = new CommonTimes(allocatedMembers); // selectableStudentDays in ein Gefäss (=commonTimes)
            commonTimes.findSelectedMemberBoundsAt(dayIndex);
            commonTimes.setStudentDayData(group, dayIndex); // Zeiten in Gruppen-Profil setzen
        }
    }

    private void showCommonTimes() {
        scheduleData.clearAllTimeMarks();
        scheduleData.setAllValidTimeMarks(group);
        scheduleData.fireTableDataChanged();
    }

    public void addEntryWidgets() {
        buttonField.add(Box.createHorizontalGlue());
        buttonField.add(cancelButton);
        buttonField.add(approveButton);
        add(BorderLayout.CENTER, scrollField);
        add(BorderLayout.PAGE_END, buttonField);
    }

    public void addEntryButtonListeners(String profileName) {
        addCancelButtonListener();
        approveButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (legalNumberOfAllocatedMembers()) {

                    if (profileName.equals(KGU_NAME)) {
                        setKGULectionLength();
                    }
                    try {
                        setProfileData(profileName);
                    } catch (NoEntryException ex) {
                        Dialogs.showNoInputError(Dialogs.NO_INPUT_MESSAGE);
                        return;
                    } catch (OutOfBoundException ex) {
                        correctInvalidEntryTimes();
                        return;
                    }
                    database.addProfile(group);
                    clearSchedule();
                    dispose();
                }
            }
        };
        approveButton.addActionListener(approveButtonListener);
    }

    private void setKGULectionLength() {
        if (allocatedMembers.size() == 2) {
            lectionLength = 45;
        }
        if (allocatedMembers.size() == 3) {
            lectionLength = 60;
        }
    }

    private void correctInvalidEntryTimes() {
        int lectionLengthAsNumberOfFields = lectionLength / 5;
        String start = timeFrame.getAbsoluteStart().toString();
        String end = timeFrame.getAbsoluteEnd().minusLengthOf(lectionLengthAsNumberOfFields + 1).toString();
        int choice = Dialogs.showStudentTimesOutOfBoundOptionMessage("Einteilbare Zeit:\n" + start + " bis " + end);
        if (choice == JOptionPane.YES_OPTION) // Stundenplan anpassen, falls NO_OPTION Schülerzeit anpassen
        {
            JDialog scheduleEdit = new ScheduleEdit(mainFrame);
            scheduleEdit.setVisible(true);
        }
    }

    public void createAndAddLectionTypeSelection() {
        Integer[] lectionTypes = new Integer[]{30, 40, 45, 50, 60, 75, 90};
        JComboBox lectiontypeSelectionBox = new JComboBox(lectionTypes);
        lectiontypeSelectionBox.setMaximumSize(new Dimension(50, 50));
        lectiontypeSelectionBox.setSelectedIndex(0);
        lectionLength = lectionTypes[0];
        lectiontypeSelectionBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lectionLength = (Integer) lectiontypeSelectionBox.getSelectedItem();
            }
        });
        buttonField.add(lectiontypeLabel);
        buttonField.add(lectiontypeSelectionBox);
    }

    // Edit
    public void updateEditData() {
        restoreMembers();
    }

    private void restoreMembers() {
        for (Integer memberID : group.getMemberIDs()) {
            allocatedMembers.add(database.getProfile(memberID));
        }
    }

    public void createEditWidgets() {
        setPreferredSize(DEFAULT_ENTRY_DIMENSION);
        createWidgets();
        selectionArea.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        approveButton = new JButton("Profil ändern");
        deleteButton = new JButton("Profil auflösen");
    }

    public void addEditWidgets() {
        addEditStudentNameLabels();
        buttonField.add(Box.createHorizontalGlue());
        buttonField.add(cancelButton);
        buttonField.add(approveButton);
        buttonField.add(deleteButton);
        add(BorderLayout.CENTER, selectionArea);
        add(BorderLayout.PAGE_END, buttonField);
    }

    private void addEditStudentNameLabels() {
        for (int i = 0; i < allocatedMembers.size(); i++) {
            textLabel = new JLabel();
            textLabel.setText(allocatedMembers.get(i).getFirstName() + " " + allocatedMembers.get(0).getName());
            textLabel.setForeground(Colors.NAMEFIELD_SELECTED_COLOR);
            selectionArea.add(textLabel);
        }
    }

    public void addEditButtonListeners(String profileName) {
        addCancelButtonListener();
        approveButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectableMemberGroupInputMask secondEntry = null;
                setMembersAllocated(false);
                if (profileName.equals(KGU_NAME)) {
                    secondEntry = new KGUEntry(mainFrame, group, "n-Profil ändern");
                } else if (profileName.equals(SDG_NAME)) {
                    secondEntry = new SDGEntry(mainFrame, group, getTitle());
                }
                secondEntry.updateSecondEntryUI(profileName, SelectableMemberGroupInputMask.this);
                secondEntry.setVisible(true);
            }
        };
        approveButton.addActionListener(approveButtonListener);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMembersAllocated(false);
                database.deleteProfile(group);
                dispose();
            }
        });
    }

    public void updateSecondEntryUI(String profileName, SelectableMemberGroupInputMask editMask) {
        textLabel.setText("Auswahl ändern:");
        approveButton.setText("Änderung speichern");
        approveButton.removeActionListener(approveButtonListener);
        approveButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (legalNumberOfAllocatedMembers()) {
                    removeMemberIDs();
                    removeMemberNames();
                    if (profileName.equals(KGU_NAME)) {
                        setKGULectionLength();
                    }
                    try {
                        setProfileData(profileName);
                    } catch (NoEntryException ex) {
                        Dialogs.showNoInputError(Dialogs.NO_INPUT_MESSAGE);
                        return;
                    } catch (OutOfBoundException ex) {
                        correctInvalidEntryTimes();
                        return;
                    }
                    database.editProfile(group);
                    clearSchedule();
                    editMask.dispose();
                    dispose();
                }
            }
        };
        approveButton.addActionListener(approveButtonListener);
    }

    private boolean legalNumberOfAllocatedMembers() {
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

    private void setProfileData(String profileName) {
        if (noTimeEntries()) {
            throw new NoEntryException();
        }
        if (outOfScheduleBounds(group)) {
            throw new OutOfBoundException();
        }
        setMembersAllocated(true);
        setMemberIDs();
        if (profileName.equals(SDG_NAME)) {
            group.setProfileType(SDG);
        } else if (profileName.equals(KGU_NAME)) {
            group.setProfileType(GROUP);
        }
        group.setProfileName(profileName);
        group.setLectionLengthInMinutes(lectionLength);
        setStudentNames();
    }

    private boolean noTimeEntries() {
        boolean noEntries = true;
        for (StudentDay day : group.getStudentTimes().getValidStudentDayList()) {
            if (!day.isEmpty()) {
                noEntries = false;
            }
        }
        return noEntries;
    }

    private boolean outOfScheduleBounds(Profile group) {
        int lectionLengthAsNumberOfFields = lectionLength / 5;
        for (StudentDay studentDay : group.getStudentTimes().getValidStudentDayList()) {
            if (studentDay.outOfTimeFrame(timeFrame, lectionLengthAsNumberOfFields)) {
                return true;
            }
        }
        return false;
    }

    private void setMembersAllocated(boolean state) {
        for (Profile member : allocatedMembers) {
            member.setAllocated(state);
        }
    }

    private void setMemberIDs() {
        for (Profile member : allocatedMembers) {
            group.addKGUMemberID(member.getID());
        }
    }

    private void removeMemberIDs() {
        group.getMemberIDs().clear();
    }

    private void removeMemberNames() {
        group.setFirstName("");
        group.setName("");
        group.setThirdName("");
    }

    private void setStudentNames() {
        group.setFirstName(allocatedMembers.get(0).getFirstName() + " " + allocatedMembers.get(0).getName());
        group.setName(allocatedMembers.get(1).getFirstName() + " " + allocatedMembers.get(1).getName());
        if (allocatedMembers.size() == 3) {
            group.setThirdName(allocatedMembers.get(2).getFirstName() + " " + allocatedMembers.get(2).getName());
        }
    }

    public void setProfile(Profile group) {
        this.group = group;
    }

    public abstract void setupUI();

    public abstract void getSelectableStudents();

}
