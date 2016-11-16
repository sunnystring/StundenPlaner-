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
    // private ArrayList<KGUDay> selectableKGUDays; // weglassen...
    //  private ArrayList<KGUTimes> allocatableGroups; 
    //   private ArrayList<GroupBundle> groupBundles;
    private ArrayList<Profile> memberList;
    // private ArrayList<ArrayList<KGUDay>> fullyAllocatableGroups;
    //  private ArrayList<ArrayList<KGUDay>> partlyAllocatableGroups;
    // private ArrayList<KGUDay> mergedKGUDays;
    private ArrayList<Profile> allocatableGroups;

    public CommonTimes(ArrayList<Profile> memberList) {
        this.memberList = memberList;
        allocatableGroups = new ArrayList<>();
        //  mergedKGUDays = new ArrayList<>();
        init();
    }

    private void init() {
        start = new Time();
        end = new Time(ABSOLUTE_END);
        favorite = new Time();
    }

    public void findAllocatableGroups(int dayIndex) {
        checkMembers(memberList, dayIndex);
        //  createGroupCollections();
        //  getSelectableKGUDaysAt(dayIndex);
        // findAllocatableGroupCombinations();
        //  mergedDaysLog();
    }

    // Test
//    private void mergedDaysLog() {
//        for (KGUDay kGUDay : mergedKGUDays) {
//            String text = "";
//            String ids = " member-IDs: ";
//            for (Integer id : kGUDay.getMemberIDs()) {
//                ids += id.toString() + " ";
//            }
//            text += "  start:" + kGUDay.start().toString() + " end:" + kGUDay.end().toString()
//                    + " fav:" + kGUDay.favorite().toString() + ids;
//            System.out.println(text);
//        }
//        System.out.println("total: " + mergedKGUDays.size());
//        System.out.println("*****************************************************");
//
//    }
    private void checkMembers(ArrayList<Profile> tempMemberList, int dayIndex) {
        int numberOfMembers = tempMemberList.size();
        if (numberOfMembers == 1) {
            return;
        } else if (numberOfMembers == 0) {
            return;
        } else {
            ArrayList<Profile> resultingMemberList2 = new ArrayList<>();
            ArrayList<Profile> resultingMemberList3 = new ArrayList<>();
            ArrayList<StudentDay> groupOfTwo, groupOfThree;
            for (int i = 0; i < numberOfMembers; i++) {
                for (int j = i + 1; j < numberOfMembers; j++) {
                    if (numberOfMembers > 2) {
                        for (int k = j + 1; k < numberOfMembers; k++) {
                            groupOfThree = new ArrayList<>();
                            groupOfThree.add(tempMemberList.get(i).getStudentDay(dayIndex));
                            groupOfThree.add(tempMemberList.get(j).getStudentDay(dayIndex));
                            groupOfThree.add(tempMemberList.get(k).getStudentDay(dayIndex));
                            init();
                            findCommonStudentTimesOf(groupOfThree);
                            if (commonBoundsExist()) {
                                createKGUProfile(dayIndex);
                                resultingMemberList3.clear();
                                resultingMemberList3.addAll(tempMemberList);
                                resultingMemberList3.remove(tempMemberList.get(i));
                                resultingMemberList3.remove(tempMemberList.get(j));
                                resultingMemberList3.remove(tempMemberList.get(k));

                            } else {
                                break;
                            }
                        }
                    }
                    groupOfTwo = new ArrayList<>();
                    groupOfTwo.add(tempMemberList.get(i).getStudentDay(dayIndex));
                    groupOfTwo.add(tempMemberList.get(j).getStudentDay(dayIndex));
                    init();
                    findCommonStudentTimesOf(groupOfTwo);
                    if (commonBoundsExist()) {
                        createKGUProfile(dayIndex);
                        resultingMemberList2.clear();
                        resultingMemberList2.addAll(tempMemberList);
                        resultingMemberList2.remove(tempMemberList.get(i));
                        resultingMemberList2.remove(tempMemberList.get(j));

                    } else {
                        break;
                    }

                }
            }

            checkMembers(resultingMemberList2, dayIndex);
            checkMembers(resultingMemberList3, dayIndex);
        }
    }

    private void createKGUProfile(int dayIndex) {
        Profile kgu = new Profile();
        setStudentDayDataToProfile(kgu, dayIndex);
        allocatableGroups.add(kgu);
    }

//    private boolean findGroups(ArrayList<Profile> tempMemberList, int dayIndex) {
//        findGroupsOfTwo(tempMemberList, dayIndex);
//        findGroupsOfThree(tempMemberList, dayIndex);
//        return tempMemberList.size() <= 1;
//    }
//
//    private void findGroupsOfTwo(ArrayList<Profile> tempMemberList, int dayIndex) {
//        ArrayList<StudentDay> studentDays = new ArrayList<>();
//
//        while (tempMemberList.size() >= 1) {
//            int i = tempMemberList.size() - 1;
//            studentDays.add(tempMemberList.get(i - 1).getStudentDay(dayIndex));
//            studentDays.add(tempMemberList.get(i).getStudentDay(dayIndex));
//            findCommonStudentTimesOf(studentDays);
//            createKGUDay();
//            tempMemberList.remove(i - 1);
//            tempMemberList.remove(i);
//        }
//    }
//
//    private void findGroupsOfThree(ArrayList<Profile> tempMemberList, int dayIndex) {
//        ArrayList<StudentDay> studentDays = new ArrayList<>();
//        int i = tempMemberList.size() - 1;
//        while (i >= 2) {
//            studentDays.add(tempMemberList.get(i - 2).getStudentDay(dayIndex));
//            studentDays.add(tempMemberList.get(i - 1).getStudentDay(dayIndex));
//            studentDays.add(tempMemberList.get(i).getStudentDay(dayIndex));
//            findCommonStudentTimesOf(studentDays);
//            createKGUDay();
//            i--;
//        }
//    }
//    private void createGroupCollections() {
//        //   allocatableGroups = new ArrayList<>();
//        //   groupBundles = new ArrayList<>();
//      //  fullyAllocatableGroups = new ArrayList<>();
//      //  partlyAllocatableGroups = new ArrayList<>();
//    }
//    private void findAllocatableGroupCombinations() {
//        for (int i = 0; i < selectableKGUDays.size(); i++) {
//            for (int j = i + 1; j < selectableKGUDays.size(); j++) {
//                init();
//                ArrayList<KGUDay> groupOfTwo = new ArrayList<>();
//                groupOfTwo.add(selectableKGUDays.get(i));
//                groupOfTwo.add(selectableKGUDays.get(j));
//                findCommonKGUDaysOf(groupOfTwo);
//                createMergedKGUDays(groupOfTwo);
//                // createGroupBundles(groupOfTwo);
//                for (int k = j + 1; k < selectableKGUDays.size(); k++) {
//                    init();
//                    ArrayList<KGUDay> groupOfThree = new ArrayList<>();
//                    groupOfThree.add(selectableKGUDays.get(i));
//                    groupOfThree.add(selectableKGUDays.get(j));
//                    groupOfThree.add(selectableKGUDays.get(k));
//                    findCommonKGUDaysOf(groupOfThree);
//                    //  createGroupBundles(groupOfThree);
//                    createMergedKGUDays(groupOfThree);
//                }
//            }
//        }
//    }
//    private void createMergedKGUDays(ArrayList<KGUDay> groupOfX) {
//        if (!this.commonBoundsExist()) {
//            KGUDay mergedDay = new KGUDay();
//            mergedDay.setTime(start, end, favorite);
//            for (KGUDay member : groupOfX) {
//                mergedDay.addMemberID(member.getMemberID());
//            }
//            mergedKGUDays.add(mergedDay);
//        }
//
//    }
//    public ArrayList<KGUBundle> createKGUBundles() {
//        ArrayList<KGUBundle> KGUBundles = new ArrayList<>();
//        KGUBundle bundle;
//        KGUDay mergedDay;
//        int maximalNumberOfIterations = memberList.size() / 2;
//        for (int i = 0; i < maximalNumberOfIterations; i++) {
//            mergedDay = mergedKGUDays.get(i);
//            bundle = new KGUBundle(memberList.size());
//            bundle.addGroup(mergedDay);
//            bundle.addMemberIDs(mergedDay.getMemberIDs());
//            for (int j = i + 1; j < maximalNumberOfIterations; j++) {
//                mergedDay = mergedKGUDays.get(j);
//                if (bundle.excludes(mergedDay)) {
//                    bundle = new KGUBundle(memberList.size());
//                    bundle.addGroup(mergedDay);
//                    bundle.addMemberIDs(mergedDay.getMemberIDs());
//                    KGUBundles.add(bundle); //
////                    if (bundle.excludes(mergedDay)) {
////                        bundle.addGroup(mergedDay);
////                        bundle.addMemberIDs(mergedDay.getMemberIDs());
//                    for (int k = j + 1; k < maximalNumberOfIterations; k++) {
//                        mergedDay = mergedKGUDays.get(k);
//                        if (bundle.excludes(mergedDay)) {
//                            bundle = new KGUBundle(memberList.size());
//                            bundle.addGroup(mergedDay);
//                            bundle.addMemberIDs(mergedDay.getMemberIDs());
//                            KGUBundles.add(bundle);
//                        }
//                    }
//                }
//            }
//            bundle = new KGUBundle(memberList.size());
//
//           
//        }
//         return KGUBundles;
//    }
    //    private void createGroupBundles(ArrayList<KGUDay> groupOfX) {
    //        if (!this.commonBoundsExist()) {
    //            KGUDay mergedDay = new KGUDay();
    //            mergedDay.setTime(start, end, favorite);
    //            for (KGUDay member : groupOfX) {
    //                mergedDay.addMemberID(member.getMemberID());
    //            }
    //            // array <KGUTimes> mergedKGUTimes bundle check trennen
    //            //bundles ->auslagern in KGuInputM. über alle checken
    //            GroupBundle newBundle = new GroupBundle(memberList.size()); // falscher Algorithmus!! es werden nicht alle mergedTimes miteiender verglichen
    //            newBundle.addGroup(mergedDay);
    //            newBundle.addMemberIDs(mergedDay.getMemberIDs());
    //            if (groupBundles.size() > 0) {
    //                for (GroupBundle existingBundle : groupBundles) {
    //                    if (existingBundle.excludes(mergedDay)) {
    //                        existingBundle.addGroup(mergedDay);
    //                        existingBundle.addMemberIDs(mergedDay.getMemberIDs());
    //                    }
    //                }
    //            } // if(members of groupBundle ist einmalig)
    //            groupBundles.add(newBundle);
    //        }
    //    }
    public void findSelectedMemberBoundsAt(int dayIndex) {
        getSelectableStudentDaysAt(dayIndex);
        findCommonStudentTimesOf(selectableStudentDays);
    }

//    private void getSelectableKGUDaysAt(int dayIndex) {
//        KGUDay kguDay;
//        ArrayList<KGUDay> selectableKGUDays = new ArrayList<>();
//        for (Profile member : memberList) { // gleiche Tage/Zeiten/Member-IDs in ein Gefäss (=selectableKGUTimes)
//            kguDay = new KGUDay();
//            StudentDay studentDay = member.getStudentTimes().getDaySelectionListAt(dayIndex);
//            kguDay.setMemberID(member.getProfileID());
//            kguDay.setTime(studentDay.start1(), studentDay.end1(), studentDay.favorite());
//            selectableKGUDays.add(kguDay);
//        }
//        this.selectableKGUDays = selectableKGUDays;
//    }
    private void getSelectableStudentDaysAt(int dayIndex) {
        ArrayList<StudentDay> selectableStudentDays = new ArrayList<>();
        for (Profile member : memberList) { // gleiche Tage/Zeiten aller member in ein Gefäss (=selectableStudentDays)
            StudentDay studentDay = member.getStudentTimes().getDaySelectionListAt(dayIndex);
            selectableStudentDays.add(studentDay);
        }
        this.selectableStudentDays = selectableStudentDays;
    }

//    private void findCommonKGUDaysOf(ArrayList<KGUDay> kguDays) {
//        if (kguDays.size() > 0) {
//            for (KGUDay kgu : kguDays) {
//                if (!kgu.commonBoundsExist()) {
//                    findBounds(kgu.start(), kgu.end(), kgu.favorite());
//                } else {
//                    clearAll();
//                }
//            }
//        } else {
//            clearAll();
//        }
//    }
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

//    public ArrayList<ArrayList<KGUDay>> getPartlyAllocatableGroups() {
//        return partlyAllocatableGroups;
//    }
//
//    public ArrayList<ArrayList<KGUDay>> getFullyAllocatableGroups() {
//        return fullyAllocatableGroups;
//    }
}
