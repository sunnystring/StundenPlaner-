/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group;

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

    public CommonTimes(ArrayList<Profile> memberList) {
        this.memberList = memberList;
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
}
