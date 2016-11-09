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
public class KGUTimes {

    private Time start, end, favorite;
    private int memberID;
    private ArrayList<Integer> commonMemberIDs;

    public KGUTimes() {
        commonMemberIDs = new ArrayList<>();
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

    public void setTimes(Time start, Time end, Time favorite) {
        this.start = start;
        this.end = end;
        this.favorite = favorite;
    }

    public void addCommonMemberID(Integer id) {
        commonMemberIDs.add(id);
    }

    public ArrayList<Integer> getCommonMemberIDs() {
        return commonMemberIDs;
    }
}
