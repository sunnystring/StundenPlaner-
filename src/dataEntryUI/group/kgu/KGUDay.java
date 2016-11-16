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
public class KGUDay {

    private Time start, end, favorite;
    private int memberID; // bevor als mergedTimes-Objekt benutzt
    private ArrayList<Integer> memberIDs;

    public KGUDay() {
        memberIDs = new ArrayList<>();
    }

    public boolean hasCommonMembers(KGUDay kGUDay) {
        return true;
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

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public int getMemberID() {
        return memberID;
    }

    public boolean isEmpty() {
        return start.isEmpty() && favorite.isEmpty() && end.isEmpty();
    }

    public void setTime(Time start, Time end, Time favorite) {
        this.start = start;
        this.end = end;
        this.favorite = favorite;
    }

    public void addMemberID(Integer id) {
        memberIDs.add(id);
    }

    public ArrayList<Integer> getMemberIDs() {
        return memberIDs;
    }
}
