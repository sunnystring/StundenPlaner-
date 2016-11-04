/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userUtils;

import core.Profile;
import core.StudentDay;
import java.util.ArrayList;
import utils.Time;

/**
 *
 * @author mathiaskielholz
 */
public class CommonStudentTimes {

    private ArrayList<StudentDay> selectableStudentDays;
    private Time start, end, favorite;

    public CommonStudentTimes(ArrayList<StudentDay> selectableStudentDays) {
        this.selectableStudentDays = selectableStudentDays;
        start = new Time();
        end = new Time("23.55");
        favorite = new Time();
    }

    public void findBounds() {
        if (!selectableStudentDays.isEmpty()) {
            for (StudentDay studentDay : selectableStudentDays) {
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
                            favorite = memberFavorite.clone();   // memberFavorite priorisiert this.favorite, falls 2 Favoriten
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

    private void clearAll() {
        start.reset();
        end.reset();
        favorite.reset();
    }

    public void updateStudentDayOf(Profile kgu, int dayIndex) {
        StudentDay studentDay = kgu.getStudentTimes().getDaySelectionListAt(dayIndex);
        studentDay.setStart1(start);
        studentDay.setEnd1(end);
        studentDay.setFavorite(favorite);
        studentDay.setSelectionState();
        studentDay.setSingleSlots();
        studentDay.setTimeBounds();
    }
}
