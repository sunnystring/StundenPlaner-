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
public class CommonTimes {

    private Time start, end, favorite;
    private ArrayList<StudentDay> selectableStudentDays;
    private ArrayList<Profile> memberList;
   // private ArrayList<KGUDay> allocatableGroups;  // not in use

    public CommonTimes(ArrayList<Profile> memberList) {
        this.memberList = memberList;
      //  allocatableGroups = new ArrayList<>();
        init();
    }

    private void init() {
        start = new Time();
        end = new Time(ABSOLUTE_END);
        favorite = new Time();
    }

    public void findSelectedMemberBoundsAt(int dayIndex) {
        getSelectableStudentDaysAt(dayIndex);
        findCommonStudentTimesOf(selectableStudentDays);
    }

    private void getSelectableStudentDaysAt(int dayIndex) {
        ArrayList<StudentDay> selectableStudentDays = new ArrayList<>();
        for (Profile member : memberList) { // gleiche Tage/Zeiten aller member in ein Gefäss (=selectableStudentDays)
            StudentDay studentDay = member.getStudentTimes().getDaySelectionListAt(dayIndex);
            selectableStudentDays.add(studentDay);
        }
        this.selectableStudentDays = selectableStudentDays;
    }

    private void findCommonStudentTimesOf(ArrayList<StudentDay> selectableDays) {
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

    public void setStudentDayData(Profile kgu, int dayIndex) {
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

    public boolean commonBoundsExist() {
        return !start.isEmpty() || !favorite.isEmpty();
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

    //-------------------------------not working yet---------------------
    //-------------------------------------------------------------------
//    public void getKGUBundles(int dayIndex) {
//        createKGUBundles(memberList, dayIndex);
//        KGUDaysLog();
//    }
//
//    private void createKGUBundles(ArrayList<Profile> tempMemberList, int dayIndex) {
//        System.out.println("memberlistSize:" + tempMemberList.size());
//        if (tempMemberList.size() == 1) {
//            tempMemberList.clear();
//            tempMemberList.addAll(memberList);
//            System.out.println("partly");
//            return;
//        } else if (tempMemberList.isEmpty()) {
//            tempMemberList.addAll(memberList);
//            System.out.println("fully");
//            return;
//        } else {
//            ArrayList<Profile> resultingMemberList = new ArrayList<>();
//            resultingMemberList.addAll(tempMemberList);
//            int numberOfMembers = resultingMemberList.size();
//            for (int i = 0; i < numberOfMembers; i++) {
//                for (int j = i + 1; j < numberOfMembers; j++) {
//                    if (resultingMemberList.size() > 1) {
//                        ArrayList<StudentDay> groupOfTwo = new ArrayList<>();
//                        ArrayList<Integer> memberIds = new ArrayList<>();
//                        Profile member1 = resultingMemberList.get(i), member2 = resultingMemberList.get(j);
//                        groupOfTwo.add(member1.getStudentDay(dayIndex));
//                        groupOfTwo.add(member2.getStudentDay(dayIndex));
//                        init();
//                        findCommonStudentTimesOf(groupOfTwo);
//                        if (commonBoundsExist()) {
//                            memberIds.add(member1.getProfileID());
//                            memberIds.add(member2.getProfileID());
//                            createKGUDay(memberIds);
//                            resultingMemberList.remove(member1);
//                            resultingMemberList.remove(member2);
//                            createKGUBundles(resultingMemberList, dayIndex);
//                            for (int k = j + 1; k < numberOfMembers; k++) {
//                                if (resultingMemberList.size() > 2) {
//                                    Profile member3 = resultingMemberList.get(k);
//                                    ArrayList<StudentDay> groupOfThree = new ArrayList<>();
//                                    groupOfThree.add(member1.getStudentDay(dayIndex));
//                                    groupOfThree.add(member2.getStudentDay(dayIndex));
//                                    groupOfThree.add(member3.getStudentDay(dayIndex));
//                                    init();
//                                    findCommonStudentTimesOf(groupOfThree);
//                                    if (commonBoundsExist()) {
//                                        memberIds.clear();
//                                        memberIds.add(member1.getProfileID());
//                                        memberIds.add(member2.getProfileID());
//                                        memberIds.add(member3.getProfileID());
//                                        createKGUDay(memberIds);
//                                    }
//                                    resultingMemberList.remove(member1);
//                                    resultingMemberList.remove(member2);
//                                    resultingMemberList.remove(member3);
//                                    createKGUBundles(resultingMemberList, dayIndex);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private void createKGUDay(ArrayList<Integer> memberIDs) {
//        KGUDay kguDay = new KGUDay();
//        kguDay.setTime(start, end, favorite);
//        kguDay.setMemberIDs(memberIDs);
//        allocatableGroups.add(kguDay);
//        logKGU(kguDay);
//    }
//
//    private void logKGU(KGUDay kguDay) {
//        String text = "";
//        String ids = " member-IDs: ";
//        for (Integer id : kguDay.getMemberIDs()) {
//            ids += id.toString() + " ";
//        }
//        text += "  start:" + kguDay.start().toString() + " end:" + kguDay.end().toString()
//                + " fav:" + kguDay.favorite().toString() + ids;
//        System.out.println(text);
//    }
//
//    // Test
//    private void KGUDaysLog() {
//        for (KGUDay kGUDay : allocatableGroups) {
//            String text = "";
//            String ids = " member-IDs: ";
//            for (Integer id : kGUDay.getMemberIDs()) {
//                ids += id.toString() + " ";
//            }
//            text += "  start:" + kGUDay.start().toString() + " end:" + kGUDay.end().toString()
//                    + " fav:" + kGUDay.favorite().toString() + ids;
//            System.out.println(text);
//        }
//        System.out.println("total: " + allocatableGroups.size());
//        System.out.println("*****************************************************");
//    }
}
