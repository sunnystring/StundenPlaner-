/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group.kgu;

import core.Profile;
import core.StudentDay;
import java.util.ArrayList;
import java.util.List;
import utils.Time;
import static utils.Time.ABSOLUTE_END;

/**
 *
 * @author mathiaskielholz
 */
public class CommonStudentTimes {

    private Time start, end, favorite;
    private List selectableStudentDays;
    private ArrayList<KGU> allocatableGroups;
    ArrayList<Profile> memberList;

    public CommonStudentTimes(ArrayList<Profile> memberList) {
        this.memberList = memberList;
        //    this.selectableStudentDays = selectableStudentDays;
        start = new Time();
        end = new Time(ABSOLUTE_END);
        favorite = new Time();
    }

    public void findAllocatableGroups(int dayIndex) {
        getMemberStudentDaysAt(dayIndex);
        //  findTwoMemberGroups();
        //  findThreeMemberGroups();
    }

    private void findTwoMemberGroups() {
        for (int i = 0; i < selectableStudentDays.size() - 1; i++) {
            for (int j = i + 1; j < selectableStudentDays.size(); j++) {
                findBounds(selectableStudentDays.subList(i, j));
                if (!this.isEmpty()) {
                    allocatableGroups.add(new KGU(this));
                }
            }
        }
    }

    private void findThreeMemberGroups() {
        for (int i = 0; i < selectableStudentDays.size() - 2; i++) {
            for (int j = i + 1; j < selectableStudentDays.size() - 1; j++) {
                for (int k = j + 1; k < selectableStudentDays.size(); k++) {
                    findBounds(selectableStudentDays.subList(i, k));
                }
            }
        }
    }

    public void findSelectedMemberBoundsAt(int dayIndex) {
        getMemberStudentDaysAt(dayIndex);
        findBounds(selectableStudentDays);
    }

    private void getMemberStudentDaysAt(int dayIndex) {
        ArrayList<StudentDay> selectableDays = new ArrayList<>();
        selectableDays = new ArrayList<>();
        for (Profile member : memberList) { // gleiche Tage aller member in ein Gefäss (=selectableStudentDays)
            StudentDay studentDay = member.getStudentTimes().getDaySelectionListAt(dayIndex);
            selectableDays.add(studentDay);
        }
        selectableStudentDays = selectableDays;
    }

  

    private void findBounds(List<StudentDay> selectableDays) {
        if (!selectableDays.isEmpty()) {
            for (StudentDay studentDay : selectableDays) {
                if (!studentDay.isEmpty()) {
                    Time memberStart = studentDay.start1(), memberEnd = studentDay.end1(), memberFavorite = studentDay.favorite();
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
                } else {
                    clearAll();
                }
            }
        } else {
            clearAll();
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
}
