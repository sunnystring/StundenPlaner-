/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group.kgu;

import userUtils.CommonStudentTimes;
import core.ProfileTypes;
import core.Database;
import core.Profile;
import core.StudentDay;
import exceptions.NoEntryException;
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
import scheduleData.ScheduleData;
import utils.Colors;
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
    private ArrayList<Profile> studentSelectionList;
    private ArrayList<Profile> allocatedMemberList;
    private JPanel selectionField, buttonField;
    private JLabel textlabel;
    private JRadioButton studentSelection;
    private JButton cancelButton, approveButton, deleteButton;
    private ActionListener approveButtonListener;
    private ArrayList<Integer> selectableDayIndizes;

    public KGUInputMask(MainFrame mainFrame, Profile kgu) {
        this.database = mainFrame.getDatabase();
        this.mainFrame = mainFrame;
        scheduleData = mainFrame.getScheduleData();
        this.kgu = kgu;
        studentSelectionList = new ArrayList<>();
        getStudents();
        allocatedMemberList = new ArrayList<>();
        selectableDayIndizes = database.getScheduleTimes().getValidDaysAsAbsoluteIndizes();
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
        for (Profile member : studentSelectionList) {
            studentSelection = new JRadioButton();
            studentSelection.setForeground(Colors.NAMEFIELD_SELECTED_COLOR);
            studentSelection.setText(member.getFirstName() + " " + member.getName());
            selectionField.add(studentSelection);
            studentSelection.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        allocatedMemberList.add(member);
                        findCommonTimesAndSetProfile();
                    } else {
                        allocatedMemberList.remove(member);
                        findCommonTimesAndSetProfile();
                    }
                    kgu.getStudentTimes().updateValidStudentDays();
                    showCommonTimes();
                }
            });
        }
    }

    private void findCommonTimesAndSetProfile() {
        CommonStudentTimes commonTimes;
        for (Integer dayIndex : selectableDayIndizes) {
            ArrayList<StudentDay> selectableDays = new ArrayList<>();
            for (Profile member : allocatedMemberList) { // gleiche Tage aller member in ein Gefäss (= selectableStudentDays)
                StudentDay studentDay = member.getStudentTimes().getDaySelectionListAt(dayIndex);
                selectableDays.add(studentDay);
            }
            commonTimes = new CommonStudentTimes(selectableDays); // selectableStudentDays in ein Gefäss (=commonTimes)
            commonTimes.findBounds();
            commonTimes.updateStudentDayOf(kgu, dayIndex);
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
        add(BorderLayout.CENTER, selectionField);
        add(BorderLayout.PAGE_END, buttonField);
    }

    public void addEntryButtonListeners() {
        addCancelButtonListener();
        approveButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!legalNumberOfMembers()) {
                    Dialogs.illegalNumberOfMembersErrorMessage();
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
                if (!legalNumberOfMembers()) {
                    Dialogs.illegalNumberOfMembersErrorMessage();
                } else {
                    removeMemberIDs();
                    removeMemberNames();
                    setLectionLength();
                    setProfileData();
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

    private void removeMemberNames() {
        kgu.setFirstName("");
        kgu.setName("");
        kgu.setThirdName("");
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

//    private void findBounds(Profile member) {
//        initCommonTimeMarks();
//        StudentTimes studentTimes = member.getStudentTimes();
//        for (int i = 0; i < studentTimes.getNumberOfSelectedDays(); i++) {
//            StudentDay studentDay = studentTimes.getValidStudentDay(i);
//            findNewTimeBounds(studentDay.start1(), studentDay.end1());
//            findNewTimeBounds(studentDay.start2(), studentDay.end2());
//            findNewFavorite(studentDay.favorite());
//        }
//    }
//
//    private void initCommonTimeMarks() {
//        start1 = new Time();
//        end1 = new Time("23.55");
//        start2 = new Time();
//        end2 = new Time("23.55");
//        favorite = new Time();
//    }
//
//    private void findNewTimeBounds(Time memberStart, Time memberEnd) {
//        // Intervall_1
//        if (start1.lessEqualsThan(memberEnd)) { // Member-Intervall überlappt Vergleichs-Intervall unten
//            end1 = memberEnd;
//            if (start1.lessEqualsThan(memberStart)) { // Member-Intervall liegt innerhalb Vergleichs-Intervall
//                start1 = memberStart;
//            }
//        } else if (start1.lessEqualsThan(memberStart)) { // Member-Intervall überlappt Vergleichs-Intervall oben
//            start1 = memberStart;
//            if (end1.greaterEqualsThan(memberEnd)) { // Member-Intervall liegt innerhalb Vergleichs-Intervall
//                end1 = memberEnd;
//            }
//        } else if (start1.greaterThan(memberEnd)) { // Member-Intervall liegt vollständig unterhalb Vergleichs-Intervall
//            start1.reset();
//        } else if (end1.lessThan(memberStart)) { // Member-Intervall liegt vollständig oberhalb Vergleichs-Intervall
//            end1.reset();
//        }
//        // Intervall_2
//        if (start2.lessEqualsThan(memberEnd)) {
//            end2 = memberEnd;
//            if (start2.lessEqualsThan(memberStart)) {
//                start2 = memberStart;
//            }
//        } else if (start2.lessEqualsThan(memberStart)) {
//            start2 = memberStart;
//            if (end2.greaterEqualsThan(memberEnd)) {
//                end2 = memberEnd;
//            }
//        } else if (start2.greaterThan(memberEnd)) {
//            start2.reset();
//        } else if (end2.lessThan(memberStart)) {
//            end2.reset();
//        }
//        // Vergleichs-Favorite liegt ausserhalb Member-Intervall
//        if(favorite.lessThan(memberStart)|| favorite.greaterThan(memberEnd))
//            favorite.reset();
//    }
//
//    private void findNewFavorite(Time memberFavorite) {
//        boolean whithin1 = memberFavorite.greaterEqualsThan(start1) && memberFavorite.lessEqualsThan(end1);
//        boolean whithin2 = memberFavorite.greaterEqualsThan(start2) && memberFavorite.lessEqualsThan(end2);
//        if (whithin1 || whithin2 || memberFavorite.equals(favorite)) {
//            favorite = memberFavorite;
//        } else {
//            favorite.reset();
//        }
//    }
//
//    private void updateStudentDayOf() {
//        Time[] timeSlots = new Time[StudentDay.SLOTS];
//        timeSlots[0] = start1;
//        timeSlots[1] = end1;
//        timeSlots[2] = start2;
//        timeSlots[3] = end2;
//        timeSlots[4] = favorite;
//        StudentTimes studentTimes = kgu.getStudentTimes();
//        for (int i = 0; i < studentTimes.getNumberOfSelectedDays(); i++) {
//            StudentDay studentDay = studentTimes.getValidStudentDay(i);
//            studentDay.updateTimeSlots(timeSlots);
//            studentDay.setSelectionState();
//            studentDay.setSingleSlots();
//            studentDay.setTimeBounds();
//        }
//        studentTimes.updateValidStudentDays();
//    }
//
    public abstract void setupUI();
}
