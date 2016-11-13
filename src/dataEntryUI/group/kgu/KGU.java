/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group.kgu;

import java.util.ArrayList;
import utils.Time;

/**
 *
 * @author mathiaskielholz
 */
public class KGU {

    private Time start, end, favorite;
    private ArrayList<Integer> memberIDs;

    public KGU(CommonStudentTimes commonStudentTimes) {
        start = commonStudentTimes.start();
        end = commonStudentTimes.end();
        favorite = commonStudentTimes.favorite();
        memberIDs = new ArrayList<>();
    }

    public Time getStart() {
        return start;
    }

    public Time getEnd() {
        return end;
    }

    public Time getFavorite() {
        return favorite;
    }

    public ArrayList<Integer> getMemberIDs() {
        return memberIDs;
    }

}
