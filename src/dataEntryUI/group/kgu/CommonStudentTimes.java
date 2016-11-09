/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group.kgu;

import core.Profile;
import core.StudentDay;
import java.util.ArrayList;
import utils.Time;
import static utils.Time.ABSOLUTE_END;

/**
 *
 * @author mathiaskielholz
 */
public class CommonStudentTimes {

    private Time start, end, favorite;
    private ArrayList<StudentDay> selectableStudentDays;
    private ArrayList<KGUTimes> selectableKGUTimes;
    private ArrayList<KGUTimes> groups;
    private ArrayList<Profile> memberList;
    private ArrayList<ArrayList<KGUTimes>> fullyAllocatableGroups;
    private ArrayList<ArrayList<KGUTimes>> partlyAllocatableGroups;

    public CommonStudentTimes(ArrayList<Profile> memberList) {
        this.memberList = memberList;
        init();
    }

    private void init() {
        start = new Time();
        end = new Time(ABSOLUTE_END);
        favorite = new Time();
    }

    public void findAllocatableGroups(int dayIndex) {
        createGroupCollections();
        getSelectableKGUTimes(dayIndex);
        findAllocatableGroupCombinations();
        for (int i = 0; i < groups.size(); i++) {
            System.out.println("start:" + groups.get(i).start().toString() + " end:" + groups.get(i).end().toString() + " fav:" + groups.get(i).favorite().toString());
        }
    }

    private void createGroupCollections() {
        groups = new ArrayList<>();
        fullyAllocatableGroups = new ArrayList<>();
        partlyAllocatableGroups = new ArrayList<>();
    }

    private void findAllocatableGroupCombinations() {
        for (int i = 0; i < selectableKGUTimes.size(); i++) {
            for (int j = i + 1; j < selectableKGUTimes.size(); j++) {
                init();
                ArrayList<KGUTimes> groupOfTwo = new ArrayList<>();
                groupOfTwo.add(selectableKGUTimes.get(i));
                groupOfTwo.add(selectableKGUTimes.get(j));
                findCommonKGUTimesBoundsOf(groupOfTwo);
                createCommonProfileOf(groupOfTwo);
                for (int k = j + 1; k < selectableKGUTimes.size(); k++) {
                    init();
                    ArrayList<KGUTimes> groupOfThree = new ArrayList<>();
                    groupOfThree.add(selectableKGUTimes.get(i));
                    groupOfThree.add(selectableKGUTimes.get(j));
                    groupOfThree.add(selectableKGUTimes.get(k));
                    findCommonKGUTimesBoundsOf(groupOfThree);
                    createCommonProfileOf(groupOfThree);
                }
            }
        }
    }

    private void createCommonProfileOf(ArrayList<KGUTimes> groupOfX) {
        if (!this.isEmpty()) {
            KGUTimes commonKGUTimes = new KGUTimes();
            commonKGUTimes.setTimes(start, end, favorite);
            for (KGUTimes singleKGUTimes : groupOfX) {
                commonKGUTimes.addCommonMemberID(singleKGUTimes.getMemberID());
            }
            groups.add(commonKGUTimes);
        }
    }

    public void findSelectedMemberBoundsAt(int dayIndex) {
        getSelectableStudentDaysAt(dayIndex);
        findCommonStudentDayBoundsOf(selectableStudentDays);
    }

    private void getSelectableKGUTimes(int dayIndex) {
        KGUTimes kguTimes;
        ArrayList<KGUTimes> selectableKGUTimes = new ArrayList<>();
        for (Profile member : memberList) { // gleiche Tage/Zeiten/Member-IDs in ein Gefäss (=selectableKGUTimes)
            kguTimes = new KGUTimes();
            StudentDay studentDay = member.getStudentTimes().getDaySelectionListAt(dayIndex);
            kguTimes.setMemberID(member.getProfileID());
            kguTimes.setTimes(studentDay.start1(), studentDay.end1(), studentDay.favorite());
            selectableKGUTimes.add(kguTimes);
        }
        this.selectableKGUTimes = selectableKGUTimes;
    }

    private void getSelectableStudentDaysAt(int dayIndex) {
        ArrayList<StudentDay> selectableStudentDays = new ArrayList<>();
        for (Profile member : memberList) { // gleiche Tage/Zeiten aller member in ein Gefäss (=selectableStudentDays)
            StudentDay studentDay = member.getStudentTimes().getDaySelectionListAt(dayIndex);
            selectableStudentDays.add(studentDay);
        }
        this.selectableStudentDays = selectableStudentDays;
    }

    private void findCommonKGUTimesBoundsOf(ArrayList<KGUTimes> kguTimes) {
        if (!kguTimes.isEmpty()) {
            for (KGUTimes kgu : kguTimes) {
                if (!kgu.isEmpty()) {
                    findBounds(kgu.start(), kgu.end(), kgu.favorite());
                } else {
                    clearAll();
                }
            }
        } else {
            clearAll();
        }
    }

    private void findCommonStudentDayBoundsOf(ArrayList<StudentDay> selectableDays) {
        if (!selectableDays.isEmpty()) {
            for (StudentDay studentDay : selectableDays) {
                if (!studentDay.isEmpty()) {
                    findBounds(studentDay.start1(), studentDay.end1(), studentDay.favorite());
                } else {
                    clearAll();
                }
            }
        } else {
            clearAll();
        }
    }

    private void findBounds(Time memberStart, Time memberEnd, Time memberFavorite) {
        // Favorit prüfen
        if (!favorite.equals(memberFavorite)) {  // falls beide gleich (d.h. auch 0), favorit bleibt (d.h. auch 0)
            // this.favorit
            boolean outOfMemberBounds = favorite.greaterThan(memberEnd) || favorite.lessThan(memberStart);
            if (outOfMemberBounds) {
                favorite.reset();
            }
            // memberFavorit prüfen
            boolean whithinThisBounds = memberFavorite.greaterEqualsThan(start) && memberFavorite.lessEqualsThan(end);
            if (whithinThisBounds) {
                if (!memberFavorite.isEmpty()) { // Spezialfall: start = end = memberFavorite = 0 und favorite > 0
                    favorite = memberFavorite.clone();   // memberFavorite priorisiert this.favorite, falls 2 Favoriten
                }
            }
        }
        // neuer Start bestimmen
        if (memberStart.lessEqualsThan(end)) {
            if (memberStart.greaterEqualsThan(start)) { // memberStart liegt innerhalb Vergleichs-Intervall, falls unterhalb -> start1 bleibt
                start = memberStart.clone();
            }
        } else {
            start.reset();
            end.reset();
        }
        // neues Ende bestimmen
        if (memberEnd.greaterEqualsThan(start)) {
            if (memberEnd.lessEqualsThan(end)) { // memberEnd liegt innerhalb Vergleichs-Intervall, falls oberhalb -> end1 bleibt
                end = memberEnd.clone();
            }
        } else {
            start.reset();
            end.reset();
        }
    }

    public void setStudentDayDataToProfile(Profile kgu, int dayIndex) {
        StudentDay studentDay = kgu.getStudentTimes().getDaySelectionListAt(dayIndex);
        studentDay.setStart1(start);
        studentDay.setEnd1(end);
        studentDay.setFavorite(favorite);
        studentDay.setSelectionState();
        studentDay.setSingleSlots();
        studentDay.setTimeBounds();
    }

    private void clearAll() {
        start.reset();
        end.reset();
        favorite.reset();
    }

    public boolean isEmpty() {
        return start.isEmpty() && favorite.isEmpty() && (end.isEmpty() || end.equals(ABSOLUTE_END));
    }

    public Time start() {
        return start;
    }

    public Time end() {
        return end;
    }

    public Time favorite() {
        return favorite;
    }

    public ArrayList<ArrayList<KGUTimes>> getPartlyAllocatableGroups() {
        return partlyAllocatableGroups;
    }

    public ArrayList<ArrayList<KGUTimes>> getFullyAllocatableGroups() {
        return fullyAllocatableGroups;
    }

}
